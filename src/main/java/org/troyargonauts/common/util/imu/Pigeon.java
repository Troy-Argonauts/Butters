package org.troyargonauts.common.util.imu;

import com.ctre.phoenix.sensors.Pigeon2;

public class Pigeon extends Pigeon2 {
    /**
     * Creates the Pigeon object with the specified CAN ID
     *
     * @param canID - the CAN ID of the Pigeon
     */
    private Pigeon(int canID) {
        super(canID);
    }

    /**
     * @return the Pigeon instance
     */
    public Pigeon2 getInstance() {
        return this;
    }

    /**
     * Measures how far the Pigeon has turned
     * @return the Pigeon's angle relative to last reset
     */
    public double getAngle() {
        return (this.getYaw() % 360);
    }

    /**
     * Measures how far the Pigeon is tilted forward
     * @return the Pigeon's forward tilt
     */
    public double getVerticalRotation() {
        return (this.getPitch() % 360);
    }

    /**
     * Measures how far the Pigeon is tilted horizontally
     * @return the Pigeon's horizontal tilt
     */
    public double getHorizontalRotation() {
        return (this.getRoll() % 360);
    }

    /**
     * Resets the Pigeon's angle to 0
     */
    public void resetAngle() {
        this.setYaw(0);
    }
}
