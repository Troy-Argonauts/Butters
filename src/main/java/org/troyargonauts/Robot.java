// Copyright (c) FIRST and other WPILib contributors.

// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package org.troyargonauts;

import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import org.troyargonauts.subsystems.*;
import com.ctre.phoenix.sensors.Pigeon2;
import com.ctre.phoenix.sensors.Pigeon2Configuration;

import edu.wpi.first.wpilibj.TimedRobot;
import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.CommandScheduler;
import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import edu.wpi.first.wpilibj2.command.button.POVButton;

import org.troyargonauts.subsystems.*;

/**
 * The VM is configured to automatically run this class, and to call the methods corresponding to
 * each mode, as described in the TimedRobot documentation. If you change the name of this class or
 * the package after creating this project, you must also update the build.gradle file in the
 * project.
 */
public class Robot extends TimedRobot {
    private Command autonomousCommand;
    
    private static RobotContainer robotContainer;

    static DriveTrain driveTrain;

    private final SendableChooser<Command> chooser = new SendableChooser<>();

    static Pigeon2 pigeon;
    private static Arm arm;
    private final SendableChooser<Command> chooser = new SendableChooser<>();

    @Override
    public void robotInit() {
        // Instantiate our RobotContainer.  This will perform all our button bindings, and put our
        // autonomous chooser on the dashboard

        arm = new Arm();
        driveTrain = new DriveTrain();
        robotContainer = new RobotContainer();

        // autonomous chooser on the dashboard.
        SmartDashboard.putData("Autonomous modes", chooser);
        chooser.setDefaultOption("Arm PID", Robot.getArm().armPID(-30));
      
        driveTrain.resetEncoders();
        SmartDashboard.putData("Autonomous modes", chooser);
        chooser.setDefaultOption("Drive PID", getDrivetrain().drivePID(60));
//        chooser.addOption("Turn PID", getDrivetrain().turnPID(90));


        pigeon.configFactoryDefault();
        pigeon.clearStickyFaults();
        final Pigeon2Configuration pigeonConfig = new Pigeon2Configuration();
        pigeonConfig.MountPosePitch = 0;
        pigeonConfig.MountPoseRoll = 0;
        pigeonConfig.MountPoseYaw = 0;
        pigeon.configAllSettings(pigeonConfig);
    }

    @Override
    public void robotPeriodic()
    {
        SmartDashboard.putNumber("Left Y", RobotContainer.getDriver().getLeftJoystickY());
        SmartDashboard.putNumber("Right X", RobotContainer.getDriver().getRightJoystickX());
        CommandScheduler.getInstance().run();
    }

    @Override
    public void disabledInit() {}

    @Override
    public void disabledPeriodic() {}
    
    @Override
    public void autonomousInit()
    {
        autonomousCommand = chooser.getSelected();
        if (autonomousCommand != null)
        {
            autonomousCommand.schedule();
        }
    }
    
    @Override
    public void autonomousPeriodic() {}

    @Override
    public void teleopInit()
    {
        if (autonomousCommand != null)
        {
            autonomousCommand.cancel();
        }
    }
    
    @Override
    public void teleopPeriodic() {
        SmartDashboard.putBoolean("DPAD DOWN", new POVButton(RobotContainer.getDriver(), 180).getAsBoolean());
    }

    @Override
    public void testInit()
    {
        // Cancels all running commands at the start of test mode.
        CommandScheduler.getInstance().cancelAll();
    }

    @Override
    public void testPeriodic() {}

    @Override
    public void simulationInit() {}

    @Override
    public void simulationPeriodic() {}

    public static Arm getArm() {
        if (arm == null) {
            arm = new Arm();
        }
        return arm;
    /** 
     * Returns driveTrain object
     * @return DriveTrain object instantiated in Robot class
     */
    public static DriveTrain getDrivetrain() {
        if (driveTrain == null) {
            driveTrain = new DriveTrain();
        }
        return driveTrain;
    }

    public static RobotContainer getRobotContainer() {
        if (robotContainer == null) {
            robotContainer = new RobotContainer();
        }
        return robotContainer;
    }
}
