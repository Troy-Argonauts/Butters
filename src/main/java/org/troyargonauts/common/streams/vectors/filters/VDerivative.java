/* Copyright (c) 2023 StuyPulse Robotics. All rights reserved. */
/* This work is licensed under the terms of the MIT license */
/* found in the root directory of this project. */

package org.troyargonauts.common.streams.vectors.filters;

import org.troyargonauts.common.streams.filters.Derivative;

/**
 * Filter that takes the derivative of a VStream with respect to time.
 *
 * @author Sam (sam.belliveau@gmail.com)
 */
public class VDerivative extends XYFilter {

    public VDerivative() {
        super(new Derivative(), new Derivative());
    }
}
