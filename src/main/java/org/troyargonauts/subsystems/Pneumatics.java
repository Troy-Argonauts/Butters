package org.troyargonauts.subsystems;

import org.troyargonauts.Constants.PneumaticsConstants;

import edu.wpi.first.wpilibj.DoubleSolenoid;
import edu.wpi.first.wpilibj.PneumaticsModuleType;
import edu.wpi.first.wpilibj2.command.SubsystemBase;

public class Pneumatics extends SubsystemBase {

    private final DoubleSolenoid solenoid;
    private DoubleSolenoid.Value currentState;

    public Pneumatics() {
        solenoid = new DoubleSolenoid(PneumaticsModuleType.CTREPCM, PneumaticsConstants.kForwardChannel, PneumaticsConstants.kReverseChannel);
        currentState = DoubleSolenoid.Value.kForward;
        updateState();
    }

    public void pickupIntake(){
        if (currentState == DoubleSolenoid.Value.kReverse) {
            currentState = DoubleSolenoid.Value.kForward;
            updateState();
        }
    }

    public void dropIntake() {
        if (currentState == DoubleSolenoid.Value.kForward) {
            currentState = DoubleSolenoid.Value.kReverse;
            updateState();
        }
    }

    public void updateState() {
        solenoid.set(currentState);
    }
}