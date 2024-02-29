// RobotBuilder Version: 6.1
//
// This file was generated by RobotBuilder. It contains sections of
// code that are automatically generated and assigned by robotbuilder.
// These sections will be updated in the future when you export to
// Java from RobotBuilder. Do not put any code or make any change in
// the blocks indicating autogenerated code or it will be lost on an
// update. Deleting the comments indicating the section will prevent
// it from being updated in the future.

// ROBOTBUILDER TYPE: Robot.

package frc.robot;

import edu.wpi.first.hal.FRCNetComm.tInstances;
import edu.wpi.first.hal.FRCNetComm.tResourceType;

import static frc.robot.Constants.DriveConstants.ANGLE_TOLERANCE;

import edu.wpi.first.cameraserver.CameraServer;
import edu.wpi.first.hal.HAL;
import edu.wpi.first.wpilibj.DataLogManager;
import edu.wpi.first.wpilibj.TimedRobot;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.CommandScheduler;
import edu.wpi.first.util.datalog.*;



/**
 * The VM is configured to automatically run this class, and to call the
 * functions corresponding to each mode, as described in the TimedRobot
 * documentation. If you change the name of this class or the package after
 * creating this project, you must also update the build.properties file in 
 * the project.
 */
public class Robot extends TimedRobot {
    double map(double x, double in_min, double in_max, double out_min, double out_max)
    {
        return (x - in_min) * (out_max - out_min) / (in_max - in_min) + out_min;
    }
        /**
     * Converts a raw optical inverse-square reading into a fitted, calibrated linear reading in
     * INCHES.
     */
    

    

    private Command m_autonomousCommand;


    private RobotContainer m_robotContainer;
    DataLog log = DataLogManager.getLog();
    


    public void robotInit() {

        // Instantiate our RobotContainer.  This will perform all our button bindings, and put our
        // autonomous chooser on the dashboard.
        m_robotContainer = RobotContainer.getInstance();
        HAL.report(tResourceType.kResourceType_Framework, tInstances.kFramework_RobotBuilder);
        enableLiveWindowInTest(true);

        // colorSensor.configureColorSensor(ColorSensorResolution.kColorSensorRes20bit, ColorSensorMeasurementRate.kColorRate25ms, GainFactor.kGain9x);
                CameraServer.startAutomaticCapture().setFPS(15);
                DataLogManager.start();
                
                


        
    }


    /**
    * This function is called every robot packet, no matter the mode. Use this for items like
    * diagnostics that you want ran during disabled, autonomous, teleoperated and test.
    *
    * <p>This runs after the mode specific periodic functions, but before
    * LiveWindow and SmartDashboard integrated updating.
    */
    @Override
    public void robotPeriodic() {
        // Runs the Scheduler.  This is responsible for polling buttons, adding newly-scheduled
        // commands, running already-scheduled commands, removing finished or interrupted commands,
        // and running subsystem periodic() methods.  This must be called from the robot's periodic
        // block in order for anything in the Command-based framework to work.
        CommandScheduler.getInstance().run();


        
        //Field
        SmartDashboard.putNumber("Field/Pose X Value", m_robotContainer.driveSubsystem.getPose().getX());
        SmartDashboard.putNumber("Field/Pose Y Value", m_robotContainer.driveSubsystem.getPose().getY());
        SmartDashboard.putNumber("Field/Pose Rotation Value", m_robotContainer.driveSubsystem.getPose().getRotation().getDegrees());
        SmartDashboard.putNumber("Field/Yaw", m_robotContainer.driveSubsystem.getHeading());

        //Drive
        SmartDashboard.putNumber("Drive/Encoder/LeftEncoder/Velocity",m_robotContainer.driveSubsystem.getLeftEncoder().getVelocity());
        SmartDashboard.putNumber("Drive/Encoder/RightEncoder/Velocity",m_robotContainer.driveSubsystem.getRightEncoder().getVelocity());
        SmartDashboard.putNumber("Drive/Encoder/LeftEncoder/Position", m_robotContainer.driveSubsystem.getLeftEncoder().getPosition());
        SmartDashboard.putNumber("Drive/Encoder/RightEncoder/Position", m_robotContainer.driveSubsystem.getRightEncoder().getPosition());
        SmartDashboard.putNumber("Drive/Encoder/Average Speed", (m_robotContainer.driveSubsystem.getLeftEncoder().getVelocity() + m_robotContainer.driveSubsystem.getRightEncoder().getVelocity())/2);
        SmartDashboard.putBoolean("Drive/Is Angle To Speaker", (m_robotContainer.driveSubsystem.calcAngleToSpeaker() < ANGLE_TOLERANCE));
      


        //Intake Tab
        SmartDashboard.putNumber("Transfer/Color/Red", m_robotContainer.transferSubsystem.colorSensor.getRed());
        SmartDashboard.putNumber("Transfer/Color/Green", m_robotContainer.transferSubsystem.colorSensor.getGreen());
        SmartDashboard.putNumber("Transfer/Color/Blue", m_robotContainer.transferSubsystem.colorSensor.getBlue());
        SmartDashboard.putString("Transfer/Color/Color",m_robotContainer.transferSubsystem.colorSensor.getColor().toHexString());
        SmartDashboard.putBoolean("Transfer/Color/IsNoteIn",m_robotContainer.transferSubsystem.isNoteIn());
        // SmartDashboard.putNumber("Intake/Lift/Encoder Position", m_robotContainer.intakeLiftSubsystem.getEncoderPosition());


        //Shooting Tab
        // SmartDashboard.putNumber("Arm/Encoder Position", m_robotContainer.shootingArmSubsystem.getEncoderPosition());
        // SmartDashboard.putNumber("Arm/Speed", m_robotContainer.shootingArmSubsystem.getEncoder().getVelocity());
        // SmartDashboard.putBoolean("Arm/Intake Can Move", m_robotContainer.shootingArmSubsystem.intakeCanMove().getAsBoolean());
        // SmartDashboard.putBoolean("Arm/Limit Switch", m_robotContainer.shootingArmSubsystem.isFoldSwitchPressed());
        SmartDashboard.putNumber("Shooting/Speed", m_robotContainer.shootingSubsystem.getRate());
        SmartDashboard.putBoolean("Shooting/Is At Wanted Speed", isAutonomous());
        // m_robotContainer.ledSubsystem.setLeds(m_robotContainer.intakeSubsystem.colorSensor.getRed(), m_robotContainer.intakeSubsystem.colorSensor.getGreen(), m_robotContainer.intakeSubsystem.colorSensor.getBlue());        



       


    }
    
    
    /**
     * This function is called once each time the robot enters Disabled mode.
     */
    @Override
    public void disabledInit() {
        // m_robotContainer.ledSubsystem.setLeds(Shuffleboard.getTab("Intake").getComponents().)
        m_robotContainer.driveSubsystem.stopMotor();
        // m_robotContainer.intakeLiftSubsystem.stopMotor();
        m_robotContainer.intakeRollerSubsystem.stopMotor();
        m_robotContainer.shootingSubsystem.stopMotor();
        // m_robotContainer.shootingArmSubsystem.stopMotor();
        m_robotContainer.transferSubsystem.stopMotor();
    }
    
    @Override
    public void disabledPeriodic() {



    }
    
    /**
     * This autonomous runs the autonomous command selected by your {@link RobotContainer} class.
     */
    @Override
    public void autonomousInit() {
        m_autonomousCommand = m_robotContainer.getAutonomousCommand();

        // schedule the autonomous command (example)
        if (m_autonomousCommand != null) {
            m_autonomousCommand.schedule();
        }
    }
    
    /**
     * This function is called periodically during autonomous.
     */
    @Override
    public void autonomousPeriodic() {
    }
    
    @Override
    public void teleopInit() {
        // This makes sure that the autonomous stops running when
        // teleop starts running. If you want the autonomous to
        // continue until interrupted by another command, remove
        // this line or comment it out.
        if (m_autonomousCommand != null) {  
            m_autonomousCommand.cancel();
        }
    }

    /**
     * This function is called periodically during operator control.
     */
    @Override
    public void teleopPeriodic() {
        // Uncomment This To GO RAINBOW
        // m_robotContainer.ledSubsystem.rainbow();

        
       
    }   

    @Override
    public void testInit() {
        // Cancels all running commands at the start of test mode.
        CommandScheduler.getInstance().cancelAll();
    }

    /**
    * This function is called periodically during test mode.
    */
    @Override
    public void testPeriodic() {
    }

}