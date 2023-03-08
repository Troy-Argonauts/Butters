package org.troyargonauts.common.util.motorcontrol;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.can.WPI_TalonSRX;

/**
 * Sends only new commands to the Talon to reduce CAN usage.
 */
public class LazyTalonSRX extends WPI_TalonSRX {

	private double prevValue = 0;
	private final ControlMode prevControlMode = ControlMode.Disabled;

	public LazyTalonSRX(int deviceNumber) {
		super(deviceNumber);
		enableVoltageCompensation(true);
		configVoltageCompSaturation(12, 10);
	}

	@Override
	public void set(ControlMode mode, double outputValue) {
		//return;

		if (outputValue != prevValue || mode != prevControlMode) {
			super.set(mode, outputValue);
			prevValue = outputValue;
		}
	}

	public double getSetpoint() {
		return prevValue;
	}
}
