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
    private CANSparkMax frontRight, middleRight, backRight, frontLeft, middleLeft, backLeft;

    Pigeon2 pigeon;

    PIDController drive, turn;

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

        pigeon = new Pigeon2(DriveConstants.kPigeonID);

        drive = new PIDController(DriveConstants.kP, DriveConstants.kI, DriveConstants.kD);
        turn = new PIDController(DriveConstants.kTurnP, DriveConstants.kTurnI, DriveConstants.kTurnD);

        drive.setTolerance(DriveConstants.kDriveTolerance);
        turn.setTolerance(DriveConstants.kTurnToleranceDeg);

        turn.enableContinuousInput(-180, 180);
    }

    public void cheesyDrive(double speed, double turn, double nerf) {
        frontRight.set((speed + turn) * nerf);
        middleRight.set((speed + turn) * nerf);
        backRight.set((speed + turn) * nerf);
        frontLeft.set((speed - turn) * nerf);
        middleLeft.set((speed - turn) * nerf);
        backLeft.set((speed - turn) * nerf);
    }

    public double getPosition() {
        return (frontRight.getEncoder().getPosition() + frontLeft.getEncoder().getPosition()) / (2 * DriveConstants.kEncoderGearboxScale);
    }

    public void resetSensors() {
        frontRight.getEncoder().setPosition(0);
        middleRight.getEncoder().setPosition(0);
        backRight.getEncoder().setPosition(0);
        frontLeft.getEncoder().setPosition(0);
        middleLeft.getEncoder().setPosition(0);
        backLeft.getEncoder().setPosition(0);

        pigeon.setYaw(0);
    }

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

    public PIDCommand drivePID(double setpoint) {
        return new PIDCommand(
            drive,
            () -> getPosition(),
            setpoint * DriveConstants.kDistanceConvertion,
            output -> cheesyDrive(output, 0, 1),
            Robot.getDrivetrain()
        );
    }

    public PIDCommand turnPID(double angle) {
        return new PIDCommand(
            turn,
            () -> getAngle(),
            angle,
            output -> cheesyDrive(0, output, 1),
            Robot.getDrivetrain()
        );
    }
}
