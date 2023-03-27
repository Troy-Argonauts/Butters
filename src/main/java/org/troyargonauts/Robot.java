// Copyright (c) FIRST and other WPILib contributors.

// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package org.troyargonauts;

import org.troyargonauts.subsystems.*;

import edu.wpi.first.wpilibj.TimedRobot;
import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.CommandScheduler;
import edu.wpi.first.wpilibj2.command.WaitCommand;

/**
 * The VM is configured to automatically run this class, and to call the methods corresponding to
 * each mode, as described in the TimedRobot documentation. If you change the name of this class or
 * the package after creating this project, you must also update the build.gradle file in the
 * project.
 */
public class Robot extends TimedRobot {
    private Command autonomousCommand;
    
    private static RobotContainer robotContainer;

    private static Arm arm;

    private final SendableChooser<Command> chooser = new SendableChooser<>();

    @Override
    public void robotInit() {
        // Instantiate our RobotContainer.  This will perform all our button bindings, and put our
        // autonomous chooser on the dashboard

        arm = new Arm();
        robotContainer = new RobotContainer();

        SmartDashboard.putData(chooser);
        chooser.setDefaultOption("Wait", new WaitCommand(0));
    }

    @Override
    public void robotPeriodic()
    {
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
    public void teleopPeriodic() {}

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
    }

    public static RobotContainer getRobotContainer() {
        if (robotContainer == null) {
            robotContainer = new RobotContainer();
        }
        return robotContainer;
    }
}
