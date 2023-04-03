package org.troyargonauts.robot;

import edu.wpi.first.math.util.Units;
import org.troyargonauts.common.motors.wrappers.MotorController;
import org.troyargonauts.common.util.Gains;

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
        double DEADBAND = 0.08;
        int WHEEL_DIAMETER = 6;
        MotorController.GearingParameters gearingLowGear = new MotorController.GearingParameters(1.0 / 19.6, Units.inchesToMeters(WHEEL_DIAMETER / 2.0), 2048);
        MotorController.GearingParameters gearingHighGear = new MotorController.GearingParameters(1.0 / 9.06677, Units.inchesToMeters(WHEEL_DIAMETER / 2.0), 2048);

        int SHIFTING_AMP_THRESHOLD = 10;

        //PID Tuning Values
        Gains driveGains = new Gains(0, 0, 0, 10);
        Gains turnGains = new Gains(0, 0, 0, 10);

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
        double ARM_P = 0.08;
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

        double WRIST_P = 0.002;
        double WRIST_I = 0;
        double WRIST_D = 0;
        double WRIST_GEAR_RATIO = 49;
        double MAXIMUM_SPEED = 0.7;
        int WRIST_UPPER_LIMIT_PORT = 1;
        int WRIST_LOWER_LIMIT_PORT = 2;
    }

    public interface Elevator {
        int LIFT_MOTOR_PORT = 10;
        double ELEV_P = 0.0055;
        double ELEV_I = 0.0000008;
        double ELEV_D = 0.0002;
        double ELEVATOR_GEARBOX_SCALE = 9;
        int TOP_PORT = 5;
        int BOTTOM_PORT = 0;
    }
}
