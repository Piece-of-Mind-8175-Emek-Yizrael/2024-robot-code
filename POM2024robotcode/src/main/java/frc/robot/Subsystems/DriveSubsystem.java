package frc.robot.Subsystems;

import static frc.robot.Constants.DriveConstants.*;

import java.util.ArrayList;
import java.util.function.DoubleSupplier;
import java.util.function.Supplier;

import static edu.wpi.first.units.MutableMeasure.mutable;
import static edu.wpi.first.units.Units.Meters;
import static edu.wpi.first.units.Units.MetersPerSecond;
import static edu.wpi.first.units.Units.Volts;

import com.ctre.phoenix.sensors.WPI_PigeonIMU;
import com.revrobotics.CANSparkBase.IdleMode;
import com.revrobotics.CANSparkLowLevel.MotorType;
import com.revrobotics.CANSparkMax;
import com.revrobotics.RelativeEncoder;
import com.revrobotics.SparkPIDController;

import edu.wpi.first.math.VecBuilder;
import edu.wpi.first.math.estimator.DifferentialDrivePoseEstimator;
import edu.wpi.first.math.filter.SlewRateLimiter;
import edu.wpi.first.math.geometry.Pose2d;
import edu.wpi.first.math.geometry.Rotation2d;
import edu.wpi.first.math.geometry.Transform2d;
import edu.wpi.first.math.geometry.Translation2d;
import edu.wpi.first.math.kinematics.DifferentialDriveOdometry;
import edu.wpi.first.math.kinematics.DifferentialDriveWheelPositions;
import edu.wpi.first.math.kinematics.DifferentialDriveWheelSpeeds;
import edu.wpi.first.math.trajectory.Trajectory;
import edu.wpi.first.networktables.NetworkTableInstance;
import edu.wpi.first.units.Distance;
import edu.wpi.first.units.Measure;
import edu.wpi.first.units.MutableMeasure;
import edu.wpi.first.units.Velocity;
import edu.wpi.first.units.Voltage;
import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.DriverStation.Alliance;
import edu.wpi.first.wpilibj.RobotController;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.drive.DifferentialDrive;
import edu.wpi.first.wpilibj.smartdashboard.Field2d;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.sysid.SysIdRoutine;
import frc.robot.Commands.TrajectoryFactory;
/**
 *
 */
public class DriveSubsystem extends PomSubsystem {

  private Field2d field;

  private final CANSparkMax masterRightMotor = new CANSparkMax(RIGHT_MOTOR_LEAD, MotorType.kBrushless);
  private final CANSparkMax slaveRightMotor = new CANSparkMax(RIGHT_MOTOR_SLAVE, MotorType.kBrushless);

  private final CANSparkMax masterLeftMotor = new CANSparkMax(LEFT_MOTOR_LEAD, MotorType.kBrushless);
  private final CANSparkMax slaveLeftMotor = new CANSparkMax(LEFT_MOTOR_SLAVE, MotorType.kBrushless);

  private final SparkPIDController leftPid = masterLeftMotor.getPIDController();
  private final SparkPIDController rightPid = masterRightMotor.getPIDController();

  private final RelativeEncoder leftEncoder = masterLeftMotor.getEncoder();
  private final RelativeEncoder rightEncoder = masterRightMotor.getEncoder();

  private final DifferentialDrive mDrive = new DifferentialDrive(masterLeftMotor, masterRightMotor);

  private final WPI_PigeonIMU mGyro = new WPI_PigeonIMU(GYRO_ID);

  private final DifferentialDriveOdometry odometry = new DifferentialDriveOdometry(
                                            mGyro.getRotation2d(),
                                          leftEncoder.getPosition(), 
                                          rightEncoder.getPosition()
                                          );


  double x = 0,y = 0;
  boolean isNote = false;

  private final DifferentialDrivePoseEstimator poseEstimator = new DifferentialDrivePoseEstimator
              (DRIVE_KINEMATICS, 
              mGyro.getRotation2d(), 
              leftEncoder.getPosition(), 
              rightEncoder.getPosition(), 
              new Pose2d(),
              VecBuilder.fill(0.02, 0.02, 0.01),
              VecBuilder.fill(0.1, 0.1, 0.15));

  double [] botPose = new double[BOT_POSE_LEN];

  /** Creates a new DriveSubsystem. */
  public DriveSubsystem() {

    leftPid.setP(KP);
    leftPid.setI(KI);
    leftPid.setD(KD);

    rightPid.setP(KP);
    rightPid.setI(KI);
    rightPid.setD(KD);

    zeroHeading();

    // We need to invert one side of the drivetrain so that positive voltages
    // result in both sides moving forward. Depending on how your robot's
    // gearbox is constructed, you might have to invert the left side instead.
    masterRightMotor.setInverted(true);
    slaveRightMotor.setInverted(true);
    
    leftEncoder.setPositionConversionFactor(ROTATIONS_TO_METERS);
    rightEncoder.setPositionConversionFactor(ROTATIONS_TO_METERS);

    leftEncoder.setVelocityConversionFactor(ROTATIONS_TO_METERS / 60);
    rightEncoder.setVelocityConversionFactor(ROTATIONS_TO_METERS / 60);

    field = new Field2d();


    SmartDashboard.putData("Field", field);

    slaveLeftMotor.follow(masterLeftMotor);
    slaveRightMotor.follow(masterRightMotor);

    masterLeftMotor.setIdleMode(IdleMode.kCoast);
    masterRightMotor.setIdleMode(IdleMode.kCoast);
    slaveLeftMotor.setIdleMode(IdleMode.kCoast);
    slaveRightMotor.setIdleMode(IdleMode.kCoast);
    
    mGyro.reset();
  }

  private final MutableMeasure<Voltage> m_appliedVoltage = mutable(Volts.of(0));
  // Mutable holder for unit-safe linear distance values, persisted to avoid reallocation.
  private final MutableMeasure<Distance> m_distance = mutable(Meters.of(0));
  // Mutable holder for unit-safe linear velocity values, persisted to avoid reallocation.
  private final MutableMeasure<Velocity<Distance>> m_velocity = mutable(MetersPerSecond.of(0));



  // Create a new SysId routine for characterizing the drive.
  private final SysIdRoutine m_sysIdRoutine =
      new SysIdRoutine(
          // Empty config defaults to 1 volt/second ramp rate and 7 volt step voltage.
          new SysIdRoutine.Config(),
          new SysIdRoutine.Mechanism(
              // Tell SysId how to plumb the driving voltage to the motors.
              (Measure<Voltage> volts) -> {
                masterLeftMotor.setVoltage(volts.in(Volts));
                masterRightMotor.setVoltage(volts.in(Volts));
              },
              // Tell SysId how to record a frame of data for each motor on the mechanism being
              // characterized.
              log -> {
                // Record a frame for the left motors.  Since these share an encoder, we consider
                // the entire group to be one motor.
                log.motor("drive-left")
                    .voltage(
                        m_appliedVoltage.mut_replace(
                            masterLeftMotor.get() * RobotController.getBatteryVoltage(), Volts))
                    .linearPosition(m_distance.mut_replace(leftEncoder.getPosition(), Meters))
                    .linearVelocity(
                        m_velocity.mut_replace(leftEncoder.getVelocity(), MetersPerSecond));
                // Record a frame for the right motors.  Since these share an encoder, we consider
                // the entire group to be one motor.
                log.motor("drive-right")
                    .voltage(
                        m_appliedVoltage.mut_replace(
                            masterRightMotor.get() * RobotController.getBatteryVoltage(), Volts))
                    .linearPosition(m_distance.mut_replace(rightEncoder.getPosition(), Meters))
                    .linearVelocity(
                        m_velocity.mut_replace(rightEncoder.getVelocity(), MetersPerSecond));
              },
              // Tell SysId to make generated commands require this subsystem, suffix test state in
              // WPILog with this subsystem's name ("drive")
              this));


  
  
  @Override
  public void periodic() {
    // This method will be called once per scheduler run
    // Update the odometry in the periodic block
    if (NetworkTableInstance.getDefault().getTable("limelight-pom").getEntry("tv").getDouble(0) == 1) {
      try {

        if (DriverStation.getAlliance().get() == Alliance.Red) {
          botPose = NetworkTableInstance.getDefault().getTable("limelight-pom").getEntry("botpose_wpired")
              .getDoubleArray(new double[7]);
        } 
        else {
          botPose = NetworkTableInstance.getDefault().getTable("limelight-pom").getEntry("botpose_wpiblue")
              .getDoubleArray(new double[7]);
        }
      } 
      catch (Exception e) {
        botPose = NetworkTableInstance.getDefault().getTable("limelight-pom").getEntry("botpose_wpired")
            .getDoubleArray(new double[7]);
      }
      finally {
        poseEstimator.addVisionMeasurement(new Pose2d(botPose[0], botPose[1], Rotation2d.fromDegrees(botPose[5])), Timer.getFPGATimestamp() - (botPose[TL]/1000.0));
      }
    }
    odometry.update(mGyro.getRotation2d(), new DifferentialDriveWheelPositions(leftEncoder.getPosition(), rightEncoder.getPosition()));
    poseEstimator.update(mGyro.getRotation2d(), new DifferentialDriveWheelPositions(leftEncoder.getPosition(), rightEncoder.getPosition()));
    field.setRobotPose(getPose());
    
    x = NetworkTableInstance.getDefault().getTable("Vision").getEntry("x").getDouble(0);
    y = NetworkTableInstance.getDefault().getTable("Vision").getEntry("y").getDouble(0);
    isNote = NetworkTableInstance.getDefault().getTable("Vision").getEntry("See?").getBoolean(false);
    if(isNote)
    {
        Pose2d notePose = getPose().transformBy(new Transform2d(x,y, new Rotation2d(-Math.atan(x/y))));
        field.getObject("note").setPose(notePose);
    }
    else{
      x = 0;
      y = 0;
      field.getObject("note").setPose(new Pose2d(-100, -100, Rotation2d.fromDegrees(0)));
    }

  }

  @Override
  public void simulationPeriodic() {
    // This method will be called once per scheduler run during simulation
  }


  @Override
  public void stopMotor(){
    masterLeftMotor.stopMotor();
    masterRightMotor.stopMotor();
  }

  /**
   * sets the mottors to go a distance from the curent position.
   * @param targetPosition the distance to drive
   */
  @Override
  public void setSetPoint(double distance) {
    leftPid.setReference(getLeftEncoder().getPosition() + distance, CANSparkMax.ControlType.kPosition);
    rightPid.setReference(getLeftEncoder().getPosition() + distance, CANSparkMax.ControlType.kPosition);
  }

  /** returns the current pitch of the robot from gyro
   * @return the pitch angle
  */
  public double getPitchAngle() {
    return mGyro.getPitch();
  }

  /**
   * Returns the currently-estimated pose of the robot.
   *
   * @return The pose.
   */
  public Pose2d getPose() {
    return poseEstimator.getEstimatedPosition();
  }
  public Pose2d getPoseOdometry() {
    return odometry.getPoseMeters();
  }

  /**sets all of the motors to a paramater value
   * @param speed the power to set the motors to
  */
  @Override
  public void setMotor(double speed) {
    masterLeftMotor.set(speed);
    masterRightMotor.set(speed);
  }

  /**
   * Returns the current wheel speeds of the robot.
   *
   * @return The current wheel speeds.
   */
  public DifferentialDriveWheelSpeeds getWheelSpeeds() {
    return new DifferentialDriveWheelSpeeds(masterLeftMotor.getEncoder().getVelocity(),
        masterRightMotor.getEncoder().getVelocity());
  }

  /**
   * Drives the robot using arcade controls.
   *
   * @param fwd the commanded forward movement
   * @param rot the commanded rotation
   */
  public void arcadeDrive(double fwd, double rot) {
    //restroe if slew rate doesnt work properly
    // if (Math.abs(output) < Math.abs(fwd / 2)) {
    //   output = fwd / 2;
    // }
    // if (fwd - output > RATE) {
    //   output += RATE;
    // } else if (output - fwd > RATE) {
    //   output -= RATE;
    // }

    mDrive.arcadeDrive(fwd, rot);
  }

  /**
   * Controls the left and right sides of the drive directly with voltages.
   *
   * @param leftVolts  the commanded left output
   * @param rightVolts the commanded right output
   */
  public void tankDriveVolts(double leftVolts, double rightVolts) {
    masterLeftMotor.setVoltage(leftVolts);
    masterRightMotor.setVoltage(rightVolts);
    mDrive.feed();
  }

  /** Resets the drive encoders to currently read a position of 0. */
  @Override
  public void resetEncoder() {
    masterLeftMotor.getEncoder().setPosition(0);
    masterRightMotor.getEncoder().setPosition(0);
  }

  /**
   * Gets the average distance of the two encoders.
   *
   * @return the average of the two encoder readings
   */
  public double getAverageEncoderDistance() {
    return (masterLeftMotor.getEncoder().getPosition()
        + masterRightMotor.getEncoder().getPosition()) / 2.0;
  }

  /**
   * Gets the left drive encoder.
   *
   * @return the left drive encoder
   */
  public RelativeEncoder getLeftEncoder() {
    return masterLeftMotor.getEncoder();
  }

  /**
   * Gets the right drive encoder.
   *
   * @return the right drive encoder
   */
  public RelativeEncoder getRightEncoder() {
    return masterRightMotor.getEncoder();
  }

  /**
   * Sets the max output of the drive. Useful for scaling the drive to drive more
   * slowly.
   *
   * @param maxOutput the maximum output to which the drive will be constrained
   */
  public void setMaxOutput(double maxOutput) {
    mDrive.setMaxOutput(maxOutput);
  }

  /** Zeroes the heading of the robot. */
  public void zeroHeading() {
    mGyro.setFusedHeading(0);
  }

  /**
   * Returns the heading of the robot.
   *
   * @return the robot's heading in degrees, from -180 to 180
   */
  public double getHeading() {
    return mGyro.getFusedHeading();
  }

  /**
   * Returns the turn rate of the robot.
   *
   * @return The turn rate of the robot, in degrees per second
   */
  public double getTurnRate() {
    return -mGyro.getRate();
  }


  public Command tankDriveCommand(Supplier<Double> left, Supplier<Double> right)
  {
    return this.run(() -> tankDriveVolts(left.get(), right.get()));
  }


  public Command arcadeDriveCommand(DoubleSupplier fwd, DoubleSupplier rot) {
    // A split-stick arcade command, with forward/backward controlled by the left
    // hand, and turning controlled by the right.
    return run(() -> mDrive.arcadeDrive(fwd.getAsDouble(), rot.getAsDouble()))
        .withName("arcadeDrive");
  }

  /**
   * Returns a command that will execute a quasistatic test in the given direction.
   *
   * @param direction The direction (forward or reverse) to run the test in
   */
  public Command sysIdQuasistatic(SysIdRoutine.Direction direction) {
    return m_sysIdRoutine.quasistatic(direction);
  }

  /**
   * Returns a command that will execute a dynamic test in the given direction.
   *
   * @param direction The direction (forward or reverse) to run the test in
   */
  public Command sysIdDynamic(SysIdRoutine.Direction direction) {
    return m_sysIdRoutine.dynamic(direction);
  }
  public double calcAngleToSpeaker()
  {
    try{
      return Math.atan((getPose().getY() - SPEAKER_Y) / 
      (DriverStation.getAlliance().get() == Alliance.Red ? getPose().getX() : FIELD_X - getPose().getX()));
    }
    catch(Exception e)
    {
      return 0;

    }
  }

  public Trajectory driveToNoteTrajectory()
  {

    Pose2d notePose = getPose().transformBy(new Transform2d(x,y, new Rotation2d(-Math.atan(x/y))));
    field.getObject("note").setPose(notePose);

    Trajectory t = TrajectoryFactory.trajectoryFactory(getPose(), new ArrayList<Translation2d>(), notePose, false);
    field.getObject("traj").setTrajectory(t);
    return t;
  }
}

