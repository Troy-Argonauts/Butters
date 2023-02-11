package sensors;

import com.revrobotics.ColorMatch;
import com.revrobotics.ColorMatchResult;
import com.revrobotics.ColorSensorV3;

import edu.wpi.first.wpilibj.I2C;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj.util.Color;
import edu.wpi.first.wpilibj2.command.SubsystemBase;

public class ColorSensor extends SubsystemBase {
    private final ColorSensorV3 colorSensor;

    public static Color kYellow;
    public static Color kPurple;
    public static Color kMiddle;

    public static ColorMatch colorMatch;

    Color detectedColor;

    ColorMatchResult match;

    public ColorSensor() {
        colorSensor = new ColorSensorV3(I2C.Port.kOnboard);

        kYellow = new Color(0.32, 0.52, 0.14);
        kPurple = new Color(0.18, 0.31, 0.51);
        kMiddle = new Color(0.3, 0.5, 0.24);

        colorMatch = new ColorMatch();
    }

    public String getColor() {
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

    @Override
    public void periodic() {
        detectedColor = colorSensor.getColor();
        match = colorMatch.matchClosestColor(detectedColor);

        SmartDashboard.putString("Color", detectedColor.red + ", " + detectedColor.green + ", " + detectedColor.blue);
        SmartDashboard.putString("Color Detected", getColor());
    }

}
