package org.troyargonauts;

public final class Constants {

    public interface Drivetrain {

    }

    public interface Elevator {
        int LEFT = 0;
        int RIGHT = 1;
        
        double kP = 1;
        double kI = 0;
        double kD = 0;

        double PERIOD = 0.02;

        double kEncoderGearboxScale = 1;
    }

    public interface Turret {
        double kP = 1;
        double kI = 0;
        double kD = 0;

        double PERIOD = 0.02;
        int CHANNEL = 0;
        double FULL_RANGE = 180;
        double OFFSET = 30;

        int PORT = 0;
    }

    public interface Manipulator {
        int kManipulatorForwardChannel = 0;
        int kManipulatorReverseChannel = 1;
    }

}
