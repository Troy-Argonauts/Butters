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

    public final static int gold[] = {51, 51, 58};

    public final static int yellow[] = {255, 255, 0};

    public final static int purple[] = {70, 0, 106};

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

    /**
     * This method makes the LEDs rainbow
     */
    public void rainbow(){
        config.brightnessScalar = 0.5;
        candle.configAllSettings(config);
        RainbowAnimation rainbowAnimation = new RainbowAnimation(1,0.5,ledLength);
        candle.animate(rainbowAnimation);
    }

    /**
     * This method displays the Argonauts' gold color
     */

    // We could maybe set it up so that if we win, these are the lights that will display
    public void argoColors() {
        candle.setLEDs(gold[0], gold[1], gold[2]);
    }

    /**
     * This method will turn the LEDs purple
     */
    public void purpleCube() {
        DriverStation.reportWarning("Purple", false);
        candle.setLEDs(purple[0], purple[1], purple[2]);
    }

    /**
     * This method will turn the LEDs yellow
     */
    public void yellowCone() {
        DriverStation.reportWarning("Yellow", false);
        candle.setLEDs(yellow[0], yellow[1], yellow[2]);
    }

    /**
     * This method turns the LEDs off
     */
    public void ledOff() {
        candle.setLEDs(0, 0, 0);
    }

}