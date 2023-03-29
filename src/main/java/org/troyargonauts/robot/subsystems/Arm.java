package org.troyargonauts.robot.subsystems;
import com.revrobotics.*;
import com.revrobotics.CANSparkMax.IdleMode;
import com.revrobotics.CANSparkMaxLowLevel.MotorType;
import com.revrobotics.SparkMaxLimitSwitch.Type;

import edu.wpi.first.math.controller.PIDController;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.PIDCommand;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import org.troyargonauts.robot.Constants;
import org.troyargonauts.robot.Robot;

/**
 * Class representing arm. Includes PID control and absolute encoders
 * @author TeoElRey, ASH-will-WIN, SolidityContract
 */
public class Arm extends SubsystemBase {
    private final CANSparkMax armMotor, wristMotor, rollMotor;
    private final PIDController armPID, wristPID;
    private double armEncoderValue, wristEncoderValue;
    private double armSetpoint, wristSetpoint;
    private final SparkMaxLimitSwitch upperLimitSwitchArm, lowerLimitSwitchArm, upperLimitSwitchWrist, lowerLimitSwitchWrist;

    /**
     * Here, the motors, absolute encoders, and PID Controller are instantiated.
     */
    public Arm() {
        armMotor = new CANSparkMax(Constants.Arm.ARM_PORT, CANSparkMaxLowLevel.MotorType.kBrushless);
        wristMotor = new CANSparkMax(Constants.Arm.WRIST_PORT, CANSparkMaxLowLevel.MotorType.kBrushless);
        rollMotor = new CANSparkMax(Constants.Arm.ROLLER_PORT, MotorType.kBrushless);

        upperLimitSwitchArm = armMotor.getForwardLimitSwitch(Type.kNormallyClosed);
        lowerLimitSwitchArm = armMotor.getForwardLimitSwitch(Type.kNormallyClosed);

        upperLimitSwitchWrist = wristMotor.getForwardLimitSwitch(Type.kNormallyClosed);
        lowerLimitSwitchWrist = wristMotor.getForwardLimitSwitch(Type.kNormallyClosed);

        armMotor.setIdleMode(IdleMode.kBrake);
        wristMotor.setIdleMode(IdleMode.kBrake);
        rollMotor.setIdleMode(IdleMode.kCoast);

        armPID = new PIDController(Constants.Arm.ARM_D, Constants.Arm.ARM_I, Constants.Arm.ARM_P);
        armPID.setTolerance(Constants.Arm.ARM_TOLERANCE);

        wristPID = new PIDController(Constants.Arm.WRIST_P, Constants.Arm.WRIST_I, Constants.Arm.WRIST_D);
        wristPID.setTolerance(Constants.Arm.WRIST_TOLERANCE);
    }

    @Override
    public void periodic() {
        armEncoderValue = armMotor.getEncoder().getPosition();
        wristEncoderValue = wristMotor.getEncoder().getPosition();

        setArmPower(armPID.calculate(armEncoderValue, armSetpoint));
        setWristPower(wristPID.calculate(wristEncoderValue, wristSetpoint));

        SmartDashboard.putNumber("Arm Encoder", armEncoderValue);
        SmartDashboard.putBoolean("Arm Finished", armPID.atSetpoint());

        SmartDashboard.putNumber("Wrist Encoder", wristEncoderValue);
        SmartDashboard.putBoolean("Wrist Finished", wristPID.atSetpoint());
    }

    /**
     * Enums are the states of the intake rollers (FORWARD, OFF, BACKWARD).
     */
    public enum IntakeState {
        FORWARD, OFF, BACKWARD
    }

    /**
     * Intake roller state is set here.
     * @param state desired state of intake rollers to a motor speed.
     */
    public void setIntakeState(IntakeState state) {
        switch(state) {
            case FORWARD:
                rollMotor.set(0.5);
                break;
            case OFF:
                rollMotor.set(0);
                break;
            case BACKWARD:
                rollMotor.set(-0.5);
                break;
        }
    }

    /**
     * Sets Elbow to set speed given it is within encoder limits.
     * @param speed sets elbow motor to desired speed given that it is within the encoder limits.
     */
    public void setArmPower(double speed) {
        if (speed > 0) {
            if (upperLimitSwitchArm.isPressed()) {
                armMotor.set(0);
            } else {
                armMotor.set(speed);
                SmartDashboard.putNumber("Arm Speed", speed);
            }
        } else {
            if (lowerLimitSwitchArm.isPressed()) {
                armMotor.set(0);
            } else {
                armMotor.set(speed);
                SmartDashboard.putNumber("Arm Speed", speed);
            }
        }
    }

    /**
     * Sets Wrist to set speed given it is within encoder limits.
     * @param speed sets wrist motor to desired speed given that it is within the encoder limits.
     */
    public void setWristPower(double speed) {
        if (speed > 0) {
            if (upperLimitSwitchWrist.isPressed()) {
                wristMotor.set(0);
            } else {
                wristMotor.set(speed);
                SmartDashboard.putNumber("Wrist Speed", speed);
            }
        } else {
            if (lowerLimitSwitchWrist.isPressed()) {
                wristMotor.set(0);
            } else {
                wristMotor.set(speed);
                SmartDashboard.putNumber("Wrist Speed", speed);
            }
        }
    }

    /**
     * Returns elbow position
     * @return returns the position of elbow
     */
    public double getArmPosition() {
        return armEncoderValue / Constants.Arm.ARM_GEAR_RATIO;
    }

    /**
     * Returns wrist position
     * @return returns the position of wrost
     */
    public double getWristPosition() {
        return wristEncoderValue / Constants.Arm.WRIST_GEAR_RATIO;
    }

    public void setArmPosition(double setpoint) {
        armSetpoint = setpoint;
    }

    public void setWristPosition(double setpoint) {
        wristSetpoint = setpoint;
    }

    public void resetEncoders() {
        armMotor.getEncoder().setPosition(0);
        wristMotor.getEncoder().setPosition(0);
    }

    /**
     * Using a PID command, elbow will rotate to a setpoint using the PID Controller.
     * @param setpoint the point desired to be reached by the elbow.
     */
    public PIDCommand armPID(double setpoint) {
        armPID.setSetpoint(setpoint);
        return new PIDCommand(
            armPID,
            this::getArmPosition,
            setpoint,
            this::setArmPower, 
            Robot.getArm()
        );
    }

    /**
     * Using a PID command, wrist will rotate to a setpoint using the PID Controller.
     * @param setpoint the point desired to be reached by the wrist.
     */
    public PIDCommand wristPID(double setpoint) {
        wristPID.setSetpoint(setpoint);
        return new PIDCommand(
            wristPID,
            this::getWristPosition,
            setpoint,
            this::setWristPower, 
            Robot.getArm()
        );
    }
}
