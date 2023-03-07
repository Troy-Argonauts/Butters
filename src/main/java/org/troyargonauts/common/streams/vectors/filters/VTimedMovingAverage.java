/* Copyright (c) 2023 StuyPulse Robotics. All rights reserved. */
/* This work is licensed under the terms of the MIT license */
/* found in the root directory of this project. */

package org.troyargonauts.common.streams.vectors.filters;

import org.troyargonauts.common.streams.filters.TimedMovingAverage;

/**
 * A filter that takes a timed moving average of a VStream
 *
 * @author Sam (sam.belliveau@gmail.com)
 */
public class VTimedMovingAverage extends XYFilter {

    public VTimedMovingAverage(Number time) {
        super(new TimedMovingAverage(time), new TimedMovingAverage(time));
    }
}
