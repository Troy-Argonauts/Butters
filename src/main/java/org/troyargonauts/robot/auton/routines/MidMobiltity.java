package org.troyargonauts.robot.auton.routines;


import edu.wpi.first.wpilibj2.command.RunCommand;
import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;
import org.troyargonauts.robot.Robot;
import org.troyargonauts.robot.auton.commands.DropPiece;
import org.troyargonauts.robot.auton.commands.Mobility;
import org.troyargonauts.robot.commands.MidScoring;
import org.troyargonauts.robot.commands.StartingSequence;

public class MidMobiltity extends SequentialCommandGroup {
    public MidMobiltity() {
        super(
                new StartingSequence().withTimeout(2),
                new MidScoring().withTimeout(5),
                new DropPiece().withTimeout(2),
                new Mobility()
        );
    }
}