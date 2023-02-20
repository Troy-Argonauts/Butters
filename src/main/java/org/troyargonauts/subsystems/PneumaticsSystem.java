package org.troyargonauts.subsystems;

import org.troyargonauts.Constants.*;

import edu.wpi.first.wpilibj.DoubleSolenoid;
import edu.wpi.first.wpilibj.PneumaticsModuleType;
import edu.wpi.first.wpilibj2.command.SubsystemBase;

public class PneumaticsSystem extends SubsystemBase {

    public static DoubleSolenoid manipulatorSolenoid;
    public static DoubleSolenoid alignSolenoid;
    public static DoubleSolenoid grabSolenoid;
    public static DoubleSolenoid rotateSolenoid;

    public enum ManipulatorState {
        GRAB, RELEASE;
    }

    public PneumaticsSystem() {
        manipulatorSolenoid = new DoubleSolenoid(PneumaticsModuleType.CTREPCM, Manipulator.kManipulatorForwardChannel, Manipulator.kManipulatorReverseChannel);
        manipulatorSolenoid.set(DoubleSolenoid.Value.kForward);
    }

    public void setManipulatorState(ManipulatorState state) {
        switch (state) {
            case GRAB:
                manipulatorSolenoid.set(DoubleSolenoid.Value.kForward);
                break;
            case RELEASE:
                manipulatorSolenoid.set(DoubleSolenoid.Value.kReverse);
                break;
        }
    }
}