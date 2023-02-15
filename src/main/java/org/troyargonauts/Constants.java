package org.troyargonauts;

public final class Constants {

    public interface DriveConstants {
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
        double kLeftP = 1;
        double kLeftI = 0;
        double kLeftD = 0;

        double kRightP = 1;
        double kRightI = 0;
        double kRightD = 0;
        
        double kTurnP = 1;
        double kTurnI = 0;
        double kTurnD = 0;

        double kLeftDriveTolerance = 1;
        double kRightDriveTolerance = 1;
        double kTurnToleranceDeg = 1;
    }

}
