package org.troyargonauts;

public final class Constants {

    public interface Manipulator {
        public static final int kForwardChannel = 0;
        public static final int kReverseChannel = 1;
    }

    public interface Arm {
        public static final int ARM_MOTOR = 0;
        public static final int ARM_INTAKE = 1;
        public static final int TOP_ELBOW_ENCODER_LIMIT = 10;
        public static final int LOWER_ELBOW_ENCODER_LIMIT = 10;
        public static final int TOP_WRIST_ENCODER_LIMIT = 10;
        public static final int LOWER_WRIST_ENCODER_LIMIT = 10;
        public static final double FORWARD_INTAKE_SPEED = 0.25;
        public static final double REVERSE_INTAKE_SPEED = -0.25;
    }

}
