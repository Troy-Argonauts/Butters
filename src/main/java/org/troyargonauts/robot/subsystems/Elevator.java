package org.troyargonauts.robot.subsystems;
import com.revrobotics.*;

import edu.wpi.first.math.controller.PIDController;
import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.PIDCommand;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import org.troyargonauts.robot.Constants;
import org.troyargonauts.robot.Robot;

/**
 * Class representing elevator subsystem. includes PID control and limit switches
 * @author TeoElRey, sgowda260, SolidityContract, ASH-will-WIN
 */
public class Elevator extends SubsystemBase {
    private final CANSparkMax liftMotor;
    // private final DigitalInput topLimitSwitch;
    private final DigitalInput bottomLimitSwitch;
    private final PIDController liftPID;
    private double liftMotorPosition;
    private double liftMotorSetpoint;



    /**
     * Instantiates the motor controllers, limit switches, encoder, and PID controller for the Elevator.
     * Additionally, the left side motor is set to be inverted
     * Encoders built into the NEO 550 motors track position of the elevator's carriage
     * Motors are set to brake mode
     * soft limit is set to 7, meaning motors will have a limit of 7 rotations backwards
     */
    public Elevator() {
        liftMotor = new CANSparkMax(Constants.Elevator.LIFT_MOTOR_PORT, CANSparkMaxLowLevel.MotorType.kBrushless);

        liftMotor.setInverted(true);

        liftMotor.setIdleMode(CANSparkMax.IdleMode.kBrake);

        liftMotor.setSoftLimit(CANSparkMax.SoftLimitDirection.kReverse, 7);

        liftMotor.getEncoder().setPositionConversionFactor(9);

        // topLimitSwitch = new DigitalInput(Constants.Elevator.TOP_PORT);
        bottomLimitSwitch = new DigitalInput(Constants.Elevator.BOTTOM_PORT);

        liftPID = new PIDController(Constants.Elevator.kP, Constants.Elevator.kI ,Constants.Elevator.kD);
        liftPID.setTolerance(Constants.Elevator.ELEVATOR_TOLERANCE);
    }

    /**
     * Periodic will constantly check the encoder and limit switch values.
     * These values will later be used to make sure elevator extends or retracts to optimal position.
     */
    @Override
    public void periodic() {
        liftMotorPosition = liftMotor.getEncoder().getPosition();

//        liftPID.calculate(liftMotorPosition, liftMotorSetpoint);

        SmartDashboard.putNumber("Lift Motor Position", liftMotorPosition);
        // SmartDashboard.putBoolean("Top Limit Switch", topLimitSwitch.get());
        SmartDashboard.putBoolean("Bottom Limit Switch", !bottomLimitSwitch.get());
        SmartDashboard.putBoolean("Elevator Setpoint", liftPID.atSetpoint());

        if (!bottomLimitSwitch.get()) {
            resetEncoders();
        }
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
            if (liftMotorPosition > 219.8557891845703) {
                liftMotor.set(0);
            } else {
                liftMotor.set(speed);
            }
        } else {
            if (!bottomLimitSwitch.get()) {
                liftMotor.set(0);
            } else {
                liftMotor.set(speed);
            }
        }
    }

    /**
     * Sets all encoder values to 0.
     */
    public void resetEncoders(){
        liftMotor.getEncoder().setPosition(0);
    }

    /**
     * Returns the average position of both motors.
     * @return average position of both motors.
     */
    public double getPosition() {
        return liftMotorPosition / Constants.Elevator.ELEVATOR_GEARBOX_SCALE;
    }

    public void setElevatorSetpoint(double setpoint) {
        liftMotorSetpoint = setpoint;
    }

    /**
     * The elevator will shift to a given setpoint using the
     * predetermined PID Controller.
     * 
     * @param setpoint will be the desired extension point
     */
    public PIDCommand elevatorPID(double setpoint) {
        liftPID.setSetpoint(setpoint);
        return new PIDCommand(
            liftPID,
            () -> getPosition(),
            setpoint,
            output -> setPower(output),
            Robot.getElevator()
        );
    }
}
