package org.troyargonauts.robot.auton.routines;


import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;
import org.troyargonauts.robot.auton.commands.AutoBalance;
import org.troyargonauts.robot.commands.StartingSequence;

public class Balance extends SequentialCommandGroup {
    public Balance() {
        super(
                new StartingSequence(),
                new AutoBalance()
        );
    }
}