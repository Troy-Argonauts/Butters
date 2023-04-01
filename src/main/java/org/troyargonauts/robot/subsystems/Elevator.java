package org.troyargonauts.robot.subsystems;
import com.revrobotics.*;

import edu.wpi.first.math.controller.PIDController;
import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import org.troyargonauts.common.motors.wrappers.LazyCANSparkMax;
import org.troyargonauts.robot.Constants;

/**
 * Class representing elevator subsystem. includes PID control and limit switches
 * @author TeoElRey, sgowda260, SolidityContract, ASH-will-WIN
 */
public class Elevator extends SubsystemBase {
    private final LazyCANSparkMax elevatorMotor;
    private final DigitalInput bottomLimitSwitch;
    private final PIDController elevatorPID;
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
        elevatorMotor = new LazyCANSparkMax(Constants.Elevator.LIFT_MOTOR_PORT, CANSparkMaxLowLevel.MotorType.kBrushless);

        elevatorMotor.setInverted(true);

        elevatorMotor.setIdleMode(CANSparkMax.IdleMode.kBrake);

        elevatorMotor.setSoftLimit(CANSparkMax.SoftLimitDirection.kReverse, 7);

        elevatorMotor.getEncoder().setPositionConversionFactor(9);

        bottomLimitSwitch = new DigitalInput(Constants.Elevator.BOTTOM_PORT);

        elevatorPID = new PIDController(Constants.Elevator.kP, Constants.Elevator.kI ,Constants.Elevator.kD);
    }

    /**
     * Periodic will constantly check the encoder and limit switch values.
     * These values will later be used to make sure elevator extends or retracts to optimal position.
     */
    @Override
    public void periodic() {
        elevatorEncoder = elevatorMotor.getEncoder().getPosition();

        SmartDashboard.putNumber("Lift Motor Position", elevatorEncoder);
//        SmartDashboard.putBoolean("Bottom Limit Switch", !bottomLimitSwitch.get());

        if (!bottomLimitSwitch.get()) {
            resetEncoders();
        }
    }

    public void run() {
        double motorSpeed = elevatorPID.calculate(elevatorEncoder, desiredTarget);
        if (motorSpeed > Constants.Elevator.MAXIMUM_SPEED) motorSpeed = Constants.Elevator.MAXIMUM_SPEED;
        else if (motorSpeed < -Constants.Elevator.MAXIMUM_SPEED) motorSpeed = -Constants.Elevator.MAXIMUM_SPEED;
        setPower(motorSpeed);
    }

    public void setDesiredTarget(double desiredTarget) {
        this.desiredTarget = desiredTarget;
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
            if (elevatorEncoder > 219.8557891845703) {
                elevatorMotor.set(0);
            } else {
                elevatorMotor.set(speed);
            }
        } else {
            if (!bottomLimitSwitch.get()) {
                elevatorMotor.set(0);
            } else {
                elevatorMotor.set(speed);
            }
        }
    }

    /**
     * Sets all encoder values to 0.
     */
    public void resetEncoders(){
        elevatorMotor.getEncoder().setPosition(0);
    }

    /**
     * Returns the average position of both motors.
     * @return average position of both motors.
     */
    public double getPosition() {
        return elevatorEncoder / Constants.Elevator.ELEVATOR_GEARBOX_SCALE;
    }

    public void setElevatorSetpoint(double setpoint) {
        elevatorEncoder = setpoint;
    }

}
