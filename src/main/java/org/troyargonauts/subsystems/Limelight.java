package org.troyargonauts.subsystems;

import edu.wpi.first.networktables.NetworkTableEntry;
import edu.wpi.first.networktables.NetworkTableInstance;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import org.troyargonauts.Constants;

/**
 * Limelight code
 * @author SavageCabbage360, TeoElRay and Sharvayu-Chavan
 */



public class Limelight extends SubsystemBase {
    /**
     * Instance variables for the code
     * PipeNumber value is the value of the current pipeline
     */
    public static int pipeNumber = 0;
    private NetworkTableInstance table = null;

    /**
     * States of the LEDs on the limelight
     * The states are ON, OFF, and BLINK
     */
    public enum LightMode {
        ON, OFF, BLINK
    }

    /**
     * The mode of the Limelight
     * The modes are VISION and DRIVER
     */

    public enum CameraMode {
        VISION, DRIVER
    }


    private final static Limelight INSTANCE = new Limelight();

    /**
     * Constructor for the limelight class
     */
    private Limelight() {

    }

    public static Limelight getInstance() {
        return INSTANCE;
    }

    /**
    * Method to raise value of pipeNumber variable
    * @return new integer value of the pipeNumber variable
    * */
    public static int raisePipe(){
       if (pipeNumber < 2) {
           return pipeNumber++;
       }else{
           return pipeNumber;
       }
    }

    /**
     * method to lower value of pipeNumber variable
     * @return new integer value of the pipeNumber variable
     */

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
        double angle = Constants.Limelight.MOUNTING_ANGLE + getTy();
        if (angle < 1 || angle > 89)
            return 0;
        double tan = Math.tan(Math.toRadians(angle));
        return (Constants.Limelight.APRIL_TAG_HEIGHT - Constants.Limelight.LIMELIGHT_HEIGHT) / tan;

    }

    /**
     * takes ty as reported from above and finds distance from low cone
     * @return distance the robot is from the low cone
     */

    public double getLowConeDistance() {
        double angle = Constants.Limelight.MOUNTING_ANGLE + getTy();
        if (angle < 1 || angle > 89)
            return 0;
        double tan = Math.tan(Math.toRadians(angle));
        return (Constants.Limelight.LOW_CONE_HEIGHT - Constants.Limelight.LIMELIGHT_HEIGHT) / tan;

    }

    /**
     * takes ty from above and calculates distance from robot to high cone stand
     * @return distance robot is from the high cone
     */
    public double getHighConeDistance() {
        double angle = Constants.Limelight.MOUNTING_ANGLE + getTy();
        if (angle < 1 || angle > 89)
            return 0;
        double tan = Math.tan(Math.toRadians(angle));
        return ((Constants.Limelight.HIGH_CONE_HEIGHT - Constants.Limelight.LIMELIGHT_HEIGHT) / tan) + Constants.Limelight.DISTANCE_BETWEEN_CONES;

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
     * pipeline the robot is currently on (is between 0 and 9)
     */
    public void setPipeline() {
        getValue("pipeline").setNumber(pipeNumber);
    }


    /**
     * Displays April tag distance, Low cone distance, High Cone distance on SmartDashboard
     */
    @Override
    public void periodic() {
        SmartDashboard.putNumber("April Tag Distance", getDistanceFromAprilTagInches());
        SmartDashboard.putNumber("Low Cone Distance", getLowConeDistance());
        SmartDashboard.putNumber("High Cone Distance: ", getHighConeDistance());
        setPipeline();
    }


}