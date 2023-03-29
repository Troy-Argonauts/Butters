package org.troyargonauts.auton;

import org.troyargonauts.Robot;

import edu.wpi.first.wpilibj2.command.InstantCommand;
import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;

public class StateDriveThroughCSBalance extends SequentialCommandGroup {

    public StateDriveThroughCSBalance() {
        addCommands(
                new InstantCommand(Robot.getDrivetrain()::resetEncoders),
                Robot.getDrivetrain().drivePID(-150),
                Robot.getDrivetrain().drivePID(50),
                new InstantCommand(Robot.getDrivetrain()::breakMode)
        );
    }

}