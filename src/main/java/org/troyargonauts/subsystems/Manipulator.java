package org.troyargonauts.subsystems;

import org.troyargonauts.Constants;

import com.revrobotics.CANSparkMax;
import com.revrobotics.CANSparkMaxLowLevel.MotorType;

import edu.wpi.first.wpilibj2.command.SubsystemBase;

public class Manipulator extends SubsystemBase {
    
    CANSparkMax intakeMotor;

    public enum ManipulatorStates {
        IN, OUT, OFF;
    }

    public Manipulator() {
        intakeMotor = new CANSparkMax(Constants.Manipulator.PORT, MotorType.kBrushless);
    }

    public void setState(ManipulatorStates state) {
        switch (state) {
            case IN: 
                intakeMotor.set(0.5);
                break;
            case OUT:
                intakeMotor.set(-0.5);
                break;
            case OFF:
                intakeMotor.set(0);
                break;
        }
    }
}
