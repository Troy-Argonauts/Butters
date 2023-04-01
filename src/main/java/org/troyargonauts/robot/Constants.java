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
        int WHEEL_DIAMETER = 6;
        MotorController.GearingParameters gearingLowGear = new MotorController.GearingParameters(1.0 / 19.6, Units.inchesToMeters(WHEEL_DIAMETER / 2.0), 2048);
        MotorController.GearingParameters gearingHighGear = new MotorController.GearingParameters(1.0 / 9.06677, Units.inchesToMeters(WHEEL_DIAMETER / 2.0), 2048);

        int SHIFTING_AMP_THRESHOLD = 10;
        //PID Tuning Values
        double DRIVE_P = 0.08;
        double DRIVE_I = 0.005;
        double DRIVE_D = 0.03;

        double TURN_P = 1;
        double TURN_I = 0;
        double TURN_D = 0;
        double BALANCE_THRESHOLD = 1.5;
        double DRIVE_TOLERANCE = 1;
        double TURN_TOLERANCE_DEG = 1;
        double VELOCITY_TOLERANCE = 0.05;

        int LOW_HIGH_THRESHOLD = 4362;
        int HIGH_LOW_THRESHOLD = 2018;
        double SHIFTING_THRESHOLD = 1;
    }

    public interface LEDs {
        int CANDLE = 24;
        int ledLength = 141;
    }

    public interface Arm {
        int ARM_PORT = 9;
        double ARM_P = 0.00008;
        double ARM_I = 0;
        double ARM_D = 0;
        double ARM_TOLERANCE = 5;
        double ARM_GEAR_RATIO = 125;
        double MAXIMUM_SPEED = 0.6;
        int ARM_UPPER_LIMIT_PORT = 3;
        int ARM_LOWER_LIMIT_PORT = 4;
    }

    public interface Wrist {
        int WRIST_PORT = 8;
        int ROTATE_PORT = 7;
        double[] WRIST_GAINS = {0.005, 0, 0};
        double WRIST_GEAR_RATIO = 49;
        double ROTATE_GEAR_RATIO = 100;
        double MAXIMUM_SPEED = 1;
        int WRIST_UPPER_LIMIT_PORT = 1;
        int WRIST_LOWER_LIMIT_PORT = 2;
    }

    public interface Elevator {
        int LIFT_MOTOR_PORT = 10;
        double kP = 0.01;
        double kI = 0;
        double kD = 0;
        double MAXIMUM_SPEED = 0.6;
        double ELEVATOR_GEARBOX_SCALE = 1;
        int TOP_PORT = 1;
        int BOTTOM_PORT = 0;
    }
}
