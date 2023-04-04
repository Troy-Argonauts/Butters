package org.troyargonauts.robot.auton;


import edu.wpi.first.wpilibj2.command.RunCommand;
import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;
import org.troyargonauts.robot.Robot;

public class ScoreBalance extends SequentialCommandGroup {
    public ScoreBalance() {
        super(
                new RunCommand(() -> Robot.getDrivetrain().cheesyDrive(-0.2, 0, 1), Robot.getDrivetrain()).withTimeout(0.5),
                new RunCommand(() -> Robot.getDrivetrain().cheesyDrive(0.2, 0, 1), Robot.getDrivetrain()).withTimeout(0.6)
                //Robot.getDrivetrain().drivePID(88).withTimeout(6)
        );
    }
}