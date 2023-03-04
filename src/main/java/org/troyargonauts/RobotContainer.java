// Copyright (c) FIRST and other WPILib contributors.

// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package org.troyargonauts;

import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.InstantCommand;
import edu.wpi.first.wpilibj2.command.RunCommand;
import org.troyargonauts.subsystems.Arm;
/**
 * This class is where the bulk of the robot should be declared. Since Command-based is a
 * "declarative" paradigm, very little robot logic should actually be handled in the {@link Robot}
 * periodic methods (other than the scheduler calls). Instead, the structure of the robot (including
 * subsystems, commands, and trigger mappings) should be declared here.
 */
public class RobotContainer {

    public static ArgoController driver;
    public static ArgoController operator;

    public RobotContainer() {
        // Configure the trigger bindings
        driver = new ArgoController(0, 0.1);
        operator = new ArgoController(1, 0.15);
        configureBindings();
    }

    /**
     * Use this method to define your trigger->command mappings.
     */
    private void configureBindings() {
        Robot.getDrivetrain().setDefaultCommand(
                new RunCommand(
                        () -> {
                            Robot.getDrivetrain().cheesyDrive(driver.getLeftJoystickY(), driver.getRightJoystickX(), 1);
                        }, Robot.getDrivetrain()
                )
        );

        Robot.getArm().setDefaultCommand(
                new RunCommand(() -> {
                    Robot.getArm().wristTeleOp(operator.getRightJoystickY());
                    Robot.getArm().armTeleOp(operator.getLeftJoystickY());
                }, Robot.getArm())
        );

        operator.getLBButton().onTrue(
                        new InstantCommand(() -> Robot.getArm().setIntakeState(Arm.IntakeState.BACKWARD), Robot.getArm()))
                .onFalse(new InstantCommand(() -> Robot.getArm().setIntakeState(Arm.IntakeState.OFF), Robot.getArm()));

        operator.getRBButton().onTrue(
                        new InstantCommand(() -> Robot.getArm().setIntakeState(Arm.IntakeState.FORWARD), Robot.getArm()))
                .onFalse(new InstantCommand(() -> Robot.getArm().setIntakeState(Arm.IntakeState.OFF), Robot.getArm()));

        //Cone - Human Player
        operator.getAButton().toggleOnTrue(
                new InstantCommand(() -> Robot.getArm().setArmSetpoint(5.5)).alongWith(
                        new InstantCommand(() -> Robot.getArm().setWristSetpoint(-25))
                )
        );

        //Cube - Human Player
        operator.getBButton().toggleOnTrue(
                new InstantCommand(() -> Robot.getArm().setArmSetpoint(13)).alongWith(
                        new InstantCommand(() -> Robot.getArm().setWristSetpoint(-32))
                )
        );

        //Home
        operator.getXButton().toggleOnTrue(
                new InstantCommand(() -> Robot.getArm().setArmSetpoint(-5)).alongWith(
                        new InstantCommand(() -> Robot.getArm().setWristSetpoint(0))
                )
        );

        //Cone Score
        operator.getYButton().toggleOnTrue(
                new InstantCommand(() -> Robot.getArm().setArmSetpoint(39)).alongWith(
                        new InstantCommand(() -> Robot.getArm().setWristSetpoint(-21))
                )
        );

        //Something
//        operator.getSTARTButton().toggleOnTrue(
//                new InstantCommand(() -> Robot.getArm().setArmSetpoint(27.5)).alongWith(
//                        new InstantCommand(() -> Robot.getArm().setWristSetpoint(-39))
//                )
//        );

        //Floor Pickup
        operator.getSTARTButton().toggleOnTrue(
                new InstantCommand(() -> Robot.getArm().setArmSetpoint(75)).alongWith(
                        new InstantCommand(() -> Robot.getArm().setWristSetpoint(-36))
                )
        );


//                Robot.getElevator().setDefaultCommand(
//                        new RunCommand(() -> {
//                            Robot.getElevator().setPower(operator.getLeftJoystickY());
//                        }, Robot.getElevator())
//                );

//        Robot.getTurret().setDefaultCommand(
//            new RunCommand(() -> {
//                Robot.getTurret().turretManual(-operator.getLeftTrigger());
//                Robot.getTurret().turretManual(operator.getRightTrigger());
//            }, Robot.getTurret())
//        );

//        controller.getDirection(ArgoController.Direction.UP).onTrue(
//                new InstantCommand(() -> Robot.getTurret().setTurretSetpoint(0), Robot.getTurret()));
//
//        controller.getDirection(ArgoController.Direction.RIGHT).onTrue(
//                new InstantCommand(() -> Robot.getTurret().setTurretSetpoint(0), Robot.getTurret()));
//
//        controller.getDirection(ArgoController.Direction.DOWN).onTrue(
//                new InstantCommand(() -> Robot.getTurret().setTurretSetpoint(0), Robot.getTurret()));
//
//        controller.getDirection(ArgoController.Direction.LEFT).onTrue(
//                new InstantCommand(() -> Robot.getTurret().setTurretSetpoint(0), Robot.getTurret()));
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

    public static ArgoController getDriver() {
        return driver;
    }

    public static ArgoController getOperator() {
        return operator;
    }
}