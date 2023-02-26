package org.troyargonauts.subsystems;

import edu.wpi.first.networktables.NetworkTableEntry;
import edu.wpi.first.networktables.NetworkTableInstance;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import org.troyargonauts.Constants;
import static org.troyargonauts.Robot.limelight;

/**
 * Limelight code
 * @author SavageCabbage360, TeoElRay, and Sharvayu-Chavan
 */



public class Limelight extends SubsystemBase {

    /**
     * The current pipeline number being used by the Limelight.
     */
    public static int pipeNumber = 0;
    private NetworkTableInstance table = null;

    /**
     * The different modes the Limelight's LED can be set to.
     */
    public enum LightMode {
        ON, OFF, BLINK
    }
    /**
     * The different modes the Limelight's camera can be set to.
     */
    public enum CameraMode {
        VISION, DRIVER
    }


    private final static Limelight INSTANCE = new Limelight();


    /**
     * Creates an instance of the Limelight subsystem and sets the initial LED mode to ON and the camera mode to VISION.
     */
    private Limelight() {
        limelight.setLedMode(Limelight.LightMode.ON);
        limelight.setCameraMode(Limelight.CameraMode.VISION);
    }


    /**
     * Returns the instance of the Limelight subsystem.
     *
     * @return The instance of the Limelight subsystem.
     */
    public static Limelight getInstance() {
        return INSTANCE;
    }

    /**
     * Raises the pipeline number by 1 if it is less than 2.
     *
     * @return The new pipeline number.
     */
    public static int raisePipe(){
       if (pipeNumber < 2) {
           return pipeNumber++;
       }else{
           return pipeNumber;
       }
    }

    /**
     * Lowers the pipeline number by 1 if it is greater than 0.
     *
     * @return The new pipeline number.
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
     * Returns the Ty (vertical offset from the crosshair to the target) value from the Limelight camera.
     *
     * @return The Ty(vertical offset from the crosshair to the target) value from the Limelight camera.
     */

    public double getTy() {
        return getValue("ty").getDouble(0.00);
    }

    /**
     * Calculates and returns the distance from the Limelight to the AprilTag target in inches.
     *
     * @return The distance from the Limelight to the AprilTag target in inches.
     */

    public double getDistanceFromAprilTagInches()  {
        double angle = Constants.Limelight.MOUNTING_ANGLE + getTy();
        if (angle < 1 || angle > 89)
            return 0;
        double tan = Math.tan(Math.toRadians(angle));
        return (Constants.Limelight.APRIL_TAG_HEIGHT - Constants.Limelight.LIMELIGHT_HEIGHT) / tan;

    }

    /**
     * Calculates and returns the distance from the Limelight to the low cone in inches.
     *
     * @return The distance from the Limelight to the low cone in inches.
     */
    public double getLowConeDistance() {
        double angle = Constants.Limelight.MOUNTING_ANGLE + getTy();
        if (angle < 1 || angle > 89)
            return 0;
        double tan = Math.tan(Math.toRadians(angle));
        return (Constants.Limelight.LOW_CONE_HEIGHT - Constants.Limelight.LIMELIGHT_HEIGHT) / tan;

    }

    /**
     * Calculates and returns the distance from the Limelight to the high cone in inches.
     *
     * @return The distance from the Limelight to the high cone in inches.
     */
    public double getHighConeDistance() {
        double angle = Constants.Limelight.MOUNTING_ANGLE + getTy();
        if (angle < 1 || angle > 89)
            return 0;
        double tan = Math.tan(Math.toRadians(angle));
        return ((Constants.Limelight.HIGH_CONE_HEIGHT - Constants.Limelight.LIMELIGHT_HEIGHT) / tan) + Constants.Limelight.DISTANCE_BETWEEN_CONES;

    }


    /**
     * Returns the network table entry for a given key in the Limelight table.
     *
     * @param key The key of the network table entry to retrieve.
     * @return The network table entry for the given key in the Limelight table.
     */

    private NetworkTableEntry getValue(String key) {
        if (table == null) {
            table = NetworkTableInstance.getDefault();
        }

        return table.getTable("limelight").getEntry(key);
    }


    /**
     * Sets the LED mode of the Limelight.
     *
     * @param mode the new LED mode to set
     */
    public void setLedMode(LightMode mode) {
        getValue("ledMode").setNumber(mode.ordinal());
    }

    /**
     * Sets the camera mode of the Limelight.
     *
     * @param mode the new camera mode to set
     */
    public void setCameraMode(CameraMode mode) {
        getValue("camMode").setNumber(mode.ordinal());
    }

    /**
     * Sets the pipeline number of the Limelight.
     *
     */
    public void setPipeline() {
        getValue("pipeline").setNumber(pipeNumber);
    }

    /**
     * This method displays the April Tag Distance, Low Cone Distance, and High Cone Distance from the robot calculated from the getDistanceFromAprilTagInches,
     * getLowConeDistance, and getHighConeDistance methods respectively. The setPipeline() calls the setPipeline method to check if the raise/lower pipeline method is called
     * and if it is, then it will change the pipeline accordingly
     */
    @Override
    public void periodic() {
        SmartDashboard.putNumber("April Tag Distance", getDistanceFromAprilTagInches());
        SmartDashboard.putNumber("Low Cone Distance", getLowConeDistance());
        SmartDashboard.putNumber("High Cone Distance: ", getHighConeDistance());
        setPipeline();
    }


}