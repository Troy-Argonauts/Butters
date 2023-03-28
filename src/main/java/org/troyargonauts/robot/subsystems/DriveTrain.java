package org.troyargonauts.robot.subsystems;

import com.ctre.phoenix.motorcontrol.StatorCurrentLimitConfiguration;
import com.ctre.phoenix.motorcontrol.can.TalonFX;
import com.ctre.phoenix.sensors.Pigeon2;
import edu.wpi.first.math.controller.PIDController;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.PIDCommand;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import org.troyargonauts.common.motors.MotorCreation;
import org.troyargonauts.common.motors.wrappers.LazyTalon;
import org.troyargonauts.common.motors.wrappers.MotorController;
import org.troyargonauts.common.motors.wrappers.MotorControllerGroup;
import org.troyargonauts.robot.Constants;
import org.troyargonauts.robot.Robot;
import org.troyargonauts.robot.subsystems.DualSpeedTransmission.Gear;

import java.util.List;
import java.util.function.BiConsumer;

public class DriveTrain extends SubsystemBase {


    /**
     * Motor controller groups for the right and left sides of the drivetrain.
     */
    private final MotorControllerGroup<TalonFX> rightSide, leftSide;

    /**
     * PID controllers for the drivetrain. The turn PID is used to turn the robot to a specific angle. The drive PID is used to drive the robot a specific distance.
     */
    private final PIDController drivePID, turnPID;

    /**
     * Collects encoder values from the right and left sides of the drivetrain. Uses master motor (frontRight and frontLeft) for encoder values.
     */
    private double rightEncoderValue, leftEncoderValue;

    private Pigeon2 pigeon;

    public double pigeonRoll, pigeonYaw;
    public short[] pigeonAccelValue = new short[3];

    private DualSpeedTransmission dualSpeedTransmission;
    private static final StatorCurrentLimitConfiguration CURRENT_LIMIT = new StatorCurrentLimitConfiguration(
            true, 60, 60,0.2
    );

    /**
     * Constructor for the robot's Drivetrain. Instantiates motor controllers, changes encoder conversion factors and instantiates PID controllers.
     * Motor controllers on the right side are reversed
     * Motor controllers are added to groups as their respective role (master or slave)
     */
    public DriveTrain() {
        LazyTalon<TalonFX> frontRight = MotorCreation.createDriveTalonFX(Constants.DriveTrain.FRONT_RIGHT, false);
        LazyTalon<TalonFX> middleRight =  MotorCreation.createDriveTalonFX(Constants.DriveTrain.TOP_RIGHT, true);
        LazyTalon<TalonFX> backRight =  MotorCreation.createDriveTalonFX(Constants.DriveTrain.REAR_RIGHT, true);

        LazyTalon<TalonFX> frontLeft =  MotorCreation.createDriveTalonFX(Constants.DriveTrain.FRONT_LEFT, false);
        LazyTalon<TalonFX> middleLeft =  MotorCreation.createDriveTalonFX(Constants.DriveTrain.TOP_LEFT, true);
        LazyTalon<TalonFX> backLeft =  MotorCreation.createDriveTalonFX(Constants.DriveTrain.REAR_LEFT, true);

        rightSide = new MotorControllerGroup<>(frontRight, List.of(middleRight, backRight), true, false);
        leftSide = new MotorControllerGroup<>(frontLeft, List.of(middleLeft, backLeft), false, false);

        dualSpeedTransmission = new DualSpeedTransmission(this);

        pigeon = new Pigeon2(Constants.DriveTrain.PIGEON);

        configMotors(rightSide);
        configMotors(leftSide);

        drivePID = new PIDController(Constants.DriveTrain.DRIVE_P, Constants.DriveTrain.DRIVE_I, Constants.DriveTrain.DRIVE_D);
        turnPID = new PIDController(Constants.DriveTrain.TURN_P, Constants.DriveTrain.TURN_I, Constants.DriveTrain.TURN_D);

        drivePID.setTolerance(Constants.DriveTrain.DRIVE_TOLERANCE, Constants.DriveTrain.VELOCITY_TOLERANCE);
        turnPID.setTolerance(Constants.DriveTrain.TURN_TOLERANCE_DEG, Constants.DriveTrain.VELOCITY_TOLERANCE);

        turnPID.enableContinuousInput(-180, 180);

        rightSide.forEach(talonFX -> talonFX.setGearingParameters(Constants.DriveTrain.gearingLowGear));
        leftSide.forEach(talonFX -> talonFX.setGearingParameters(Constants.DriveTrain.gearingLowGear));
    }

    @Override
    public void periodic() {
        rightEncoderValue = rightSide.getMaster().getInternalController().getSensorCollection().getIntegratedSensorPosition();
        leftEncoderValue = leftSide.getMaster().getInternalController().getSensorCollection().getIntegratedSensorPosition();

        pigeon.getBiasedAccelerometer(pigeonAccelValue);
        //System.out.println(pigeon.getYaw());

        SmartDashboard.putNumber("DT Right RPM", rightSide.getMaster().getMotorRotations());
        SmartDashboard.putNumber("DT Left RPM", leftSide.getMaster().getMotorRotations());
        SmartDashboard.putBoolean("DT Auto Shifting", getDualSpeedTransmission().isAutomaticShifting());
        SmartDashboard.putNumber("DT Right Amps", rightSide.getMaster().getDrawnCurrentAmps());
        SmartDashboard.putNumber("DT Left Amps", leftSide.getMaster().getDrawnCurrentAmps());
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
        rightSide.getMaster().set((speed + turn) * nerf);
        leftSide.getMaster().set((speed - turn) * nerf);
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
        rightSide.getMaster().set(right * nerf);
        leftSide.getMaster().set(left * nerf);
    }



    public void resetEncoders() {
        rightSide.forEach(talonFX -> talonFX.getInternalController().getSensorCollection().setIntegratedSensorPosition(0, 50));
        leftSide.forEach(talonFX -> talonFX.getInternalController().getSensorCollection().setIntegratedSensorPosition(0, 50));
    }

    /**
     * Returns encoder position based on the average value from the frontLeft and frontRight motor controller encoders.
     * @return encoder position based on encoder values.
     */
    public double getPosition() {
        if (dualSpeedTransmission.getGear().equals(Gear.LOW)) {
            return (leftEncoderValue + rightEncoderValue) / (2 * Constants.DriveTrain.LOW_GEARBOX_RATIO);
        } else {
            return (leftEncoderValue + rightEncoderValue) / (2 * Constants.DriveTrain.HIGH_GEARBOX_RATIO);
        }
    }

    /**
     * Uses PIDController to drive the robot a certain distance based on the average of the left and right encoder values
     * @param setpoint the setpoint in inches we want the robot to drive to.
     * @return PIDCommand that turns robot to target angle
     */
    public void drivePID(double setpoint) {
        new PIDCommand(
            drivePID,
            this::getPosition,
            setpoint,
            output -> cheesyDrive(output, 0, 1),
            Robot.getDrivetrain()
        );
    }

    /**
     * Resets encoders to 0.
     * Allows the robot to resist moving forward or backward.
     */
    public void resistMovement() {
        resetEncoders();
        drivePID(0);
    }

    public MotorControllerGroup<TalonFX> getRightSide() {
        return rightSide;
    }

    public MotorControllerGroup<TalonFX> getLeftSide() {
        return leftSide;
    }

    public void setGearingParameters(MotorController.GearingParameters gearingParameters) {
        rightSide.forEach(talonFX -> talonFX.setGearingParameters(gearingParameters));
        leftSide.forEach(talonFX -> talonFX.setGearingParameters(gearingParameters));
    }

    public void set(final BiConsumer<MotorControllerGroup<TalonFX>, MotorControllerGroup<TalonFX>> consumer) {
        consumer.accept(leftSide, rightSide);
    }

    public DualSpeedTransmission getDualSpeedTransmission() {
        return dualSpeedTransmission;
    }

    private void configMotors(final MotorControllerGroup<TalonFX> motors) {
        motors.forEach(motor -> {
            motor.setNeutralBehaviour(MotorController.NeutralBehaviour.BRAKE);
            motor.getInternalController().configVoltageCompSaturation(12.0);
            motor.getInternalController().enableVoltageCompensation(true);
            motor.getInternalController().configStatorCurrentLimit(CURRENT_LIMIT);
        });
    }
}
