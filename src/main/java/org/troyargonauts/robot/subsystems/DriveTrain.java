package org.troyargonauts.robot.subsystems;

import com.ctre.phoenix.motorcontrol.can.TalonFX;
import edu.wpi.first.math.controller.PIDController;
import edu.wpi.first.wpilibj.DoubleSolenoid;
import edu.wpi.first.wpilibj.PneumaticsModuleType;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj2.command.PIDCommand;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import org.troyargonauts.common.motors.MotorCreation;
import org.troyargonauts.common.motors.wrappers.LazyTalon;
import org.troyargonauts.common.motors.wrappers.MotorController;
import org.troyargonauts.common.motors.wrappers.MotorControllerGroup;
import org.troyargonauts.robot.Constants;
import org.troyargonauts.robot.Robot;

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
    private double frontRightValue, frontLeftValue, middleRightValue, backRightValue, middleLeftValue, backLeftValue;

    private final DualSpeedTransmission dualSpeedTransmission;

    /**
     * Constructor for the robot's Drivetrain. Instantiates motor controllers, changes encoder conversion factors and instantiates PID controllers.
     * Motor controllers on the right side are reversed
     * Motor controllers are added to groups as their respective role (master or slave)
     */
    public DriveTrain() {
        LazyTalon<TalonFX> frontRight = MotorCreation.createDriveTalonFX(Constants.DriveTrain.FRONT_RIGHT, false);
        LazyTalon<TalonFX> middleRight =  MotorCreation.createDriveTalonFX(Constants.DriveTrain.MIDDLE_RIGHT, true);
        LazyTalon<TalonFX> backRight =  MotorCreation.createDriveTalonFX(Constants.DriveTrain.BACK_RIGHT, true);

        LazyTalon<TalonFX> frontLeft =  MotorCreation.createDriveTalonFX(Constants.DriveTrain.FRONT_LEFT, false);
        LazyTalon<TalonFX> middleLeft =  MotorCreation.createDriveTalonFX(Constants.DriveTrain.MIDDLE_LEFT, true);
        LazyTalon<TalonFX> backLeft =  MotorCreation.createDriveTalonFX(Constants.DriveTrain.BACK_LEFT, true);

        rightSide = new MotorControllerGroup<>(frontRight, List.of(middleRight, backRight), true);
        leftSide = new MotorControllerGroup<>(frontLeft, List.of(middleLeft, backLeft), false);

        drivePID = new PIDController(Constants.DriveTrain.kDriveP, Constants.DriveTrain.kDriveI, Constants.DriveTrain.kDriveD);
        turnPID = new PIDController(Constants.DriveTrain.kTurnP, Constants.DriveTrain.kTurnI, Constants.DriveTrain.kTurnD);

        drivePID.setTolerance(Constants.DriveTrain.kDriveTolerance);
        turnPID.setTolerance(Constants.DriveTrain.kTurnToleranceDeg);

        turnPID.enableContinuousInput(-180, 180);

        rightSide.forEach(talonFX -> talonFX.setGearingParameters(Constants.DriveTrain.gearingLowGear));

        leftSide.forEach(talonFX -> talonFX.setGearingParameters(Constants.DriveTrain.gearingLowGear));

        dualSpeedTransmission = new DualSpeedTransmission(this);
    }

    @Override
    public void periodic() {
        frontRightValue = rightSide.getMaster().getInternalController().getSensorCollection().getIntegratedSensorPosition();
        middleRightValue = rightSide.getSlaves().get(0).getInternalController().getSensorCollection().getIntegratedSensorPosition();
        backLeftValue = rightSide.getSlaves().get(1).getInternalController().getSensorCollection().getIntegratedSensorPosition();

        frontLeftValue = leftSide.getMaster().getInternalController().getSensorCollection().getIntegratedSensorPosition();
        middleLeftValue = leftSide.getSlaves().get(0).getInternalController().getSensorCollection().getIntegratedSensorPosition();
        backLeftValue = leftSide.getSlaves().get(1).getInternalController().getSensorCollection().getIntegratedSensorPosition();
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
        rightSide.getMaster().set((speed - turn) + Constants.DriveTrain.RIGHT_CORRECTION * nerf);
        leftSide.getMaster().set((speed + turn) * nerf);
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
        rightSide.getMaster().set((right + Constants.DriveTrain.RIGHT_CORRECTION) * nerf);
        leftSide.getMaster().set(left * nerf);
    }



    public void resetEncoders() {
        rightSide.getMaster().getInternalController().getSensorCollection().setIntegratedSensorPosition(0, 50);
        leftSide.getMaster().getInternalController().getSensorCollection().setIntegratedSensorPosition(0, 50);
    }

    /**
     * Returns encoder position based on the average value from the frontLeft and frontRight motor controller encoders.
     * @return encoder position based on encoder values.
     */
    public double getMasterEncoderValue() {
        return (frontLeftValue + frontRightValue) / 2;
    }

    public double getAverageEncoderValue() {
        return (((frontLeftValue + middleLeftValue + backLeftValue) / 3) + ((frontRightValue + middleRightValue + backRightValue) / 3)) / 2;
    }

    /**
     * Uses PIDController to drive the robot a certain distance based on the average of the left and right encoder values
     * @param setpoint the setpoint in inches we want the robot to drive to.
     * @return PIDCommand that turns robot to target angle
     */
    public void drivePID(double setpoint) {
        new PIDCommand(
                drivePID,
                this::getMasterEncoderValue,
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

    public static class DualSpeedTransmission extends SubsystemBase {

        private final DoubleSolenoid rightSolenoid;
        private final DoubleSolenoid leftSolenoid;
        private final DriveTrain driveTrain;
        private final Timer timer;
        private double shiftTime;
        private boolean automaticShifting = true;

        public DualSpeedTransmission(DriveTrain driveTrain) {
            this.driveTrain = driveTrain;
            this.timer = new Timer();

            // TODO: Find what channel is right and left sides
            rightSolenoid = new DoubleSolenoid(PneumaticsModuleType.CTREPCM, 0, 1);
            leftSolenoid = new DoubleSolenoid(PneumaticsModuleType.CTREPCM,2, 3);
        }

        public enum Gear {
            LOW, HIGH
        }

        // TODO: Find what value is low gear and high gear
        public void setGear(Gear gear) {
            switch(gear) {
            case LOW:
                rightSolenoid.set(DoubleSolenoid.Value.kReverse);
                leftSolenoid.set(DoubleSolenoid.Value.kReverse);
                break;
            case HIGH:
                rightSolenoid.set(DoubleSolenoid.Value.kForward);
                leftSolenoid.set(DoubleSolenoid.Value.kForward);
                break;
            }
        }

        public Gear getGear() {
            if(rightSolenoid.get() == DoubleSolenoid.Value.kForward && leftSolenoid.get() == DoubleSolenoid.Value.kForward) {
                return Gear.HIGH;
            } else {
                return Gear.LOW;
            }
        }

        public void disableAutomaticShifting() {
            automaticShifting = false;
        }

        public void enableAutomaticShifting() {
            automaticShifting = true;
        }

        public boolean isAutomaticShifting() {
            return automaticShifting;
        }

        /**
         * If this method overloads the roborio main thread, implement CompletableFuture.
         */
        @Override
        public void periodic() {
            MotorController<TalonFX> rightMaster = driveTrain.getRightSide().getMaster();
            MotorController<TalonFX> leftMaster = driveTrain.getLeftSide().getMaster();
            if (isAutomaticShifting()) {
                if ((rightMaster.getMotorRotations() > Constants.DriveTrain.LOW_HIGH_THRESHOLD) && (leftMaster.getMotorRotations() > Constants.DriveTrain.LOW_HIGH_THRESHOLD) && (getGear() == Gear.LOW)) {
                    shiftTime += Timer.getFPGATimestamp() - timer.get();
                    if (shiftTime > Constants.DriveTrain.SHIFTING_THRESHOLD) {
                        setGear(Gear.HIGH);
                        driveTrain.set((rightSide, leftSide) -> {
                            rightSide.getMaster().getInternalController().configVoltageCompSaturation(8.5);
                            leftSide.getMaster().getInternalController().configVoltageCompSaturation(8.5);
                        });
                        driveTrain.setGearingParameters(Constants.DriveTrain.gearingHighGear);
                        shiftTime = 0;
                    }
                } else if ((rightMaster.getMotorRotations() < Constants.DriveTrain.HIGH_LOW_THRESHOLD) && (leftMaster.getMotorRotations() < Constants.DriveTrain.HIGH_LOW_THRESHOLD) && (getGear() == Gear.HIGH)) {
                    setGear(Gear.LOW);
                    driveTrain.set((rightSide, leftSide) -> {
                        rightSide.getMaster().getInternalController().configVoltageCompSaturation(12);
                        leftSide.getMaster().getInternalController().configVoltageCompSaturation(12);
                    });
                    driveTrain.setGearingParameters(Constants.DriveTrain.gearingLowGear);
                }
            }
        }
    }
}
