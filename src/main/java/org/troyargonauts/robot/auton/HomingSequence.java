package org.troyargonauts.robot.auton;

import edu.wpi.first.wpilibj2.command.InstantCommand;
import edu.wpi.first.wpilibj2.command.ParallelCommandGroup;
import org.troyargonauts.robot.Robot;
import org.troyargonauts.robot.subsystems.Elevator;
import org.troyargonauts.robot.subsystems.Wrist;

public class HomingSequence extends ParallelCommandGroup {

    public HomingSequence() {
        addCommands(
                new InstantCommand(() -> Robot.getElevator().setDesiredTarget(Elevator.ElevatorState.INITIAL_MOVEMENT), Robot.getElevator()),
                new InstantCommand(() -> Robot.getWrist().setDesiredTarget(Wrist.WristState.INITIAL_HOME), Robot.getWrist())
        );
    }
}
