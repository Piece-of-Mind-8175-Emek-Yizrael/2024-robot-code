package frc.robot.Subsystems.intake_subsystems;

import static frc.robot.Constants.IntakeConstants.*;

import com.ctre.phoenix.motorcontrol.can.WPI_TalonSRX;

import edu.wpi.first.math.controller.PIDController;
import edu.wpi.first.wpilibj.Encoder;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.RunCommand;
import frc.robot.Subsystems.PomSubsystem;

public class IntakeLiftSubsystem extends PomSubsystem{
    WPI_TalonSRX motor;
    Encoder encoder;
    PIDController pid;
    public IntakeLiftSubsystem()
    {
        motor = new WPI_TalonSRX(0);
        encoder = new Encoder(ENCODER_PORTS[0], ENCODER_PORTS[1]);
        pid = new PIDController(KP, KI, KD);
        pid.setTolerance(TOLERANCE);
        motor.clearStickyFaults();
    }

    @Override
    public void stopMotor()
    {
        motor.set(0);
    }

    @Override
    public void setMotor(double speed)
    {
        motor.set(speed);
    }

    @Override
    public void resetEncoder()
    {
        encoder.reset();
    }

    @Override
    public double getEncoderPosition()
    {
        return encoder.getDistance();
    }

    public Command OpenCloseIntake(boolean open)
    {
        pid.setSetpoint(open ? GROUND : FOLD);
        return new RunCommand(() -> setMotor(pid.calculate(encoder.getDistance())), this).until(() -> pid.atSetpoint()).andThen(this.runOnce(() -> stopMotor()));
    }
}