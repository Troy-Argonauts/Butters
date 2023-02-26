package org.troyargonauts;

import edu.wpi.first.math.kinematics.DifferentialDriveKinematics;

public class Constants {

    public interface DriveConstants {
        int kFrontRightID = 0;
        int kMiddleRightID = 1;
        int kBackRightID = 2;
        int kFrontLeftID = 3;
        int kMiddleLeftID = 4;
        int kBackLeftID = 5;

        int kPigeonID = 6;

        double kWheelDiameterMeters = 0.1524;

        double kWheelDiameterInches = 6;
        double kEncoderNUPerWheelRevolution = 42;
        double kWheelRevolutionDistanceInches = kWheelDiameterInches * Math.PI;
        double kDistanceConvertion = kWheelRevolutionDistanceInches / kEncoderNUPerWheelRevolution;


        double kEncoderGearboxScale = 8.56;
        double trackWidthIn = 19.5;
        double kLeftP = 1;
        double kLeftI = 0;
        double kLeftD = 0;

        double kP = 1;
        double kI = 0;
        double kD = 0;
        
        double kTurnP = 1;
        double kTurnI = 0;
        double kTurnD = 0;

        double kDriveTolerance = 1;
        double kTurnToleranceDeg = 1;
        
        double ks = 0.1;
        double kv = 0.1;
        double ka = 0.1;

        double kPDrive = 0.1;

        double kTrackwidthMeters = 0.7;
        DifferentialDriveKinematics kDriveKinematics = new DifferentialDriveKinematics(kTrackwidthMeters);

        double kMaxSpeed = 3;
        double kMaxAcceleration = 1;

        double kRamseteB = 2;
        double kRamseteZeta = 0.7;
    }

    public interface Elevator {
        int LEFT = 11;
        int RIGHT = 12;
        
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

        int PORT = 10;

        double NERF = 0.5;
    }

    public interface Manipulator {
        int kManipulatorForwardChannel = 0;
        int kManipulatorReverseChannel = 1;
    }
    
}
