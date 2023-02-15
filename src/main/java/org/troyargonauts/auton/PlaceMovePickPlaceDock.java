package org.troyargonauts.auton;

import org.troyargonauts.Robot;

import edu.wpi.first.wpilibj2.command.InstantCommand;
import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;

public class PlaceMovePickPlaceDock extends SequentialCommandGroup {

    public PlaceMovePickPlaceDock() {
        addCommands(
            new InstantCommand(Robot.getDrivetrain()::resetSensors),
            // place elevetor here
            Robot.getDrivetrain().turnPID(180),
            Robot.getDrivetrain().drivePID(-120),
            // pick up intake
            Robot.getDrivetrain().drivePID(-120),
            Robot.getDrivetrain().turnPID(180),
            // place elevetor here
            Robot.getDrivetrain().drivePID(-60),
            new InstantCommand(Robot.getDrivetrain()::breakMode)
        );
    }
    
}
