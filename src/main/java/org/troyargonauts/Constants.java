package org.troyargonauts;

public final class Constants {
    
    public interface Arm {
        int ARM_PORT = 9;
        int WRIST_PORT = 8;
        int ROLLER_PORT = 10;
        
        double WRIST_P = 0.142;
        double WRIST_I = 0.2;
        double WRIST_D = 0;
        double WRIST_TOLERANCE = 0.1;

        double ARM_P = 0.1;
        double ARM_I = 0.03;
        double ARM_D = 0;
        double ARM_TOLERANCE = 0.1;

        double ARM_GEAR_RATIO = 250;
        double WRIST_GEAR_RATIO = 1;
    }
}