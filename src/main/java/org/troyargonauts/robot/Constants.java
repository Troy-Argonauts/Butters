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
        double SHIFTING_THRESHOLD = 0.1;
    }
}
