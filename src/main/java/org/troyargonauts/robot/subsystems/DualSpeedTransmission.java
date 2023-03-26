package org.troyargonauts.robot.subsystems;

import com.ctre.phoenix.motorcontrol.can.TalonFX;
import edu.wpi.first.wpilibj.Compressor;
import edu.wpi.first.wpilibj.DoubleSolenoid;
import edu.wpi.first.wpilibj.PneumaticsModuleType;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import org.troyargonauts.common.motors.wrappers.MotorController;
import org.troyargonauts.robot.Constants;

public class DualSpeedTransmission extends SubsystemBase {

	private final DoubleSolenoid shiftSolenoid;
	private final DriveTrain driveTrain;
	private final Timer timer;
	private double shiftTime;
	private Compressor compressor;
	private boolean automaticShifting = true;
	MotorController<TalonFX> rightMaster;
	MotorController<TalonFX> leftMaster;
	public DualSpeedTransmission(DriveTrain driveTrain) {
		this.driveTrain = driveTrain;
		this.timer = new Timer();
		compressor = new Compressor(PneumaticsModuleType.CTREPCM);

		rightMaster = driveTrain.getRightSide().getMaster();
		leftMaster = driveTrain.getLeftSide().getMaster();

		compressor.enableDigital();
		shiftSolenoid = new DoubleSolenoid(PneumaticsModuleType.CTREPCM, 0, 1);
	}

	public enum Gear {
		LOW, HIGH
	}

	public void setGear(Gear gear) {
		switch(gear) {
		case LOW:
			shiftSolenoid.set(DoubleSolenoid.Value.kReverse);
			driveTrain.set((rightSide, leftSide) -> {
				setMaxVoltage(12);
			});
			driveTrain.setGearingParameters(Constants.DriveTrain.gearingLowGear);
			break;
		case HIGH:
			shiftSolenoid.set(DoubleSolenoid.Value.kForward);
			driveTrain.set((rightSide, leftSide) -> {
				setMaxVoltage(8.5);
			});
			driveTrain.setGearingParameters(Constants.DriveTrain.gearingHighGear);
			break;
		}
	}

	public Gear getGear() {
		if(shiftSolenoid.get() == DoubleSolenoid.Value.kForward) {
			return Gear.HIGH;
		} else {
			return Gear.LOW;
		}
	}

	public void disableAutomaticShifting() {
		automaticShifting = false;
	}

	public void enableAutomaticShifting() {
		automaticShifting = true;
	}

	public boolean isAutomaticShifting() {
		return automaticShifting;
	}

	/**
	 * If this method overloads the roborio main thread, implement CompletableFuture.
	 */

	@Override
	public void periodic() {
		if (isAutomaticShifting()) {
			if (drivetrainReady() && lowAmpDraw() && (getGear() == Gear.LOW)) {
				shiftTime += Timer.getFPGATimestamp() - timer.get();
				if (shiftTime > Constants.DriveTrain.SHIFTING_THRESHOLD) {
					setGear(Gear.HIGH);
					shiftTime = 0;
				}
			} else if (drivetrainReady() && (getGear() == Gear.HIGH)) {
				shiftTime += Timer.getFPGATimestamp() - timer.get();
				if (shiftTime > Constants.DriveTrain.SHIFTING_THRESHOLD) {
					setGear(Gear.LOW);
					shiftTime = 0;
				}
			}
		}

		SmartDashboard.putBoolean("Low Gear", getGear() == Gear.LOW);
	}

	public boolean drivetrainReady() {
		if (getGear() == Gear.LOW) {
			return (Math.abs(rightMaster.getMotorRotations()) > Constants.DriveTrain.LOW_HIGH_THRESHOLD) && (Math.abs(leftMaster.getMotorRotations()) > Constants.DriveTrain.LOW_HIGH_THRESHOLD);
		} else if (getGear() == Gear.HIGH) {
			return (Math.abs(rightMaster.getMotorRotations()) < Constants.DriveTrain.HIGH_LOW_THRESHOLD) && (Math.abs(leftMaster.getMotorRotations()) < Constants.DriveTrain.HIGH_LOW_THRESHOLD);
		}
		return false;
	}

	public boolean lowAmpDraw() {
		return (Math.abs(rightMaster.getDrawnCurrentAmps()) < Constants.DriveTrain.AMPS_THRESHOLD) && (Math.abs(leftMaster.getDrawnCurrentAmps()) < Constants.DriveTrain.AMPS_THRESHOLD);
	}

	public void setMaxVoltage(double voltage) {
		rightMaster.getInternalController().configVoltageCompSaturation(voltage);
		leftMaster.getInternalController().configVoltageCompSaturation(voltage);
	}
}

