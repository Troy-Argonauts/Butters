package org.troyargonauts.robot.subsystems;

import com.revrobotics.CANSparkMax;
import com.revrobotics.CANSparkMaxLowLevel;
import edu.wpi.first.math.controller.PIDController;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.PIDCommand;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import org.troyargonauts.common.util.motorcontrol.LazyCANSparkMax;
import org.troyargonauts.robot.Constants;
import org.troyargonauts.robot.Robot;

/**
 * Class representing arm. Includes PID control and absolute encoders
 * @author TeoElRey, ASH-will-WIN, SolidityContract
 */
public class Arm extends SubsystemBase {
    private final LazyCANSparkMax armMotor, manipulatorMotor, wristMotor;
//    private final AbsoluteEncoder elbowEncoder;
//    private final AbsoluteEncoder wristEncoder;
    private final PIDController wristPID, armPID;
    private double armEncoderValue;
    private double wristEncoderValue;
    private double wristSetpoint, armSetpoint;

    /**
     * Here, the motors, absolute encoders, and PID Controller are instantiated.
     */
    public Arm() {
        armMotor = new LazyCANSparkMax(Constants.Arm.ELBOW, CANSparkMaxLowLevel.MotorType.kBrushless);
        manipulatorMotor = new LazyCANSparkMax(Constants.Arm.MANIPULATOR, CANSparkMaxLowLevel.MotorType.kBrushless);
        wristMotor = new LazyCANSparkMax(Constants.Arm.WRIST, CANSparkMaxLowLevel.MotorType.kBrushless);

        armMotor.setIdleMode(CANSparkMax.IdleMode.kBrake);
        manipulatorMotor.setIdleMode(CANSparkMax.IdleMode.kBrake);
        wristMotor.setIdleMode(CANSparkMax.IdleMode.kBrake);

        armMotor.setSmartCurrentLimit(Constants.Arm.CURRENT_LIMIT);
        manipulatorMotor.setSmartCurrentLimit(Constants.Arm.CURRENT_LIMIT);
        wristMotor.setSmartCurrentLimit(Constants.Arm.CURRENT_LIMIT);

//        elbowEncoder = elbowMotor.getAbsoluteEncoder(SparkMaxAbsoluteEncoder.Type.kDutyCycle);
//        wristEncoder = wristMotor.getAbsoluteEncoder(SparkMaxAbsoluteEncoder.Type.kDutyCycle);

        resetEncoders();
        wristPID = new PIDController(Constants.Arm.WRIST_P, Constants.Arm.WRIST_I, Constants.Arm.WRIST_D, Constants.Arm.WRIST_PERIOD);
        wristPID.setTolerance(Constants.Arm.WRIST_TOLERANCE);
        wristSetpoint = Constants.Arm.WRIST_DEFAULT;

        armPID = new PIDController(Constants.Arm.ARM_P, Constants.Arm.ARM_I, Constants.Arm.ARM_D, Constants.Arm.ARM_PERIOD);
        armPID.setTolerance(Constants.Arm.ARM_TOLERANCE);
        armSetpoint = Constants.Arm.ARM_DEFAULT;

    }
    @Override
    public void periodic() {
        wristEncoderValue = wristMotor.getEncoder().getPosition();
        armEncoderValue = armMotor.getEncoder().getPosition();

        setWristPower(wristPID.calculate(wristEncoderValue, wristSetpoint));
        setArmPower(armPID.calculate(armEncoderValue, armSetpoint));

        SmartDashboard.putNumber("Wrist Encoder", wristEncoderValue);
        SmartDashboard.putNumber("Arm Encoder", armEncoderValue);

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
                manipulatorMotor.set(Constants.Arm.FORWARD_INTAKE_SPEED);
                break;
            case OFF:
                manipulatorMotor.set(0);
                break;
            case BACKWARD:
                manipulatorMotor.set(Constants.Arm.REVERSE_INTAKE_SPEED);
                break;
        }
    }

    /**
     * Sets Elbow to set speed given it is within encoder limits.
     * @param speed sets elbow motor to desired speed given that it is within the encoder limits.
     */
    public void setArmPower(double speed) {
        armMotor.set(speed * 0.5);
    }

    public void armTeleOp(double speed) {
//        if (armSetpoint >= 0) {
//            armSetpoint = 0;
//        } else {
//            armSetpoint += (speed * 0.6);
//        }

        armSetpoint += (speed * 0.6);
    }

    public void setArmSetpoint(double setpoint) {
        armSetpoint = setpoint;
    }

    /**
     * Sets Wrist to set speed given it is within encoder limits.
     * @param speed sets wrist motor to desired speed given that it is within the encoder limits.
     */
    public void setWristPower(double speed) {
            wristMotor.set((speed * 0.2));
    }

    public void wristTeleOp(double speed) {
        wristSetpoint += speed * 1.1;
    }

    public void setWristSetpoint(double setpoint) {
        wristSetpoint = setpoint;
    }

    /**
     * Returns elbow position
     * @return returns the position of elbow
     */
    public double getArmPosition() {
        return armEncoderValue;
    }

    /**
     * Returns wrist position
     * @return returns the position of wrist
     */
    public double getWristPosition() {
        return wristEncoderValue;
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
     * @param setpoint the point desired to be reached by the wrist
     */
    public PIDCommand wristPid(double setpoint) {
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
