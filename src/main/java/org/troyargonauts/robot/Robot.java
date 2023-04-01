// Copyright (c) FIRST and other WPILib contributors.

// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package org.troyargonauts.robot;

import edu.wpi.first.math.controller.PIDController;
import edu.wpi.first.wpilibj.TimedRobot;
import edu.wpi.first.wpilibj.livewindow.LiveWindow;
import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.CommandScheduler;
import edu.wpi.first.wpilibj2.command.InstantCommand;
import edu.wpi.first.wpilibj2.command.WaitCommand;
import org.troyargonauts.robot.subsystems.*;
import org.troyargonauts.robot.subsystems.Arm;

import java.beans.beancontext.BeanContextServiceAvailableEvent;

/**
 * The VM is configured to automatically run this class, and to call the methods corresponding to
 * each mode, as described in the TimedRobot documentation. If you change the name of this class or
 * the package after creating this project, you must also update the build.gradle file in the
 * project.
 */
public class Robot extends TimedRobot {
    private static RobotContainer robotContainer;
    private static DriveTrain driveTrain;
    private static Arm arm;
    private static Elevator elevator;
    private static Wrist wrist;
    private final SendableChooser<Command> chooser = new SendableChooser<>();
    private static LEDSystem led;
    private Command autonomousCommand;

    @Override
    public void robotInit() {
        LiveWindow.disableAllTelemetry();
        LiveWindow.setEnabled(false);

        driveTrain = new DriveTrain();
        arm = new Arm();
        elevator = new Elevator();
        led = new LEDSystem();
        wrist = new Wrist();
        robotContainer = new RobotContainer();

        driveTrain.resetEncoders();
        SmartDashboard.putData("Autonomous modes", chooser);
        chooser.setDefaultOption("Nothing", new WaitCommand(15));
        chooser.addOption("Wrist PID", new InstantCommand(() -> Robot.getWrist().setDesiredTarget(303), Robot.getWrist()));
        chooser.addOption("Arm PID", new InstantCommand(() -> Robot.getArm().setDesiredTarget(-2000), Robot.getArm()));

        elevator.resetEncoders();
        arm.resetEncoders();
        wrist.resetEncoders();
        SmartDashboard.putNumber("wrist p", Constants.Wrist.WRIST_GAINS[0]);
        SmartDashboard.putNumber("wrist i", Constants.Wrist.WRIST_GAINS[1]);
        SmartDashboard.putNumber("wrist d", Constants.Wrist.WRIST_GAINS[2]);
//        CameraServer.startAutomaticCapture();
    }

    @Override
    public void robotPeriodic() {
        wrist.run();
//        elevator.run();
        arm.run();

        Robot.getWrist().wristPID = new PIDController(Constants.Wrist.WRIST_GAINS[0], Constants.Wrist.WRIST_GAINS[1], Constants.Wrist.WRIST_GAINS[2]);


//        SmartDashboard.putNumber("Left Y", RobotContainer.getDriver().getLeftY());
//        SmartDashboard.putNumber("Right X", RobotContainer.getDriver().getRightX());
        CommandScheduler.getInstance().run();
    }

    @Override
    public void disabledInit() {
        getDrivetrain().getDualSpeedTransmission().setGear(DualSpeedTransmission.Gear.HIGH);
        //arm.setDesiredTarget(0);
        wrist.setDesiredTarget(0);
        //elevator.setDesiredTarget(0);
    }

    @Override
    public void autonomousInit() {
        getDrivetrain().getDualSpeedTransmission().setGear(DualSpeedTransmission.Gear.LOW);
        Robot.getDrivetrain().resetEncoders();

        autonomousCommand = chooser.getSelected();
        if (autonomousCommand != null) {
            autonomousCommand.schedule();
        }
    }

    @Override
    public void teleopInit() {
        if (autonomousCommand != null) {
            autonomousCommand.cancel();
        }
        getDrivetrain().getDualSpeedTransmission().setGear(DualSpeedTransmission.Gear.LOW);
    }


    @Override
    public void teleopPeriodic() {
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

    public static LEDSystem getLEDs() {
        if (led == null) led = new LEDSystem();
        return led;
    }

    public static RobotContainer getRobotContainer() {
        if (robotContainer == null) robotContainer = new RobotContainer();
        return robotContainer;
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



}
