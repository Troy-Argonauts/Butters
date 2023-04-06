package org.troyargonauts.robot.auton.commands;


import edu.wpi.first.wpilibj2.command.InstantCommand;
import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;
import org.troyargonauts.robot.Robot;
import org.troyargonauts.robot.subsystems.Wrist;

public class DropPiece extends SequentialCommandGroup {
    public DropPiece() {
        // TODO: Add your sequential commands in the super() call, e.g.
        //           super(new OpenClawCommand(), new MoveArmCommand());
        super(
                new InstantCommand(() -> Robot.getWrist().setIntakeState(Wrist.IntakeState.BACKWARD), Robot.getWrist()).withTimeout(1),
                new InstantCommand(() -> Robot.getWrist().setIntakeState(Wrist.IntakeState.FORWARD), Robot.getWrist()).withTimeout(1),
                new InstantCommand(() -> Robot.getWrist().setIntakeState(Wrist.IntakeState.OFF), Robot.getWrist())
                );
    }
}