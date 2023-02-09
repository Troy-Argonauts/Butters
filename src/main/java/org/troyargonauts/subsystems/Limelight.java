package org.troyargonauts.subsystems;

import edu.wpi.first.networktables.NetworkTableEntry;
import edu.wpi.first.networktables.NetworkTableInstance;
import edu.wpi.first.wpilibj2.command.SubsystemBase;


public class Limelight extends SubsystemBase {

    private NetworkTableInstance table = null;
    private final double limelightHeight = 0;
    private final double aprilTagHeight = 0;
    private final double mountingAngle = 0;

    public enum LightMode {
        ON, OFF, BLINK
    }

    public enum CameraMode {
        VISION, DRIVER
    }


    private final static Limelight INSTANCE = new Limelight();

    private Limelight() {

    }

    public static Limelight getInstance() {
        return INSTANCE;
    }

    public double getTy() {
        return getValue("ty").getDouble(0.00);
    }

    public double getDistanceFromTargetInches()  {
        double angle = mountingAngle + getTy();
        if (angle < 1 || angle > 89)
            return 0;
        double tan = Math.tan(Math.toRadians(angle));
        return (aprilTagHeight - limelightHeight) / tan;
    }


    private NetworkTableEntry getValue(String key) {
        if (table == null) {
            table = NetworkTableInstance.getDefault();
        }

        return table.getTable("limelight").getEntry(key);
    }

    public void setLedMode(LightMode mode) {
        getValue("ledMode").setNumber(mode.ordinal());
    }

    public void setCameraMode(CameraMode mode) {
        getValue("camMode").setNumber(mode.ordinal());
    }
    public void setPipeline(int number) {
        getValue("pipeline").setNumber(number);
    }


}