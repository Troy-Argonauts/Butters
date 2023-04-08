package org.troyargonauts.robot.auton.routines;


import edu.wpi.first.wpilibj2.command.RunCommand;
import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;
import org.troyargonauts.robot.Robot;
import org.troyargonauts.robot.commands.StartingSequence;

public class DriveOut extends SequentialCommandGroup {
    public DriveOut() {
        super(
                new StartingSequence().withTimeout(2),
                new RunCommand(() -> Robot.getDrivetrain().cheesyDrive(-0.3, 0, 1), Robot.getDrivetrain()).withTimeout(3)
        );
    }
}