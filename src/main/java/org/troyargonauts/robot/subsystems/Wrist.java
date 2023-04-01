package org.troyargonauts.robot.subsystems;

import com.revrobotics.CANSparkMax;
import com.revrobotics.CANSparkMaxLowLevel;
import edu.wpi.first.math.controller.PIDController;
import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import org.troyargonauts.common.motors.wrappers.LazyCANSparkMax;
import org.troyargonauts.robot.Constants;

public class Wrist extends SubsystemBase {

	private final LazyCANSparkMax wristMotor, rotateMotor;
	public PIDController wristPID;
	private double wristEncoder, desiredTarget;
	private final DigitalInput upLimitWrist, downLimitWrist;

	public Wrist() {
		wristMotor = new LazyCANSparkMax(Constants.Wrist.WRIST_PORT, CANSparkMaxLowLevel.MotorType.kBrushless);
		rotateMotor = new LazyCANSparkMax(Constants.Wrist.ROTATE_PORT, CANSparkMaxLowLevel.MotorType.kBrushless);

		upLimitWrist = new DigitalInput(Constants.Wrist.WRIST_UPPER_LIMIT_PORT);
		downLimitWrist = new DigitalInput(Constants.Wrist.WRIST_LOWER_LIMIT_PORT);

		wristMotor.getEncoder().setPositionConversionFactor(Constants.Wrist.WRIST_GEAR_RATIO);
		rotateMotor.getEncoder().setPositionConversionFactor(Constants.Wrist.ROTATE_GEAR_RATIO);

		wristMotor.setInverted(false);
		rotateMotor.setInverted(false);

		wristMotor.setIdleMode(CANSparkMax.IdleMode.kBrake);
		rotateMotor.setIdleMode(CANSparkMax.IdleMode.kBrake);

		wristPID = new PIDController(Constants.Wrist.WRIST_GAINS[0], Constants.Wrist.WRIST_GAINS[1], Constants.Wrist.WRIST_GAINS[2]);
	}

	@Override
	public void periodic() {
		wristEncoder = wristMotor.getEncoder().getPosition();

//		SmartDashboard.putBoolean("up limit wrist", !upLimitWrist.get());
//		SmartDashboard.putBoolean("down limit wrist", !downLimitWrist.get());
//
		SmartDashboard.putNumber("Wrist Encoder", wristEncoder);
		SmartDashboard.putBoolean("Wrist Finished", wristPID.atSetpoint());
		SmartDashboard.putNumber("Error", wristPID.getPositionError());

		if (!downLimitWrist.get()) {
			wristMotor.getEncoder().setPosition(0);
		}
	}

	public void run() {
		double motorSpeed = wristPID.calculate(wristEncoder, desiredTarget);
//		if (motorSpeed > Constants.Wrist.MAXIMUM_SPEED) motorSpeed = Constants.Wrist.MAXIMUM_SPEED;
//		else if (motorSpeed < -Constants.Wrist.MAXIMUM_SPEED) motorSpeed = -Constants.Wrist.MAXIMUM_SPEED;
		System.out.println(motorSpeed);
		wristMotor.set(motorSpeed);
	}

	public void setDesiredTarget(double desiredTarget) {
		System.out.println("set wrist target to " + desiredTarget);
		this.desiredTarget = desiredTarget;
	}

	/**
	 * Sets Wrist to set speed given it is within encoder limits.
	 * @param speed sets wrist motor to desired speed given that it is within the encoder limits.
	 */
	public void setPower(double speed) {
		if ((speed > 0 && !upLimitWrist.get()) || (speed < 0 && !downLimitWrist.get())) {
			speed = 0;
		}
		System.out.println(speed);
		wristMotor.set(speed);
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
				rotateMotor.set(0.5);
				break;
			case OFF:
				rotateMotor.set(0);
				break;
			case BACKWARD:
				rotateMotor.set(-0.5);
				break;
		}
	}

	public void resetEncoders() {
		wristMotor.getEncoder().setPosition(0);
	}
}
