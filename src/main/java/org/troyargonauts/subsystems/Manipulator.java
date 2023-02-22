package org.troyargonauts.subsystems;

import com.revrobotics.CANSparkMax;
import com.revrobotics.CANSparkMaxLowLevel.MotorType;

import edu.wpi.first.wpilibj2.command.SubsystemBase;

import org.troyargonauts.Constants.ManipulatorConstants;

/**
 * runs 1 motor to run manipulator
 * @author @SolidityContract @sgowda260 @Shreyan-M
 */
public class Manipulator extends SubsystemBase {

    /** 
     * The States enum represents the possible states of the Manipulator motor.
     * The states are FORWARD, REVERSE, and OFF.
     */
    public enum States {
        FORWARD, REVERSE, OFF;
    }

    private final CANSparkMax main;
    
    /** 
     * The Manipulator class controls the motor of the Manipulator subsystem
     * Instantiates the main CANSparkMax motor controller for the Manipulator subsystem.
     */
    public Manipulator() {
        main = new CANSparkMax(ManipulatorConstants.MAIN, MotorType.kBrushless);
    }
    
    
    /** 
     * Sets the state of the Manipulator motor to the specified state.
     * @param state the state to set the motor to
     */
    public void setState(States state) {
        switch (state) {
            case FORWARD:
                main.set(0.5);
                break;
            case REVERSE:
                main.set(-0.5);
                break;
            case OFF:
                main.set(0);
                break;
        }
    }

}