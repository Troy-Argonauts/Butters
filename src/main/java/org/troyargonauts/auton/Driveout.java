package org.troyargonauts.auton;

import org.troyargonauts.Robot;

import edu.wpi.first.wpilibj2.command.InstantCommand;
import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;

public class Driveout extends SequentialCommandGroup {
    
    public Driveout() {
        addCommands(
            new InstantCommand(Robot.getDrivetrain()::resetEncoders),
            Robot.getDrivetrain().drivePID(60),
                new InstantCommand(Robot.getDrivetrain()::breakMode)
        );
    }

}
