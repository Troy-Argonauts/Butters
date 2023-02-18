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
public class Arm extends SubsystemBase {
    public final CANSparkMax elbowMotor;
    public final CANSparkMax intakeMotor;
    public final CANSparkMax wristMotor;
    public final AbsoluteEncoder elbowEncoder;
    public final AbsoluteEncoder wristEncoder;
    PIDController pid;
    public Arm() {
        elbowMotor = new CANSparkMax(Constants.Arm.ARM_MOTOR, CANSparkMaxLowLevel.MotorType.kBrushless);
        intakeMotor = new CANSparkMax(Constants.Arm.ARM_INTAKE, CANSparkMaxLowLevel.MotorType.kBrushless);
        wristMotor = new CANSparkMax(Constants.Arm.ARM_INTAKE, CANSparkMaxLowLevel.MotorType.kBrushless);

        elbowMotor.setIdleMode(CANSparkMax.IdleMode.kBrake);
        intakeMotor.setIdleMode(CANSparkMax.IdleMode.kBrake);
        wristMotor.setIdleMode(CANSparkMax.IdleMode.kBrake);

        elbowEncoder = elbowMotor.getAbsoluteEncoder(SparkMaxAbsoluteEncoder.Type.kDutyCycle);
        wristEncoder = wristMotor.getAbsoluteEncoder(SparkMaxAbsoluteEncoder.Type.kDutyCycle);
    }

    public enum intakeState {
        FORWARD, OFF, BACKWARD
    }
    public void setIntakeState(intakeState state) {
        switch(state) {
            case FORWARD:
                activateIntake();
            case OFF:
                deactivateIntake();
            case BACKWARD:
                reverseIntake();
        }
    }

    public void activateIntake() {
        intakeMotor.set(Constants.Arm.FORWARD_INTAKE_SPEED);
    }
    public void deactivateIntake() {
        intakeMotor.set(0);
    }
    public void reverseIntake() {
        intakeMotor.set(Constants.Arm.REVERSE_INTAKE_SPEED);
    }


    public void setElbowPower(double speed) {
        if ((getElbowPosition() < Constants.Arm.TOP_ELBOW_ENCODER_LIMIT) || (getWristPosition() > Constants.Arm.LOWER_ELBOW_ENCODER_LIMIT)) {
            elbowMotor.set(0);
        }
        else {
            elbowMotor.set(speed);
        }
    }
    public void setWristPower(double speed) {
        if ((getWristPosition() < Constants.Arm.TOP_WRIST_ENCODER_LIMIT) || (getWristPosition() > Constants.Arm.LOWER_WRIST_ENCODER_LIMIT)) {
            wristMotor.set(0);
        }
        else {
            wristMotor.set(speed);
        }
    }
    public double getElbowPosition() {
        return elbowEncoder.getPosition();
    }
    public double getWristPosition() {
        return wristEncoder.getPosition();
    }
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
