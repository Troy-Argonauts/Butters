// Copyright (c) FIRST and other WPILib contributors.

// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package org.troyargonauts;

import org.troyargonauts.subsystems.Arm.IntakeState;

import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.InstantCommand;
import edu.wpi.first.wpilibj2.command.RunCommand;
/**
 * This class is where the bulk of the robot should be declared. Since Command-based is a
 * "declarative" paradigm, very little robot logic should actually be handled in the {@link Robot}
 * periodic methods (other than the scheduler calls). Instead, the structure of the robot (including
 * subsystems, commands, and trigger mappings) should be declared here.
 */
public class RobotContainer {

    public static ArgoController operator;

    public RobotContainer() {
        // Configure the trigger bindings
        configureBindings();
        operator = new ArgoController(0, 0.1);
    }
    
    
    /** Use this method to define your trigger->command mappings. */
    private void configureBindings() {
        Robot.getArm().setDefaultCommand(
            new RunCommand(
                () -> { 
                    Robot.getArm().setArmPower(operator.getRightJoystickY()); 
                    Robot.getArm().setWristPower(operator.getLeftJoystickY());
                }, Robot.getArm()
            )
        );

        operator.getRBButton().whileTrue(
            new InstantCommand(() -> Robot.getArm().setIntakeState(IntakeState.FORWARD), Robot.getArm())
        ).whileFalse(
            new InstantCommand(() -> Robot.getArm().setIntakeState(IntakeState.OFF), Robot.getArm())
        );

        operator.getLBButton().whileTrue(
            new InstantCommand(() -> Robot.getArm().setIntakeState(IntakeState.BACKWARD), Robot.getArm())
        ).whileFalse(
            new InstantCommand(() -> Robot.getArm().setIntakeState(IntakeState.OFF), Robot.getArm())
        );
    }
    
    /**
     * 
     * Use this to pass the autonomous command to the main {@link Robot} class.
     *
     * @return the command to run in autonomous
     */
    public Command getAutonomousCommand()
    {
        // TODO: Implement properly
        return null;
    }

    public static ArgoController getOperator() {
        return operator;
    }
}
