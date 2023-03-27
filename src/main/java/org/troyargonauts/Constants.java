package org.troyargonauts;

public final class Constants {

    public interface Elevator {
        int LIFT_MOTOR_PORT = 11;
        
        double kP = 1;
        double kI = 0;
        double kD = 0;

        double ELEVATOR_GEARBOX_SCALE = 1;

        int TOP_PORT = 1;
        int BOTTOM_PORT = 0;
    }
}
