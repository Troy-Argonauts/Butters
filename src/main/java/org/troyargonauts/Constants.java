package org.troyargonauts;

public final class Constants {

    public interface DriveConstants {
        int kFrontRightID = 4;
        int kMiddleRightID = 5;
        int kBackRightID = 6;
        int kFrontLeftID = 1;
        int kMiddleLeftID = 2;
        int kBackLeftID = 3;

        int kPigeonID = 6;

        double kWheelDiameterFeet = 0.5;
        double kEncoderNUPerWheelRevolution = 42;
        double kWheelRevolutionDistanceFeet = kWheelDiameterFeet * Math.PI;
        double kDistanceConvertion = kWheelRevolutionDistanceFeet / kEncoderNUPerWheelRevolution;

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
