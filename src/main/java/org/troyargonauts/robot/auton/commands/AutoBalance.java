package org.troyargonauts.robot.auton.commands;


import edu.wpi.first.wpilibj2.command.InstantCommand;
import edu.wpi.first.wpilibj2.command.RunCommand;
import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;
import edu.wpi.first.wpilibj2.command.WaitUntilCommand;
import org.troyargonauts.robot.Robot;
import org.troyargonauts.robot.commands.HomePosition;
import org.troyargonauts.robot.subsystems.Elevator;

public class AutoBalance extends SequentialCommandGroup {
    public AutoBalance() {
        super(
                new InstantCommand(() -> Robot.getElevator().setDesiredTarget(Elevator.ElevatorState.INITIAL_MOVEMENT)),
                new WaitUntilCommand(Robot.getElevator()::isPIDFinished),
                new HomePosition(),
                new RunCommand(() -> Robot.getDrivetrain().cheesyDrive(-0.4, 0, 1), Robot.getDrivetrain()).withTimeout(2),
                new RunCommand(() -> Robot.getDrivetrain().balance(), Robot.getDrivetrain())
        );
    }
}