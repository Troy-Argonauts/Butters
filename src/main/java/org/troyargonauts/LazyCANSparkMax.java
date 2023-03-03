package org.troyargonauts;

import com.revrobotics.CANSparkMax;

public class LazyCANSparkMax extends CANSparkMax {

    private final static double EPSILON = 1.0e-6;
    private double prevValue = 0;
    private double prevVoltage = 100;


    public LazyCANSparkMax(int deviceId, MotorType type) {
        super(deviceId, type);
        enableVoltageCompensation(10);
    }

    @Override
    public void set(double speed) {
        //return;
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
