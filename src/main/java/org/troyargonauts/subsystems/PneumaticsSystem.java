package org.troyargonauts.subsystems;

import org.troyargonauts.Constants.*;

import edu.wpi.first.wpilibj.DoubleSolenoid;
import edu.wpi.first.wpilibj.PneumaticsModuleType;
import edu.wpi.first.wpilibj2.command.SubsystemBase;

public class PneumaticsSystem extends SubsystemBase {

    private final DoubleSolenoid manipulatorSolenoid;
    private DoubleSolenoid.Value manipulatorState;

    public PneumaticsSystem() {
        manipulatorSolenoid = new DoubleSolenoid(PneumaticsModuleType.CTREPCM, Manipulator.kForwardChannel, Manipulator.kReverseChannel);
        manipulatorState = DoubleSolenoid.Value.kReverse;
        updateManipulatorState();
    }

    public void manipulatorGrab(){
        if (manipulatorState == DoubleSolenoid.Value.kReverse) {
            manipulatorState = DoubleSolenoid.Value.kForward;
            updateManipulatorState();
        }
    }

    public void manipulatorRelease() {
        if (manipulatorState == DoubleSolenoid.Value.kForward) {
            manipulatorState = DoubleSolenoid.Value.kReverse;
            updateManipulatorState();
        }
    }

    public void updateManipulatorState() {
        manipulatorSolenoid.set(manipulatorState);
    }
}