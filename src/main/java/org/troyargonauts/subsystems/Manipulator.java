package org.troyargonauts.subsystems;

import com.revrobotics.CANSparkMax;
import com.revrobotics.CANSparkMaxLowLevel.MotorType;

import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import sensors.ColorSensor;

import org.troyargonauts.Constants;

/**
 * Runs motor to run manipulator, will be used to intake pieces using the arm.
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

    private final ColorSensor colorSensor;
    
    /** 
     * Creates a manipulator subsystem and instantiates the main CANSparkMax motor controller for the Manipulator subsystem.
     */
    public Manipulator() {
        main = new CANSparkMax(Constants.Manipulator.MAIN, MotorType.kBrushless);

        colorSensor = new ColorSensor();
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

    /**
     * Periodically updates the detected color and the closest match to a pre-defined color.
     * It also updates the color and the detected color on the SmartDashboard.
     */
    @Override
    public void periodic() {
        SmartDashboard.putString("Color", colorSensor.getOutput().red + ", " + colorSensor.getOutput().green + ", " + colorSensor.getOutput().blue);
        SmartDashboard.putString("Color Detected", colorSensor.getColor());
    }

}