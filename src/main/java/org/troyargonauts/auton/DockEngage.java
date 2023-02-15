package org.troyargonauts.auton;

import edu.wpi.first.wpilibj2.command.InstantCommand;
import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;
import org.troyargonauts.Robot;

public class DockEngage extends SequentialCommandGroup {

    public DockEngage() {
        addCommands(
            new InstantCommand(Robot.getDrivetrain()::resetEncoders),
            // elevator code to place here
            Robot.getDrivetrain().turnPID(180),
            Robot.getDrivetrain().drivePID(60),
            Robot.getDrivetrain().turnPID(90),
            Robot.getDrivetrain().drivePID(10),
            Robot.getDrivetrain().turnPID(90),
            Robot.getDrivetrain().drivePID(10)
        );
    }

}
