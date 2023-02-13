package org.troyargonauts.commands;

import org.opencv.core.Point;
import org.troyargonauts.libs.Matrix;
import org.troyargonauts.libs.Newton_Raphson;
import org.troyargonauts.libs.QuinticSquared;

public class QuarticPathGenerator {

    double [][] points = new double[5][2];
    double [][] coefficents = new double[5][5];

    double [] variables = new double[5];
    public QuarticPathGenerator(double x1, double y1, double x2, double y2, double x3, double y3, double x4, double y4, double x5, double y5){
        points[0][0] = x1;
        points[1][0] = x2;
        points[2][0] = x3;
        points[3][0] = x4;
        points[4][0] = x5;
        points[0][1] = y1;
        points[1][1] = y2;
        points[2][1] = y3;
        points[3][1] = y4;
        points[4][1] = y5;

    }


    public void generatePath(){
        for (int row = 0; row < 6; row++){
           for (int column = 0; column < 6; column++){

               coefficents[column][row] = Math.pow(points[row][0], Math.abs(column - 4));

           }
        }

        double [][] inverseCoefficents = Matrix.inverse(coefficents);



        for (int i = 0; i < 3; i++){
           variables[i] =  inverseCoefficents[0][i]*points[i][1] + inverseCoefficents[1][i]*points[i][1] + inverseCoefficents[2][i]*points[i][1] +inverseCoefficents[3][i]*points[i][1] + inverseCoefficents[4][i]*points[i][1];
        }

    }

    public Point returnPoint(double x){

        double y = Math.pow(x,4)*variables[0]+Math.pow(x,3)*variables[1]+Math.pow(x,2)*variables[2] +x*variables[3]+ variables[4];
        Point pos = new Point(x,y);
        return pos;
    }

    public Point nextPoint(Point currentCoord, double distance, Point prevPoint){

        double[] squaredCoefficients = QuinticSquared.quinticSquared(variables, 4);

        squaredCoefficients[8] = squaredCoefficients[8] + currentCoord.y - Math.pow(currentCoord.x, 2) - Math.pow(distance,2);
        squaredCoefficients[7] = squaredCoefficients[7] - 2*currentCoord.x;
        squaredCoefficients[6] --;


        double[] solutions = new double[2];
        double[] derivedVariables = Newton_Raphson.Derive(squaredCoefficients);
        double estimate = Newton_Raphson.rootEstimate(squaredCoefficients);

        solutions[0] = Newton_Raphson.Newton_Raphson_Calc(squaredCoefficients, derivedVariables, 0.05, estimate);

        double[] newCoefficents = new double[8];
        for (int i =0; i < 8; i++){
            newCoefficents[i] = squaredCoefficients[i+1]/solutions[0];
        }
        derivedVariables = Newton_Raphson.Derive(newCoefficents);
        estimate = Newton_Raphson.rootEstimate(newCoefficents);

        solutions[1] = Newton_Raphson.Newton_Raphson_Calc(newCoefficents, derivedVariables, 0.05, estimate);

        Point point1 = returnPoint(solutions[0]);
        Point point2 = returnPoint(solutions[1]);

        double DistanceToPoint1 = Math.sqrt(Math.pow((prevPoint.x - point1.x),2 ) +Math.pow((prevPoint.y - point1.y),2 ));
        double DistanceToPoint2 = Math.sqrt(Math.pow((prevPoint.x - point2.x),2 ) +Math.pow((prevPoint.y - point2.y),2 ));

        if (DistanceToPoint1> DistanceToPoint2){
            return point1;
        } else {
            return point2;
        }
    }




}
