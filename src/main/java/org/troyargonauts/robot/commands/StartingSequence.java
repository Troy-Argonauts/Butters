package org.troyargonauts.robot.commands;

import edu.wpi.first.wpilibj2.command.*;
import org.troyargonauts.robot.Robot;
import org.troyargonauts.robot.subsystems.Elevator;
import org.troyargonauts.robot.subsystems.Wrist;

public class StartingSequence extends SequentialCommandGroup {

    public StartingSequence() {
        super(
                new InstantCommand(() -> System.out.println("Starting Sequence")),
                new InstantCommand(() -> Robot.getElevator().setDesiredTarget(Elevator.ElevatorState.INITIAL_MOVEMENT), Robot.getElevator()),
                new WaitUntilCommand(() -> Robot.getElevator().isPIDFinished()),
                new WaitCommand(0.2),
                new InstantCommand(() -> Robot.getWrist().setDirectPower(-0.2)),
                new WaitUntilCommand(Robot.getWrist()::getDownLimitSwitch),
                new InstantCommand(() -> Robot.getWrist().setDirectPower(0)),
                new InstantCommand(Robot.getWrist()::resetEncoders)
                );
    }
}

