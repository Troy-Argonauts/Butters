package org.troyargonauts.subsystems;

import org.troyargonauts.Constants.*;

import edu.wpi.first.wpilibj.DoubleSolenoid;
import edu.wpi.first.wpilibj.PneumaticsModuleType;
import edu.wpi.first.wpilibj2.command.SubsystemBase;

/**
 * Pneumatics are used on the elevator to lift and drop the elevator
 * @author aarooshg, Solidarity Contract, firearcher2012, TheFlyingPig25, SavageCabbage360
 */
public class Pneumatics extends SubsystemBase {
    /**
     * Variables created for the solenoid controlling the elevator as well as the variable that specifies if the solenoid is in or out.
     */
    private DoubleSolenoid elevatorSolenoid;
    private DoubleSolenoid.Value elevatorState;

    /**
     * Constructor for the pneumatics.
     * The pneumatics are instantiated and are given values
     */

    public Pneumatics() {

        elevatorSolenoid = new DoubleSolenoid(PneumaticsModuleType.CTREPCM, Elevator.kElevatorForwardChannel, Elevator.kElevatorReverseChannel);
        elevatorState = DoubleSolenoid.Value.kForward;
    }

    /**
     * This enum displays the states that the pneumatics can go in
     * The states that the pneumatics solenoid can go in are FORWARD and REVERSE
     */
    public enum State {
        FORWARD, REVERSE;
    }

    /**
     *This switch-case is how the state of the pneumatics is changed
     * @param state the state that pneumatic is going to be set to
     */
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