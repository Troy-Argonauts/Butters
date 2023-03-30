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
                new RunCommand(
                        () -> {
                            double wristSpeed = IStream.create(operator::getLeftY)
                                    .filtered(x -> OMath.deadband(x, Constants.DriveTrain.DEADBAND))
                                    .get();
                            double armSpeed = IStream.create(operator::getRightY)
                                    .filtered(x -> OMath.deadband(x, Constants.DriveTrain.DEADBAND))
                                    .get();
                            Robot.getArm().setWristPower(wristSpeed * 0.3);
                            Robot.getArm().setArmPower(armSpeed * 0.5);
                        }, Robot.getArm()
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
                            System.out.println(upSpeed - downSpeed);
                            Robot.getElevator().setPower((upSpeed - downSpeed) * 0.3);
                        }, Robot.getElevator()
                )
        );

        driver.getRightBumper().whileTrue(
                new InstantCommand(() -> Robot.getDrivetrain().getDualSpeedTransmission().disableAutomaticShifting())
        );

        driver.getLeftBumper().whileTrue(
                new InstantCommand(() -> Robot.getDrivetrain().getDualSpeedTransmission().enableAutomaticShifting())
        );

//        //LED - Purple
//        driver.getLeftBumper().toggleOnTrue(
//                new InstantCommand(() -> Robot.getLEDs().purpleCube(), Robot.getLEDs())
//        );
//
//        //LED - Yellow
//        driver.getRightBumper().toggleOnTrue(
//                new InstantCommand(() -> Robot.getLEDs().yellowCone(), Robot.getLEDs())
//        );

        //LED -Rainbow
        driver.getTopButton().toggleOnTrue(
                new InstantCommand(() -> Robot.getLEDs().rainbow(), Robot.getLEDs())
        );

        driver.getBottomButton().whileTrue(
                new RunCommand(() -> Robot.getDrivetrain().balance(), Robot.getDrivetrain())
        );

        operator.getRightBumper().whileTrue(
                new InstantCommand(() -> Robot.getArm().setIntakeState(Arm.IntakeState.FORWARD))
        ).whileFalse(
                new InstantCommand(() -> Robot.getArm().setIntakeState(Arm.IntakeState.OFF))
        );

        operator.getLeftBumper().whileTrue(
                new InstantCommand(() -> Robot.getArm().setIntakeState(Arm.IntakeState.BACKWARD))
        ).whileFalse(
                new InstantCommand(() -> Robot.getArm().setIntakeState(Arm.IntakeState.OFF))
        );

        operator.getTopButton().onTrue(
                Robot.getArm().wristPID(104.666)
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