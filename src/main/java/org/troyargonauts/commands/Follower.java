package org.troyargonauts.commands;

import org.opencv.core.Point;
import org.troyargonauts.Constants;

public class Follower {

    QuarticPathGenerator path;

    Point prevPoint = new Point(-1,-1);
    Point currentPoint = new Point(0,0);

    Point nextPoint = new Point(0,0);

    double currentHeading = 0;
    public Follower(QuarticPathGenerator path){

        this.path = path;

        path.generatePath();
    }

    public Point circleCenter(Point currentPoint, Point nextPoint, double heading){

        Point midpoint = new Point((currentPoint.x + nextPoint.x)/2,(currentPoint.y + nextPoint.y)/2);


        double slope = (currentPoint.y-nextPoint.y)/ (currentPoint.x - nextPoint.x);




        Point circleCenter;

        try{
        circleCenter = new Point((slope*midpoint.x- midpoint.y+Math.tan(Math.toRadians(heading))*currentPoint.x)/(slope+Math.tan(Math.toRadians(heading))), slope*((slope*midpoint.x- midpoint.y+Math.tan(Math.toRadians(heading))*currentPoint.x)/(slope+Math.tan(Math.toRadians(heading)))) - slope* midpoint.x + midpoint.y);
        } catch (Exception e){

            circleCenter = new Point((slope*midpoint.x - midpoint.y)/slope, currentPoint.y);

        }
        return circleCenter;




    }




    Double[] FollowPath(double speed){

        nextPoint = path.nextPoint(currentPoint, speed, prevPoint);

        Point circleCenter = circleCenter(currentPoint, nextPoint, currentHeading);

        double radius = Math.abs(circleCenter.x - currentPoint.x);


        double leftRadius, rightRadius;
        if ((nextPoint.x - currentPoint.x)*(circleCenter.y - currentPoint.y) - (nextPoint.y - currentPoint.y)*(circleCenter.x - currentPoint.x) > 0  ){
             leftRadius = radius - Constants.DriveConstants.trackWidthIn/2;

             rightRadius = radius + Constants.DriveConstants.trackWidthIn/2;


        } else {
            leftRadius = radius + Constants.DriveConstants.trackWidthIn/2;

            rightRadius = radius - Constants.DriveConstants.trackWidthIn/2;
        }


        double circlePercent = Math.toDegrees(Math.acos((currentPoint.x- nextPoint.x)/radius))/360;


        Double[] motorSpeeds = new Double[2];

        motorSpeeds[0] = (leftRadius*2*Math.PI*circlePercent/(Constants.DriveConstants.kWheelDiameterFeet *12)/0.5636)*(speed/3.316);

        motorSpeeds[1] = (rightRadius*2*Math.PI*circlePercent/(Constants.DriveConstants.kWheelDiameterFeet *12)/ 0.5636)*(speed/3.316);

        return motorSpeeds;

    }


}
