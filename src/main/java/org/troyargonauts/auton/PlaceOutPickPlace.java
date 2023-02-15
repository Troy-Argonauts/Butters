package org.troyargonauts.auton;

import edu.wpi.first.wpilibj2.command.InstantCommand;
import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;
import org.troyargonauts.Robot;

public class PlaceOutPickPlace extends SequentialCommandGroup {

    public PlaceOutPickPlace() {
        addCommands(
            new InstantCommand(Robot.getDrivetrain()::resetEncoders),
            // add elevator code to place
            Robot.getDrivetrain().drivePID(-60),
            // add intake code to pick
                // add elevator code to place
        );
    }

}
