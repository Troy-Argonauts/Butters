package org.troyargonauts.robot.auton.routines;


import edu.wpi.first.wpilibj2.command.RunCommand;
import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;
import org.troyargonauts.robot.Robot;
import org.troyargonauts.robot.auton.commands.DropPiece;
import org.troyargonauts.robot.commands.HybridScoring;
import org.troyargonauts.robot.commands.MidScoring;
import org.troyargonauts.robot.commands.StartingSequence;

public class HybridMobility extends SequentialCommandGroup {
    public HybridMobility() {
        // TODO: Add your sequential commands in the super() call, e.g.
        //           super(new OpenClawCommand(), new MoveArmCommand());
        super(
                new StartingSequence().withTimeout(2),
                new HybridScoring().withTimeout(5),
                new DropPiece().withTimeout(2),
                new RunCommand(() -> Robot.getDrivetrain().cheesyDrive(-0.3, 0, 1), Robot.getDrivetrain()).withTimeout(5)
        );
    }
}