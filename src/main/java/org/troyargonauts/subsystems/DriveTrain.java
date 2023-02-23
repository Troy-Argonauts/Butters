package org.troyargonauts.subsystems;

import com.ctre.phoenix.sensors.Pigeon2;
import com.revrobotics.CANSparkMax;
import com.revrobotics.CANSparkMaxLowLevel;

import edu.wpi.first.math.controller.PIDController;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.PIDCommand;
import edu.wpi.first.wpilibj2.command.SubsystemBase;

import org.troyargonauts.Constants;
import org.troyargonauts.Robot;

/**
 * Drivetrain uses PID as well as runs with certian speed and turn to reach its desired target
 * @author @SolidityContract @sgowda260 @Shreyan-M
 */
public class DriveTrain extends SubsystemBase {

    private final CANSparkMax frontRight, middleRight, backRight, frontLeft, middleLeft, backLeft;

    private final Pigeon2 pigeon;

    private final PIDController drivePID, turnPID;

    /**
     * Constructor for the robot's Drivetrain. Instantiates motor controllers, changes encoder convertion factors and instantiates pid controllers.
     * Motor controllers are reversed and set to follow other motor controllers.
     */

    public DriveTrain() {
        frontRight = new CANSparkMax(Constants.DriveTrain.FRONT_RIGHT, CANSparkMaxLowLevel.MotorType.kBrushless);
        middleRight = new CANSparkMax(Constants.DriveTrain.MIDDLE_RIGHT, CANSparkMaxLowLevel.MotorType.kBrushless);
        backRight = new CANSparkMax(Constants.DriveTrain.BACK_RIGHT, CANSparkMaxLowLevel.MotorType.kBrushless);
        frontLeft = new CANSparkMax(Constants.DriveTrain.FRONT_LEFT, CANSparkMaxLowLevel.MotorType.kBrushless);
        middleLeft = new CANSparkMax(Constants.DriveTrain.MIDDLE_LEFT, CANSparkMaxLowLevel.MotorType.kBrushless);
        backLeft = new CANSparkMax(Constants.DriveTrain.BACK_LEFT, CANSparkMaxLowLevel.MotorType.kBrushless);

        frontLeft.setInverted(false);
        middleLeft.setInverted(false);
        backLeft.setInverted(false);

        frontRight.setInverted(true);
        middleRight.setInverted(true);
        backRight.setInverted(true);

        backRight.follow(frontRight);
        middleRight.follow(frontRight);

        backLeft.follow(frontLeft);
        middleLeft.follow(frontLeft);

        frontRight.getEncoder().setPositionConversionFactor(Constants.DriveTrain.REVOLUTION_DISTANCE);
        middleRight.getEncoder().setPositionConversionFactor(Constants.DriveTrain.REVOLUTION_DISTANCE);
        backRight.getEncoder().setPositionConversionFactor(Constants.DriveTrain.REVOLUTION_DISTANCE);
        frontLeft.getEncoder().setPositionConversionFactor(Constants.DriveTrain.REVOLUTION_DISTANCE);
        middleLeft.getEncoder().setPositionConversionFactor(Constants.DriveTrain.REVOLUTION_DISTANCE);
        backLeft.getEncoder().setPositionConversionFactor(Constants.DriveTrain.REVOLUTION_DISTANCE);

        pigeon = new Pigeon2(Constants.DriveTrain.PIGEON);

        drivePID = new PIDController(Constants.DriveTrain.kDriveP, Constants.DriveTrain.kDriveI, Constants.DriveTrain.kDriveD);
        turnPID = new PIDController(Constants.DriveTrain.kTurnP, Constants.DriveTrain.kTurnI, Constants.DriveTrain.kTurnD);

        drivePID.setTolerance(Constants.DriveTrain.kDriveTolerance);
        turnPID.setTolerance(Constants.DriveTrain.kTurnToleranceDeg);

        turnPID.enableContinuousInput(-180, 180);
    }

    @Override
    public void periodic() {
        SmartDashboard.putNumber("Left Encoder", getLeftPosition());
        SmartDashboard.putNumber("Right Encoder", getRightPosition());
        SmartDashboard.putNumber("Position", getPosition());

        SmartDashboard.putNumber("Angle", getAngle());
    }


    
    /** 
     * Sets motors value based on speed and turn parameters. 
     * Robots speed and turn will be controlled by different joysticks.
     * Allows robot to move in specified direction with more control.
     * @param speed speed of robot.
     * @param turn amount we want to turn.
     * @param nerf decreases the max speed and amount we want to turn the robot.
     */
    public void cheesyDrive(double speed, double turn, double nerf) {
        frontRight.set(((speed - turn) + 0.0010) * nerf);
        frontLeft.set((speed + turn) * nerf);
    }

    /** 
     * Sets left and right motor values based on left and right parameters. 
     * Robots left and right side will be controlled by different joysticks.
     * Used for mainly testing and troubleshooting.
     * @param left speed of the left side of the robot.
     * @param right speed of the right side of the robot.
     * @param nerf decreases the max speed and amount we want to turn the robot.
     */
    public void tankDrive(double left, double right, double nerf) {
        frontRight.set(right * nerf);
        frontLeft.set(left * nerf);
    }

    
    /** 
     * Returns encoder position based on the average value from the frontLeft and frontRight motor controller encoders.
     * @return encoder position based on encoder values.
     */
    public double getPosition() {
        return (frontRight.getEncoder().getPosition() + frontLeft.getEncoder().getPosition()) / 2;
    }

    /** 
     * Returns encoder position based on the value from the frontLeft motor controller encoder.
     * @return encoder position based on frontLeft motor controller encoder.
     */
    public double getLeftPosition() {
        return frontLeft.getEncoder().getPosition();
    }

    /** 
     * Returns encoder position based on the value from the frontRight motor controller encoder.
     * @return encoder position based on frontRight motor controller encoder.
     */
    public double getRightPosition() {
        return frontRight.getEncoder().getPosition();
    }

    /**
     * Resets the encoders to a position of 0
     */

    public void resetEncoders() {
        frontRight.getEncoder().setPosition(0);
        middleRight.getEncoder().setPosition(0);
        backRight.getEncoder().setPosition(0);
        frontLeft.getEncoder().setPosition(0);
        middleLeft.getEncoder().setPosition(0);
        backLeft.getEncoder().setPosition(0);
    }

    /**
     * Resets the pigeon to a yaw of 0
     */
    public void resetAngle() {
        pigeon.setYaw(0);
    }

    
    /** 
     * Returns angles between -180 and 180 degrees from pigeon
     * @return angle of robot
     */
    public double getAngle() {
        double output = pigeon.getYaw() % 360;
        while (Math.abs(output) > 180) {
            if (output < 0) {
                output += 360;
            } else {
                output -= 360;
            }
        }
        return output;
    }

    /** 
     * Uses PIDController to turn the robot a certain angle based on the pigeons yaw
     * @param angle the angle we want the robot to be at
     * @return PIDCommand that turns robot to target angle
     */
    public PIDCommand turnPID(double angle) {
        return new PIDCommand(
            turnPID,
            () -> getAngle(),
            angle,
            output -> cheesyDrive(0, output, 1),
            Robot.getDrivetrain()
        );
    }

    /** 
     * Uses PIDController to drive the robot a certain distance based on the average of the left and right encoder values
     * @param setpoint the setpoint in inches we want the robot to drive to.
     * @return PIDCommand that turns robot to target angle
     */
    public PIDCommand drivePID(double setpoint) {
        return new PIDCommand(
                drivePID,
                () -> getPosition(),
                setpoint,
                output -> cheesyDrive(output, 0, 1),
                Robot.getDrivetrain()
        );
    }

    /** 
     * Causes the robot to break.
     * Resets encoders to 0.
     */
    public void breakMode() {
        resetEncoders();
        drivePID(0);
    }
}
