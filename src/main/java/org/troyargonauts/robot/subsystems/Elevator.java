package org.troyargonauts.robot.subsystems;

import com.revrobotics.CANSparkMax;
import com.revrobotics.CANSparkMaxLowLevel;
import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import org.troyargonauts.common.motors.MotorCreation;
import org.troyargonauts.common.motors.wrappers.LazyCANSparkMax;

import static org.troyargonauts.robot.Constants.Elevator.*;

/**
 * Class representing elevator subsystem. includes PID control and limit switches
 *
 * @author TeoElRey, sgowda260, SolidityContract, ASH-will-WIN
 */
public class Elevator extends SubsystemBase {
    private final LazyCANSparkMax elevatorMotor;
    private final DigitalInput bottomLimitSwitch, upperLimitSwitch;
    private double elevatorEncoder;
    private double desiredTarget;

    /**
     * Instantiates the motor controllers, limit switches, encoder, and PID controller for the Elevator.
     * Additionally, the left side motor is set to be inverted
     * Encoders built into the NEO 550 motors track position of the elevator's carriage
     * Motors are set to brake mode
     * soft limit is set to 7, meaning motors will have a limit of 7 rotations backwards
     */
    public Elevator() {
        elevatorMotor = MotorCreation.createDefaultSparkMax(LIFT_MOTOR_PORT);

        elevatorMotor.setInverted(true);

        elevatorMotor.setIdleMode(CANSparkMax.IdleMode.kBrake);

        elevatorMotor.setSoftLimit(CANSparkMax.SoftLimitDirection.kReverse, 7);

        elevatorMotor.getEncoder().setPositionConversionFactor(ELEVATOR_GEARBOX_SCALE);

        bottomLimitSwitch = new DigitalInput(BOTTOM_PORT);
        upperLimitSwitch = new DigitalInput(TOP_PORT);

        elevatorMotor.getPIDController().setP(ELEV_P);
        elevatorMotor.getPIDController().setI(ELEV_I);
        elevatorMotor.getPIDController().setD(ELEV_D);
    }

    /**
     * Periodic will constantly check the encoder and limit switch values.
     * These values will later be used to make sure elevator extends or retracts to optimal position.
     */
    @Override
    public void periodic() {
        elevatorEncoder = elevatorMotor.getEncoder().getPosition();

        SmartDashboard.putNumber("elevator motor", elevatorEncoder);
        SmartDashboard.putBoolean("Down limit elevator", !bottomLimitSwitch.get());

        if (!bottomLimitSwitch.get()) {
            resetEncoders();
        }
    }

    public void run() {
        elevatorMotor.getPIDController().setOutputRange(-0.4, 0.4);
        elevatorMotor.getPIDController().setReference(desiredTarget, CANSparkMax.ControlType.kPosition);
    }

    public void setDesiredTarget(ElevatorState desiredState) {
        this.desiredTarget = desiredState.getEncoderPosition();
    }

    /**
     * Elevator power will be set to a given speed from -1 to 1
     * If it is positive, elevator will extend until upper limit switch turns on
     * If it is negative, elevator retracts until lower limit switch turns on
     *
     * @param joyStickValue desired elevator extension or retraction speed
     */
    public void setPower(double joyStickValue) {
        double newTarget = desiredTarget + (joyStickValue * 30);
        if ((desiredTarget <= 5 || desiredTarget >= 0) && newTarget > 0 && desiredTarget != newTarget) {
            desiredTarget = newTarget;
        } else if (newTarget < desiredTarget && bottomLimitSwitch.get()) { // If elevator is moving down (new encoder value is less than current encoder value) and bottomLimitSwitch is not pressed
            desiredTarget = newTarget;
        }
    }

    public enum ElevatorState {

        HOME(0),
        INITIAL_MOVEMENT(101),
        MIDDLE(200),
        HIGH(221);
        final double encoderPosition;

        ElevatorState(double encoderPosition) {
            this.encoderPosition = encoderPosition;
        }

        public double getEncoderPosition() {
            return this.encoderPosition;
        }
    }

    /**
     * Sets all encoder values to 0.
     */
    public void resetEncoders(){
        elevatorMotor.getEncoder().setPosition(0);
    }
}
