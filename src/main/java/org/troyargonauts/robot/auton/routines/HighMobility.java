package org.troyargonauts.robot.auton.routines;


import edu.wpi.first.wpilibj2.command.InstantCommand;
import edu.wpi.first.wpilibj2.command.RunCommand;
import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;
import edu.wpi.first.wpilibj2.command.WaitCommand;
import org.troyargonauts.robot.Robot;
import org.troyargonauts.robot.auton.commands.DropPiece;
import org.troyargonauts.robot.auton.commands.Mobility;
import org.troyargonauts.robot.commands.*;

public class HighMobility extends SequentialCommandGroup {
    public HighMobility() {
        super(
                new StartingSequence(),
                new HighCubeScoring().withTimeout(5),
                new DropPiece().withTimeout(2),
                new Mobility()
        );
    }
}