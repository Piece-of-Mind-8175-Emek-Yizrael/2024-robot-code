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


import frc.robot.Commands.DriveCommands.TurnToSpeaker;
import frc.robot.Subsystems.*;
import frc.robot.Subsystems.intake_subsystems.IntakeLiftSubsystem;
import frc.robot.Subsystems.intake_subsystems.IntakeRollerSubsystem;
import frc.robot.Subsystems.led_subsystems.LeftLedSubsystem;
import frc.robot.Subsystems.led_subsystems.RightLedSubsystem;
import frc.robot.Subsystems.shooting_subsystems.ShootingArmSubsystem;
import frc.robot.Subsystems.shooting_subsystems.ShootingSubsystem;
import edu.wpi.first.cameraserver.CameraServer;
import edu.wpi.first.wpilibj.GenericHID;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.XboxController;
import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj.util.Color;
// BEGIN AUTOGENERATED CODE, SOURCE=ROBOTBUILDER ID=IMPORTS
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.Commands;
import edu.wpi.first.wpilibj2.command.ConditionalCommand;
import edu.wpi.first.wpilibj2.command.WaitCommand;
import edu.wpi.first.wpilibj2.command.button.CommandJoystick;
import edu.wpi.first.wpilibj2.command.button.Trigger;

import static frc.robot.Constants.DriveConstants.ANGLE_TOLERANCE;
import static frc.robot.Constants.JoystickConstants.*;
import static frc.robot.Constants.LedsConstants.POM_PURPLE;
import static frc.robot.Constants.ShootingConstants.*;



// END AUTOGENERATED CODE, SOURCE=ROBOTBUILDER ID=IMPORTS


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
    public final LeftLedSubsystem leftledSubsystem = new LeftLedSubsystem();
    public final RightLedSubsystem rightLedSubsystem = new RightLedSubsystem();
    public final IntakeLiftSubsystem intakeLiftSubsystem = new IntakeLiftSubsystem();
    public final IntakeRollerSubsystem intakeRollerSubsystem = new IntakeRollerSubsystem();
    public final ShootingSubsystem shootingSubsystem = new ShootingSubsystem();
    public final ShootingArmSubsystem shootingArmSubsystem = new ShootingArmSubsystem();
    public final TransferSubsystem transferSubsystem = new TransferSubsystem();

    public CameraServer cameraServer;
    
// Joysticks
    public Joystick driveJoystick = new Joystick(DRIVE_JOYSTICK);
    public Joystick operateJoystick = new Joystick(OPERATE_JOYSTICK);
    
    public CommandJoystick driverCommandJoystick = new CommandJoystick(DRIVE_JOYSTICK);
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
    intakeLiftSubsystem.setArmSup(shootingArmSubsystem.intakeCanMove());
    shootingArmSubsystem.setIntakeSup(intakeLiftSubsystem.armCanMove());
    // Smartdashboard Subsystems
    driveSubsystem.setDefaultCommand(driveSubsystem.arcadeDriveCommand(() -> driveJoystick.getRawAxis(RIGHT_JOYSTICK_Y), () -> driveJoystick.getRawAxis(LEFT_TRIGGER) - driveJoystick.getRawAxis(RIGHT_TRIGGER)));
    
    leftledSubsystem.setDefaultCommand(
      (new ConditionalCommand(
        leftledSubsystem.setLedCommand(Color.kKhaki),
        new ConditionalCommand(
          leftledSubsystem.setLedCommand(Color.kOrange),
          leftledSubsystem.setLedCommand(POM_PURPLE), 
          () -> transferSubsystem.isNoteIn()), 
        () -> shootingSubsystem.atWantedSpeed() && shootingSubsystem.getRate() > 0).
      andThen(new WaitCommand(0.2))).
    repeatedly());

    rightLedSubsystem.setDefaultCommand(
      (new ConditionalCommand(
        leftledSubsystem.setLedCommand(POM_PURPLE),
        new ConditionalCommand(
          leftledSubsystem.setLedCommand(Color.kOrange), 
          leftledSubsystem.setLedCommand(Color.kTurquoise),
          () -> driveSubsystem.calcAngleToSpeaker() < ANGLE_TOLERANCE),
        () -> transferSubsystem.isNoteIn()).
     andThen(new WaitCommand(0.2))).
     repeatedly());

     
    // SmartDashboard Buttons
    
    // END AUTOGENERATED CODE, SOURCE=ROBOTBUILDER ID=SMARTDASHBOARD
    // Configure the button bindings
    configureButtonBindings();
    
    // Configure default commands
    // BEGIN AUTOGENERATED CODE, SOURCE=ROBOTBUILDER ID=SUBSYSTEM_DEFAULT_COMMAND
    
    
    // END AUTOGENERATED CODE, SOURCE=ROBOTBUILDER ID=SUBSYSTEM_DEFAULT_COMMAND
    
    // Configure autonomous sendable chooser
    // BEGIN AUTOGENERATED CODE, SOURCE=ROBOTBUILDER ID=AUTONOMOUS
    
    // m_chooser.setDefaultOption();
    
    // END AUTOGENERATED CODE, SOURCE=ROBOTBUILDER ID=AUTONOMOUS
    
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
    new Trigger(operateCommandJoystick.povLeft()).onTrue(shootingArmSubsystem.goToAngleCommand(SUB_INTAKE_POS));
    new Trigger(operateCommandJoystick.povUp()).onTrue(shootingArmSubsystem.goToAngleCommand(SHOOT_AMP_POS));
    new Trigger(operateCommandJoystick.povDown()).onTrue(shootingArmSubsystem.goToAngleCommand(shootingArmSubsystem.calcArmPosForShoot(() -> driveSubsystem.getPose())));
    new Trigger(operateCommandJoystick.povRight()).onTrue(shootingArmSubsystem.goToAngleCommand(SHOOT_PODIUM_POS));
    new Trigger(operateCommandJoystick.axisGreaterThan(RIGHT_TRIGGER, THRESHOLD)).onTrue(shootingSubsystem.spinWheelsCommand());
    new Trigger(operateCommandJoystick.axisGreaterThan(RB, THRESHOLD)).onTrue(shootingSubsystem.stopWheelsCommand().alongWith(leftledSubsystem.runOnce(()->{})).withTimeout(0.2));
    new Trigger(operateCommandJoystick.axisGreaterThan(LEFT_TRIGGER, THRESHOLD)).onTrue(transferSubsystem.transfer(true).andThen(shootingSubsystem.stopWheelsCommand()));

    new Trigger(driverCommandJoystick.button(X)).onTrue(new TurnToSpeaker(driveSubsystem).andThen(leftledSubsystem.setLedCommand(Color.kKhaki).until(() -> !transferSubsystem.isNoteIn() || driverCommandJoystick.button(B).getAsBoolean())));


    // END AUTOGENERATED CODE, SOURCE=ROBOTBUILDER ID=BUTTONS


  }
  
  // BEGIN AUTOGENERATED CODE, SOURCE=ROBOTBUILDER ID=FUNCTIONS
  
  // END AUTOGENERATED CODE, SOURCE=ROBOTBUILDER ID=FUNCTIONS
  
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
    return (new ConditionalCommand(
        shootingArmSubsystem.OpenForIntakeCommand().
          andThen(intakeLiftSubsystem.OpenCloseIntake(true)), 
        Commands.runOnce(() -> {}), () -> !intakeLiftSubsystem.isOpen())
        .andThen(shootingArmSubsystem.goToAngleCommand(SUB_INTAKE_POS)).
        andThen(intakeRollerSubsystem.intakeNoteCommand().raceWith(transferSubsystem.getFromIntake()))).until(operateCommandJoystick.button(B));
  }

  

  public Command closeIntakeCommand()
  {
    return shootingArmSubsystem.OpenForIntakeCommand().andThen(intakeLiftSubsystem.OpenCloseIntake(false));
  }
  
}

