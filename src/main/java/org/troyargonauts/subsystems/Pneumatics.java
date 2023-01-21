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


    public void setManipulatorSolenoid(boolean state) {
        if (state == true) {
            manipulatorState = DoubleSolenoid.Value.kForward;

        } else {
            manipulatorState = DoubleSolenoid.Value.kReverse;

            }
        }


    public void setElevatorSolenoid(boolean state) {
        if (state == true) {
            elevatorState = DoubleSolenoid.Value.kForward;

        } else {
            elevatorState = DoubleSolenoid.Value.kReverse;
        }
    }
}