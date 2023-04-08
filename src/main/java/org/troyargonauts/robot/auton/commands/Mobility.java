package org.troyargonauts.robot.auton.commands;


import edu.wpi.first.wpilibj2.command.*;
import org.troyargonauts.robot.Robot;
import org.troyargonauts.robot.commands.FloorPickup;
import org.troyargonauts.robot.commands.HomePosition;
import org.troyargonauts.robot.subsystems.Elevator;
import org.troyargonauts.robot.subsystems.Wrist;

public class Mobility extends SequentialCommandGroup {
    public Mobility() {
        super(
                new HomePosition(),
                new WaitUntilCommand(Robot.getElevator()::isPIDFinished),
                new RunCommand(() -> Robot.getDrivetrain().cheesyDrive(-0.3, 0, 1), Robot.getDrivetrain()).withTimeout(2),
                new InstantCommand(() -> Robot.getWrist().setIntakeState(Wrist.IntakeState.BACKWARD), Robot.getWrist()).withTimeout(0.5),
                new FloorPickup(),
                new InstantCommand(() -> Robot.getDrivetrain().cheesyDrive(0,0,0), Robot.getDrivetrain()),
                new RunCommand(() -> Robot.getDrivetrain().cheesyDrive(-0.3, 0, 1), Robot.getDrivetrain()).withTimeout(2.5),
                new InstantCommand(() -> Robot.getWrist().setIntakeState(Wrist.IntakeState.OFF), Robot.getWrist()).withTimeout(0.5),
                new HomePosition(),
                new WaitUntilCommand(Robot.getElevator()::isPIDFinished)
                );
    }
}