package org.troyargonauts.subsystems;
import com.revrobotics.CANSparkMax;
import com.revrobotics.CANSparkMaxLowLevel;
import edu.wpi.first.math.controller.PIDController;
import edu.wpi.first.wpilibj2.command.PIDCommand;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import com.revrobotics.SparkMaxLimitSwitch;
import org.troyargonauts.Robot;

/**
 * Elevator Code
 *
 * @author Teodor Topan, Sudeep Gowda
 */
public class Elevator extends SubsystemBase {
    private final CANSparkMax leftMotor;
    private final CANSparkMax rightMotor;
    private SparkMaxLimitSwitch upperLimitSwitch;
    private SparkMaxLimitSwitch lowerLimitSwitch;
    private boolean upperLimitSwitchValue;
    private boolean lowerLimitSwitchValue;
    private Double leftMotorEncoder;
    PIDController pid;

    /**
     * Here, the motors, limit switches, and PID controller are instantiated.
     * Additionally, the right motor is inverted for convenience
     */
    public Elevator() {
        leftMotor = new CANSparkMax(0, CANSparkMaxLowLevel.MotorType.kBrushless);
        rightMotor = new CANSparkMax(1, CANSparkMaxLowLevel.MotorType.kBrushless);

        upperLimitSwitch = leftMotor.getForwardLimitSwitch(SparkMaxLimitSwitch.Type.kNormallyClosed);
        upperLimitSwitch = rightMotor.getForwardLimitSwitch(SparkMaxLimitSwitch.Type.kNormallyClosed);

        lowerLimitSwitch = leftMotor.getForwardLimitSwitch(SparkMaxLimitSwitch.Type.kNormallyClosed);
        lowerLimitSwitch = rightMotor.getForwardLimitSwitch(SparkMaxLimitSwitch.Type.kNormallyClosed);

        rightMotor.setInverted(true);
        pid = new PIDController(1, 0 ,0, 0.2);
    }

    /**
     * Periodic will constantly check the encoder and limit switch values.
     * These values will later be used to make sure elevator extends or retracts to optimal position.
     */
    @Override
    public void periodic() {
        leftMotorEncoder = leftMotor.getEncoder().getPosition();
        upperLimitSwitchValue = getUpperLimitSwitchState();
        lowerLimitSwitchValue = getLowerLimitSwitchState();

    }

    /**
     * Both of these methods access the state of each limit switch,
     * we will check these in periodic and turn off motors when one of them returns true to prevent
     * elevator from trying to extend when it is already at max extension
     *
     * @return  returns whether the limit switch is enabled or not.
     */
    public boolean getUpperLimitSwitchState() {
        return upperLimitSwitch.isLimitSwitchEnabled();
    }
    public boolean getLowerLimitSwitchState() {
        return lowerLimitSwitch.isLimitSwitchEnabled();
    }

    /**
     * Elevator power will be set to a given speed from -1 to 1
     *
     * @param speed desired elevator extension or retraction speed
     */
    public void setElevatorPower(double speed) {
        leftMotor.set(speed);
        rightMotor.set(speed);
    }

    /**
     * Using a PID command, the elevator will shift to a given setpoint using the
     * predetermined PID Controller. Will be used mainly in Autonomous
     * 
     * @param setpoint desired extension point
     */
    public void elevatorPID(double setpoint) {
        new PIDCommand(
                pid,
                () -> leftMotorEncoder,
                setpoint,
                this::setElevatorPower,
                Robot.getElevator()
        );
    }


}
