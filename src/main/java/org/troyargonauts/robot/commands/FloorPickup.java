package org.troyargonauts.robot.commands;


import edu.wpi.first.wpilibj2.command.InstantCommand;
import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;
import org.troyargonauts.robot.Robot;
import org.troyargonauts.robot.subsystems.Arm;
import org.troyargonauts.robot.subsystems.Elevator;
import org.troyargonauts.robot.subsystems.Wrist;

public class FloorPickup extends SequentialCommandGroup {
    public FloorPickup() {
        // TODO: Add your sequential commands in the super() call, e.g.
        //           super(new OpenClawCommand(), new MoveArmCommand());
        super(
                new InstantCommand(() -> Robot.getArm().setDesiredTarget(Arm.ArmState.FLOOR_PICKUP), Robot.getArm()),
                new InstantCommand(() -> Robot.getElevator().setDesiredTarget(Elevator.ElevatorState.HOME), Robot.getElevator()),
                new InstantCommand(() -> Robot.getWrist().setDesiredTarget(Wrist.WristState.FLOOR_PICKUP), Robot.getWrist())
        );
    }
}