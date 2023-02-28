package org.troyargonauts;

public final class Constants {

    public interface Arm {
        int ARM_MOTOR = 0;
        int ARM_INTAKE = 1;

        double TOP_ELBOW_ENCODER_LIMIT = 10;
        double LOWER_ELBOW_ENCODER_LIMIT = 10;
        double TOP_WRIST_ENCODER_LIMIT = 10;
        double LOWER_WRIST_ENCODER_LIMIT = 10;

        double FORWARD_INTAKE_SPEED = 0.25;
        double REVERSE_INTAKE_SPEED = -0.25;
        
        double kWristP = 0;
        double kWristI = 0;
        double kWristD = 0;
        double WRIST_PERIOD = 0.02;

        double kElbowP = 0;
        double kElbowI = 0;
        double kElbowD = 0;
        double ELBOW_PERIOD = 0.02;
    }

}
