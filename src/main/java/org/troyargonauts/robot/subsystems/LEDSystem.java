package org.troyargonauts.robot.subsystems;

import com.ctre.phoenix.led.CANdle;
import com.ctre.phoenix.led.CANdleConfiguration;
import com.ctre.phoenix.led.RainbowAnimation;
import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import org.troyargonauts.robot.Constants;

import static org.troyargonauts.robot.Constants.LEDs.ledLength;


/**
 * This is the class for all LED methods
 */
public class LEDSystem extends SubsystemBase {

    public final static int goldR = 51;
    public final static int goldG = 51;
    public final static int goldB = 58;

    public final static int yellowR = 255;
    public final static int yellowG = 255;
    public final static int yellowB = 0;

    public final static int purpleR = 70;
    public final static int purpleG = 0;
    public final static int purpleB = 106;

    private final CANdle candle;
    public CANdleConfiguration config;

    /**
     * This class uses REV's CANdle to control the LEDs on the robot.
     * @author JJCgits, firearcher2012, ASH-will-WIN
     */
    public LEDSystem() {
        candle = new CANdle(Constants.LEDs.CANDLE);
        config = new CANdleConfiguration();
        config.stripType = CANdle.LEDStripType.RGB;
        candle.configAllSettings(config);
    }

    public void rainbow(){
        config.brightnessScalar = 0.5;
        candle.configAllSettings(config);
        RainbowAnimation rainbowAnimation = new RainbowAnimation(1,1,ledLength);
        candle.animate(rainbowAnimation);
    }

    /**
     * This method displays the Argonauts' gold color
     */

    // We could maybe set it up so that if we win, these are the lights that will display
    public void argoColors() {
        candle.setLEDs(goldR, goldG, goldB);
    }

    /**
     * This method will turn the LEDs purple
     */
    public void purpleCube() {
        candle.setLEDs(purpleR, purpleG, purpleB);
    }

    /**
     * This method will turn the LEDs yellow
     */
    public void yellowCone() {
        candle.setLEDs(yellowR, yellowG, yellowB);
    }

    /**
     * This method turns the LEDs off
     */
    public void ledOff() {
        candle.setLEDs(0, 0, 0);
    }

}