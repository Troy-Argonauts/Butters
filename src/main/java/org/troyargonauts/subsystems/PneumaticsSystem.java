package org.troyargonauts.subsystems;

import org.troyargonauts.Constants.*;

import edu.wpi.first.wpilibj.DoubleSolenoid;
import edu.wpi.first.wpilibj.PneumaticsModuleType;
import edu.wpi.first.wpilibj2.command.SubsystemBase;

public class PneumaticsSystem extends SubsystemBase {

    public static DoubleSolenoid manipulatorSolenoid;

    public enum ManipulatorState {
        GRAB, RELEASE;
    }

    public PneumaticsSystem() {
        manipulatorSolenoid = new DoubleSolenoid(PneumaticsModuleType.CTREPCM, Manipulator.kManipulatorForwardChannel, Manipulator.kManipulatorReverseChannel);
        manipulatorSolenoid.set(DoubleSolenoid.Value.kForward);
    }
}