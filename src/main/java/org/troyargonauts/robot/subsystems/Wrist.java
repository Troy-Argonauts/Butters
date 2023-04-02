package org.troyargonauts.robot.subsystems;

import com.revrobotics.CANSparkMax;
import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import org.troyargonauts.common.motors.MotorCreation;
import org.troyargonauts.common.motors.wrappers.LazyCANSparkMax;

import static org.troyargonauts.robot.Constants.Wrist.*;

public class Wrist extends SubsystemBase {

	private final LazyCANSparkMax wristMotor, rotateMotor;
	private double desiredTarget;
	private final DigitalInput upLimitWrist, downLimitWrist;

	public Wrist() {
		wristMotor = MotorCreation.createDefaultSparkMax(WRIST_PORT);
		rotateMotor = MotorCreation.createDefaultSparkMax(ROTATE_PORT);

		upLimitWrist = new DigitalInput(WRIST_UPPER_LIMIT_PORT);
		downLimitWrist = new DigitalInput(WRIST_LOWER_LIMIT_PORT);

		wristMotor.getEncoder().setPositionConversionFactor(WRIST_GEAR_RATIO);

		wristMotor.setInverted(false);
		rotateMotor.setInverted(false);

		wristMotor.setIdleMode(CANSparkMax.IdleMode.kBrake);
		rotateMotor.setIdleMode(CANSparkMax.IdleMode.kBrake);

		wristMotor.getPIDController().setP(WRIST_P);
		wristMotor.getPIDController().setI(WRIST_I);
		wristMotor.getPIDController().setD(WRIST_D);

		wristMotor.getPIDController().setOutputRange(-0.6, 0.6);

		wristMotor.burnFlash();
	}

	@Override
	public void periodic() {
		SmartDashboard.putNumber("Wrist Encoder", wristMotor.getEncoder().getPosition());

		if (!downLimitWrist.get()) {
			wristMotor.getEncoder().setPosition(0);
			setDesiredTarget(WristState.HOME);
		}
	}

	public void run() {
		wristMotor.getPIDController().setReference(desiredTarget, CANSparkMax.ControlType.kPosition);
	}

	public void setDesiredTarget(WristState desiredState) {
		desiredTarget = desiredState.getEncoderPosition();
	}


	public void setPower(double joyStickValue) {
		double newTarget = desiredTarget + joyStickValue * 100;
		if (desiredTarget == 0 && !downLimitWrist.get() && newTarget > 0) {
			desiredTarget = newTarget;
		} else if (!upLimitWrist.get() && desiredTarget > newTarget) {
			desiredTarget = newTarget;
		}
	}

	/**
	 * Intake roller state is set here.
	 * @param state desired state of intake rollers to a motor speed.
	 */
	public void setIntakeState(IntakeState state) {
		switch(state) {
			case FORWARD:
				rotateMotor.set(0.7);
				break;
			case OFF:
				rotateMotor.set(0);
				break;
			case BACKWARD:
				rotateMotor.set(-0.7);
				break;
		}
	}

	/**
	 * Enums are the states of the intake rollers (FORWARD, OFF, BACKWARD).
	 */
	public enum IntakeState {
		FORWARD, OFF, BACKWARD
	}

	public enum WristState {
		INITIAL_HOME(-710),
		MIDDLE_CONE(834),
		HOME(0);

		private final int encoderPosition;

		WristState(int encoderPosition) {
			this.encoderPosition = encoderPosition;
		}

		public int getEncoderPosition() {
			return encoderPosition;
		}
	}

	public void resetEncoders() {
		wristMotor.getEncoder().setPosition(0);
	}
}
