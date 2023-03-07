/* Copyright (c) 2023 StuyPulse Robotics. All rights reserved. */
/* This work is licensed under the terms of the MIT license */
/* found in the root directory of this project. */

package org.troyargonauts.common.streams.vectors.filters;

import org.troyargonauts.common.streams.filters.MovingAverage;

/**
 * A filter that takes a moving average of a VStream
 *
 * @author Sam (sam.belliveau@gmail.com)
 */
public class VMovingAverage extends XYFilter {

    public VMovingAverage(int size) {
        super(new MovingAverage(size), new MovingAverage(size));
    }
}
