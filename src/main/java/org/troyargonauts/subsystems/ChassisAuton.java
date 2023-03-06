package org.troyargonauts.subsystems;


import edu.wpi.first.wpilibj2.command.RunCommand;
import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;
import org.troyargonauts.Robot;

public class ChassisAuton extends SequentialCommandGroup {
    public ChassisAuton() {
        // TODO: Add your sequential commands in the super() call, e.g.
        //           super(new OpenClawCommand(), new MoveArmCommand());
        super(
                new RunCommand(() -> Robot.getDrivetrain().cheesyDrive(-0.2, 0, 1), Robot.getDrivetrain()).withTimeout(1),
                new RunCommand(() -> Robot.getDrivetrain().cheesyDrive(0.3, 0, 1), Robot.getDrivetrain()).withTimeout(1.5),
                new RunCommand(() -> Robot.getDrivetrain().cheesyDrive(-0.3, 0, 1), Robot.getDrivetrain()).withTimeout(3.5)
                );
    }
}