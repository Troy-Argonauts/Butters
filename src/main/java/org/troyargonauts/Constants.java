package org.troyargonauts;

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
        
        double WRIST_P = 0.021;
        double WRIST_I = 0;
        double WRIST_D = 0;
        double WRIST_PERIOD = 0.02;

        double ARM_P = 0.4;
        double ARM_I = 0;
        double ARM_D = 0;
        double ARM_PERIOD = 0.02;

        double ARM_GEAR_RATIO = 250;
    }

}
