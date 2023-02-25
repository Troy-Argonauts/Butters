package org.troyargonauts.subsystems;

import com.ctre.phoenix.led.CANdle;
import com.ctre.phoenix.led.CANdleConfiguration;
import com.ctre.phoenix.led.RainbowAnimation;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import org.troyargonauts.Constants;

/**
 * This is the class for all LED methods
 */
public class LEDSystem extends SubsystemBase {
    // The Variables

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
     * TThis instantiates and configures the CANdle
     */
    public LEDSystem() {
        candle = new CANdle(Constants.LEDs.kPort);
        config = new CANdleConfiguration();
        config.stripType = CANdle.LEDStripType.RGB;
        candle.configAllSettings(config);
    }


    /**
     * This method turns the LEDs to a rainbow color pattern. We will generally use this during standby.
     * @param ledLength refers to the number of LEDs on the strip that will be turned on
     */
    public void Rainbow(int ledLength) {
        // dim the LEDs to half brightness
        config.brightnessScalar = 0.5;
        candle.configAllSettings(config);
        RainbowAnimation rainbowAnim = new RainbowAnimation(1, 0.5, ledLength);
        candle.animate(rainbowAnim);
    }

    /**
     * This method displays the Argonauts' gold color
     */
    // We could maybe set it up so that if we win, these are the lights that will display
    public void argoColors() {
        candle.setLEDs(goldR, goldG, goldB);
    }
    // Switches color from "Black" (dark gray) to gold

    /**
     * This method will turn the LEDs purple
     */
    public void purpleCube() {
        candle.setLEDs(purpleR, purpleG, purpleB);
    }

    /**
     * This method will turn the LEDs yellow if we need a yellow cone
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

    /**
     * This method makes the LEDs red
     */
    public void losingState() {
        candle.setLEDs(255, 0, 0);
    }
}