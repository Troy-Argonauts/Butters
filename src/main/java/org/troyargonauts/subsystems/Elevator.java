package org.troyargonauts.subsystems;
import com.revrobotics.*;
import edu.wpi.first.math.controller.PIDController;
import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.PIDCommand;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import org.troyargonauts.Constants;
import org.troyargonauts.Robot;

/**
 * Class representing elevator subsystem. includes PID control and limit switches
 * @author TeoElRey, sgowda260
 */
public class Elevator extends SubsystemBase {
    private final CANSparkMax leftMotor;
    private final CANSparkMax rightMotor;
    private SparkMaxLimitSwitch upperLimitSwitch;
    private SparkMaxLimitSwitch lowerLimitSwitch;
    // private boolean upperLimitSwitchValue;
    // private boolean lowerLimitSwitchValue;
    private PIDController pid;
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
        leftMotor = new CANSparkMax(Constants.Elevator.LEFT, CANSparkMaxLowLevel.MotorType.kBrushless);
        rightMotor = new CANSparkMax(Constants.Elevator.RIGHT, CANSparkMaxLowLevel.MotorType.kBrushless);

        leftMotor.setInverted(false);
        rightMotor.setInverted(false);

        leftMotor.setIdleMode(CANSparkMax.IdleMode.kBrake);
        rightMotor.setIdleMode(CANSparkMax.IdleMode.kBrake);

        leftMotor.setSoftLimit(CANSparkMax.SoftLimitDirection.kReverse, 7);
        rightMotor.setSoftLimit(CANSparkMax.SoftLimitDirection.kReverse, 7);

//        upperLimitSwitch = leftMotor.getForwardLimitSwitch(SparkMaxLimitSwitch.Type.kNormallyClosed);
//        upperLimitSwitch = rightMotor.getForwardLimitSwitch(SparkMaxLimitSwitch.Type.kNormallyClosed);
//
//        lowerLimitSwitch = leftMotor.getForwardLimitSwitch(SparkMaxLimitSwitch.Type.kNormallyClosed);
//        lowerLimitSwitch = rightMotor.getForwardLimitSwitch(SparkMaxLimitSwitch.Type.kNormallyClosed);
        pid = new PIDController(Constants.Elevator.kP, Constants.Elevator.kI ,Constants.Elevator.kD, Constants.Elevator.PERIOD);
    }

    /**
     * Periodic will constantly check the encoder and limit switch values.
     * These values will later be used to make sure elevator extends or retracts to optimal position.
     */
    @Override
    public void periodic() {
//        upperLimitSwitchValue = getUpperLimitSwitchState();
//        lowerLimitSwitchValue = getLowerLimitSwitchState();
        rightMotorPosition = rightMotor.getEncoder().getPosition();
        leftMotorPosition = leftMotor.getEncoder().getPosition();
        SmartDashboard.putNumber("Right Encoder", rightMotor.getEncoder().getPosition());
        SmartDashboard.putNumber("Left Encoder", leftMotor.getEncoder().getPosition());

        SmartDashboard.putNumber("Left Draw", leftMotor.getOutputCurrent());
        SmartDashboard.putNumber("Right Draw", rightMotor.getOutputCurrent());
    }

    // /**
    //  * Will check whether the upper limit switch is enabled or not.
    //  * If it is, this means the elevator has extended to desired point and will stop extending
    //  *
    //  * @return  returns whether the limit switch is enabled or not.
    //  */
    // public boolean getUpperLimitSwitchState() {
    //     return upperLimitSwitch.isLimitSwitchEnabled();
    // }
    // /**
    //  * Will check whether the lower limit switch is enabled or not.
    //  * If it is, this means the elevator has retracted to desired point and will stop retracting
    //  *
    //  * @return  returns whether the limit switch is enabled or not.
    //  */
    // public boolean getLowerLimitSwitchState() {
    //     return lowerLimitSwitch.isLimitSwitchEnabled();
    // }

    /**
     * Elevator power will be set to a given speed from -1 to 1
     * If it is positive, elevator will extend until upper limit switch turns on
     * If it is negative, elevator retracts until lower limit switch turns on
     *
     * @param speed desired elevator extension or retraction speed
     */
    public void setPower(double speed, double nerf) {
        leftMotor.set(speed * nerf);
        rightMotor.set(speed * nerf);
    }
    /**
     * The elevator will shift to a given setpoint using the
     * predetermined PID Controller.
     * 
     * @param setpoint will be the desired extension point
     */
    public void elevatorPID(double setpoint) {
        pid.setSetpoint(setpoint);
        new PIDCommand(
            pid,
            () -> ((rightMotorPosition + leftMotorPosition) / (2 * Constants.Elevator.kEncoderGearboxScale)),
            setpoint,
            output -> setPower(output, 0.5),
            Robot.getElevator()
        );
    }
}
