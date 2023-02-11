// Copyright (c) FIRST and other WPILib contributors.

// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package org.troyargonauts;

import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import org.troyargonauts.subsystems.Limelight;
import org.troyargonauts.subsystems.PneumaticsSystem;

import edu.wpi.first.wpilibj.TimedRobot;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.CommandScheduler;
import com.ctre.phoenix.sensors.Pigeon2;
import com.ctre.phoenix.sensors.Pigeon2Configuration;
import com.ctre.phoenix.sensors.WPI_Pigeon2;

import edu.wpi.first.wpilibj.TimedRobot;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;


/**
 * The VM is configured to automatically run this class, and to call the methods corresponding to
 * each mode, as described in the TimedRobot documentation. If you change the name of this class or
 * the package after creating this project, you must also update the build.gradle file in the
 * project.
 */
public class Robot extends TimedRobot {
    private Command autonomousCommand;
    
    private RobotContainer robotContainer;

    static PneumaticsSystem pneumatics;

    private static Limelight limelight;

    private Pigeon2 pigeon;
    private WPI_Pigeon2 pigeon_wpi;



    @Override
    public void robotInit() {
        // Instantiate our RobotContainer.  This will perform all our button bindings, and put our
        // autonomous chooser on the dashboard.
        robotContainer = new RobotContainer();

        pneumatics = new PneumaticsSystem();

        limelight = Limelight.getInstance();

        limelight.setLedMode(Limelight.LightMode.ON);
        limelight.setCameraMode(Limelight.CameraMode.VISION);

        pigeon= new Pigeon2(11);


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
        CommandScheduler.getInstance().run();
        SmartDashboard.putNumber("Limelight Distance", limelight.getTy());
        System.out.println("Pigeon yaw: " + pigeon.getYaw());
        System.out.println("Pigeon pitch: " + pigeon.getPitch());
        System.out.println("Pigeon roll: " + pigeon.getRoll());
        SmartDashboard.putNumber("Pigeon yaw", pigeon.getYaw());
        SmartDashboard.putNumber("Pigeon pitch", pigeon.getPitch());
        SmartDashboard.putNumber("Pigeon roll", pigeon.getRoll());
    }

    @Override
    public void disabledInit() {}

    @Override
    public void disabledPeriodic() {}
    
    @Override
    public void autonomousInit()
    {
        autonomousCommand = robotContainer.getAutonomousCommand();
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

    public static PneumaticsSystem getPneumatics() {
        if (pneumatics == null) {
            pneumatics = new PneumaticsSystem();
        }
        return pneumatics;
    }
}
