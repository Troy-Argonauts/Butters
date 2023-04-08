package org.troyargonauts.robot.commands;


import edu.wpi.first.wpilibj2.command.InstantCommand;
import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;
import edu.wpi.first.wpilibj2.command.WaitCommand;
import edu.wpi.first.wpilibj2.command.WaitUntilCommand;
import org.troyargonauts.robot.Robot;
import org.troyargonauts.robot.subsystems.Arm;
import org.troyargonauts.robot.subsystems.Elevator;
import org.troyargonauts.robot.subsystems.Wrist;

public class HumanPlayer extends SequentialCommandGroup {
    public HumanPlayer() {
        // TODO: Add your sequential commands in the super() call, e.g.
        //           super(new OpenClawCommand(), new MoveArmCommand());
        super(
                new InstantCommand(() -> System.out.println("Human Player")),
                new InstantCommand(() -> Robot.getArm().setDesiredTarget(Arm.ArmState.HUMAN_PLAYER), Robot.getArm()),
                new WaitCommand(0.5),
                new InstantCommand(() -> Robot.getElevator().setDesiredTarget(Elevator.ElevatorState.HUMAN_PLAYER), Robot.getElevator()),
                new WaitUntilCommand(Robot.getElevator()::isPIDFinished),
                new InstantCommand(() -> Robot.getWrist().setDesiredTarget(Wrist.WristState.HUMAN_PLAYER), Robot.getWrist())
        );
    }
}