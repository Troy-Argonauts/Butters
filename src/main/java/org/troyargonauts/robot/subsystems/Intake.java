/**
 Represents the intake subsystem of the robot that includes the squeeze motor and rotate motor with their limit switches.
 Provides methods for controlling the state of the squeeze motor and rotate motor.
 */
package org.troyargonauts.robot.subsystems;
import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import org.troyargonauts.Constants;
import com.revrobotics.CANSparkMax;
import com.revrobotics.CANSparkMaxLowLevel.MotorType;
import edu.wpi.first.wpilibj2.command.SubsystemBase;

import java.sql.Driver;

/**
 * Back Intake Code
 * @author Sharvayu-Chavan SavageCabbage360
 */
public class Intake extends SubsystemBase {
    /**
     * The CANSparkMax object representing the squeeze motor.
     */
    private static CANSparkMax squeezeMotor;

    /**
     * The CANSparkMax object representing the rotate motor.
     */
    private static CANSparkMax rotateMotor;

    /**
     * The current state of the squeeze motor (either "OPEN", "CLOSE", or "STOP").
     */
    private static String intakeSqueezeState = "";

    /**
     * The current state of the rotate motor (either "UP", "DOWN", or "STOP").
     */
    private static String intakeRotateState = "";

    /**
     * The limit switch on the rotate motor in the backward direction.
     */
    public static DigitalInput rotateBackwardLimitSwitch;
    public static DigitalInput outLimitSwitch;


    /**
     * Constructs a new Intake object with the squeeze and rotate motors initialized to the ports specified in Constants.
     * The limit switches are also initialized.
     */
    public Intake() {
        squeezeMotor = new CANSparkMax(Constants.Intake.SQUEEZE_MOTOR_PORT, MotorType.kBrushless);
        rotateMotor = new CANSparkMax(Constants.Intake.ROTATE_MOTOR_PORT, MotorType.kBrushless);
        outLimitSwitch = new DigitalInput(Constants.Intake.OUT_LIMIT_SWTICH);
        rotateBackwardLimitSwitch = new DigitalInput(Constants.Intake.TOP_LIMIT_SWITCH);

        squeezeMotor.restoreFactoryDefaults();
        rotateMotor.restoreFactoryDefaults();

        squeezeMotor.getEncoder().setPosition(0);
        rotateMotor.getEncoder().setPosition(0);

        rotateMotor.setIdleMode(CANSparkMax.IdleMode.kBrake);
        squeezeMotor.setIdleMode(CANSparkMax.IdleMode.kBrake);
    }
    /**
     * Represents the possible states of the squeeze motor.
     */
    public enum squeezeStates {
        OPEN, CLOSE, STOP
    }

    /**
     * Represents the possible states of the rotate motor.
     */
    public enum rotateStates {
        UP, DOWN, STOP
    }
    /**
     * Sets the state of the squeeze motor based on the input squeezeStates enum.
     * @param state The state to set the squeeze motor to (either "OPEN", "CLOSE", or "STOP").
     */
    public static void setSqueezeIntakeState(squeezeStates state) {
        DriverStation.reportWarning("Here", false);
        switch (state) {
            case OPEN:
                DriverStation.reportWarning("AT Open", false);
                if (!outLimitSwitch.get()) {
                    DriverStation.reportWarning("Limit Switch", false);
                    squeezeMotor.set(0);
                    intakeSqueezeState = "STOP";
                } else {
                    DriverStation.reportWarning("Applying Power", false);
                    squeezeMotor.set(Constants.Intake.SQUEEZE_MOTOR_SPEED);
                    intakeSqueezeState = "OPEN";
                }
                break;
            case CLOSE:
                if (squeezeMotor.getEncoder().getPosition() < -22) {
                    squeezeMotor.set(0);
                    intakeSqueezeState = "STOP";
                } else {
                    squeezeMotor.set(-Constants.Intake.SQUEEZE_MOTOR_SPEED);
                    intakeSqueezeState = "CLOSE";
                }
                break;
            case STOP:
                squeezeMotor.set(0.0);
                intakeSqueezeState = "STOP";
                break;
        }
    }

    /**
     * Sets the state of the rotate motor based on the input rotateStates enum.
     * @param state The state to set the rotate motor to (either "UP", "DOWN", or "STOP").
     */
    public static void setRotateIntakeState(rotateStates state) {
        switch (state) {
            case UP:
                if (!rotateBackwardLimitSwitch.get()) {
                    rotateMotor.set(0.0);
                    intakeRotateState = "STOP";
                } else {
                    rotateMotor.set(-Constants.Intake.ROTATE_MOTOR_SPEED);
                    intakeRotateState = "OPEN";
                }
                break;
            case DOWN:
                if (rotateMotor.getEncoder().getPosition() < -1) {
                    rotateMotor.set(0.0);
                    intakeRotateState = "STOP";
                } else {
                    rotateMotor.set(Constants.Intake.ROTATE_MOTOR_SPEED);
                    intakeRotateState = "DOWN";
                }
                break;
            case STOP:
                rotateMotor.set(0.0);
                intakeRotateState = "STOP";
                break;
        }
    }


    /**

     This method is called periodically.
     It updates the SmartDashboard with the current states of the intake squeeze, intake rotate, and the forward and backward limit switches of the intake rotate motor.
     */
    @Override
    public void periodic() {
        SmartDashboard.putString("Intake Squeeze State", intakeSqueezeState);
        SmartDashboard.putString("Intake Squeeze State", intakeRotateState);
        SmartDashboard.putBoolean("Rotate Limit Switch Backward", !rotateBackwardLimitSwitch.get());
        SmartDashboard.putBoolean("Out Limit Switch", !outLimitSwitch.get());
        SmartDashboard.putNumber("Out Encoder", squeezeMotor.getEncoder().getPosition());
        SmartDashboard.putNumber("Rotate Encoder", rotateMotor.getEncoder().getPosition());
    }

    public void resetEndcoders() {
        rotateMotor.getEncoder().setPosition(0);
        squeezeMotor.getEncoder().setPosition(0);
    }

    public void setIdleState(CANSparkMax.IdleMode idleState) {
        rotateMotor.setIdleMode(idleState);
        squeezeMotor.setIdleMode(idleState);
    }
}