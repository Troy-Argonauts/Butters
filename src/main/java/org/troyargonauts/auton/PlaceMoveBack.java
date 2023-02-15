package org.troyargonauts.auton;

import org.troyargonauts.Robot;

import edu.wpi.first.wpilibj2.command.InstantCommand;
import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;

public class PlaceMoveBack extends SequentialCommandGroup {
    
    public PlaceMoveBack() {
        addCommands(
            new InstantCommand(Robot.getDrivetrain()::resetSensors),
            // place elevetor here
            Robot.getDrivetrain().drivePID(-60),
                new InstantCommand(Robot.getDrivetrain()::breakMode)
        );
    }

}
