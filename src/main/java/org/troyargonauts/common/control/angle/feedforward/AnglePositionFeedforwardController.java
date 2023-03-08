/* Copyright (c) 2023 StuyPulse Robotics. All rights reserved. */
/* This work is licensed under the terms of the MIT license */
/* found in the root directory of this project. */

package org.troyargonauts.common.control.angle.feedforward;

import org.troyargonauts.common.control.angle.AngleController;
import org.troyargonauts.common.control.feedforward.MotorFeedforward;
import org.troyargonauts.common.math.Angle;
import org.troyargonauts.common.util.AngleVelocity;

/**
 * A positional feedforward controller for angular systems.
 *
 * @see org.troyargonauts.common.control.feedforward.PositionFeedforwardController
 * @author Myles Pasetsky (myles.pasetsky@gmail.com)
 */
public class AnglePositionFeedforwardController extends AngleController {

    /** the feedforward model */
    private final MotorFeedforward mFeedforward;

    /** find the derivative of angular setpoints */
    private final AngleVelocity mDerivative;

    /**
     * Create an angle position feedforward controller
     *
     * @param feedforward model
     */
    public AnglePositionFeedforwardController(MotorFeedforward feedforward) {
        mFeedforward = feedforward;
        mDerivative = new AngleVelocity();
    }

    /**
     * Calculates a motor output by feeding the derivative of a positional setpoint to a feedforward
     * model
     *
     * @param setpoint angular positional setpoint
     * @param measurement angular position measurement, which is not used by the feedforward model
     * @return motor output from feedforward model
     */
    @Override
    protected double calculate(Angle setpoint, Angle measurement) {
        return mFeedforward.calculate(mDerivative.get(setpoint));
    }
}
