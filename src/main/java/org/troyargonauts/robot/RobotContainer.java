// Copyright (c) FIRST and other WPILib contributors.

// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package org.troyargonauts.robot;

import com.ctre.phoenix.motorcontrol.ControlMode;
import edu.wpi.first.wpilibj2.command.InstantCommand;
import edu.wpi.first.wpilibj2.command.RunCommand;
import org.troyargonauts.common.input.Gamepad;
import org.troyargonauts.common.input.gamepads.AutoGamepad;
import org.troyargonauts.common.math.OMath;
import org.troyargonauts.common.streams.IStream;
import org.troyargonauts.robot.commands.*;
import org.troyargonauts.robot.subsystems.DualSpeedTransmission;
import org.troyargonauts.robot.subsystems.Wrist;

/**
 * This class is where the bulk of the robot should be declared. Since Command-based is a
 * "declarative" paradigm, very little robot logic should actually be handled in the {@link Robot}
 * periodic methods (other than the scheduler calls). Instead, the structure of the robot (including
 * subsystems, commands, and trigger mappings) should be declared here.
 */
public class RobotContainer {

    public static final Gamepad driver = new AutoGamepad(0);
    public static final Gamepad operator = new AutoGamepad(1);
    public static double slowSpeed = 0.04;

    public RobotContainer() {
        // Configure the trigger bindings
        configureBindings();
    }

    /**
     * Use this method to define your trigger->command mappings.
     */
    private void configureBindings() {
        Robot.getDrivetrain().setDefaultCommand(
                new RunCommand(
                        () -> {
                            double speed = IStream.create(driver::getLeftY)
                                    .filtered(x -> OMath.deadband(x, Constants.DriveTrain.DEADBAND))
                                    .get();
                            double angle = IStream.create(driver::getRightX)
                                    .filtered(x -> OMath.deadband(x, Constants.DriveTrain.DEADBAND))
                                    .get();
                            Robot.getDrivetrain().cheesyDrive(speed, angle, 1);
                        }, Robot.getDrivetrain()
                )
        );

        driver.getRightBumper().whileTrue(
                new InstantCommand(() -> Robot.getDrivetrain().getDualSpeedTransmission().disableAutomaticShifting())
                        .andThen(new InstantCommand(() -> getDriver().setRumble(1.0, 0.5)))
        );

        driver.getLeftBumper().whileTrue(
                new InstantCommand(() -> Robot.getDrivetrain().getDualSpeedTransmission().enableAutomaticShifting())
                        .andThen(new InstantCommand(() -> Robot.getDrivetrain().getDualSpeedTransmission().setGear(DualSpeedTransmission.Gear.LOW)))
        );

        driver.getRightButton().whileTrue(
                new InstantCommand(() -> Robot.getDrivetrain().getDualSpeedTransmission().disableAutomaticShifting())
                        .andThen(() -> Robot.getDrivetrain().getDualSpeedTransmission().setGear(DualSpeedTransmission.Gear.LOW))
        );

        driver.getLeftButton().whileTrue(
                new InstantCommand(() -> Robot.getDrivetrain().getDualSpeedTransmission().disableAutomaticShifting())
                        .andThen(() -> Robot.getDrivetrain().getDualSpeedTransmission().setGear(DualSpeedTransmission.Gear.HIGH))
        );

        driver.getBottomButton().whileTrue(
                new RunCommand(() -> Robot.getDrivetrain().balance(), Robot.getDrivetrain())
        );

        driver.getSelectButton().whileTrue(
                new InstantCommand(() -> Robot.getDrivetrain().resistMovement(), Robot.getDrivetrain())
        ).onFalse(
                new InstantCommand(() -> Robot.getDrivetrain().set((leftSide, rightSide) -> {
                    leftSide.getMaster().getInternalController().set(ControlMode.PercentOutput, 0);
                    rightSide.getMaster().getInternalController().set(ControlMode.PercentOutput, 0);
                }))
        );


        Robot.getArm().setDefaultCommand(
                new RunCommand(
                        () -> {
                            double armSpeed = IStream.create(operator::getRightY)
                                    .filtered(x -> OMath.deadband(x, Constants.DriveTrain.DEADBAND))
                                    .get();
                            Robot.getArm().setPower(armSpeed);
                        }, Robot.getArm()
                )
        );

        Robot.getWrist().setDefaultCommand(
                new RunCommand(
                        () -> {
                            double wristSpeed = IStream.create(operator::getLeftY)
                                    .filtered(x -> OMath.deadband(x, Constants.DriveTrain.DEADBAND))
                                    .get();
                            Robot.getWrist().setPower(wristSpeed);
                        }, Robot.getWrist()
                )
        );

        Robot.getElevator().setDefaultCommand(
                new RunCommand(
                        () -> {
                            double upSpeed = IStream.create(operator::getRightTrigger)
                                    .filtered(x -> OMath.deadband(x, Constants.DriveTrain.DEADBAND))
                                    .get();
                            double downSpeed = IStream.create(operator::getLeftTrigger)
                                    .filtered(x -> OMath.deadband(x, Constants.DriveTrain.DEADBAND))
                                    .get();
                            Robot.getElevator().setPower((upSpeed - downSpeed));
                        }, Robot.getElevator()
                )
        );

        operator.getRightBumper().whileTrue(
                new InstantCommand(() -> Robot.getWrist().setIntakeState(Wrist.IntakeState.FORWARD), Robot.getWrist())
        ).whileFalse(
                new InstantCommand(() -> Robot.getWrist().setIntakeState(Wrist.IntakeState.OFF), Robot.getWrist())
        );

        operator.getLeftBumper().whileTrue(
                new InstantCommand(() -> Robot.getWrist().setIntakeState(Wrist.IntakeState.BACKWARD), Robot.getWrist())
        ).whileFalse(
                new InstantCommand(() -> Robot.getWrist().setIntakeState(Wrist.IntakeState.OFF), Robot.getWrist())
        );

        operator.getLeftButton().toggleOnTrue(
                new HybridScoring()
        );

        operator.getBottomButton().toggleOnTrue(
                new HomePosition()
        );

        operator.getRightButton().toggleOnTrue(
                new FloorPickup()
        );

        operator.getTopButton().toggleOnTrue(
                new MidScoring()
        );

        operator.getDPadUp().toggleOnTrue(
                new HighCubeScoring()
        );

        operator.getDPadRight().toggleOnTrue(
                new HumanPlayer()
        );
    }

    public static Gamepad getDriver() {
        return driver;
    }

    public static Gamepad getOperator() {
        return operator;
    }
}