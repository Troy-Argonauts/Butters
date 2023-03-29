package org.troyargonauts.auton;

import org.troyargonauts.Robot;

import edu.wpi.first.wpilibj2.command.InstantCommand;
import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;

public class StateScoreBalance extends SequentialCommandGroup {

    public StateScoreBalance() {
        addCommands(
                new InstantCommand(Robot.getDrivetrain()::resetEncoders),
//                Tune for Scoring
//                Robot.getElevator().elevatorPID(),
//                Robot.getArm().armPID(),
//                Robot.getArm().wristPID(),
                Robot.getDrivetrain().drivePID(-100),
//                Robot.getDrivetrain().autoBalance(),
                new InstantCommand(Robot.getDrivetrain()::breakMode)
        );
    }

}