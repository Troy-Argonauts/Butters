package org.troyargonauts.common.motors.wrappers;

import org.troyargonauts.common.math.OMath;
import org.troyargonauts.common.util.Gains;

public interface MotorController<T> {
    T getInternalController();

    class GearingParameters {
        final double cylinderToEncoderReduction, cylinderRadiusMeters, encoderCountsPerRevolution;

        public GearingParameters(
                final double reduction,
                final double cylinderRadiusMeters,
                final double encoderCountsPerRevolution
        ) {
            this.cylinderToEncoderReduction = reduction;
            this.cylinderRadiusMeters = cylinderRadiusMeters;
            this.encoderCountsPerRevolution = encoderCountsPerRevolution;
        }
    }

    enum NeutralBehaviour {
        COAST, BRAKE
    }

    /**
     * @return Volts fed into the motor controller
     */
    double getVoltageInput();
    /**
     * @return Volts applied to the motor
     */
    double getVoltageOutput();
    /**
     * @return Amps of the output
     */
    double getDrawnCurrentAmps();

    int getDeviceID();

    /**
     * @return Whether an encoder is attached
     */
    boolean isEncoderPresent();



    /**
     * @param profileID Set what profile is chosen for configuration or use
     */
    void setSelectedProfile(int profileID);

    /**
     * @param other A motorcontroller of the same type as this one
     * @param invert Whether to reverse the output direction of the followed motor
     * @return If the operation was successful
     */
    boolean follow(MotorController<T> other, boolean invert);

    MotorController<T> getLeader();

    /**
     * @param inverted Should the output of the motor be reversed from what is commanded
     * @return If the operation was successful
     */
    boolean setInverted(boolean inverted);

    /**
     * @param mode The behaviour of the motor controller with no volts applied
     * @return If the operation was successful
     */
    boolean setNeutralBehaviour(NeutralBehaviour mode);

    boolean setGearingParameters(GearingParameters gearingParameters);
    default boolean setGearingParameters(final double reduction, final double cylinderRadiusMeters, final double encoderCountsPerRevolution) {
        return setGearingParameters(new GearingParameters(reduction, cylinderRadiusMeters, encoderCountsPerRevolution));
    }

    /**
     * @param position Forces the encoder to set its current position
     * @return If the operation was successful
     */
    boolean setEncoderCounts(double position);

    /**
     * @param timeToRamp The amount of time, in seconds, to change 12V of the output
     * @return If the write succeeded
     */
    boolean setOpenLoopVoltageRampRate(double timeToRamp);
    boolean setClosedLoopVoltageRampRate(double timeToRamp);
    void set(final double value);

    /**
     * Zero-power
     */
    void setNeutral();

    boolean setPIDF(final double kP, final double kI, final double kD, final double kFF, final double tolerance);

    /**
     * @param cycle % of voltage input to apply to motor
     * @param feedforward
     */
    void setDutyCycle(double cycle, double feedforward);
    default void setDutyCycle(final double cycle) {
        setDutyCycle(cycle, 0.0);
    }

    /**
     * @param voltage Voltage to apply [-12.0, +12.0]
     * @param feedforward Feedforward duty cycle [-1.0, +1.0]
     */
    void setVoltage(double voltage, double feedforward);
    default void setVoltage(final double voltage) {
        setVoltage(voltage, 0.0);
    }

    void setVelocityMetersPerSecond(double velocity, double feedforward);
    default void setVelocityMetersPerSecond(final double velocity) {
        setVelocityMetersPerSecond(velocity, 0.0);
    }

    void setVelocityRPM(double rpm, double feedforward);
    default void setVelocityRPM(double rpm) {
        setVelocityRPM(rpm, 0.0);
    }

    default void setVelocityRadiansPerSecond(final double radiansPerSecond) {
        setVelocityRadiansPerSecond(radiansPerSecond, 0.0);
    }
    default void setVelocityRadiansPerSecond(final double radiansPerSecond, final double feedforward) {
        setVelocityRPM(OMath.radiansToRotations(radiansPerSecond), feedforward);
    }

    void setPositionMeters(double position, double feedforward);
    default void setPositionMeters(final double position) {
        setPositionMeters(position, 0.0);
    }

    void setPositionRotations(double position, double feedforward);
    default void setPositionRotations(final double position) {
        setPositionRotations(position, 0.0);
    }

    double getPositionMeters();
    double getPositionRotations();

    double getMotorRotations();

    /**
     * @return Current linear velocity in m/s
     */
    double getVelocityLinearMetersPerSecond();
    double getVelocityAngularRPM();

    void configurePIDF(final Gains gains, final int profileID);

    /**
     * @return radians/s of the motor
     */
    default double getVelocityAngularRadiansPerSecond() {

        return OMath.radiansToRotations(getVelocityAngularRPM());
    }

    int getSelectedProfile();
}

