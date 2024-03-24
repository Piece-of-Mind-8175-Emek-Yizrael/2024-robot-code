// RobotBuilder Version: 6.1
//
// This file was generated by RobotBuilder. It contains sections of
// code that are automatically generated and assigned by robotbuilder.
// These sections will be updated in the future when you export to
// Java from RobotBuilder. Do not put any code or make any change in
// the blocks indicating autogenerated code or it will be lost on an
// update. Deleting the comments indicating the section will prevent
// it from being updated in the future.

// ROBOTBUILDER TYPE: RobotContainer.

package frc.robot;


// END AUTOGENERATED CODE, SOURCE=ROBOTBUILDER ID=IMPORTS
import static frc.robot.Constants.JoystickConstants.A;
import static frc.robot.Constants.JoystickConstants.B;
import static frc.robot.Constants.JoystickConstants.BACK;
import static frc.robot.Constants.JoystickConstants.DRIVE_JOYSTICK;
import static frc.robot.Constants.JoystickConstants.LB;
import static frc.robot.Constants.JoystickConstants.LEFT_JOYSTICK_Y;
import static frc.robot.Constants.JoystickConstants.LEFT_TRIGGER;
import static frc.robot.Constants.JoystickConstants.OPERATE_JOYSTICK;
import static frc.robot.Constants.JoystickConstants.RB;
import static frc.robot.Constants.JoystickConstants.RIGHT_JOYSTICK_Y;
import static frc.robot.Constants.JoystickConstants.RIGHT_TRIGGER;
import static frc.robot.Constants.JoystickConstants.START;
import static frc.robot.Constants.JoystickConstants.THRESHOLD;
import static frc.robot.Constants.JoystickConstants.X;
import static frc.robot.Constants.JoystickConstants.Y;
import static frc.robot.Constants.LedsConstants.POM_PURPLE;
import static frc.robot.Constants.ShootingConstants.SHOOT_AMP_POS;
import static frc.robot.Constants.ShootingConstants.SHOOT_AMP_SPEED;
import static frc.robot.Constants.ArmLockConstants.LOCK_ANGLE;
import static frc.robot.Constants.ArmLockConstants.OPEN_ANGLE;

import edu.wpi.first.wpilibj.GenericHID;
import edu.wpi.first.wpilibj.XboxController;
import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj.util.Color;
// BEGIN AUTOGENERATED CODE, SOURCE=ROBOTBUILDER ID=IMPORTS
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.ConditionalCommand;
import edu.wpi.first.wpilibj2.command.InstantCommand;
import edu.wpi.first.wpilibj2.command.PrintCommand;
import edu.wpi.first.wpilibj2.command.RunCommand;
import edu.wpi.first.wpilibj2.command.StartEndCommand;
import edu.wpi.first.wpilibj2.command.WaitCommand;
import edu.wpi.first.wpilibj2.command.button.CommandJoystick;
import edu.wpi.first.wpilibj2.command.button.CommandXboxController;
import edu.wpi.first.wpilibj2.command.button.Trigger;
import frc.robot.Commands.autonomousCommands;
import frc.robot.Subsystems.ArmLockSubsystem;
import frc.robot.Subsystems.DriveSubsystem;
import frc.robot.Subsystems.TransferSubsystem;
import frc.robot.Subsystems.intake_subsystems.IntakeLiftSubsystem;
import frc.robot.Subsystems.intake_subsystems.IntakeRollerSubsystem;
import frc.robot.Subsystems.led_subsystems.LedSubsystem;
import frc.robot.Subsystems.shooting_subsystems.ShootingArmSubsystem;
import frc.robot.Subsystems.shooting_subsystems.ShootingSubsystem;


/**
 * This class is where the bulk of the robot should be declared.  Since Command-based is a
 * "declarative" paradigm, very little robot logic should actually be handled in the {@link Robot}
 * periodic methods (other than the scheduler calls).  Instead, the structure of the robot
 * (including subsystems, commands, and button mappings) should be declared here.
 */
public class RobotContainer {

  private static RobotContainer m_robotContainer = new RobotContainer();

    // BEGIN AUTOGENERATED CODE, SOURCE=ROBOTBUILDER ID=DECLARATIONS
// The robot's subsystems
    public final DriveSubsystem driveSubsystem = new DriveSubsystem();
    public final LedSubsystem ledSubsystem = new LedSubsystem();
    public final IntakeLiftSubsystem intakeLiftSubsystem = new IntakeLiftSubsystem();
    public final IntakeRollerSubsystem intakeRollerSubsystem = new IntakeRollerSubsystem();
    public final ShootingSubsystem shootingSubsystem = new ShootingSubsystem();
    public final ShootingArmSubsystem shootingArmSubsystem = new ShootingArmSubsystem();
    public final TransferSubsystem transferSubsystem = new TransferSubsystem();
    public final ArmLockSubsystem armLockSubsystem = new ArmLockSubsystem();
    public final autonomousCommands autonomousCommands = new autonomousCommands(driveSubsystem, intakeLiftSubsystem, intakeRollerSubsystem, shootingSubsystem, shootingArmSubsystem, transferSubsystem);


    // public CameraServer cameraServer;
    
// Joysticks
    
    public CommandXboxController driverCommandJoystick = new CommandXboxController(DRIVE_JOYSTICK);
    public CommandJoystick operateCommandJoystick = new CommandJoystick(OPERATE_JOYSTICK);

  //Commands  
  
  // END AUTOGENERATED CODE, SOURCE=ROBOTBUILDER ID=DECLARATIONS
  
  // A chooser for autonomous commands
  SendableChooser<Command> m_chooser = new SendableChooser<>();
  
  /**
   * The container for the robot.  Contains subsystems, OI devices, and commands.
   */
  private RobotContainer() {
    // BEGIN AUTOGENERATED CODE, SOURCE=ROBOTBUILDER ID=SMARTDASHBOARD    
    intakeLiftSubsystem.setArmSup(shootingArmSubsystem.intakeCantMove());
    shootingArmSubsystem.setIntakeSup(intakeLiftSubsystem.armCantMove());
    driveSubsystem.setDefaultCommand(driveSubsystem.arcadeDriveCommand(() -> driverCommandJoystick.getRawAxis(RIGHT_JOYSTICK_Y) * 0.3, () -> (driverCommandJoystick.getRawAxis(LEFT_TRIGGER) - driverCommandJoystick.getRawAxis(RIGHT_TRIGGER)) * 0.3, driverCommandJoystick.povUp()));
    ledSubsystem.setDefaultCommand(new ConditionalCommand(ledSubsystem.setLedCommand(Color.kOrange), ledSubsystem.setLedCommand(Color.kBlack), () -> transferSubsystem.isNoteIn()));



    m_chooser.setDefaultOption("none", new InstantCommand());
    m_chooser.addOption("shoot", autonomousCommands.shoot());
    m_chooser.addOption("move out", autonomousCommands.moveOut());
    m_chooser.addOption("shoot move out", autonomousCommands.shootMoveOut());
    m_chooser.addOption("shoot try collect", autonomousCommands.shootTryCollect());
    m_chooser.addOption("shoot collect shoot", autonomousCommands.shootCollectShoot());
    
    configureButtonBindings();    
    SmartDashboard.putData("Auto Mode", m_chooser);
  }
  
  public static RobotContainer getInstance() {
    return m_robotContainer;
  }

  /**
   * Use this method to define your button->command mappings.  Buttons can be created by
   * instantiating a {@link GenericHID} or one of its subclasses ({@link
   * edu.wpi.first.wpilibj.Joystick} or {@link XboxController}), and then passing it to a
   * {@link edu.wpi.first.wpilibj2.command.button.JoystickButton}.
   */
  private void configureButtonBindings() {
    // BEGIN AUTOGENERATED CODE, SOURCE=ROBOTBUILDER ID=BUTTONS
    // Create some buttons
    new Trigger(operateCommandJoystick.button(A)).onTrue(intakeNoteCommand());
    new Trigger(operateCommandJoystick.button(X)).onTrue(closeIntakeCommand());
    new Trigger(operateCommandJoystick.axisGreaterThan(RIGHT_TRIGGER, THRESHOLD)).onTrue(shootingSubsystem.spinWheelsCommand());
    new Trigger(operateCommandJoystick.axisLessThan(LEFT_JOYSTICK_Y, -THRESHOLD)).onTrue(intakeRollerSubsystem.intakeNoteCommand().raceWith(transferSubsystem.getFromIntake()).until(operateCommandJoystick.button(B)));
    new Trigger(operateCommandJoystick.button(RB)).onTrue(shootingSubsystem.stopWheelsCommand().alongWith(transferSubsystem.stopWheelsCommand()));
    new Trigger(operateCommandJoystick.button(LB)).onTrue((shootingSubsystem.spinWheelsToSpeedCommand(SHOOT_AMP_SPEED).raceWith(transferSubsystem.amp())).andThen(shootingSubsystem.stopWheelsCommand()));
    new Trigger(operateCommandJoystick.button(Y)).onTrue(transferSubsystem.transfer(false).raceWith(intakeRollerSubsystem.outakeNoteCommand()).until(operateCommandJoystick.button(B)));
    // new Trigger(operateCommandJoystick.axisGreaterThan(LEFT_TRIGGER, THRESHOLD)).onTrue((transferSubsystem.outForShootCommand().raceWith(intakeRollerSubsystem.slow(false))).andThen(new WaitCommand(0.15)).andThen((transferSubsystem.inForShootCommand().raceWith(intakeRollerSubsystem.intakeNoteCommand()))).andThen(transferSubsystem.transfer(true)).andThen(shootingSubsystem.stopWheelsCommand()));    
    new Trigger(operateCommandJoystick.axisGreaterThan(LEFT_TRIGGER, THRESHOLD)).onTrue(((transferSubsystem.transfer(true).raceWith(intakeRollerSubsystem.slow(true))).andThen(shootingSubsystem.stopWheelsCommand())));    
    new Trigger(operateCommandJoystick.povLeft()).onTrue(shootingArmSubsystem.closeSlow()); 
    new Trigger(operateCommandJoystick.povRight()).onTrue(shootingArmSubsystem.goToAngleCommand(SHOOT_AMP_POS)); 
    new Trigger(operateCommandJoystick.povDown()).whileTrue(shootingArmSubsystem.joystickShootCommand(() -> operateCommandJoystick.getRawAxis(RIGHT_JOYSTICK_Y) * -0.2)); 
    new Trigger(operateCommandJoystick.povUp()).whileTrue((shootingArmSubsystem.goToAngleCommand(0.25).raceWith(shootingSubsystem.spinWheelsCommand())).andThen(transferSubsystem.transfer(true)).andThen((shootingSubsystem.stopWheelsCommand().alongWith(transferSubsystem.stopWheelsCommand()))).andThen(shootingArmSubsystem.closeSlow()));
    new Trigger(operateCommandJoystick.button(START)).onTrue(armLockSubsystem.TurnTo(LOCK_ANGLE));
    new Trigger(operateCommandJoystick.button(BACK)).onTrue(armLockSubsystem.TurnTo(OPEN_ANGLE));
    // new Trigger(operateCommandJoystick.button(BACK)).onTrue(armLockSubsystem.TurnTo(OPEN_ANGLE).andThen(shootingArmSubsystem.servo_closed = false));
    // new Trigger(operateCommandJoystick.button(START)).onTrue(Commands.print("intake").andThen(intakeLiftSubsystem.OpenCloseIntakeTimers(true)).until(operateCommandJoystick.button(B)));
    // new Trigger(operateCommandJoystick.button(BACK)).onTrue(Commands.print("intake").andThen(intakeLiftSubsystem.OpenCloseIntakeTimers(false)).until(operateCommandJoystick.button(B)));
    new Trigger(operateCommandJoystick.button(9)).whileTrue(ledSubsystem.setLedCommand(POM_PURPLE));
  }  
  /**
   * Use this to pass the autonomous command to the main {@link Robot} class.
   *
   * @return the command to run in autonomous
   */
  public Command getAutonomousCommand() {
    // The selected command will be run in autonomous
    return m_chooser.getSelected();
  }
  
  public Command intakeNoteCommand()
  {
    var openIntake = shootingArmSubsystem.OpenForIntakeCommand().andThen(intakeLiftSubsystem.OpenCloseIntakeTimers(true));
    return (openIntake.unless(intakeLiftSubsystem::isOpen)
      .andThen(shootingArmSubsystem.closeSlow())
      .andThen(new WaitCommand(0.1))
      .andThen(intakeRollerSubsystem.intakeNoteCommand().raceWith(transferSubsystem.getFromIntake()))).until(operateCommandJoystick.button(B));
  }

  public Command closeIntakeCommand()
  {
    return (shootingArmSubsystem.OpenForIntakeCommand().andThen(intakeLiftSubsystem.OpenCloseIntakeTimers(false))).until(operateCommandJoystick.button(B)).unless(intakeLiftSubsystem::isClosed);
  }

  public Command climb()
  {
    return shootingArmSubsystem.Climb().
    andThen(new PrintCommand("now servo")).
    andThen(armLockSubsystem.TurnTo(LOCK_ANGLE).alongWith(new StartEndCommand(() -> shootingArmSubsystem.setMotor(-0), shootingArmSubsystem::stopMotor, shootingArmSubsystem)).alongWith(new RunCommand(() -> {}))).withTimeout(2);
  }
  
}

