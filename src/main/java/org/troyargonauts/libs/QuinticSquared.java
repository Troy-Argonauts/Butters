package org.troyargonauts.libs;

import org.opencv.core.Mat;

public class QuinticSquared {

    public static double[] quinticSquared(double [] variables, int degree){

        double[] squaredCoefficients = new double[degree*2];

        squaredCoefficients[0] = Math.pow(variables[0], 2);
        squaredCoefficients[1] = 2*(variables[0]*variables[1]);
        squaredCoefficients[2] = 2*(variables[0]*variables[2]) + Math.pow(variables[1], 2);
        squaredCoefficients[3] = 2*(variables[0]*variables[3]) + 2*(variables[1]*variables[2]);
        squaredCoefficients[4] = 2*(variables[0]*variables[4]) + 2*(variables[1]*variables[3]) + Math.pow(variables[2],2);
        squaredCoefficients[5] = 2*(variables[1]*variables[4]) + 2*(variables[2]*variables[3]);
        squaredCoefficients[6] = Math.pow(variables[3],2);
        squaredCoefficients[7] = 2*(variables[3]*variables[4]);
        squaredCoefficients[8] = Math.pow(variables[4], 2);



        return squaredCoefficients;

    }
}
