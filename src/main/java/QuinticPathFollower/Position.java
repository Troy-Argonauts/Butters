package QuinticPathFollower;

import org.troyargonauts.Constants.DriveConstants;

public class Position {

    private double x, y, prevangle, prevleftPos, prevrightPos;
    
    public Position(double x, double y, double angle) {
        this.x = x;
        this.y = y;
        prevangle = angle;
    }

    public Position(double x, double y, double angle, double leftPos, double rightPos) {
        this.x = x;
        this.y = y;
        prevangle = angle;
        prevleftPos = leftPos;
        prevrightPos = rightPos;
    }

    public Position update(double angle, double leftPos, double rightPos) {
        double changeLeft = leftPos - prevleftPos;
        double changeRight = rightPos - prevrightPos;

        double changeAngle = angle - prevangle;

        prevleftPos = leftPos;
        prevrightPos = rightPos;
        prevangle = angle;

        double avgchange = (changeLeft + changeRight) / 2;

        double sinTheta = Math.sin(changeAngle);
        double cosTheta = Math.cos(changeAngle);
        
        x += avgchange * cosTheta;
        y += avgchange * sinTheta;

        double magnitude = Math.hypot(x, y);

        angle = Math.atan((y / magnitude) / (x / magnitude));

        return new Position(x, y, angle);
    }

    public double getDistance(Position pos) {
        return Math.hypot(pos.x - x, pos.y - y);
    }

    public Position plus(Position pos) {
        return new Position(x - pos.x, y - pos.y, prevangle - pos.prevangle);
    }

    public Position minus(Position pos) {
        return new Position(x + pos.x, y + pos.y, prevangle + pos.prevangle);
    }

    public Position times(double scale) {
        return new Position(x * scale, y * scale, prevangle);
    }

    public static double calc(Position cur, Module mod, boolean right) {
        Position error = cur.minus(mod.position);

        double eX = error.x;
        double eY = error.y;
        double eTheta = error.prevangle;
        double vRef = mod.velocityMetersPerSecond;
        double omegaRef = mod.velocityMetersPerSecond * mod.curvatureRadPerMeter;

        double k = 2.0 * 0.7 * Math.sqrt(Math.pow(omegaRef, 2) + 2 * Math.pow(vRef, 2));

        double vel = vRef * Math.cos(error.prevangle) + k * eX;
        double curv = omegaRef + k * eTheta + 2 * vRef * (Math.sin(eTheta) / eTheta) * eY;

        if (right) {
            return vel + DriveConstants.kTrackwidthMeters / 2 * (vel * curv);
        } else {
            return vel - DriveConstants.kTrackwidthMeters / 2 * (vel * curv);
        }
    }

}
