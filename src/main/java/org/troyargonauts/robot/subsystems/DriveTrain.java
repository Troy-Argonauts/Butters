package org.troyargonauts.robot.subsystems;

import com.ctre.phoenix.sensors.Pigeon2;
import com.revrobotics.CANSparkMax;
import com.revrobotics.CANSparkMaxLowLevel;
import edu.wpi.first.math.controller.PIDController;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.FunctionalCommand;
import edu.wpi.first.wpilibj2.command.InstantCommand;
import edu.wpi.first.wpilibj2.command.PIDCommand;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import org.troyargonauts.common.util.motorcontrol.LazyCANSparkMax;
import org.troyargonauts.robot.Constants;
import org.troyargonauts.robot.Robot;

/**
 * Drivetrain allows control of the robot's drivetrain in cheesy drive and tank drive. 
 * Includes a distance PID and a turn PID using the Pigeon 2 from CTRE.
 * @author @SolidityContract @sgowda260 @Shreyan-M
 */
public class DriveTrain extends SubsystemBase {

    private final LazyCANSparkMax frontRight, middleRight, backRight, frontLeft, middleLeft, backLeft;

    private Pigeon2 pigeon;

    private final PIDController drivePID, turnPID, autoBalancePID;

    public double frontRightEncoderValue, middleRightEncoderValue, backRightEncoderValue, frontLeftEncoderValue, middleLeftEncoderValue, backLeftEncoderValue;

    public double gyroValue;

    /**
     * Constructor for the robot's Drivetrain. Instantiates motor controllers, changes encoder conversion factors and instantiates PID controllers.
     * Motor controllers on the right side are reversed and set to follow other motor controllers.
     */

    public DriveTrain() {
        frontRight = new LazyCANSparkMax(Constants.DriveTrain.FRONT_RIGHT, CANSparkMaxLowLevel.MotorType.kBrushless);
        middleRight = new LazyCANSparkMax(Constants.DriveTrain.MIDDLE_RIGHT, CANSparkMaxLowLevel.MotorType.kBrushless);
        backRight = new LazyCANSparkMax(Constants.DriveTrain.BACK_RIGHT, CANSparkMaxLowLevel.MotorType.kBrushless);
        frontLeft = new LazyCANSparkMax(Constants.DriveTrain.FRONT_LEFT, CANSparkMaxLowLevel.MotorType.kBrushless);
        middleLeft = new LazyCANSparkMax(Constants.DriveTrain.MIDDLE_LEFT, CANSparkMaxLowLevel.MotorType.kBrushless);
        backLeft = new LazyCANSparkMax(Constants.DriveTrain.BACK_LEFT, CANSparkMaxLowLevel.MotorType.kBrushless);

        frontRight.restoreFactoryDefaults();
        middleRight.restoreFactoryDefaults();
        backRight.restoreFactoryDefaults();
        frontLeft.restoreFactoryDefaults();
        middleLeft.restoreFactoryDefaults();
        backLeft.restoreFactoryDefaults();

        frontLeft.setInverted(true);
        middleLeft.setInverted(true);
        backLeft.setInverted(true);
        frontRight.setInverted(false);
        middleRight.setInverted(false);
        backRight.setInverted(false);

        backRight.follow(frontRight);
        middleRight.follow(frontRight);
        backLeft.follow(frontLeft);
        middleLeft.follow(frontLeft);

        frontRight.getEncoder().setPositionConversionFactor(Constants.DriveTrain.DISTANCE_CONVERSION);
        middleRight.getEncoder().setPositionConversionFactor(Constants.DriveTrain.DISTANCE_CONVERSION);
        backRight.getEncoder().setPositionConversionFactor(Constants.DriveTrain.DISTANCE_CONVERSION);
        frontLeft.getEncoder().setPositionConversionFactor(Constants.DriveTrain.DISTANCE_CONVERSION);
        middleLeft.getEncoder().setPositionConversionFactor(Constants.DriveTrain.DISTANCE_CONVERSION);
        backLeft.getEncoder().setPositionConversionFactor(Constants.DriveTrain.DISTANCE_CONVERSION);

        pigeon = new Pigeon2(Constants.DriveTrain.PIGEON);

        drivePID = new PIDController(Constants.DriveTrain.kDriveP, Constants.DriveTrain.kDriveI, Constants.DriveTrain.kDriveD);
        turnPID = new PIDController(Constants.DriveTrain.kTurnP, Constants.DriveTrain.kTurnI, Constants.DriveTrain.kTurnD);
        autoBalancePID = new PIDController(Constants.DriveTrain.kBalanceP, Constants.DriveTrain.kBalanceI, Constants.DriveTrain.kBalanceP);

        drivePID.setTolerance(Constants.DriveTrain.kDriveTolerance, Constants.DriveTrain.kVelcoityTolerance);
        turnPID.setTolerance(Constants.DriveTrain.kTurnToleranceDeg, Constants.DriveTrain.kVelcoityTolerance);
        autoBalancePID.setTolerance(Constants.DriveTrain.kBalanceToleranceDeg, Constants.DriveTrain.kVelcoityTolerance);

        turnPID.enableContinuousInput(-180, 180);

        frontRight.setOpenLoopRampRate(0.35);
        middleRight.setOpenLoopRampRate(0.35);
        backRight.setOpenLoopRampRate(0.35);
        frontLeft.setOpenLoopRampRate(0.35);
        middleLeft.setOpenLoopRampRate(0.35);
        backLeft.setOpenLoopRampRate(0.35);

        frontRight.setClosedLoopRampRate(0.25);
        middleRight.setClosedLoopRampRate(0.25);
        backRight.setClosedLoopRampRate(0.25);
        frontLeft.setClosedLoopRampRate(0.25);
        middleLeft.setClosedLoopRampRate(0.25);
        backLeft.setClosedLoopRampRate(0.25);

        resetEncoders();

//        frontRight.burnFlash();
//        middleRight.burnFlash();
//        backRight.burnFlash();
//        frontLeft.burnFlash();
//        middleLeft.burnFlash();
//        backLeft.burnFlash();
    }

    @Override
    public void periodic() {
        frontRightEncoderValue = frontRight.getEncoder().getPosition();
        middleRightEncoderValue = middleRight.getEncoder().getPosition();
        backRightEncoderValue = backRight.getEncoder().getPosition();
        frontLeftEncoderValue = frontLeft.getEncoder().getPosition();
        middleLeftEncoderValue = middleLeft.getEncoder().getPosition();
        backLeftEncoderValue = backLeft.getEncoder().getPosition();

        SmartDashboard.putNumber("frontRightEncoderValue", frontRightEncoderValue);
        SmartDashboard.putNumber("middleRightEncoderValue", middleRightEncoderValue);
        SmartDashboard.putNumber("backRightEncoderValue", backRightEncoderValue);
        SmartDashboard.putNumber("frontLeftEncoderValue", frontLeftEncoderValue);
        SmartDashboard.putNumber("middleLeftEncoderValue", middleLeftEncoderValue);
        SmartDashboard.putNumber("backLeftEncoderValue", backLeftEncoderValue);
        SmartDashboard.putNumber("Right Position", getRightPosition());
        SmartDashboard.putNumber("Left Position", getLeftPosition());
        SmartDashboard.putNumber("Position", getPosition());

        gyroValue = pigeon.getYaw();
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
        frontRight.set(((speed + (turn * 0.85)) + Constants.DriveTrain.RIGHT_CORRECTION) * nerf);
        frontLeft.set((speed - (turn * 0.85)) * nerf);
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
        frontRight.set((right + Constants.DriveTrain.RIGHT_CORRECTION) * nerf);
        frontLeft.set(left * nerf);
    }

    
    /** 
     * Returns encoder position based on the average value from the frontLeft and frontRight motor controller encoders.
     * @return encoder position based on encoder values.
     */
    public double getPosition() {
        return (getLeftPosition() + getRightPosition()) / 2;
    }

    /** 
     * Returns encoder position based on the value from all the left motor controllers.
     * @return encoder position based on frontLeft motor controller encoder.
     */
    public double getLeftPosition() {
        return -(frontLeftEncoderValue + middleLeftEncoderValue + backLeftEncoderValue) / 3;
    }

    /** 
     * Returns encoder position based on the value from all the right motor controllers.
     * @return encoder position based on frontRight motor controller encoder.
     */
    public double getRightPosition() {
        return -(frontRightEncoderValue + middleRightEncoderValue + backRightEncoderValue) / 3;
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
        double output = gyroValue % 360;
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
            output -> cheesyDrive(-output, 0, 1),
            Robot.getDrivetrain()
        );
    }

    /** 
     * Causes the robot to brake.
     * Resets encoders to 0.
     */
    public void brakeMode() {
        resetEncoders();
        drivePID(0);
    }






    /** 
     * Uses PIDController to balance the robot on the charging station based on the angular offset determined by the gyro.
     * @return PIDCommand that balances robot.
     */
    public PIDCommand autoBalance() {
        return new PIDCommand(
            autoBalancePID,
            () -> pigeon.getPitch(),
            0,
            output -> cheesyDrive(output, 0, 0.2),
            Robot.getDrivetrain()
        );
    }
    public void setIdleMode(CANSparkMax.IdleMode idleMode) {
        frontLeft.setIdleMode(idleMode);
        middleLeft.setIdleMode(idleMode);
        backLeft.setIdleMode(idleMode);
        frontRight.setIdleMode(idleMode);
        middleRight.setIdleMode(idleMode);
        backRight.setIdleMode(idleMode);
    }

    public void reverseRightMotors() {
//        frontLeft.setInverted(true);
//        middleLeft.setInverted(true);
//        backLeft.setInverted(true);
        frontRight.setInverted(true);
        middleRight.setInverted(true);
        backRight.setInverted(true);
    }
}
