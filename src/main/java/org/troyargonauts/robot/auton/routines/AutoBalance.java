package org.troyargonauts.robot.auton.routines;


import edu.wpi.first.wpilibj2.command.InstantCommand;
import edu.wpi.first.wpilibj2.command.RunCommand;
import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;
import org.troyargonauts.robot.Robot;
import org.troyargonauts.robot.commands.HomePosition;
import org.troyargonauts.robot.commands.StartingSequence;

public class AutoBalance extends SequentialCommandGroup {
    public AutoBalance() {
        super(
                new StartingSequence(),
                new HomePosition(),
                new RunCommand(() -> Robot.getDrivetrain().cheesyDrive(-0.5, 0, 1), Robot.getDrivetrain()).withTimeout(1.5),
                new RunCommand(() -> Robot.getDrivetrain().balance(), Robot.getDrivetrain())
        );
    }
}