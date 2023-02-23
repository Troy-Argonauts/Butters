package org.troyargonauts.subsystems;
import com.revrobotics.AbsoluteEncoder;
import com.revrobotics.CANSparkMax;
import com.revrobotics.CANSparkMaxLowLevel;
import com.revrobotics.SparkMaxAbsoluteEncoder;
import edu.wpi.first.math.controller.PIDController;
import edu.wpi.first.wpilibj2.command.PIDCommand;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import org.troyargonauts.Constants;
import org.troyargonauts.Robot;

/**
 * Arm Code
 * @author Ashwin Shrivastav, Teodor Topan, Rohan Gajula-Ghosh
 */
public class Arm extends SubsystemBase {
    private final CANSparkMax elbowMotor;
    private final CANSparkMax intakeMotor;
    private final CANSparkMax wristMotor;
    private final AbsoluteEncoder elbowEncoder;
    private final AbsoluteEncoder wristEncoder;
    private final PIDController pid;

    /**
     * Here, the motors, absolute encoders, and PID Controller are instantiated.
     */
    public Arm() {
        elbowMotor = new CANSparkMax(Constants.Arm.ARM_MOTOR, CANSparkMaxLowLevel.MotorType.kBrushless);
        intakeMotor = new CANSparkMax(Constants.Arm.ARM_INTAKE, CANSparkMaxLowLevel.MotorType.kBrushless);
        wristMotor = new CANSparkMax(Constants.Arm.ARM_INTAKE, CANSparkMaxLowLevel.MotorType.kBrushless);

        elbowMotor.setIdleMode(CANSparkMax.IdleMode.kBrake);
        intakeMotor.setIdleMode(CANSparkMax.IdleMode.kBrake);
        wristMotor.setIdleMode(CANSparkMax.IdleMode.kBrake);

        elbowEncoder = elbowMotor.getAbsoluteEncoder(SparkMaxAbsoluteEncoder.Type.kDutyCycle);
        wristEncoder = wristMotor.getAbsoluteEncoder(SparkMaxAbsoluteEncoder.Type.kDutyCycle);

        pid = new PIDController(Constants.Arm.kP, Constants.Arm.kI, Constants.Arm.kD, Constants.Arm.PERIOD);
    }

    /**
     * Enums are the states of the intake rollers (FORWARD, OFF, BACKWARD).
     */
    public enum intakeState {
        FORWARD, OFF, BACKWARD
    }

    /**
     * Intake roller state is set here.
     * @param state desired state of intake rollers to a motor speed.
     */
    public void setIntakeState(intakeState state) {
        switch(state) {
            case FORWARD:
                intakeMotor.set(Constants.Arm.FORWARD_INTAKE_SPEED);
                break;
            case OFF:
                intakeMotor.set(0);
                break;
            case BACKWARD:
                intakeMotor.set(Constants.Arm.REVERSE_INTAKE_SPEED);
                break;
        }
    }

    /**
     * Sets Elbow to set speed given it is within encoder limits.
     * @param speed sets elbow motor to desired speed given that it is within the encoder limits.
     */
    public void setElbowPower(double speed) {
        if ((getElbowPosition() < Constants.Arm.TOP_ELBOW_ENCODER_LIMIT) || (getWristPosition() > Constants.Arm.LOWER_ELBOW_ENCODER_LIMIT)) {
            elbowMotor.set(0);
        }
        else {
            elbowMotor.set(speed);
        }
    }

    /**
     * Sets Wrist to set speed given it is within encoder limits.
     * @param speed sets wrist motor to desired speed given that it is within the encoder limits.
     */
    public void setWristPower(double speed) {
        if ((getWristPosition() < Constants.Arm.TOP_WRIST_ENCODER_LIMIT) || (getWristPosition() > Constants.Arm.LOWER_WRIST_ENCODER_LIMIT)) {
            wristMotor.set(0);
        }
        else {
            wristMotor.set(speed);
        }
    }

    /**
     * Returns elbow position
     * @return returns the position of elbow
     */
    public double getElbowPosition() {
        return elbowEncoder.getPosition();
    }

    /**
     * Returns wrist position
     * @return returns the position of wrist
     */
    public double getWristPosition() {
        return wristEncoder.getPosition();
    }

    /**
     * Using a PID command, elbow will rotate to a setpoint using the PID Controller.
     * @param setpoint the point desired to be reached by the elbow.
     */
    public PIDCommand elbowPid(double setpoint) {
        pid.setSetpoint(setpoint);
        return new PIDCommand(
            pid,
            this::getElbowPosition,
            setpoint,
            this::setElbowPower,
            Robot.getArm()
        );
    }

    /**
     * Using a PID command, wrist will rotate to a setpoint using the PID Controller.
     * @param setpoint the point desired to be reached by the wrist
     */
    public PIDCommand wristPid(double setpoint) {
        pid.setSetpoint(setpoint);
        return new PIDCommand(
            pid,
            this::getWristPosition,
            setpoint,
            this::setWristPower,
            Robot.getArm()
        );
    }
}
