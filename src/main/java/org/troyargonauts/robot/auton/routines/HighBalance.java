package org.troyargonauts.robot.auton.routines;


import edu.wpi.first.wpilibj2.command.*;
import org.troyargonauts.robot.GamePiece;
import org.troyargonauts.robot.Robot;
import org.troyargonauts.robot.auton.commands.AutoBalance;
import org.troyargonauts.robot.auton.commands.DropPiece;
import org.troyargonauts.robot.auton.commands.MidScoringAuton;
import org.troyargonauts.robot.commands.HighCubeScoring;
import org.troyargonauts.robot.commands.HomePosition;
import org.troyargonauts.robot.commands.MidScoring;
import org.troyargonauts.robot.commands.StartingSequence;
import org.troyargonauts.robot.subsystems.Wrist;

public class HighBalance extends SequentialCommandGroup {
	public HighBalance() {
		super(
				new StartingSequence(),
				new HighCubeScoring().withTimeout(5),
				new WaitUntilCommand(Robot.getWrist()::isPIDFinished),
				new DropPiece().withTimeout(2),
				new AutoBalance()
		);
	}
}