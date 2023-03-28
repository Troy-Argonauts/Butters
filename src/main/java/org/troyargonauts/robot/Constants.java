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
        double SHIFTING_THRESHOLD = 1;

        double LOW_GEARBOX_RATIO = 19.61;
        double HIGH_GEARBOX_RATIO = 9.07;
    }
}
