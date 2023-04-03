package org.troyargonauts.robot.subsystems;

import com.revrobotics.CANSparkMax;
import com.revrobotics.CANSparkMax.IdleMode;
import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import org.troyargonauts.common.motors.MotorCreation;
import org.troyargonauts.common.motors.wrappers.LazyCANSparkMax;
import org.troyargonauts.robot.Constants;

import static org.troyargonauts.robot.Constants.Arm.*;

/**
 * Class representing arm. Includes PID control and absolute encoders
 *
 * @author TeoElRey, ASH-will-WIN, SolidityContract
 */
public class Arm extends SubsystemBase {
    private final LazyCANSparkMax armMotor;
    private final DigitalInput upLimitArm, downLimitArm;

    private double desiredTarget;

    /**
     * Here, the motors, absolute encoders, and PID Controller are instantiated.
     */
    public Arm() {
        armMotor = MotorCreation.createDefaultSparkMax(ARM_PORT);

        upLimitArm = new DigitalInput(Constants.Arm.ARM_UPPER_LIMIT_PORT);
        downLimitArm = new DigitalInput(Constants.Arm.ARM_LOWER_LIMIT_PORT);

        armMotor.getEncoder().setPositionConversionFactor(Constants.Arm.ARM_GEAR_RATIO);

        armMotor.setInverted(true);

        armMotor.setIdleMode(IdleMode.kBrake);

        armMotor.getPIDController().setP(ARM_P);
        armMotor.getPIDController().setI(ARM_I);
        armMotor.getPIDController().setD(ARM_D);

        armMotor.getPIDController().setOutputRange(-0.75, 0.75);

        armMotor.burnFlash();
    }

    @Override
    public void periodic() {
        SmartDashboard.putNumber("Arm Encoder", armMotor.getEncoder().getPosition());

        if (!upLimitArm.get()) {
            armMotor.getEncoder().setPosition(0);
        }
    }

    public void run() {
        armMotor.getPIDController().setReference(desiredTarget, CANSparkMax.ControlType.kPosition);
    }

    public void setDesiredTarget(ArmState desiredState) {
        if (desiredState.getEncoderPosition() < desiredTarget && desiredState.getEncoderPosition() < 1000) {
            armMotor.getPIDController().setOutputRange(-0.4, 0.4);
        } else {
            armMotor.getPIDController().setOutputRange(-0.75, 0.75);
        }
        desiredTarget = desiredState.getEncoderPosition();
    }

    /**
     * Sets Elbow to the joystick value given it is within encoder limits.
     *
     * @param joyStickValue sets elbow motor to a joystick value given that it is within the encoder limits.
     */
    public void setPower(double joyStickValue) {
        double newTarget = desiredTarget + (-joyStickValue * 100);
        System.out.println(newTarget);
        if ((desiredTarget <= 5 || desiredTarget >= 0) && newTarget > 0) {
            armMotor.getPIDController().setOutputRange(-0.75, 0.75);
            desiredTarget = newTarget;
        } else if (downLimitArm.get() && newTarget < desiredTarget) { // Encoder values are all negative so signs are inversed compared to others
            if (newTarget < 1000) {
                armMotor.getPIDController().setOutputRange(-0.4, 0.4);
            }
            desiredTarget = newTarget;
        }
    }

    public boolean isHomeLimitPressed() {
        return !upLimitArm.get();
    }

    public void resetEncoders() {
        armMotor.getEncoder().setPosition(0);
    }

    public enum ArmState {

        HOME(0), FLOOR_PICKUP(-7029);
        final int encoderPosition;

        ArmState(int encoderPosition) {
            this.encoderPosition = encoderPosition;
        }

        public int getEncoderPosition() {
            return encoderPosition;
        }
    }
}
