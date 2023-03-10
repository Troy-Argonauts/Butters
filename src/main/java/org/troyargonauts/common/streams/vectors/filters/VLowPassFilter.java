/* Copyright (c) 2023 StuyPulse Robotics. All rights reserved. */
/* This work is licensed under the terms of the MIT license */
/* found in the root directory of this project. */

package org.troyargonauts.common.streams.vectors.filters;

import org.troyargonauts.common.streams.filters.LowPassFilter;

/**
 * A filter that applies a LowPassFilter to a VStream
 *
 * @author Sam (sam.belliveau@gmail.com)
 */
public class VLowPassFilter extends XYFilter {

    public VLowPassFilter(Number rc) {
        super(new LowPassFilter(rc), new LowPassFilter(rc));
    }
}
