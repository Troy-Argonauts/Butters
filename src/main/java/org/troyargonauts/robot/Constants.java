package org.troyargonauts.robot;

import edu.wpi.first.math.util.Units;
import org.troyargonauts.common.motors.wrappers.MotorController;

public final class Constants {
  
    public interface DriveTrain {
        //CAN IDs
        int FRONT_RIGHT = 6;
        int TOP_RIGHT = 4;
        int REAR_RIGHT = 5;
        int FRONT_LEFT = 1;
        int TOP_LEFT = 3;
        int REAR_LEFT = 2;
        int PIGEON = 25;
        double DEADBAND = 0.05;

        //Values in Inches
        int WHEEL_DIAMETER = 6;

        MotorController.GearingParameters gearingLowGear = new MotorController.GearingParameters(1.0 / 19.6, Units.inchesToMeters(WHEEL_DIAMETER / 2.0), 2048);
        MotorController.GearingParameters gearingHighGear = new MotorController.GearingParameters(1.0 / 9.06677, Units.inchesToMeters(WHEEL_DIAMETER / 2.0), 2048);

        int AMPS_THRESHOLD = 10;
        //PID Tuning Values
        double DRIVE_P = 0.08;
        double DRIVE_I = 0.005;
        double DRIVE_D = 0.03;

        double TURN_P = 1;
        double TURN_I = 0;
        double TURN_D = 0;

        double DRIVE_TOLERANCE = 1;
        double TURN_TOLERANCE_DEG = 1;
        double VELOCITY_TOLERANCE = 0.05;

        int LOW_HIGH_THRESHOLD = 4362;
        int HIGH_LOW_THRESHOLD = 2018;
        double SHIFTING_THRESHOLD = 1;
    }

    public interface Intake {
        double SQUEEZE_MOTOR_SPEED = 0.2;
        double ROTATE_MOTOR_SPEED = 0.4;

        int SQUEEZE_MOTOR_PORT = 1;
        int ROTATE_MOTOR_PORT = 8;
        int TOP_LIMIT_SWITCH = 0;
        int OUT_LIMIT_SWTICH = 1;

        double kSqueezeP = 0.015;
        double kSqueezeI = 0.0001;
        double kSqueezeD = 0.001;

        double kRotateP = 1;
        double kRotateI = 0;
        double kRotateD = 0;

        double TOLERANCE = 0.25;
        double VELOCITY_TOLERANCE = 0.01;
    }

    public interface LEDs {
        int CANDLE = 24;
        int ledLength = 141;
    }

    public interface Arm {
        int ARM_PORT = 7;
        int WRIST_PORT = 8;
        int ROLLER_PORT = 9;

        double WRIST_P = 0.142;
        double WRIST_I = 0;
        double WRIST_D = 0;
        double WRIST_TOLERANCE = 0.1;

        double ARM_P = 0.1;
        double ARM_I = 0;
        double ARM_D = 0;
        double ARM_TOLERANCE = 0.1;

        double ARM_GEAR_RATIO = 125;
        double WRIST_GEAR_RATIO = 28;
    }

    public interface Elevator {
        int LIFT_MOTOR_PORT = 11;

        double kP = 1;
        double kI = 0;
        double kD = 0;

        double ELEVATOR_GEARBOX_SCALE = 1;

        int TOP_PORT = 1;
        int BOTTOM_PORT = 0;
    }
}
