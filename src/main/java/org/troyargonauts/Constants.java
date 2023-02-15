package org.troyargonauts;

public final class Constants {

    public interface DriveConstants {
        int kFrontRightID = 2;
        int kMiddleRightID = 3;
        int kBackRightID = 4;
        int kFrontLeftID = 5;
        int kMiddleLeftID = 6;
        int kBackLeftID = 7;

        int kPigeonID = 6;

        //Values in Inches
        double kWheelDiameter = 6;
        double kEncoderNUPerWheelRevolution = 42;
        double kWheelRevolutionDistance = kWheelDiameter * Math.PI;
        double kDistanceConvertion = kWheelRevolutionDistance / kEncoderNUPerWheelRevolution;

        double kEncoderGearboxScale = 8.54;
        
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
