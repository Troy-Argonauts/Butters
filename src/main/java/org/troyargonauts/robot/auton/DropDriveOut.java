package org.troyargonauts.robot.auton;


import edu.wpi.first.wpilibj2.command.InstantCommand;
import edu.wpi.first.wpilibj2.command.RunCommand;
import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;
import org.troyargonauts.robot.Robot;
import org.troyargonauts.robot.subsystems.Intake;

public class DropDriveOut extends SequentialCommandGroup {
    public DropDriveOut() {
        // TODO: Add your sequential commands in the super() call, e.g.
        //           super(new OpenClawCommand(), new MoveArmCommand());
        super(
                new InstantCommand(() -> Robot.getIntake().setRotateIntakeState(Intake.rotateStates.DOWN), Robot.getIntake()).withTimeout(1),
                new InstantCommand(() -> Robot.getIntake().setRotateIntakeState(Intake.rotateStates.STOP), Robot.getIntake()),
                new InstantCommand(() -> Robot.getIntake().setSqueezeIntakeState(Intake.squeezeStates.OPEN), Robot.getIntake()).withTimeout(1),
                new InstantCommand(() -> Robot.getIntake().setSqueezeIntakeState(Intake.squeezeStates.STOP), Robot.getIntake()),
                Robot.getDrivetrain().drivePID(-140)
        );
    }
}