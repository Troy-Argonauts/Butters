package org.troyargonauts.robot.auton.commands;


import edu.wpi.first.wpilibj2.command.InstantCommand;
import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;
import edu.wpi.first.wpilibj2.command.WaitUntilCommand;
import org.troyargonauts.robot.Robot;
import org.troyargonauts.robot.subsystems.Elevator;
import org.troyargonauts.robot.subsystems.Wrist;

public class MidScoringAuton extends SequentialCommandGroup {
    public MidScoringAuton() {
        super(
                new InstantCommand(() -> System.out.println("Mid Scoring")),
                new InstantCommand(() -> Robot.getElevator().setDesiredTarget(Elevator.ElevatorState.INITIAL_MOVEMENT), Robot.getElevator()),
                new InstantCommand(() -> Robot.getElevator().setDesiredTarget(Elevator.ElevatorState.MIDDLE), Robot.getElevator()),
                new InstantCommand(() -> Robot.getWrist().setDesiredTarget(Wrist.WristState.MIDDLE), Robot.getWrist()),
                new WaitUntilCommand(() -> Robot.getElevator().isPIDFinished())
                );
    }
}