package org.troyargonauts.robot.subsystems;

import com.ctre.phoenix.motorcontrol.can.TalonFX;
import edu.wpi.first.wpilibj.DoubleSolenoid;
import edu.wpi.first.wpilibj.PneumaticsModuleType;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj2.command.Subsystem;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import org.troyargonauts.common.motors.wrappers.LazyTalon;
import org.troyargonauts.common.motors.wrappers.MotorController;
import org.troyargonauts.robot.Constants;

public class DualSpeedTransmission extends SubsystemBase {

	private final DoubleSolenoid rightSolenoid;
	private final DoubleSolenoid leftSolenoid;
	private final DriveTrain driveTrain;
	private final Timer timer;
	private double shiftTime;

	public DualSpeedTransmission(DriveTrain driveTrain) {
		this.driveTrain = driveTrain;
		this.timer = new Timer();
		rightSolenoid = new DoubleSolenoid(PneumaticsModuleType.CTREPCM, 0, 1);
		leftSolenoid = new DoubleSolenoid(PneumaticsModuleType.CTREPCM,2, 3);
	}

	public enum Gear {
		LOW, HIGH
	}

	public void setGear(Gear gear) {
		switch(gear) {
		case LOW:
			rightSolenoid.set(DoubleSolenoid.Value.kReverse);
			leftSolenoid.set(DoubleSolenoid.Value.kReverse);
			break;
		case HIGH:
			rightSolenoid.set(DoubleSolenoid.Value.kForward);
			leftSolenoid.set(DoubleSolenoid.Value.kForward);
			break;
		}
	}

	public Gear getGear() {
		if(rightSolenoid.get() == DoubleSolenoid.Value.kForward && leftSolenoid.get() == DoubleSolenoid.Value.kForward) {
			return Gear.HIGH;
		} else {
			return Gear.LOW;
		}
	}

	/**
	 * If this method overloads the roborio main thread, implement CompletableFuture.
	 */
	@Override
	public void periodic() {
		MotorController<TalonFX> rightMaster = driveTrain.getRightSide().getMaster();
		MotorController<TalonFX> leftMaster = driveTrain.getLeftSide().getMaster();
		if (rightMaster.getMotorRotations() > Constants.DriveTrain.LOW_HIGH_THRESHOLD && leftMaster.getMotorRotations() > Constants.DriveTrain.LOW_HIGH_THRESHOLD && getGear() == Gear.LOW) {
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
		} else if (rightMaster.getMotorRotations() < Constants.DriveTrain.HIGH_LOW_THRESHOLD && leftMaster.getMotorRotations() < Constants.DriveTrain.HIGH_LOW_THRESHOLD && getGear() == Gear.HIGH) {
			setGear(Gear.LOW);
			driveTrain.set((rightSide, leftSide) -> {
				rightSide.getMaster().getInternalController().configVoltageCompSaturation(12);
				leftSide.getMaster().getInternalController().configVoltageCompSaturation(12);
			});
			driveTrain.setGearingParameters(Constants.DriveTrain.gearingLowGear);
		}
	}
}
