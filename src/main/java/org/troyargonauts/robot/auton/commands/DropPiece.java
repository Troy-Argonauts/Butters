package org.troyargonauts.robot.auton.commands;


import edu.wpi.first.wpilibj2.command.InstantCommand;
import edu.wpi.first.wpilibj2.command.RunCommand;
import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;
import org.troyargonauts.robot.GamePiece;
import org.troyargonauts.robot.Robot;
import org.troyargonauts.robot.subsystems.Wrist;

public class DropPiece extends SequentialCommandGroup {
    public DropPiece() {
        super(
                new RunCommand(() -> Robot.getWrist().setIntakeState(Wrist.IntakeState.BACKWARD), Robot.getWrist()).withTimeout(0.5),
                new RunCommand(() -> Robot.getWrist().setIntakeState(Wrist.IntakeState.FORWARD), Robot.getWrist()).withTimeout(0.5),
                new InstantCommand(() -> Robot.getWrist().setIntakeState(Wrist.IntakeState.OFF), Robot.getWrist())
                );
    }
}