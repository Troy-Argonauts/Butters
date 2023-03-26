package org.troyargonauts.robot.subsystems;

import com.ctre.phoenix.motorcontrol.can.TalonFX;
import edu.wpi.first.wpilibj.Compressor;
import edu.wpi.first.wpilibj.DoubleSolenoid;
import edu.wpi.first.wpilibj.PneumaticsModuleType;
import edu.wpi.first.wpilibj.Timer;
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

	public DualSpeedTransmission(DriveTrain driveTrain) {
		this.driveTrain = driveTrain;
		this.timer = new Timer();
		compressor = new Compressor(PneumaticsModuleType.CTREPCM);

		compressor.enableDigital();
		// TODO: Find what channel is right and left sides
		shiftSolenoid = new DoubleSolenoid(PneumaticsModuleType.CTREPCM, 0, 1);
	}

	public enum Gear {
		LOW, HIGH
	}

	// TODO: Find what value is low gear and high gear
	public void setGear(Gear gear) {
		switch(gear) {
		case LOW:
			shiftSolenoid.set(DoubleSolenoid.Value.kReverse);
			break;
		case HIGH:
			shiftSolenoid.set(DoubleSolenoid.Value.kForward);
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
		MotorController<TalonFX> rightMaster = driveTrain.getRightSide().getMaster();
		MotorController<TalonFX> leftMaster = driveTrain.getLeftSide().getMaster();
		if (isAutomaticShifting()) {
			if ((Math.abs(rightMaster.getMotorRotations()) > Constants.DriveTrain.LOW_HIGH_THRESHOLD) && (Math.abs(leftMaster.getMotorRotations()) > Constants.DriveTrain.LOW_HIGH_THRESHOLD) && (Math.abs(rightMaster.getDrawnCurrentAmps()) < Constants.DriveTrain.AMPS_THRESHOLD) && (Math.abs(leftMaster.getDrawnCurrentAmps()) < Constants.DriveTrain.AMPS_THRESHOLD) && (getGear() == Gear.LOW)) {
				shiftTime += Timer.getFPGATimestamp() - timer.get();
				if (shiftTime > Constants.DriveTrain.SHIFTING_THRESHOLD) {
					setGear(Gear.HIGH);
					driveTrain.set((rightSide, leftSide) -> {
						rightSide.getMaster().getInternalController().configVoltageCompSaturation(8.5);
						leftSide.getMaster().getInternalController().configVoltageCompSaturation(8.5);
					});
					driveTrain.setGearingParameters(Constants.DriveTrain.gearingHighGear);
					shiftTime = 0;
				}
			} else if ((Math.abs(rightMaster.getMotorRotations()) < Constants.DriveTrain.HIGH_LOW_THRESHOLD) && (Math.abs(leftMaster.getMotorRotations()) < Constants.DriveTrain.HIGH_LOW_THRESHOLD) && (getGear() == Gear.HIGH)) {
				setGear(Gear.LOW);
				driveTrain.set((rightSide, leftSide) -> {
					rightSide.getMaster().getInternalController().configVoltageCompSaturation(12);
					leftSide.getMaster().getInternalController().configVoltageCompSaturation(12);
				});
				driveTrain.setGearingParameters(Constants.DriveTrain.gearingLowGear);
			}
		}
	}
}
