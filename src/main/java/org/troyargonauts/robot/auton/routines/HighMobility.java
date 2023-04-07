package org.troyargonauts.robot.auton.routines;


import edu.wpi.first.wpilibj2.command.RunCommand;
import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;
import edu.wpi.first.wpilibj2.command.WaitCommand;
import org.troyargonauts.robot.Robot;
import org.troyargonauts.robot.auton.commands.DropPiece;
import org.troyargonauts.robot.commands.*;

public class HighMobility extends SequentialCommandGroup {
    public HighMobility() {
        // TODO: Add your sequential commands in the super() call, e.g.
        //           super(new OpenClawCommand(), new MoveArmCommand());
        super(
                new StartingSequence(),
                new HighCubeScoring().withTimeout(5),
                new DropPiece().withTimeout(2),
                new HomePosition(),
                new WaitCommand(3),
                new RunCommand(() -> Robot.getDrivetrain().cheesyDrive(-0.3, 0, 1), Robot.getDrivetrain()).withTimeout(5)
        );
    }
}