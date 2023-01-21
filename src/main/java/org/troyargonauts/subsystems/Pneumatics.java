package org.troyargonauts.subsystems;

import org.troyargonauts.Constants.*;

import edu.wpi.first.wpilibj.DoubleSolenoid;
import edu.wpi.first.wpilibj.PneumaticsModuleType;
import edu.wpi.first.wpilibj2.command.SubsystemBase;

public class Pneumatics extends SubsystemBase {

    private final DoubleSolenoid manipulatorSolenoid;
    private DoubleSolenoid.Value manipulatorState;
    private DoubleSolenoid elevatorSolenoid;
    private DoubleSolenoid.Value elevatorState;

    public Pneumatics() {
        manipulatorSolenoid = new DoubleSolenoid(PneumaticsModuleType.CTREPCM, Manipulator.kForwardChannel, Manipulator.kReverseChannel);
        manipulatorState = DoubleSolenoid.Value.kForward;
        elevatorSolenoid = new DoubleSolenoid(PneumaticsModuleType.CTREPCM, Elevator.kElevatorForwardChannel, Elevator.kElevatorReverseChannel);
        elevatorState = DoubleSolenoid.Value.kForward;
    }

    public enum State {
        FORWARD, REVERSE;
    }

    public void setManipulatorSolenoid(State state){
        switch (state) {
            case REVERSE:
                manipulatorState = DoubleSolenoid.Value.kReverse;
                break;
            case FORWARD:
                manipulatorState = DoubleSolenoid.Value.kForward;
                break;
        }
        manipulatorSolenoid.set(manipulatorState);
    }

    public void setElevatorSolenoid(State state) {
        switch (state) {
            case REVERSE:
                elevatorState = DoubleSolenoid.Value.kReverse;
                break;
            case FORWARD:
                elevatorState = DoubleSolenoid.Value.kForward;
                break;
        }
        elevatorSolenoid.set(elevatorState);
    }
}