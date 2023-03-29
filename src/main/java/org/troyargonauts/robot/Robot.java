// Copyright (c) FIRST and other WPILib contributors.

// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package org.troyargonauts.robot;

import edu.wpi.first.cameraserver.CameraServer;
import edu.wpi.first.wpilibj.TimedRobot;
import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.CommandScheduler;
import org.troyargonauts.robot.auton.DriveHybrid;
import org.troyargonauts.robot.auton.ScoreBalance;
import org.troyargonauts.robot.subsystems.*;
import org.troyargonauts.robot.subsystems.Arm;

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
    private final SendableChooser<Command> chooser = new SendableChooser<>();


    private static LEDSystem led;

    @Override
    public void robotInit() {
        // Instantiate our RobotContainer.  This will perform all our button bindings, and put our
        driveTrain = new DriveTrain();
        arm = new Arm();
        elevator = new Elevator();
        led = new LEDSystem();
        robotContainer = new RobotContainer();

        // autonomous chooser on the dashboard.
        driveTrain.resetEncoders();
        SmartDashboard.putData("Autonomous modes", chooser);
        chooser.addOption("Drive Hybrid Score", new DriveHybrid());
        chooser.addOption("Score and Balance", new ScoreBalance());
        chooser.addOption("Nothing", null);

        CameraServer.startAutomaticCapture();
    }

    @Override
    public void robotPeriodic() {
        SmartDashboard.putNumber("Left Y", RobotContainer.getDriver().getLeftY());
        SmartDashboard.putNumber("Right X", RobotContainer.getDriver().getRightX());
        CommandScheduler.getInstance().run();
    }

    @Override
    public void disabledInit() {
        getDrivetrain().getDualSpeedTransmission().setGear(DualSpeedTransmission.Gear.HIGH);
    }

    @Override
    public void autonomousInit() {
        getDrivetrain().getDualSpeedTransmission().setGear(DualSpeedTransmission.Gear.LOW);
        Robot.getDrivetrain().resetEncoders();
    }

    @Override
    public void teleopInit() {
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
        if (driveTrain == null) {
            driveTrain = new DriveTrain();
        }
        return driveTrain;
    }

    public static LEDSystem getLEDs() {
        if (led == null) {
            led = new LEDSystem();
        }
        return led;
    }

    public static RobotContainer getRobotContainer() {
        if (robotContainer == null) {
            robotContainer = new RobotContainer();
        }
        return robotContainer;
    }

    public static Arm getArm() {
        if (arm == null) {
            arm = new Arm();
        }

        return arm;
    }

    public static Elevator getElevator() {
        if (elevator == null) elevator = new Elevator();
        return elevator;

    }

}
