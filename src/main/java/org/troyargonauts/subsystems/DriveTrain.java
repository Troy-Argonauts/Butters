package org.troyargonauts.subsystems;

import com.ctre.phoenix.sensors.Pigeon2;
import com.revrobotics.CANSparkMax;
import com.revrobotics.CANSparkMaxLowLevel;

import edu.wpi.first.math.controller.PIDController;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.PIDCommand;
import edu.wpi.first.wpilibj2.command.ParallelCommandGroup;
import edu.wpi.first.wpilibj2.command.SubsystemBase;

import org.troyargonauts.Robot;
import org.troyargonauts.Constants.DriveConstants;
/**
 * using PID and stating our speed, turn, and nerf we made a code to run our 8 wheel tank drivetrain with 2 motors
 * @author @SolidityContract @sgowda260 @Shreyan-M
 */
public class DriveTrain extends SubsystemBase {

    private CANSparkMax frontRight, middleRight, backRight, frontLeft, middleLeft, backLeft;

    Pigeon2 pigeon;

    PIDController leftPID, rightPID, turnPID;

    /**
     * Creates a new drivetrain object for the code and states the motors needed for the drivetrain
     */

    public DriveTrain() {
        frontRight = new CANSparkMax(DriveConstants.kFrontRightID, CANSparkMaxLowLevel.MotorType.kBrushless);
        middleRight = new CANSparkMax(DriveConstants.kMiddleRightID, CANSparkMaxLowLevel.MotorType.kBrushless);
        backRight = new CANSparkMax(DriveConstants.kBackRightID, CANSparkMaxLowLevel.MotorType.kBrushless);
        frontLeft = new CANSparkMax(DriveConstants.kFrontLeftID, CANSparkMaxLowLevel.MotorType.kBrushless);
        middleLeft = new CANSparkMax(DriveConstants.kMiddleLeftID, CANSparkMaxLowLevel.MotorType.kBrushless);
        backLeft = new CANSparkMax(DriveConstants.kBackLeftID, CANSparkMaxLowLevel.MotorType.kBrushless);

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

        frontRight.getEncoder().setPositionConversionFactor(DriveConstants.kDistanceConvertion);
        middleRight.getEncoder().setPositionConversionFactor(DriveConstants.kDistanceConvertion);
        backRight.getEncoder().setPositionConversionFactor(DriveConstants.kDistanceConvertion);
        frontLeft.getEncoder().setPositionConversionFactor(DriveConstants.kDistanceConvertion);
        middleLeft.getEncoder().setPositionConversionFactor(DriveConstants.kDistanceConvertion);
        backLeft.getEncoder().setPositionConversionFactor(DriveConstants.kDistanceConvertion);

//        pigeon = new Pigeon2(DriveConstants.kPigeonID);

        leftPID = new PIDController(DriveConstants.kLeftP, DriveConstants.kLeftI, DriveConstants.kLeftD);
        rightPID = new PIDController(DriveConstants.kRightP, DriveConstants.kRightI, DriveConstants.kRightD);
        turnPID = new PIDController(DriveConstants.kTurnP, DriveConstants.kTurnI, DriveConstants.kTurnD);

        leftPID.setTolerance(DriveConstants.kLeftDriveTolerance);
        rightPID.setTolerance(DriveConstants.kRightDriveTolerance);
        turnPID.setTolerance(DriveConstants.kTurnToleranceDeg);

        turnPID.enableContinuousInput(-180, 180);
    }

    @Override
    public void periodic() {
        SmartDashboard.putNumber("Left Encoder", getLeftPosition());
        SmartDashboard.putNumber("Right Encoder", getRightPosition());
        SmartDashboard.putNumber("Position", getPosition());

//        SmartDashboard.putNumber("Angle", getAngle());
    }


    
    /** 
     * Sets motors value based on speed and turn parameters
     * @param speed speed of robot
     * @param turn amount we want to turn
     * @param nerf decreases the max speed and amount we want to turn the robot
     */
    public void cheesyDrive(double speed, double turn, double nerf) {
        frontRight.set(((speed - turn) + 0.0010) * nerf);
        frontLeft.set((speed + turn) * nerf);
    }

    public void tankDrive(double left, double right, double nerf) {
        frontRight.set(right * nerf);
        frontLeft.set(left * nerf);
    }

    
    /** 
     * Returns encoder position based on encoder values
     * @return encoder position based on encoder values
     */
    public double getPosition() {
        return (frontRight.getEncoder().getPosition() + frontLeft.getEncoder().getPosition()) / (2 * DriveConstants.kEncoderGearboxScale);
    }

    public double getLeftPosition() {
        return frontLeft.getEncoder().getPosition() / DriveConstants.kEncoderGearboxScale;
    }

    public double getRightPosition() {
        return frontRight.getEncoder().getPosition() / DriveConstants.kEncoderGearboxScale;
    }

    /**
     * Resets the encoders
     * @return encoder value to 0
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
     * Resets the angle
     * @return the angle to 0
     */
    
//    public void resetAngle() {
//        pigeon.setYaw(0);
//    }

    
    /** 
     * Returns angles between -180 and 180 degrees from pigeon
     * @return angle of robot
     */
//    public double getAngle() {
//        double output = pigeon.getYaw() % 360;
//        while (Math.abs(output) > 180) {
//            if (output < 0) {
//                output += 360;
//            } else {
//                output -= 360;
//            }
//        }
//        return output;
//    }


    /** 
     * Drives certian distance based parameter
     * @param setpoint distance away we want robot to be
     * @return PIDCommand that moved robot to setpoint
     */
    public PIDCommand leftPID(double setpoint) {
        return new PIDCommand(
            leftPID,
            () -> getLeftPosition(),
            setpoint,
            output -> tankDrive(output, 0, 1),
            Robot.getDrivetrain()
        );
    }

    
    /** 
     * Drives certian distance based parameter
     * @param setpoint distance away we want robot to be
     * @return PIDCommand that moved robot to setpoint
     */
    public PIDCommand rightPID(double setpoint) {
        return new PIDCommand(
            rightPID,
            () -> getRightPosition(),
            setpoint * DriveConstants.kDistanceConvertion,
            output -> tankDrive(0, output, 1),
            Robot.getDrivetrain()
        );
    }

    
    /** 
     * Turns certain angle based on PID
     * @param angle the angle we want the robot to be at
     * @return PIDCommand that turns robot to target angle
     */
//    public PIDCommand turnPID(double angle) {
//        return new PIDCommand(
//            turnPID,
//            () -> getAngle(),
//            angle,
//            output -> cheesyDrive(0, output, 1),
//            Robot.getDrivetrain()
//        );
//    }

    public PIDCommand PID(double setpoint) {
        return new PIDCommand(
                rightPID,
                () -> getPosition(),
                setpoint,
                output -> cheesyDrive(output, 0, 1),
                Robot.getDrivetrain()
        );
    }

    public void breakMode() {
        resetEncoders();
        rightPID(0);
        leftPID(0);
    }
}
