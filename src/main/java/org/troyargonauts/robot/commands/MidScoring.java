package org.troyargonauts.robot.commands;


import edu.wpi.first.wpilibj2.command.InstantCommand;
import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;
import edu.wpi.first.wpilibj2.command.WaitCommand;
import edu.wpi.first.wpilibj2.command.WaitUntilCommand;
import org.troyargonauts.robot.Robot;
import org.troyargonauts.robot.subsystems.Elevator;
import org.troyargonauts.robot.subsystems.Wrist;

public class MidScoring extends SequentialCommandGroup {
    public MidScoring() {
        // TODO: Add your sequential commands in the super() call, e.g.
        //           super(new OpenClawCommand(), new MoveArmCommand());
        super(
                new InstantCommand(() -> System.out.println("Mid Scoring")),
                new InstantCommand(() -> Robot.getElevator().setDesiredTarget(Elevator.ElevatorState.INITIAL_MOVEMENT), Robot.getElevator()),
                new InstantCommand(() -> Robot.getElevator().setDesiredTarget(Elevator.ElevatorState.MIDDLE), Robot.getElevator()),
                new WaitUntilCommand(() -> Robot.getElevator().isPIDFinished()),
                new InstantCommand(() -> Robot.getWrist().setDesiredTarget(Wrist.WristState.MIDDLE), Robot.getWrist())
                );
    }
}