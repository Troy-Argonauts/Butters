package org.troyargonauts.robot.commands;


import edu.wpi.first.wpilibj2.command.InstantCommand;
import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;
import edu.wpi.first.wpilibj2.command.WaitCommand;
import edu.wpi.first.wpilibj2.command.WaitUntilCommand;
import org.troyargonauts.robot.Robot;
import org.troyargonauts.robot.subsystems.Arm;
import org.troyargonauts.robot.subsystems.Elevator;
import org.troyargonauts.robot.subsystems.Wrist;

public class HomePosition extends SequentialCommandGroup {
    public HomePosition() {
        // TODO: Add your sequential commands in the super() call, e.g.
        //           super(new OpenClawCommand(), new MoveArmCommand());
        super(
                new InstantCommand(() -> Robot.getArm().setDesiredTarget(Arm.ArmState.HOME), Robot.getArm()),
                new WaitCommand(0.3),
                new InstantCommand(() -> Robot.getWrist().setDesiredTarget(Wrist.WristState.HOME), Robot.getWrist()),
                new WaitUntilCommand(() -> Robot.getWrist().getDownLimitSwitch()),
                new InstantCommand(() -> Robot.getElevator().setDesiredTarget(Elevator.ElevatorState.HOME), Robot.getElevator())
        );
    }
}