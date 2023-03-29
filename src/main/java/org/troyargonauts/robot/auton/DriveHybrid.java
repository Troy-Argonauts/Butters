package org.troyargonauts.robot.auton;

import edu.wpi.first.wpilibj2.command.RunCommand;
import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;
import org.troyargonauts.robot.Robot;

public class DriveHybrid extends SequentialCommandGroup {
    public DriveHybrid() {

        super(
                new RunCommand(() -> Robot.getDrivetrain().cheesyDrive(-0.2, 0, 1), Robot.getDrivetrain()).withTimeout(1),
                new RunCommand(() -> Robot.getDrivetrain().cheesyDrive(0.2, 0, 1), Robot.getDrivetrain()).withTimeout(1.1),
                new RunCommand(() -> Robot.getDrivetrain().cheesyDrive(-0.3, 0, 1), Robot.getDrivetrain()).withTimeout(3)
        );
    }
}