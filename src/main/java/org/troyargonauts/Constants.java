package org.troyargonauts;

public final class Constants {

    public static final class DriveConstants {
        public static final int kFrontRightID = 0;
        public static final int kMiddleRightID = 1;
        public static final int kBackRightID = 2;
        public static final int kFrontLeftID = 3;
        public static final int kMiddleLeftID = 4;
        public static final int kBackLeftID = 5;

        public static final int kPigeonID = 6;

        public static final double kWheelDiameterInches = 6.0;
        public static final double kEncoderNUPerWheelRevolution = 2004.789;
        public static final double kWheelRevolutionDistanceInches = kWheelDiameterInches * Math.PI;
        public static final double kDistanceConvertion = kEncoderNUPerWheelRevolution / kWheelRevolutionDistanceInches;

        public static final double kEncoderGearboxScale = 8.56;
    }

}
