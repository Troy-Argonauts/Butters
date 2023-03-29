package org.troyargonauts.common.util;

import com.ctre.phoenix.motorcontrol.can.TalonSRX;

public class Utils {

    public static boolean isEncoderPresent(final TalonSRX device) {
        return device.getSensorCollection().getPulseWidthRiseToRiseUs() != 0;
    }
}
