// Copyright (c) FIRST and other WPILib contributors.

// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package org.troyargonauts.robot;

import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.InstantCommand;
import edu.wpi.first.wpilibj2.command.RunCommand;
import org.troyargonauts.common.input.Gamepad;
import org.troyargonauts.common.input.gamepads.AutoGamepad;
import org.troyargonauts.common.math.OMath;
import org.troyargonauts.common.streams.IStream;
import org.troyargonauts.robot.subsystems.Arm;

/**
 * This class is where the bulk of the robot should be declared. Since Command-based is a
 * "declarative" paradigm, very little robot logic should actually be handled in the {@link Robot}
 * periodic methods (other than the scheduler calls). Instead, the structure of the robot (including
 * subsystems, commands, and trigger mappings) should be declared here.
 */
public class RobotContainer {

    public static final Gamepad driver = new AutoGamepad(0);
    public static final Gamepad operator = new AutoGamepad(1);

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

        Robot.getArm().setDefaultCommand(
                new RunCommand(() -> {
                    Robot.getArm().wristTeleOp(operator.getRightY());
                    Robot.getArm().armTeleOp(operator.getLeftY());
                }, Robot.getArm())
        );

        operator.getLeftBumper().onTrue(
                        new InstantCommand(() -> Robot.getArm().setIntakeState(Arm.IntakeState.BACKWARD), Robot.getArm()))
                .onFalse(new InstantCommand(() -> Robot.getArm().setIntakeState(Arm.IntakeState.OFF), Robot.getArm()));

        operator.getRightBumper().onTrue(
                        new InstantCommand(() -> Robot.getArm().setIntakeState(Arm.IntakeState.FORWARD), Robot.getArm()))
                .onFalse(new InstantCommand(() -> Robot.getArm().setIntakeState(Arm.IntakeState.OFF), Robot.getArm()));

        //Cone - Human Player (A Button)
        operator.getBottomButton().toggleOnTrue(
                new InstantCommand(() -> Robot.getArm().setArmSetpoint(5.5)).alongWith(
                        new InstantCommand(() -> Robot.getArm().setWristSetpoint(-25))
                )
        );

        //Cube - Human Player (B Button)
        operator.getRightButton().toggleOnTrue(
                new InstantCommand(() -> Robot.getArm().setArmSetpoint(13)).alongWith(
                        new InstantCommand(() -> Robot.getArm().setWristSetpoint(-32))
                )
        );

        //Home (X Button)
        operator.getRightButton().toggleOnTrue(
                new InstantCommand(() -> Robot.getArm().setArmSetpoint(-5)).alongWith(
                        new InstantCommand(() -> Robot.getArm().setWristSetpoint(0))
                )
        );

        //Cone Score (Y Button)
        operator.getTopButton().toggleOnTrue(
                new InstantCommand(() -> Robot.getArm().setArmSetpoint(39)).alongWith(
                        new InstantCommand(() -> Robot.getArm().setWristSetpoint(-21))
                )
        );


        //Floor Pickup
        operator.getStartButton().toggleOnTrue(
                new InstantCommand(() -> Robot.getArm().setArmSetpoint(75)).alongWith(
                        new InstantCommand(() -> Robot.getArm().setWristSetpoint(-26))
                )
        );
    }

    /**
     * Use this to pass the autonomous command to the main {@link Robot} class.
     *
     * @return the command to run in autonomous
     */
    public Command getAutonomousCommand() {
        // TODO: Implement properly
        return null;
    }

    public static Gamepad getDriver() {
        return driver;
    }

    public static Gamepad getOperator() {
        return operator;
    }
}