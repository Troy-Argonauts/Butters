package org.troyargonauts.robot.auton.routines;


import edu.wpi.first.wpilibj2.command.RunCommand;
import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;
import edu.wpi.first.wpilibj2.command.WaitUntilCommand;
import org.troyargonauts.robot.Robot;
import org.troyargonauts.robot.commands.StartingSequence;

public class HomeDrive extends SequentialCommandGroup {
    public HomeDrive() {
        // TODO: Add your sequential commands in the super() call, e.g.
        //           super(new OpenClawCommand(), new MoveArmCommand());
        super(
                new StartingSequence().withTimeout(2),
                new RunCommand(() -> Robot.getDrivetrain().cheesyDrive(-0.3, 0, 1), Robot.getDrivetrain()).withTimeout(5)
        );
    }
}