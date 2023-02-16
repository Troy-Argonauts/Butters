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
        double DRIVE_P = 0.015;
        double DRIVE_I = 0.0005;
        double DRIVE_D = 0;
        
        double TURN_P = 1;
        double TURN_I = 0;
        double TURN_D = 0;

        double DRIVE_TOLERANCE = 0.5;
        double VELOCITY_TOLERANCE = 0.05;
        double TURN_TOLERANCE = 1;
    }
    
    public interface Manipulator {
        public static final int kForwardChannel = 0;
        public static final int kReverseChannel = 1;
    }
}
