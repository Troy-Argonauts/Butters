package org.troyargonauts.subsystems;

import com.ctre.phoenix.led.CANdle;
import com.ctre.phoenix.led.CANdleConfiguration;
import com.ctre.phoenix.led.RainbowAnimation;
import edu.wpi.first.wpilibj2.command.SubsystemBase;


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
     * This configures the CANdle
     */
    public LEDSystem() {
        candle = new CANdle(5);
        config = new CANdleConfiguration();
        config.stripType = CANdle.LEDStripType.RGB;
        candle.configAllSettings(config);
    }

    /**
     * This method turns the LEDs to a rainbow color pattern. We will generally use this during standby.
     * @param ledLength refers to the number of LEDs on the strip that will be turned on
     * @param enable determines if the LEDs are rainbow or not
     */
    public void ledStandby(int ledLength, boolean enable) {
        if(enable) {
            // dim the LEDs to half brightness
            
            config.brightnessScalar = 0.5;
            candle.configAllSettings(config);

            RainbowAnimation rainbowAnim = new RainbowAnimation(1, 0.5, ledLength);
            candle.animate(rainbowAnim);
        }
    }

    /**
     * This method displays the Argonauts' gold color. We plan to use it if we win.
     * @param win determines if argo colors are on or off
     */
    public void argoColors(boolean win) {
        if(win) {
            candle.setLEDs(goldR, goldG, goldB);
        }
    }

    /**
     * This method will turn the LEDs purple if we need a purple cube
     * @param purpleCube determines if the LEDs are purple or not
     */
    public void purpleCube(boolean purpleCube) {
        if(purpleCube) {
            candle.setLEDs(purpleR, purpleG, purpleB);
        }
    }

    /**
     * This method will turn the LEDs yellow if we need a yellow cone
     * @param yellowCone determines if the LEDs are yellow or not
     */
    public void yellowCone(boolean yellowCone) {
        if(yellowCone) {
            candle.setLEDs(yellowR, yellowG, yellowB);
        }
    }

    /**
     * This method makes the LEDs red. We plan to use this when we lose
     * @param lose determines if the LEDs are red or not
     */
    public void losingState(boolean lose) {
        if(lose) {
            candle.setLEDs(255, 0, 0);
        }
    }

    /**
     * This method turns the LEDs off
     * @param off turns the LEDs off
     */
    public void ledOff(boolean off) {
        if(off) {
            candle.setLEDs(0, 0, 0);
        }
    }
}