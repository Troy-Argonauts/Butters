package org.troyargonauts.common.motors.wrappers;

// Copyright 2019 FRC Team 3476 Code Orange

import com.revrobotics.CANSparkMax;

/**
 * Sends only new commands to the Talon to reduce CAN usage.
 * TODO: Full Recode to use MotorController Interface + CANSparkMax class
 */
public class LazyCANSparkMax extends CANSparkMax {

	private final static double EPSILON = 1.0e-6;
	private double prevValue = 0;
	private double prevVoltage = 100;


	public LazyCANSparkMax(int deviceId, MotorType type) {
		super(deviceId, type);
		restoreFactoryDefaults();
		enableVoltageCompensation(10);
	}

	@Override
	public void set(double speed) {
		if (Math.abs(speed - prevValue) > EPSILON) {
			super.set(speed);
			prevValue = speed;
		}
	}

	@Override
	public void setVoltage(double outputVolts) {
		if (Math.abs(outputVolts - prevVoltage) > EPSILON) {
			prevVoltage = outputVolts;
			super.setVoltage(outputVolts);
		}
	}

	public double getSetpoint() {
		return prevValue;
	}

	public double getSetVoltage() {
		return prevVoltage;
	}
}
