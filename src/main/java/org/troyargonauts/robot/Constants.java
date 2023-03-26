package org.troyargonauts.robot;

import edu.wpi.first.math.util.Units;
import org.troyargonauts.common.motors.wrappers.MotorController;

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
        int FRONT_RIGHT = 6;
        int TOP_RIGHT = 4;
        int REAR_RIGHT = 5;
        int FRONT_LEFT = 1;
        int TOP_LEFT = 3;
        int REAR_LEFT = 2;
        int PIGEON = 25;

        double DEADBAND = 0.02;

        //Values in Inches
        int WHEEL_DIAMETER = 6;

        MotorController.GearingParameters gearingLowGear = new MotorController.GearingParameters(1.0 / 19.6, Units.inchesToMeters(WHEEL_DIAMETER / 2.0), 2048);
        MotorController.GearingParameters gearingHighGear = new MotorController.GearingParameters(1.0 / 9.06677, Units.inchesToMeters(WHEEL_DIAMETER / 2.0), 2048);

        int AMPS_THRESHOLD = 10;
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

        int LOW_HIGH_THRESHOLD = 4362;
        int HIGH_LOW_THRESHOLD = 2018;
        double SHIFTING_THRESHOLD = 0.1;
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
