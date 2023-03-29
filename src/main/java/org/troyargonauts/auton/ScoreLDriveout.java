package org.troyargonauts.auton;

import org.troyargonauts.Robot;

import edu.wpi.first.wpilibj2.command.InstantCommand;
import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;

public class ScoreLDriveout extends SequentialCommandGroup {
    
    public ScoreLDriveout() {
        addCommands(
            new InstantCommand(Robot.getElevator()::resetEncoders),
            new InstantCommand(() -> Robot.getElevator().setElevatorSetpoint(20)),
            new InstantCommand(Robot.getDrivetrain()::resetEncoders),
            Robot.getDrivetrain().drivePID(-60)
        );
    }

}
