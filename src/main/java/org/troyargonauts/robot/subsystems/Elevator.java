package org.troyargonauts.robot.subsystems;

import com.revrobotics.CANSparkMax;
import com.revrobotics.CANSparkMaxLowLevel;
import edu.wpi.first.math.controller.PIDController;
import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj2.command.PIDCommand;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import org.troyargonauts.common.util.motorcontrol.LazyCANSparkMax;
import org.troyargonauts.robot.Constants;
import org.troyargonauts.robot.Robot;

/**
 * Class representing elevator subsystem. includes PID control and limit switches
 * @author TeoElRey, sgowda260, SolidityContract, ASH-will-WIN
 */
public class Elevator extends SubsystemBase {
    private final LazyCANSparkMax leftMotor;
    private final LazyCANSparkMax rightMotor;
    private PIDController pid;
    private DigitalInput topLimitSwitch;
    private DigitalInput bottomDigitalInput;
    private double rightMotorPosition;
    private double leftMotorPosition;



    /**
     * Instantiates the motor controllers, limit switches, encoder, and PID controller for the Elevator.
     * Additionally, the left side motor is set to be inverted
     * Encoders built into the NEO 550 motors track position of the elevator's carriage
     * Motors are set to brake mode
     * soft limit is set to 7, meaning motors will have a limit of 7 rotations backwards
     */
    public Elevator() {
        leftMotor = new LazyCANSparkMax(Constants.Elevator.LEFT, CANSparkMaxLowLevel.MotorType.kBrushless);
        rightMotor = new LazyCANSparkMax(Constants.Elevator.RIGHT, CANSparkMaxLowLevel.MotorType.kBrushless);

        leftMotor.setInverted(false);
        rightMotor.setInverted(false);

        leftMotor.setIdleMode(CANSparkMax.IdleMode.kBrake);
        rightMotor.setIdleMode(CANSparkMax.IdleMode.kBrake);

        leftMotor.setSoftLimit(CANSparkMax.SoftLimitDirection.kReverse, 7);
        rightMotor.setSoftLimit(CANSparkMax.SoftLimitDirection.kReverse, 7);

        topLimitSwitch = new DigitalInput(Constants.Elevator.TOP_PORT);
        bottomDigitalInput = new DigitalInput(Constants.Elevator.BOTTOM_PORT);

        pid = new PIDController(Constants.Elevator.kP, Constants.Elevator.kI ,Constants.Elevator.kD, Constants.Elevator.PERIOD);
    }

    /**
     * Periodic will constantly check the encoder and limit switch values.
     * These values will later be used to make sure elevator extends or retracts to optimal position.
     */
    @Override
    public void periodic() {
        rightMotorPosition = rightMotor.getEncoder().getPosition();
        leftMotorPosition = leftMotor.getEncoder().getPosition();
    }

    /**
     * Elevator power will be set to a given speed from -1 to 1
     * If it is positive, elevator will extend until upper limit switch turns on
     * If it is negative, elevator retracts until lower limit switch turns on
     *
     * @param speed desired elevator extension or retraction speed
     */
    public void setPower(double speed) {
        if (speed > 0) {
            if (topLimitSwitch.get()) {
                leftMotor.set(0);
                rightMotor.set(0);
            } else{
                leftMotor.set(speed * Constants.Elevator.NERF);
                rightMotor.set(speed * Constants.Elevator.NERF);
            }
        } else {
            if (bottomDigitalInput.get()) {
                leftMotor.set(0);
                rightMotor.set(0);
            } else {
                leftMotor.set(speed * Constants.Elevator.NERF);
                rightMotor.set(speed * Constants.Elevator.NERF);
            }
        }
    }

    /**
     * Sets all encoder values to 0.
     */
    public void resetEncoders(){
        leftMotor.getEncoder().setPosition(0);
        rightMotor.getEncoder().setPosition(0);
    }

    /**
     * Returns the average position of both motors.
     * @return average position of both motors.
     */
    public double getPosition() {
        return ((rightMotorPosition + leftMotorPosition) / (2 * Constants.Elevator.kEncoderGearboxScale));
    }

    /**
     * The elevator will shift to a given setpoint using the
     * predetermined PID Controller.
     * 
     * @param setpoint will be the desired extension point
     */
    public PIDCommand elevatorPID(double setpoint) {
        pid.setSetpoint(setpoint);
        return new PIDCommand(
            pid,
                this::getPosition,
            setpoint,
                this::setPower,
            Robot.getElevator()
        );
    }
}
