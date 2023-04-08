package org.troyargonauts.robot.auton.routines;


import edu.wpi.first.wpilibj2.command.RunCommand;
import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;
import org.troyargonauts.robot.Robot;
import org.troyargonauts.robot.auton.commands.DropPiece;
import org.troyargonauts.robot.auton.commands.Mobility;
import org.troyargonauts.robot.commands.HybridScoring;
import org.troyargonauts.robot.commands.MidScoring;
import org.troyargonauts.robot.commands.StartingSequence;

public class HybridMobility extends SequentialCommandGroup {
    public HybridMobility() {
        super(
                new StartingSequence().withTimeout(2),
                new HybridScoring().withTimeout(5),
                new DropPiece().withTimeout(2),
                new Mobility()
        );
    }
}