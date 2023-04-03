// Copyright (c) FIRST and other WPILib contributors.

// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package org.troyargonauts.robot;

import edu.wpi.first.wpilibj.DataLogManager;
import edu.wpi.first.wpilibj.TimedRobot;
import edu.wpi.first.wpilibj.livewindow.LiveWindow;
import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.CommandScheduler;
import edu.wpi.first.wpilibj2.command.WaitCommand;
import org.troyargonauts.robot.auton.HomingSequence;
import org.troyargonauts.robot.subsystems.*;

/**
 * The VM is configured to automatically run this class, and to call the methods corresponding to
 * each mode, as described in the TimedRobot documentation. If you change the name of this class or
 * the package after creating this project, you must also update the build.gradle file in the
 * project.
 */
public class Robot extends TimedRobot {
    private static DriveTrain driveTrain;
    private static Arm arm;
    private static Elevator elevator;
    private static Wrist wrist;
    private static LEDSystem ledSystem;
    private final SendableChooser<Command> chooser = new SendableChooser<>();
    private boolean hasLimitBeenPressed = false;
    private Command autonomousCommand;

    public static LEDSystem getLEDSystem() {
        if (ledSystem == null) ledSystem = new LEDSystem();
        return ledSystem;
    }

    @Override
    public void robotInit() {
        LiveWindow.disableAllTelemetry();
        LiveWindow.setEnabled(false);

        DataLogManager.start("/media/sda1/logs");

        driveTrain = new DriveTrain();
        arm = new Arm();
        elevator = new Elevator();
        wrist = new Wrist();
        //ledSystem = new LEDSystem();

        resetAllEncoders();

        SmartDashboard.putData("Autonomous modes", chooser);
        chooser.setDefaultOption("Nothing", new WaitCommand(15));
        chooser.setDefaultOption("Homing Sequence", new HomingSequence());
    }

    @Override
    public void robotPeriodic() {
        elevator.run();
        wrist.run();

        if (arm.isHomeLimitPressed()) {
            hasLimitBeenPressed = true;
        }

        if (hasLimitBeenPressed) {
            arm.run();
        }
        CommandScheduler.getInstance().run();
    }

    @Override
    public void disabledInit() {
        getDrivetrain().getDualSpeedTransmission().setGear(DualSpeedTransmission.Gear.HIGH);
        arm.setDesiredTarget(Arm.ArmState.HOME);
        wrist.setDesiredTarget(Wrist.WristState.HOME);
        elevator.setDesiredTarget(Elevator.ElevatorState.HOME);
    }

    @Override
    public void autonomousInit() {
        getDrivetrain().getDualSpeedTransmission().setGear(DualSpeedTransmission.Gear.LOW);
        resetAllEncoders();

        autonomousCommand = chooser.getSelected();
        if (autonomousCommand != null) {
            autonomousCommand.schedule();
        }
    }

    @Override
    public void testInit() {
        // Cancels all running commands at the start of test mode.
        CommandScheduler.getInstance().cancelAll();
    }


    /**
     * Returns driveTrain object
     *
     * @return DriveTrain object instantiated in Robot class
     */
    public static DriveTrain getDrivetrain() {
        if (driveTrain == null) driveTrain = new DriveTrain();
        return driveTrain;
    }

    public static Arm getArm() {
        if (arm == null) arm = new Arm();
        return arm;
    }

    public static Wrist getWrist() {
        if (wrist == null) wrist = new Wrist();
        return wrist;
    }

    public static Elevator getElevator() {
        if (elevator == null) elevator = new Elevator();
        return elevator;
    }

    @Override
    public void teleopInit() {
        if (autonomousCommand != null) {
            autonomousCommand.cancel();
        }
        new RobotContainer();
        getDrivetrain().getDualSpeedTransmission().setGear(DualSpeedTransmission.Gear.LOW);
    }

    public void resetAllEncoders() {
        driveTrain.resetEncoders();
        elevator.resetEncoders();
        arm.resetEncoders();
        wrist.resetEncoders();
    }
}
