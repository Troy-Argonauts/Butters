package org.troyargonauts;

public final class Constants {

    public interface Drivetrain {

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
    }

    public interface Turret {
        double kP = 0.049;
        double kI = 0.0008;
        double kD = 0.00005;
        double TOLERANCE = 0.75;
        double DEFAULT_SETPOINT = -36;

        double PERIOD = 0.02;

        int PORT = 10;

        double NERF = 0.5;
        int LEFT_PORT = 2;
        int RIGHT_PORT = 3;
    }
}
