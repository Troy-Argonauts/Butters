/* Copyright (c) 2023 StuyPulse Robotics. All rights reserved. */
/* This work is licensed under the terms of the MIT license */
/* found in the root directory of this project. */

package org.troyargonauts.common.util;

import org.troyargonauts.common.math.Angle;

public class AngleVelocity {

    private final StopWatch mTimer;
    private Angle mPreviousAngle;

    public AngleVelocity() {
        mTimer = new StopWatch();
        mPreviousAngle = Angle.kZero;
    }

    public double get(Angle angle) {
        double velocity = angle.velocityRadians(mPreviousAngle, mTimer.reset());
        mPreviousAngle = angle;
        return velocity;
    }
}
