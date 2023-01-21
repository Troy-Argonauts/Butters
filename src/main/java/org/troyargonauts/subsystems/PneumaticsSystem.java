package org.troyargonauts.subsystems;

import org.troyargonauts.Constants.*;

import edu.wpi.first.wpilibj.DoubleSolenoid;
import edu.wpi.first.wpilibj.PneumaticsModuleType;
import edu.wpi.first.wpilibj2.command.SubsystemBase;

public class PneumaticsSystem extends SubsystemBase {

    private final DoubleSolenoid manipulatorSolenoid;
    private DoubleSolenoid.Value manipulatorState;
    private DoubleSolenoid elevatorSolenoid;
    private DoubleSolenoid.Value elevatorState;

    public PneumaticsSystem() {
        manipulatorSolenoid = new DoubleSolenoid(PneumaticsModuleType.CTREPCM, Manipulator.kForwardChannel, Manipulator.kReverseChannel);
        manipulatorState = DoubleSolenoid.Value.kForward;
        elevatorSolenoid = new DoubleSolenoid(PneumaticsModuleType.CTREPCM, Elevator.kElevatorForwardChannel, Elevator.kElevatorReverseChannel);
        elevatorState = DoubleSolenoid.Value.kForward;
        updateManipulatorState();
        updateElevatorState();
    }


    public void manipulatorGrab() {
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

    public void elevatorUp() {
        if (elevatorState == DoubleSolenoid.Value.kReverse) {
            elevatorState = DoubleSolenoid.Value.kForward;
            updateElevatorState();
        }
    }

    public void elevatorDown() {
        if (elevatorState == DoubleSolenoid.Value.kForward) {
            elevatorState = DoubleSolenoid.Value.kReverse;
            updateElevatorState();
        }
    }

    public void updateElevatorState() {
        elevatorSolenoid.set(elevatorState);
    }
}