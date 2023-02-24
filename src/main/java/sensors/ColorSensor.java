package sensors;

import com.revrobotics.ColorMatch;
import com.revrobotics.ColorMatchResult;
import com.revrobotics.ColorSensorV3;

import edu.wpi.first.wpilibj.I2C;
import edu.wpi.first.wpilibj.util.Color;
import edu.wpi.first.wpilibj2.command.SubsystemBase;

/**
 * Code for REV ColorSensorV3 detects game piece based on color detected.
 * @author @SolidityContract @Aizakkuno
 */
public class ColorSensor extends SubsystemBase {
    private final ColorSensorV3 colorSensor;

    public static Color kYellow;
    public static Color kPurple;
    public static Color kMiddle;

    public static ColorMatch colorMatch;

    Color detectedColor;

    ColorMatchResult match;

    /** 
     * Constructs a new ColorSensor object with a new ColorSensorV3 object and initializes the pre-defined colors and the ColorMatch object.
     */
    public ColorSensor() {
        colorSensor = new ColorSensorV3(I2C.Port.kOnboard);

        kYellow = new Color(0.32, 0.52, 0.14);
        kPurple = new Color(0.18, 0.31, 0.51);
        kMiddle = new Color(0.3, 0.5, 0.24);

        colorMatch = new ColorMatch();
    }

    
    /** 
     * Returns a String representing the color detected by the sensor.
     * If the detected color matches a pre-defined color, the method returns the name of that color.
     * Otherwise, it returns "Nothing" if the detected color is close to the pre-defined middle color, or "idle" if no color is detected.
     * @return String name of color detected.
     */
    public String getColor() {
        match = colorMatch.matchClosestColor(detectedColor);
        if (match.color == kPurple) {
            return "Purple";
        } else if (match.color == kYellow){
            return "Yellow";
        } else if (match.color == kMiddle){
            return "Nothing";
        } else {
            return "idle";
        }
    }

    /** 
     * Gets the color detected from the color sensor.
     * Used for getting color values in testing.
     * @return color dectected from color sensor.
     */
    public Color getOutput() {
        return colorSensor.getColor();
    }

}
