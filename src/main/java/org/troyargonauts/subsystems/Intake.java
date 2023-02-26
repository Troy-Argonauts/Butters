/**
 Represents the intake subsystem of the robot that includes the squeeze motor and rotate motor with their limit switches.
 Provides methods for controlling the state of the squeeze motor and rotate motor.
 */
package org.troyargonauts.subsystems;
import com.revrobotics.SparkMaxLimitSwitch;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import org.troyargonauts.Constants;
import com.revrobotics.CANSparkMax;
import com.revrobotics.CANSparkMaxLowLevel.MotorType;
import edu.wpi.first.wpilibj2.command.SubsystemBase;

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
    private static String intakeSqueezeState;

    /**
     * The current state of the rotate motor (either "UP", "DOWN", or "STOP").
     */
    private static String intakeRotateState;

    /**
     * The limit switch on the rotate motor in the forward direction.
     */
    public static SparkMaxLimitSwitch rotateForwardLimitSwitch;

    /**
     * The limit switch on the rotate motor in the backward direction.
     */
    public static SparkMaxLimitSwitch rotateBackwardLimitSwitch;

    /**
     * Constructs a new Intake object with the squeeze and rotate motors initialized to the ports specified in Constants.
     * The limit switches are also initialized.
     */
    public Intake() {
        squeezeMotor = new CANSparkMax(Constants.Intake.SQUEEZE_MOTOR_PORT, MotorType.kBrushless);
        rotateMotor = new CANSparkMax(Constants.Intake.ROTATE_MOTOR_PORT, MotorType.kBrushless);
        rotateForwardLimitSwitch = rotateMotor.getForwardLimitSwitch(SparkMaxLimitSwitch.Type.kNormallyClosed);
        rotateBackwardLimitSwitch = rotateMotor.getReverseLimitSwitch(SparkMaxLimitSwitch.Type.kNormallyClosed);
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
        switch (state) {
            case OPEN:
                squeezeMotor.set(Constants.Intake.SQUEEZE_MOTOR_SPEED);
                intakeSqueezeState = "OPEN";
                break;
            case CLOSE:
                squeezeMotor.set(-Constants.Intake.SQUEEZE_MOTOR_SPEED);
                intakeSqueezeState = "CLOSE";
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
                if (rotateForwardLimitSwitch.isLimitSwitchEnabled()) {
                    rotateMotor.set(0.0);
                    intakeRotateState = "STOP";
                } else {
                    rotateMotor.set(Constants.Intake.SQUEEZE_MOTOR_SPEED);
                    intakeRotateState = "OPEN";
                }
                break;
            case DOWN:
                if (rotateBackwardLimitSwitch.isLimitSwitchEnabled()){
                    rotateMotor.set(0.0);
                    intakeRotateState = "STOP";
                } else{
                    rotateMotor.set(-Constants.Intake.ROTATE_MOTOR_SPEED);
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
        SmartDashboard.putBoolean("Rotate Limit Switch Forward", rotateForwardLimitSwitch.isPressed());
        SmartDashboard.putBoolean("Rotate Limit Switch Backward", rotateBackwardLimitSwitch.isPressed());
    }
}