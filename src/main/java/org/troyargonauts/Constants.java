package org.troyargonauts;

public final class Constants {

    public interface DriveTrain {
        //CAN IDs
        int FRONT_RIGHT = 2;
        int MIDDLE_RIGHT = 3;
        int BACK_RIGHT = 4;
        int FRONT_LEFT = 5;
        int MIDDLE_LEFT = 6;
        int BACK_LEFT = 7;

        int PIGEON = 6;

        //Values in Inches
        double WHEEL_DIAMETER = 6;
        double ENCODER_NU_PER_REVOLUTION = 42;
        double REVOLUTION_DISTANCE = WHEEL_DIAMETER * Math.PI;
        double DISTANCE_CONVERSION = REVOLUTION_DISTANCE / ENCODER_NU_PER_REVOLUTION;

        double GEARBOX_SCALE = 8.54;

        //PID Tuning Values
        double kDriveP = 1;
        double kDriveI = 0;
        double kDriveD = 0;
        
        double kTurnP = 1;
        double kTurnI = 0;
        double kTurnD = 0;

        double kDriveTolerance = 1;
        double kTurnToleranceDeg = 1;
    }
    
    public interface Manipulator {
        public static final int kForwardChannel = 0;
        public static final int kReverseChannel = 1;
    }
}
