package org.troyargonauts.subsystems;

import org.troyargonauts.Constants.IntakeConstants;

import com.revrobotics.CANSparkMax;
import com.revrobotics.CANSparkMaxLowLevel.MotorType;

import edu.wpi.first.wpilibj2.command.SubsystemBase;

public class Intake extends SubsystemBase {
    
    CANSparkMax leftMotor, rightMotor;

    public enum IntakeStates {
        IN, OUT, OFF;
    }

    public Intake() {
        rightMotor = new CANSparkMax(IntakeConstants.kRightIntakeID, MotorType.kBrushless);
        leftMotor = new CANSparkMax(IntakeConstants.kLeftIntakeID, MotorType.kBrushless);

        rightMotor.setInverted(true);
    }

    public void setState(IntakeStates state) {
        switch (state) {
            case IN:
                leftMotor.set(0.5);
                rightMotor.set(0.5);
                break;
            case OUT:
                leftMotor.set(-0.5);
                rightMotor.set(-0.5);
                break;
            case OFF:
                leftMotor.set(0);
                rightMotor.set(0);
                break;
        }
    }
}
