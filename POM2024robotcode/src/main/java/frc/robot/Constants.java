// RobotBuilder Version: 6.1
//
// This file was generated by RobotBuilder. It contains sections of
// code that are automatically generated and assigned by robotbuilder.
// These sections will be updated in the future when you export to
// Java from RobotBuilder. Do not put any code or make any change in
// the blocks indicating autogenerated code or it will be lost on an
// update. Deleting the comments indicating the section will prevent
// it from being updated in the future.


package frc.robot;

import edu.wpi.first.math.kinematics.DifferentialDriveKinematics;

/**
 * The Constants class provides a convenient place for teams to hold robot-wide numerical or boolean
 * constants.  This class should not be used for any other purpose.  All constants should be
 * declared globally (i.e. public static).  Do not put anything functional in this class.
 *
 * <p>It is advised to statically import this class (or one of its inner classes) wherever the
 * constants are needed, to reduce verbosity.
 */
public class Constants {
    public static final class JoystickConstants {
        public static final int DRIVE_JOYSTICK = 0;
        public static final int OPERATE_JOYSTICK = 1;

        /** A button axis */
        public static final int A = 1;
        /** B button axis */
        public static final int B = 2;
        /** X button axis */
        public static final int X = 3;
        /** Y button axis */
        public static final int Y = 4;
        /** LB button axis */
        public static final int LB = 5;
        /**RB button axis */
        public static final int RB = 6;

        /** left joystick click button axis */
        public static final int LEFT_JOYSTICK_CLICK = 9;
        /** right joystick click button axis */
        public static final int RIGHT_JOYSTICK_CLICK = 10;

        /** left trigger button axis */
        public static final int LEFT_TRIGGER = 2;
        /** right trigger button axis */
        public static final int RIGHT_TRIGGER = 3;

        public static final int LEFT_JOYSTICK_Y = 1;
        public static final int LEFT_JOYSTICK_X = 0;
        public static final int RIGHT_JOYSTICK_Y = 5;
        public static final int RIGHT_JOYSTICK_X = 4;

        public static final int POV_DOWN = 180;
        public static final int POV_LEFT = 270;
        public static final int POV_RIGHT = 90;
        public static final int POV_UP = 0;
        public static final int POV_NONE = -1;

        public static final double THRESHOLD = 0.3;
    }

    public static final class DriveConstants
    {
        public static final int LEFT_MOTOR_LEAD = 1;
        public static final int LEFT_MOTOR_SLAVE = 2;
        public static final int RIGHT_MOTOR_LEAD = 3;
        public static final int RIGHT_MOTOR_SLAVE = 4;

        public static final int GYRO_ID = 7;

        public static final double TO_RADIANS = 1 / 180 * Math.PI;
        public static final double ROTATIONS_TO_METERS = 1 / 8.45 * 15.24 * Math.PI / 100;

        //all below need to be calculated
        public static final double KS_VOLTS = 0.5;
        public static final double KV_VOLT_SECOND_PER_METER  = 2.8419;
        public static final double KA_VOLT_SECONDS_SQUARE_PER_METER = 1.4226;

        public static final double KP = 0.8;
        public static final double KI = 0.02;
    
        public static final double KP_DRIVE_VEL = 0.002;
        public static final double KTRACK_WIDTH_METERS = 0.54;
        public static final DifferentialDriveKinematics DRIVE_KINEMATICS =
            new DifferentialDriveKinematics(KTRACK_WIDTH_METERS);
    
        public static final double MAX_SPEED_METER_PER_SECOND = 2;
        public static final double MAX_ACCELERATION_METERS_PER_SECOND_SQUARED = 2;

        public static final double RATE = 1.3; //Check!
        public static final double ANGLE_TOLERANCE = 2;

        // const values we dont need to calculate
        public static final double RAMSETE_B = 2;
        public static final double RAMSETE_ZETA = 0.7;
    }

    public static final class IntakeConstants{
       public static final int ULTRASOINC_PORT = 0;
       public static final int ROLLER_MOTOR = 5;
    }

    public static final class LedsConstants{
        public static final int pRED = 0;
        public static final int pGREEN = 1;
        public static final int pBLUE = 2;
        public static final int PWM = 9;
        public static final int NUM_LEDS = 180;
        public static final int LED_PORT = 9;
    }

    public static final class GeneralFunctions{
        /**
         * checks if you reached set point with an alowed error
         * @param state the current state
         * @param setPoint the set point
         * @param tolerance the allowed error
         * @return have you reached
         */
        public static boolean allowedError(double state, double setPoint, double tolerance)
        {
            return Math.abs(state - setPoint) < tolerance; 
        }
    }

}

