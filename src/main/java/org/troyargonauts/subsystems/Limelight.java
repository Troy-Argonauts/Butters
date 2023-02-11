package org.troyargonauts.subsystems;

import edu.wpi.first.networktables.NetworkTableEntry;
import edu.wpi.first.networktables.NetworkTableInstance;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import org.troyargonauts.Constants;


public class Limelight extends SubsystemBase {

    private NetworkTableInstance table = null;
    private final double limelightHeight = Constants.Limelight.LIMELIGHT_HEIGHT;
    private final double aprilTagHeight = Constants.Limelight.APRIL_TAG_HEIGHT;
    private final double mountingAngle = Constants.Limelight.MOUNTING_ANGLE;

    private final double lowConeHeight = Constants.Limelight.LOW_CONE_HEIGHT;

    private final double highConeHeight =  Constants.Limelight.HIGH_CONE_HEIGHT;

    private final double distanceBetweenCones =  Constants.Limelight.DISTANCE_BETWEEN_CONES;

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

    public double getDistanceFromAprilTagInches()  {
        double angle = mountingAngle + getTy();
        if (angle < 1 || angle > 89)
            return 0;
        double tan = Math.tan(Math.toRadians(angle));
        return (aprilTagHeight - limelightHeight) / tan;

    }

    public double getLowConeDistance() {
        double angle = mountingAngle + getTy();
        if (angle < 1 || angle > 89)
            return 0;
        double tan = Math.tan(Math.toRadians(angle));
        return (lowConeHeight - limelightHeight) / tan;

    }
    public double getHighConeDistance() {
        double angle = mountingAngle + getTy();
        if (angle < 1 || angle > 89)
            return 0;
        double tan = Math.tan(Math.toRadians(angle));
        return ((highConeHeight - limelightHeight) / tan) + distanceBetweenCones;

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

    @Override
    public void periodic() {
        SmartDashboard.putNumber("April Tag Distance", getDistanceFromAprilTagInches());
        SmartDashboard.putNumber("Low Cone Height", getLowConeDistance());
        SmartDashboard.putNumber("High Cone Height: ", getHighConeDistance());

    }


}