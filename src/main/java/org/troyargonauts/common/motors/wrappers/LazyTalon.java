package org.troyargonauts.common.motors.wrappers;

import com.ctre.phoenix.ErrorCode;
import com.ctre.phoenix.motorcontrol.*;
import com.ctre.phoenix.motorcontrol.can.BaseTalon;
import com.ctre.phoenix.motorcontrol.can.TalonFX;
import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.RobotController;
import org.troyargonauts.common.math.OMath;
import org.troyargonauts.common.util.Gains;

import java.util.function.Supplier;

@SuppressWarnings("HungarianNotationMemberVariables")
public class LazyTalon<TalonType extends BaseTalon> implements MotorController<TalonType> {
	private static final double Tau = 2 * Math.PI;

	private static final int
			MAX_TRIES = 3,
			TIMEOUT_MS = 50;
	private final String m_name;
	private final TalonType m_internal;
	private final boolean m_isFX;
	private final boolean m_isEncoderPresent;
	private double m_ticksPerRotation;

	private ControlMode m_lastMode;
	private DemandType m_lastDemandType;
	private int m_selectedProfileID;
	private double m_lastValue, m_lastDemand;

	private boolean m_inverted;
	private double m_cylinderToEncoderReduction, m_cylinderRadiusMeters;
	private boolean m_isMotionProfilingConfigured;
	private MotorController<TalonType> m_leader;

	protected LazyTalon(final TalonType talon, final boolean isSlave) {
		m_internal = talon;
		m_internal.enableVoltageCompensation(true);
		runWithRetries(() -> m_internal.configVoltageCompSaturation(12.0, TIMEOUT_MS));

		m_isFX = m_internal instanceof TalonFX;

		m_name = "Talon" + (m_isFX ? "FX" : "SRX") + "-" + (isSlave ? "Slave-" : "") + m_internal.getDeviceID();

		if (m_isFX) {
			m_isEncoderPresent = true;
			m_ticksPerRotation = 2048;
		} else {
			m_isEncoderPresent = new SensorCollection(m_internal).getPulseWidthRiseToRiseUs() != 0;
			m_ticksPerRotation = 4096;
		}

		m_inverted = false;
		m_selectedProfileID = 0;
		m_isMotionProfilingConfigured = false;

		m_lastMode = null;
		m_lastDemandType = null;
		m_lastValue = Double.NaN;
		m_lastDemand = Double.NaN;
	}

	public LazyTalon(final TalonType talon) {
		this(talon,false);
	}

	@Override
	public TalonType getInternalController() {
		return m_internal;
	}

	@Override
	public double getVoltageInput() {
		return m_internal.getBusVoltage();
	}

	@Override
	public double getVoltageOutput() {
		return m_internal.getMotorOutputVoltage();
	}

	@Override
	public double getDrawnCurrentAmps() {
		return m_internal.getStatorCurrent();
	}

	@Override
	public int getDeviceID() {
		return m_internal.getDeviceID();
	}

	@Override
	public boolean isEncoderPresent() {
		return m_isEncoderPresent;
	}

	@Override
	public void setSelectedProfile(final int profileID) {
		m_selectedProfileID = profileID;
	}

	@Override
	public MotorController<TalonType> getLeader() {
		return m_leader;
	}

	@Override
	public synchronized boolean follow(final MotorController<TalonType> other, final boolean oppose) {
		m_internal.setInverted(oppose ? InvertType.OpposeMaster : InvertType.FollowMaster);
		m_internal.set(ControlMode.Follower, other.getDeviceID());
		m_leader = other;
		return true;
	}

	private void set(final ControlMode mode, final double value, final DemandType demandType, final double demand) {
		if (mode != m_lastMode || value != m_lastValue || demandType != m_lastDemandType || demand != m_lastDemand) {
			m_internal.set(mode, value, demandType, demand);

			m_lastMode = mode;
			m_lastValue = value;

			m_lastDemandType = demandType;
			m_lastDemand = demand;
		}
	}

	public void set(final double value) {
		if (value != m_lastValue) {
			m_internal.set(ControlMode.PercentOutput, value);
			m_lastMode = ControlMode.PercentOutput;
			m_lastValue = value;

			m_lastDemandType = null;
			m_lastDemand = Double.NaN;
		}
	}

	@Override
	public boolean setInverted(final boolean inverted) {
		m_inverted = inverted;
		m_internal.setInverted(inverted);
		return true;
	}

	@Override
	public boolean setNeutralBehaviour(final NeutralBehaviour mode) {
		m_internal.setNeutralMode(mode == NeutralBehaviour.BRAKE ? NeutralMode.Brake : NeutralMode.Coast);
		return true;
	}

	@Override
	public boolean setGearingParameters(final GearingParameters gearingParameters) {
		m_cylinderToEncoderReduction = gearingParameters.cylinderToEncoderReduction;
		m_cylinderRadiusMeters = gearingParameters.cylinderRadiusMeters;

		if (!m_isFX) {
			m_ticksPerRotation = gearingParameters.encoderCountsPerRevolution;
		}

		return true;
	}

	@Override
	public synchronized boolean setEncoderCounts(final double position) {
		return runWithRetries(() -> m_internal.setSelectedSensorPosition((int) position, m_selectedProfileID, TIMEOUT_MS));
	}

	@Override
	public synchronized boolean setOpenLoopVoltageRampRate(final double timeToRamp) {
		return runWithRetries(() -> m_internal.configOpenloopRamp(timeToRamp, TIMEOUT_MS));
	}

	@Override
	public synchronized boolean setClosedLoopVoltageRampRate(final double timeToRamp) {
		return runWithRetries(() -> m_internal.configClosedloopRamp(timeToRamp, TIMEOUT_MS));
	}

	@Override
	public void setNeutral() {
		set(ControlMode.Disabled, 0.0, DemandType.ArbitraryFeedForward, 0.0);
	}

	@Override
	public double getPositionMeters() {
		return getPositionRotations() * (Tau * m_cylinderRadiusMeters);
	}

	@Override
	public double getPositionRotations() {
		return nativeUnits2Rotations(getInversionMultiplier() * m_internal.getSelectedSensorPosition(m_selectedProfileID));
	}

	@Override
	public double getMotorRotations() {
		return (600 * m_internal.getSelectedSensorVelocity()) / m_ticksPerRotation;
	}

	@Override
	public synchronized double getVelocityLinearMetersPerSecond() {
		return (getVelocityAngularRPM() / 60) * (Tau * m_cylinderRadiusMeters);
	}

	@Override
	public synchronized double getVelocityAngularRPM() {
		return nativeUnits2RPM(getInversionMultiplier() * (int) m_internal.getSelectedSensorVelocity(m_selectedProfileID));
	}

	@Override
	public void configurePIDF(Gains gains, int profileID) {
		setSelectedProfile(profileID);
		setPIDF(gains.getP(), gains.getI(), gains.getD(), gains.getF(), gains.getTolerance());
	}

	@Override
	public synchronized boolean setPIDF(final double p, final double i, final double d, final double ff, final double tolerance) {
		boolean success = true;

		success &= runWithRetries(() -> m_internal.config_kP(m_selectedProfileID, p, TIMEOUT_MS));
		success &= runWithRetries(() -> m_internal.config_kI(m_selectedProfileID, i, TIMEOUT_MS));
		success &= runWithRetries(() -> m_internal.config_kD(m_selectedProfileID, d, TIMEOUT_MS));
		success &= runWithRetries(() -> m_internal.config_kF(m_selectedProfileID, ff, TIMEOUT_MS));
		success &= runWithRetries(() -> m_internal.configAllowableClosedloopError(m_selectedProfileID, (int) tolerance, TIMEOUT_MS));

		return success;
	}

	@Override
	public void setDutyCycle(final double cycle, final double feedforward) {
		set(ControlMode.PercentOutput, cycle, DemandType.ArbitraryFeedForward, feedforward);
	}

	@Override
	public void setVoltage(final double voltage, final double feedforward) {
		final var currentVoltage = RobotController.getBatteryVoltage();
		setDutyCycle(voltage / currentVoltage, feedforward / currentVoltage);
	}

	@Override
	public void setVelocityMetersPerSecond(final double velocity, final double feedforward) {
		setVelocityRadiansPerSecond(velocity / m_cylinderRadiusMeters, feedforward);
	}

	@Override
	public void setVelocityRPM(final double rpm, final double feedforward) {
		set(ControlMode.Velocity,
				RPM2NativeUnitsPer100ms(rpm),
				DemandType.ArbitraryFeedForward,
				feedforward);
	}

	@Override
	public void setPositionMeters(final double meters, final double feedforward) {
		setPositionRotations(meters / (Tau * m_cylinderRadiusMeters), feedforward);
	}

	@Override
	public synchronized void setPositionRotations(final double rotations, final double feedforward) {
		set(m_isMotionProfilingConfigured ? ControlMode.MotionMagic : ControlMode.Position,
				getInversionMultiplier() * rotations2NativeUnits(rotations),
				DemandType.ArbitraryFeedForward, feedforward);
	}

	@Override
	public int getSelectedProfile() {
		return m_selectedProfileID;
	}

	public boolean setNominalOutputForward(final double percentOut) {
		return runWithRetries(() -> m_internal.configNominalOutputForward(percentOut, TIMEOUT_MS));
	}

	public boolean setNominalOutputReverse(final double percentOut) {
		return runWithRetries(() -> m_internal.configNominalOutputReverse(percentOut, TIMEOUT_MS));
	}

	public boolean setPeakOutputForward(final double percentOut) {
		return runWithRetries(() -> m_internal.configPeakOutputForward(percentOut, TIMEOUT_MS));
	}

	public boolean setPeakOutputReverse(final double percentOut) {
		return runWithRetries(() -> m_internal.configPeakOutputReverse(percentOut, TIMEOUT_MS));
	}

	public boolean setNominalOutputs(final double forward, final double reverse) {
		boolean success = true;

		success &= setNominalOutputForward(forward);
		success &= setNominalOutputReverse(reverse);

		return success;
	}

	public synchronized boolean setPeakOutputs(final double forward, final double reverse) {
		boolean success = true;

		success &= setPeakOutputForward(forward);
		success &= setPeakOutputReverse(reverse);

		return success;
	}

	private double meters2Rotations(final double meters) {
		return meters / (Tau * m_cylinderRadiusMeters);
	}

	private double metersPerSecond2RPM(final double meters) {
		return meters2Rotations(meters) * 60;
	}

	private double nativeUnits2Rotations(final double nativeUnits) {
		return nativeUnits * m_cylinderToEncoderReduction / m_ticksPerRotation;
	}

	private double nativeUnits2RPM(final int nativeUnits) {
		return nativeUnits * m_cylinderToEncoderReduction / m_ticksPerRotation * 600.0;
	}

	private int rotations2NativeUnits(final double rotations) {
		return (int) (rotations * m_ticksPerRotation / m_cylinderToEncoderReduction);
	}

	private int RPM2NativeUnitsPer100ms(final double rpm) {
		return (int) radiansPerSecond2NativeUnitsPer100ms(OMath.radiansToRotations(rpm));
}

	private double radiansPerSecond2NativeUnitsPer100ms(final double rps) {
		return (rps / Tau * m_ticksPerRotation / 10.0) / m_cylinderToEncoderReduction;
	}

	protected int getInversionMultiplier() {
		return (m_inverted && !m_isFX ? -1 : 1);
	}

	private synchronized boolean runWithRetries(final Supplier<ErrorCode> call) {
		boolean success;

		int tries = 0;

		do {
			success = call.get() == ErrorCode.OK;
		} while (!success && tries++ < MAX_TRIES);

		if (tries >= MAX_TRIES || !success) {
			DriverStation.reportError("Failed to configure Talon on Port " + m_internal.getDeviceID() + "!!", false);
			return false;
		} else {
			return true;
		}
	}
}
