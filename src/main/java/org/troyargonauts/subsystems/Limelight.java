package org.troyargonauts.subsystems;

import edu.wpi.first.networktables.NetworkTableEntry;
import edu.wpi.first.networktables.NetworkTableInstance;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import org.troyargonauts.Constants;

/**
 * Limelight code
 * @author Abhinav Daram, Teodor Topan, Somya Sakalle, Sharvayu Chavan
 */



public class Limelight extends SubsystemBase {

    public static int pipeNumber = 0;
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

/**
 * methods to change value of pipeline
 * @return integer value of pipeline
 * */
    public static int raisePipe(){
       return pipeNumber++;
    }

    public static int lowerPipe(){
        if(pipeNumber > 0) {
            return pipeNumber--;
        }
        else{
            return pipeNumber;
        }
    }
     /**
     * @return vertical angle of offset from limelight crosshair to target
     */

    public double getTy() {
        return getValue("ty").getDouble(0.00);
    }

    /**
     *Takes ty as reported from above and finds distance of april tags
     * @return distance robot is from april tag
     */

    public double getDistanceFromAprilTagInches()  {
        double angle = mountingAngle + getTy();
        if (angle < 1 || angle > 89)
            return 0;
        double tan = Math.tan(Math.toRadians(angle));
        return (aprilTagHeight - limelightHeight) / tan;

    }

    /**
     * takes ty as reported from above and finds distance from low cone
     * @return distance the robot is from the low cone
     */

    public double getLowConeDistance() {
        double angle = mountingAngle + getTy();
        if (angle < 1 || angle > 89)
            return 0;
        double tan = Math.tan(Math.toRadians(angle));
        return (lowConeHeight - limelightHeight) / tan;

    }

    /**
     * takes ty from above and calculates distance from robot to high cone stand
     * @return distance robot is from the high cone
     */
    public double getHighConeDistance() {
        double angle = mountingAngle + getTy();
        if (angle < 1 || angle > 89)
            return 0;
        double tan = Math.tan(Math.toRadians(angle));
        return ((highConeHeight - limelightHeight) / tan) + distanceBetweenCones;

    }


    /**
     * Helper method to get an entry from the Limelight NetworkTable.
     * @param key
     * @return network table entry from network table
     */

    private NetworkTableEntry getValue(String key) {
        if (table == null) {
            table = NetworkTableInstance.getDefault();
        }

        return table.getTable("limelight").getEntry(key);
    }

    /**
     *
     * @param mode
     * what light mode the limelight is on
     */

    public void setLedMode(LightMode mode) {
        getValue("ledMode").setNumber(mode.ordinal());
    }

    /**
     *
     * @param mode
     * gets camera mode for the limelight
     */

    public void setCameraMode(CameraMode mode) {
        getValue("camMode").setNumber(mode.ordinal());
    }

    /**
     *
     * @param number
     * pipeline the robot is currently on (is between 0 and 9)
     */
    public void setPipeline() {
        getValue("pipeline").setNumber(pipeNumber);
    }

    @Override
    public void periodic() {
        SmartDashboard.putNumber("April Tag Distance", getDistanceFromAprilTagInches());
        SmartDashboard.putNumber("Low Cone Height", getLowConeDistance());
        SmartDashboard.putNumber("High Cone Height: ", getHighConeDistance());
        setPipeline();
    }


}