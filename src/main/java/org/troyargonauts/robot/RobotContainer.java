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
import org.troyargonauts.robot.subsystems.Intake;

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
                            if ((driver.getLeftTrigger() == 1) && (driver.getRightTrigger() == 1)) {
                                Robot.getDrivetrain().reverseRightMotors();
                            }
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


        //Intake Up
        operator.getLeftBumper().whileTrue(
                        new InstantCommand(() -> Robot.getIntake().setRotateIntakeState(Intake.rotateStates.UP), Robot.getIntake()))
                .onFalse(new InstantCommand(() -> Robot.getIntake().setRotateIntakeState(Intake.rotateStates.STOP), Robot.getIntake())
                );

        //Intake Down
        operator.getRightBumper().whileTrue(
                        new InstantCommand(() -> Robot.getIntake().setRotateIntakeState(Intake.rotateStates.DOWN), Robot.getIntake()))
                .onFalse(new InstantCommand(() -> Robot.getIntake().setRotateIntakeState(Intake.rotateStates.STOP), Robot.getIntake())
                );

        //Claw Open - Manual
        operator.getDPadRight().whileTrue(
                        new InstantCommand(() -> Robot.getIntake().updateClawSetpoint(1), Robot.getIntake())
                );

        //Claw Close - Manual
        operator.getDPadLeft().whileTrue(
                new InstantCommand(() -> Robot.getIntake().updateClawSetpoint(-1), Robot.getIntake())
        );

        //Claw Close - Cone
        operator.getTopButton().whileTrue(
                new InstantCommand(() -> Robot.getIntake().setSqueezeSetpoint(-31), Robot.getIntake())
                );

        //Claw Close - Cube
        operator.getLeftButton().whileTrue(
                new InstantCommand(() -> Robot.getIntake().setSqueezeSetpoint(-23), Robot.getIntake())
        );

        //Claw Open
        operator.getBottomButton().whileTrue(
                new InstantCommand(() -> Robot.getIntake().setSqueezeSetpoint(0), Robot.getIntake())
        );

        //LED - Purple
        driver.getLeftBumper().toggleOnTrue(
                new InstantCommand(() -> Robot.getLEDs().purpleCube(), Robot.getLEDs())
        );

        //LED - Yellow
        driver.getRightBumper().toggleOnTrue(
                new InstantCommand(() -> Robot.getLEDs().yellowCone(), Robot.getLEDs())
        );

        //LED -Rainbow
        driver.getTopButton().toggleOnTrue(
                new InstantCommand(() -> Robot.getLEDs().rainbow(), Robot.getLEDs())
        );

        //Reset Encoders - Intake
        operator.getStartButton().toggleOnTrue(
                new InstantCommand(() -> Robot.getIntake().resetEndcoders(), Robot.getIntake())
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