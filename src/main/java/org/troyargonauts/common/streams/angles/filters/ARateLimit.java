/* Copyright (c) 2023 StuyPulse Robotics. All rights reserved. */
/* This work is licensed under the terms of the MIT license */
/* found in the root directory of this project. */

package org.troyargonauts.common.streams.angles.filters;

import org.troyargonauts.common.math.Angle;
import org.troyargonauts.common.math.OMath;
import org.troyargonauts.common.util.StopWatch;

/**
 * This class lets you rate limit a stream of inputs
 *
 * <p>Instead of being based on the rate that update is called, the value you give it is based on
 * how much it should be able to change in one second.
 *
 * @author Sam (sam.belliveau@gmail.com)
 */
public class ARateLimit implements AFilter {

    // Used to get the time since the last get call
    private StopWatch mTimer;

    // Used to limit the change from the last value
    private Angle mLastValue;
    private Number mRateLimit;

    /** @param rateLimit The max speed in rad/s. */
    public ARateLimit(Number rateLimit) {
        if (rateLimit.doubleValue() <= 0) {
            throw new IllegalArgumentException("rateLimit must be a positive number");
        }

        mTimer = new StopWatch();
        mRateLimit = rateLimit;
        mLastValue = Angle.kZero;
    }

    public Angle get(Angle next) {
        final double limit = mRateLimit.doubleValue() * mTimer.reset();
        return mLastValue =
                mLastValue.addRadians(OMath.clamp(next.sub(mLastValue).toRadians(), limit));
    }
}
