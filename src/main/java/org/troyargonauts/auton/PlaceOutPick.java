package org.troyargonauts.auton;

import edu.wpi.first.wpilibj2.command.InstantCommand;
import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;
import org.troyargonauts.Robot;

public class PlaceOutPick extends SequentialCommandGroup {

    public PlaceOutPick() {
        addCommands(
            new InstantCommand(Robot.getDrivetrain()::resetEncoders),
            // add elevator code to place
            Robot.getDrivetrain().drivePID(-60),
            // add intake code to pick
        new InstantCommand(Robot.getDrivetrain()::breakMode)
        );
    }

}