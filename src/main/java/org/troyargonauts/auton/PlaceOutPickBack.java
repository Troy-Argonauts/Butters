package org.troyargonauts.auton;

import edu.wpi.first.wpilibj2.command.InstantCommand;
import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;
import org.troyargonauts.Robot;

public class PlaceOutPickBack extends SequentialCommandGroup {

    public PlaceOutPickBack() {
        addCommands(
            new InstantCommand(Robot.getDrivetrain()::resetEncoders),
            // add elevator code
            Robot.getDrivetrain().drivePID(-60),
            // add intake code
                Robot.getDrivetrain().drivePID(60)
        );
    }

}
