package org.troyargonauts.auton;

import org.troyargonauts.Robot;

import edu.wpi.first.wpilibj2.command.InstantCommand;
import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;

public class StateScoreDriveThroughCSBalance extends SequentialCommandGroup {

    public StateScoreDriveThroughCSBalance() {
        addCommands(
                new InstantCommand(Robot.getDrivetrain()::resetEncoders),
//                Tune for Scoring
//                Robot.getElevator().elevatorPID(),
//                Robot.getArm().armPID(),
//                Robot.getArm().wristPID(),
                Robot.getDrivetrain().drivePID(-200),
                Robot.getDrivetrain().drivePID(50),
//                Robot.getDrivetrain().autoBalance(),
                new InstantCommand(Robot.getDrivetrain()::breakMode)
        );
    }

}