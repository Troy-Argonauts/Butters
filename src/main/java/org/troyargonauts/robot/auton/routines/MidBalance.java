package org.troyargonauts.robot.auton.routines;


import edu.wpi.first.wpilibj2.command.*;
import org.troyargonauts.robot.GamePiece;
import org.troyargonauts.robot.Robot;
import org.troyargonauts.robot.auton.commands.*;
import org.troyargonauts.robot.commands.MidScoring;
import org.troyargonauts.robot.commands.StartingSequence;
import org.troyargonauts.robot.subsystems.Elevator;
import org.troyargonauts.robot.subsystems.Wrist;

public class MidBalance extends SequentialCommandGroup {
	public MidBalance(GamePiece gamePiece) {
		addCommands(
				new StartingSequence()
		);

		if (gamePiece == GamePiece.CONE) {
			addCommands(
					new InstantCommand(() -> Robot.getWrist().setIntakeState(Wrist.IntakeState.FORWARD), Robot.getWrist()).withTimeout(0.5),
					new MidScoringAuton().withTimeout(5),
					new InstantCommand(() -> Robot.getWrist().setIntakeState(Wrist.IntakeState.OFF), Robot.getWrist()).withTimeout(0.5),
					new WaitUntilCommand(Robot.getWrist()::isPIDFinished)
			);
		} else {
			addCommands(
					new MidScoring().withTimeout(5),
					new WaitUntilCommand(Robot.getWrist()::isPIDFinished)
			);
		}
		addCommands(
				new DropPiece().withTimeout(2),
				new AutoBalance()
		);
	}
}