package org.troyargonauts.robot.subsystems;
import com.revrobotics.*;
import com.revrobotics.CANSparkMax.IdleMode;

import edu.wpi.first.math.controller.PIDController;
import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import org.troyargonauts.common.motors.wrappers.LazyCANSparkMax;
import org.troyargonauts.robot.Constants;

/**
 * Class representing arm. Includes PID control and absolute encoders
 * @author TeoElRey, ASH-will-WIN, SolidityContract
 */
public class Arm extends SubsystemBase {
    private final LazyCANSparkMax armMotor;
    private final PIDController armPID;
    private double armEncoder;
    private final DigitalInput upLimitArm, downLimitArm;

    private double desiredTarget;

    /**
     * Here, the motors, absolute encoders, and PID Controller are instantiated.
     */
    public Arm() {
        armMotor = new LazyCANSparkMax(Constants.Arm.ARM_PORT, CANSparkMaxLowLevel.MotorType.kBrushless);

        upLimitArm = new DigitalInput(Constants.Arm.ARM_UPPER_LIMIT_PORT);
        downLimitArm = new DigitalInput(Constants.Arm.ARM_LOWER_LIMIT_PORT);

        armMotor.getEncoder().setPositionConversionFactor(Constants.Arm.ARM_GEAR_RATIO);
        
        armMotor.setInverted(false);

        armMotor.setIdleMode(IdleMode.kBrake);

        armPID = new PIDController(Constants.Arm.ARM_P, Constants.Arm.ARM_I, Constants.Arm.ARM_D);
        armPID.setTolerance(Constants.Arm.ARM_TOLERANCE);
    }

    @Override
    public void periodic() {
        armEncoder = armMotor.getEncoder().getPosition();

//        SmartDashboard.putBoolean("up limit arm", !upLimitArm.get());
//        SmartDashboard.putBoolean("down limit arm", !downLimitArm.get());
        SmartDashboard.putNumber("Arm Encoder", armEncoder);
        SmartDashboard.putBoolean("Arm Finished", armPID.atSetpoint());

        if (!upLimitArm.get()) {
            armMotor.getEncoder().setPosition(0);
        }
    }

    public void run() {
        double motorSpeed = armPID.calculate(armEncoder, desiredTarget);
        if (motorSpeed > Constants.Arm.MAXIMUM_SPEED) motorSpeed = Constants.Arm.MAXIMUM_SPEED;
        else if (motorSpeed < -Constants.Arm.MAXIMUM_SPEED) motorSpeed = -Constants.Arm.MAXIMUM_SPEED;
        setPower(motorSpeed);
    }

    public void setDesiredTarget(double desiredTarget) {
        this.desiredTarget = desiredTarget;
    }

    /**
     * Sets Elbow to set speed given it is within encoder limits.
     * @param speed sets elbow motor to desired speed given that it is within the encoder limits.
     */
    public void setPower(double speed) {
        if (speed > 0) {
            if (!upLimitArm.get()) {
                armMotor.set(0);
            } else {
                armMotor.set(speed);
//                SmartDashboard.putNumber("Arm Speed", speed);
            }
        } else {
            if (!downLimitArm.get()) {
                armMotor.set(0);
            } else {
                armMotor.set(speed);
//                SmartDashboard.putNumber("Arm Speed", speed);
            }
        }
    }

    public void resetEncoders() {
        armMotor.getEncoder().setPosition(0);
    }
}
