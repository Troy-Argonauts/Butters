package org.troyargonauts.subsystems;

import edu.wpi.first.wpilibj.DoubleSolenoid;
import org.troyargonauts.Constants.IntakeConstants;

import com.revrobotics.CANSparkMax;
import com.revrobotics.CANSparkMaxLowLevel.MotorType;

import edu.wpi.first.wpilibj2.command.SubsystemBase;

import static org.troyargonauts.subsystems.PneumaticsSystem.*;

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

    public void setAlignState(PneumaticsSystem.AlignIntakeState state) {
        switch (state) {
            case ALIGN:
                alignSolenoid.set(DoubleSolenoid.Value.kForward);
                break;
            case UNALIGN:
                alignSolenoid.set(DoubleSolenoid.Value.kReverse);
                break;
        }
    }

    public void setGrabState(PneumaticsSystem.GrabIntakeState state) {
        switch (state) {
            case GRAB:
                grabSolenoid.set(DoubleSolenoid.Value.kForward);
                break;
            case RELEASE:
                grabSolenoid.set(DoubleSolenoid.Value.kReverse);
                break;
        }
    }

    public void setRotateState(PneumaticsSystem.RotateIntakeState state) {
        switch (state) {
            case FLAT:
                grabSolenoid.set(DoubleSolenoid.Value.kForward);
                break;
            case ROTATED:
                grabSolenoid.set(DoubleSolenoid.Value.kReverse);
                break;
        }
    }
}