package org.troyargonauts.subsystems;

import com.ctre.phoenix.sensors.Pigeon2;
import com.revrobotics.CANSparkMax;
import com.revrobotics.CANSparkMaxLowLevel;

import edu.wpi.first.math.controller.PIDController;
import edu.wpi.first.wpilibj2.command.PIDCommand;
import edu.wpi.first.wpilibj2.command.SubsystemBase;

import org.troyargonauts.Robot;
import org.troyargonauts.Constants.DriveConstants;

public class DriveTrain extends SubsystemBase {
    /** 
     * @author @SolidityContract @sgowda260 @Shreyan-M
     */
    private CANSparkMax frontRight, middleRight, backRight, frontLeft, middleLeft, backLeft;

    Pigeon2 pigeon;

    PIDController drivePID, turnPID;

    public DriveTrain() {
        frontRight = new CANSparkMax(DriveConstants.kFrontRightID, CANSparkMaxLowLevel.MotorType.kBrushless);
        middleRight = new CANSparkMax(DriveConstants.kMiddleRightID, CANSparkMaxLowLevel.MotorType.kBrushless);
        backRight = new CANSparkMax(DriveConstants.kBackRightID, CANSparkMaxLowLevel.MotorType.kBrushless);
        frontLeft = new CANSparkMax(DriveConstants.kFrontLeftID, CANSparkMaxLowLevel.MotorType.kBrushless);
        middleLeft = new CANSparkMax(DriveConstants.kMiddleLeftID, CANSparkMaxLowLevel.MotorType.kBrushless);
        backLeft = new CANSparkMax(DriveConstants.kBackLeftID, CANSparkMaxLowLevel.MotorType.kBrushless);

        frontRight.setInverted(true);
        middleRight.setInverted(true);
        backRight.setInverted(true);

        backRight.follow(frontRight);
        middleRight.follow(frontRight);

        backLeft.follow(frontLeft);
        middleLeft.follow(frontRight);

        pigeon = new Pigeon2(DriveConstants.kPigeonID);

        drivePID = new PIDController(DriveConstants.kP, DriveConstants.kI, DriveConstants.kD);
        turnPID = new PIDController(DriveConstants.kTurnP, DriveConstants.kTurnI, DriveConstants.kTurnD);

        drivePID.setTolerance(DriveConstants.kDriveTolerance);
        turnPID.setTolerance(DriveConstants.kTurnToleranceDeg);

        turnPID.enableContinuousInput(-180, 180);
    }

    
    /** 
     * Sets motors value based on speed and turn parameters
     * @param speed
     * @param turn
     * @param nerf
     */
    public void cheesyDrive(double speed, double turn, double nerf) {
        frontRight.set((speed + turn) * nerf);
        frontLeft.set((speed - turn) * nerf);
    }

    
    /** 
     * Returns encoder position based on encoder values
     * @return encoder position based on encoder values
     */
    public double getPosition() {
        return (frontRight.getEncoder().getPosition() + frontLeft.getEncoder().getPosition()) / (2 * DriveConstants.kEncoderGearboxScale);
    }

    public void resetEncoders() {
        frontRight.getEncoder().setPosition(0);
        middleRight.getEncoder().setPosition(0);
        backRight.getEncoder().setPosition(0);
        frontLeft.getEncoder().setPosition(0);
        middleLeft.getEncoder().setPosition(0);
        backLeft.getEncoder().setPosition(0);
    }

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
     * Drives certian distance based parameter
     * @param setpoint
     * @return PIDCommand that moved robot to setpoint
     */
    public PIDCommand drivePID(double setpoint) {
        return new PIDCommand(
            drivePID,
            () -> getPosition(),
            setpoint * DriveConstants.kDistanceConvertion,
            output -> cheesyDrive(output, 0, 1),
            Robot.getDrivetrain()
        );
    }

    
    /** 
     * Turns certain angle based on PID
     * @param angle
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
}
