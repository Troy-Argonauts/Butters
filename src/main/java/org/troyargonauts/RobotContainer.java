// Copyright (c) FIRST and other WPILib contributors.

// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package org.troyargonauts;

import org.troyargonauts.subsystems.Intake;
import org.troyargonauts.subsystems.Intake.rotateStates;
import org.troyargonauts.subsystems.Intake.squeezeStates;

import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.InstantCommand;

/**
 * This class is where the bulk of the robot should be declared. Since Command-based is a
 * "declarative" paradigm, very little robot logic should actually be handled in the {@link Robot}
 * periodic methods (other than the scheduler calls). Instead, the structure of the robot (including
 * subsystems, commands, and trigger mappings) should be declared here.
 */
public class RobotContainer { 
    
    private ArgoController driver;

    public RobotContainer()
    {
        // Configure the trigger bindings
        configureBindings();
        
        driver = new ArgoController(0, 0.1);
    }
    
    
    /** Use this method to define your trigger->command mappings. */
    private void configureBindings()
    {
        if (driver.getRightTrigger() > 0.5) {
            new InstantCommand(() -> Intake.setSqueezeIntakeState(squeezeStates.CLOSE));
        } else {
            new InstantCommand(() -> Intake.setSqueezeIntakeState(squeezeStates.STOP));
        }

        if (driver.getLeftTrigger() > 0.5) {
            new InstantCommand(() -> Intake.setSqueezeIntakeState(squeezeStates.OPEN));
        } else {
            new InstantCommand(() -> Intake.setSqueezeIntakeState(squeezeStates.STOP));
        }

        driver.getBButton().whileTrue(
            new InstantCommand(() -> Intake.setRotateIntakeState(rotateStates.UP))
        ).whileFalse(
            new InstantCommand(() -> Intake.setRotateIntakeState(rotateStates.STOP))
        );

        driver.getAButton().whileTrue(
            new InstantCommand(() -> Intake.setRotateIntakeState(rotateStates.DOWN))
        ).whileFalse(
            new InstantCommand(() -> Intake.setRotateIntakeState(rotateStates.STOP))
        );
    }
    
    
    /**
     * Use this to pass the autonomous command to the main {@link Robot} class.
     *
     * @return the command to run in autonomous
     */
    public Command getAutonomousCommand()
    {
        // TODO: Implement properly
        return null;
    }
}
