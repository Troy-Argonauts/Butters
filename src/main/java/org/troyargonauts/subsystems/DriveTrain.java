package org.troyargonauts.subsystems;

import com.ctre.phoenix.sensors.Pigeon2;
import com.revrobotics.CANSparkMax;
import com.revrobotics.CANSparkMaxLowLevel;

import QuinticPathFollower.Position;
import edu.wpi.first.math.controller.PIDController;
import edu.wpi.first.math.controller.RamseteController;
import edu.wpi.first.math.controller.SimpleMotorFeedforward;
import edu.wpi.first.math.geometry.Pose2d;
import edu.wpi.first.math.geometry.Rotation2d;
import edu.wpi.first.math.geometry.Translation2d;
import edu.wpi.first.math.kinematics.DifferentialDriveOdometry;
import edu.wpi.first.math.kinematics.DifferentialDriveWheelSpeeds;
import edu.wpi.first.math.trajectory.Trajectory;
import edu.wpi.first.math.trajectory.TrajectoryConfig;
import edu.wpi.first.math.trajectory.TrajectoryGenerator;
import edu.wpi.first.math.trajectory.constraint.DifferentialDriveVoltageConstraint;
import edu.wpi.first.wpilibj2.command.PIDCommand;
import edu.wpi.first.wpilibj2.command.RamseteCommand;
import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;
import edu.wpi.first.wpilibj2.command.SubsystemBase;

import java.util.List;
import java.util.function.BiConsumer;

import org.troyargonauts.Robot;
import org.troyargonauts.Constants.DriveConstants;
/**
 * using PID and stating our speed, turn, and nerf we made a code to run our 8 wheel tank drivetrain with 2 motors
 * @author @SolidityContract @sgowda260 @Shreyan-M
 */
public class DriveTrain extends SubsystemBase {

    private CANSparkMax frontRight, middleRight, backRight, frontLeft, middleLeft, backLeft;

    private Pigeon2 pigeon;

    private PIDController drivePID, turnPID;

    private DifferentialDriveOdometry odometry;

    private Position position;

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

        frontRight.setInverted(true);
        middleRight.setInverted(true);
        backRight.setInverted(true);

        backRight.follow(frontRight);
        middleRight.follow(frontRight);

        backLeft.follow(frontLeft);
        middleLeft.follow(frontRight);

        frontRight.getEncoder().setPositionConversionFactor(DriveConstants.kDistanceConvertion);
        middleRight.getEncoder().setPositionConversionFactor(DriveConstants.kDistanceConvertion);
        backRight.getEncoder().setPositionConversionFactor(DriveConstants.kDistanceConvertion);
        frontLeft.getEncoder().setPositionConversionFactor(DriveConstants.kDistanceConvertion);
        middleLeft.getEncoder().setPositionConversionFactor(DriveConstants.kDistanceConvertion);
        backLeft.getEncoder().setPositionConversionFactor(DriveConstants.kDistanceConvertion);

        pigeon = new Pigeon2(DriveConstants.kPigeonID);

        drivePID = new PIDController(DriveConstants.kP, DriveConstants.kI, DriveConstants.kD);
        turnPID = new PIDController(DriveConstants.kTurnP, DriveConstants.kTurnI, DriveConstants.kTurnD);

        drivePID.setTolerance(DriveConstants.kDriveTolerance);
        turnPID.setTolerance(DriveConstants.kTurnToleranceDeg);

        turnPID.enableContinuousInput(-180, 180);

        odometry = new DifferentialDriveOdometry(getRotation2d(), frontLeft.getEncoder().getPosition(), frontRight.getEncoder().getPosition());

        position = new Position(0, 0, getAngle() * Math.PI / 180, frontLeft.getEncoder().getPosition(), frontRight.getEncoder().getPosition());
    }

    @Override
    public void periodic() {
        odometry.update(getRotation2d(), frontLeft.getEncoder().getPosition(), frontRight.getEncoder().getPosition());

        position = position.update(getAngle(), frontLeft.getEncoder().getPosition(), frontRight.getEncoder().getPosition());
    }

    public Position getPos() {
        return position;
    }

    public Pose2d getPose() {
        return odometry.getPoseMeters();
    }

    public DifferentialDriveWheelSpeeds getWheelSpeeds() {
        return new DifferentialDriveWheelSpeeds(frontLeft.getEncoder().getVelocity(), frontRight.getEncoder().getVelocity());
    }

    public double getLeftWheelSpeeds() {
        return frontLeft.getEncoder().getVelocity();
    }

    public double getRightWheelSpeeds() {
        return frontRight.getEncoder().getVelocity();
    }

    public void resetOdometry(Pose2d pose) {
        resetEncoders();
        odometry.resetPosition(getRotation2d(), frontLeft.getEncoder().getPosition(), frontRight.getEncoder().getPosition(), pose);
    }
    
    /** 
     * Sets motors value based on speed and turn parameters
     * @param speed speed of robot
     * @param turn amount we want to turn
     * @param nerf decreases the max speed and amount we want to turn the robot
     */
    public void cheesyDrive(double speed, double turn, double nerf) {
        frontRight.set((speed + turn) * nerf);
        frontLeft.set((speed - turn) * nerf);
    }

    public BiConsumer<Double, Double> tankDriveVolts = (right, left) -> {
        frontRight.setVoltage(right);
        frontLeft.setVoltage(left);
    };

    public void tankVolts(double right, double left) {
        frontRight.setVoltage(right);
        frontLeft.setVoltage(left);
    };

    public Rotation2d getRotation2d() {
        return Rotation2d.fromDegrees(getAngle());
    }
    
    /** 
     * Returns encoder position based on encoder values
     * @return encoder position based on encoder values
     */
    public double getPosition() {
        return (frontRight.getEncoder().getPosition() + frontLeft.getEncoder().getPosition()) / (2 * DriveConstants.kEncoderGearboxScale);
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
     * @param setpoint distance away we want robot to be
     * @return PIDCommand that moved robot to setpoint
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
     * Turns certain angle based on PID
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

    public SequentialCommandGroup generate(double x_0, double y_0, double x_1, double y_1, double x_2, double y_2, double x_3, double y_3, double angle) {
        Trajectory trajectory = TrajectoryGenerator.generateTrajectory(
            new Pose2d(x_0, y_0, new Rotation2d(0)),
            List.of(
                new Translation2d(x_1, y_1),
                new Translation2d(x_2, y_2),
                new Translation2d(x_3, y_3)
            ),
            new Pose2d(x_0 + x_1 + x_2 + x_3, y_0 + y_1 + y_2 + y_3, new Rotation2d(angle)),
            new TrajectoryConfig(
                DriveConstants.kMaxSpeed, 
                DriveConstants.kMaxAcceleration
            )
            .setKinematics(DriveConstants.kDriveKinematics)
            .addConstraint(
                new DifferentialDriveVoltageConstraint(
                    new SimpleMotorFeedforward(DriveConstants.ks, DriveConstants.kv, DriveConstants.ka),
                    DriveConstants.kDriveKinematics,
                    10
                )
            )
        );

        RamseteCommand ramseteCommand = new RamseteCommand(
            trajectory,
            () -> getPose(),
            new RamseteController(DriveConstants.kRamseteB, DriveConstants.kRamseteZeta),
            new SimpleMotorFeedforward(DriveConstants.ks, DriveConstants.kv, DriveConstants.ka),
            DriveConstants.kDriveKinematics,
            () -> getWheelSpeeds(),
            new PIDController(DriveConstants.kPDrive, 0, 0),
            new PIDController(DriveConstants.kPDrive, 0, 0),
            tankDriveVolts,
            Robot.getDrivetrain()
        );

        Robot.getDrivetrain().resetOdometry(trajectory.getInitialPose());

        return ramseteCommand.andThen(() -> Robot.getDrivetrain().tankDriveVolts.accept(Double.valueOf(0), Double.valueOf(0)));
    }
}
