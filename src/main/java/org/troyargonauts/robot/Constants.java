package org.troyargonauts.robot;

public final class Constants {
    public interface Arm {
        int ELBOW = 9;
        int MANIPULATOR = 13;
        int WRIST = 8;
        double TOP_ARM_ENCODER_LIMIT = 10;
        double LOWER_ARM_ENCODER_LIMIT = 10;
        double TOP_WRIST_ENCODER_LIMIT = 10;
        double LOWER_WRIST_ENCODER_LIMIT = 10;

        double FORWARD_INTAKE_SPEED = 0.75;
        double REVERSE_INTAKE_SPEED = -0.75;

        double WRIST_P = 0.142;
        double WRIST_I = 0.2;
        double WRIST_D = 0;
        double WRIST_PERIOD = 0.02;
        double WRIST_TOLERANCE = 0.1;
        double WRIST_DEFAULT = 0.1;

        double ARM_P = 0.045;
        double ARM_I = 0.001;
        double ARM_D = 0.01;
        double ARM_PERIOD = 0.02;
        double ARM_TOLERANCE = 0.1;
        double ARM_DEFAULT = -0.1;

        int CURRENT_LIMIT = 30;
    }
  
    public interface DriveTrain {
        //CAN IDs
        int FRONT_RIGHT = 5;
        int MIDDLE_RIGHT = 6;
        int BACK_RIGHT = 7;
        int FRONT_LEFT = 2;
        int MIDDLE_LEFT = 3;
        int BACK_LEFT = 4;
        int PIGEON = 25;

        double DEADBAND = 0.02;

        //Values in Inches
        double WHEEL_DIAMETER = 6;
        double ENCODER_NU_PER_REVOLUTION = 42;
        double GEARBOX_SCALE = 8.54;
        double REVOLUTION_DISTANCE = (WHEEL_DIAMETER * Math.PI) * GEARBOX_SCALE;
        double DISTANCE_CONVERSION = REVOLUTION_DISTANCE / ENCODER_NU_PER_REVOLUTION;

        //PID Tuning Values
        double kDriveP = 0.08;
        double kDriveI = 0.005;
        double kDriveD = 0.03;

        double kTurnP = 1;
        double kTurnI = 0;
        double kTurnD = 0;

        double kBalanceP = 1;
        double kBalanceI = 0;
        double kBalanceD = 0;

        double kDriveTolerance = 1;
        double kTurnToleranceDeg = 1;
        double kBalanceToleranceDeg = 4;

        //Correction Values
        double RIGHT_CORRECTION = 0.0010;
    }

    public interface Elevator {
        int LEFT = 11;
        int RIGHT = 12;
        
        double kP = 1;
        double kI = 0;
        double kD = 0;

        double PERIOD = 0.02;

        double kEncoderGearboxScale = 1;

        double NERF = 0.3;
        int BOTTOM_PORT = 0;
        int TOP_PORT = 1;
        int FORWARD_CHANNEL = 0;
        int REVERSE_CHANNEL = 1;
    }

    public interface Turret {
        double kP = 0.049;
        double kI = 0.0008;
        double kD = 0.00005;
        double TOLERANCE = 0.75;
        double DEFAULT_SETPOINT = 0;

        double PERIOD = 0.02;

        int PORT = 10;

        double NERF = 0.5;
        int LEFT_PORT = 2;
        int RIGHT_PORT = 3;
    }
}
