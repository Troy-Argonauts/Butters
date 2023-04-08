package org.troyargonauts.robot.commands;


import edu.wpi.first.wpilibj2.command.InstantCommand;
import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;
import edu.wpi.first.wpilibj2.command.WaitCommand;
import org.troyargonauts.robot.Robot;
import org.troyargonauts.robot.subsystems.Arm;
import org.troyargonauts.robot.subsystems.Elevator;
import org.troyargonauts.robot.subsystems.Wrist;

public class FloorPickup extends SequentialCommandGroup {
    public FloorPickup() {
        super(
                new InstantCommand(() -> System.out.println("Floor Pickup")),
                new InstantCommand(() -> Robot.getElevator().setDesiredTarget(Elevator.ElevatorState.HOME), Robot.getElevator()),
                new InstantCommand(() -> Robot.getArm().setDesiredTarget(Arm.ArmState.FLOOR_PICKUP), Robot.getArm()),
                new WaitCommand(1),
                new InstantCommand(() -> Robot.getWrist().setDesiredTarget(Wrist.WristState.FLOOR_PICKUP), Robot.getWrist())
        );
    }
}