package org.troyargonauts;

import edu.wpi.first.hal.DIOJNI;
import edu.wpi.first.wpilibj.DigitalInput;

import java.lang.reflect.Field;

public class InvertedDigitalInput extends DigitalInput {
    /**
     * Create an instance of a Digital Input class. Creates a digital input given a channel.
     * Only use with limit switches on 2023 Robot
     *
     * @param channel the DIO channel for the digital input 0-9 are on-board, 10-25 are on the MXP
     */
    public InvertedDigitalInput(int channel) {
        super(channel);
    }

    @Override
    public boolean get() {
        Field m_handle = null;
        try {
            m_handle = super.getClass().getField("m_handle");
            m_handle.setAccessible(true);
            return !DIOJNI.getDIO(m_handle.getInt(super.getClass()));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }
}
