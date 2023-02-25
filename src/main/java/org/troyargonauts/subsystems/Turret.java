package org.troyargonauts.subsystems;

import com.revrobotics.*;
import edu.wpi.first.math.controller.PIDController;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.PIDCommand;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import org.troyargonauts.Constants;
import org.troyargonauts.Robot;

/**
 * Class representing the Turret. Includes PID control and limit switches.
 *
 * @author ASH-will-WIN, Aizakkuno, TeoElRey, SolidityContract
 */

public class Turret extends SubsystemBase {
    private final CANSparkMax turretMotor;
    public double encoderPosition;
    public final SparkMaxLimitSwitch rightLimitSwitch;
    public final SparkMaxLimitSwitch leftLimitSwitch;
    private PIDController pid;





    /**
     * Constructor for Turret Class. Instantiates motor, magnetic limit switches, PID Controller, and encoder.
     */
    public Turret() {
        turretMotor = new CANSparkMax(Constants.Turret.PORT, CANSparkMaxLowLevel.MotorType.kBrushless);
        rightLimitSwitch = turretMotor.getForwardLimitSwitch(SparkMaxLimitSwitch.Type.kNormallyOpen);
        leftLimitSwitch = turretMotor.getReverseLimitSwitch(SparkMaxLimitSwitch.Type.kNormallyOpen);

        pid = new PIDController(Constants.Turret.kP, Constants.Turret.kI ,Constants.Turret.kD, Constants.Turret.PERIOD);
    }

    /**
     * This sets motor to given power. Will not allow turret to go past the two magnetic limit switches.
     * @param power Desired motor power.
     * @param nerf Percentage nerf of motor power
     **/
    public void setPower(double power, double nerf){
//        if ((leftLimitSwitch.isLimitSwitchEnabled() && power < 0) || (rightLimitSwitch.isLimitSwitchEnabled() && power > 0)) {
//            turretMotor.set(0);
//        }
//        else {
//            turretMotor.set(power * nerf);
//        }


        turretMotor.set(power * nerf);
    }

    public void resetEncoders(){
        turretMotor.getEncoder().setPosition(0);
    }



    /**
     * Periodic will constantly check the encoder and limit switch values.
     * These values will later be used to make sure turrets reach positions and stay within limits.
     */
    @Override
    public void periodic(){
        SmartDashboard.putNumber("Position", encoderPosition);
        SmartDashboard.putNumber("Motor Rotations", turretMotor.getEncoder().getPosition());
        SmartDashboard.putNumber("Turret Rotations", (turretMotor.getEncoder().getPosition() / 125));
        SmartDashboard.putBoolean("Right Limit Switch", rightLimitSwitch.isLimitSwitchEnabled());
        SmartDashboard.putBoolean("Right Limit Switch", leftLimitSwitch.isLimitSwitchEnabled());
        encoderPosition = turretMotor.getEncoder().getPosition();
    }


    /**
     * Turret will rotate to a setpoint using the PID Controller.
     * @param setpoint Setpoint for the Turret
     */
    public PIDCommand turretPID(double setpoint){
        pid.setSetpoint(setpoint);
        return new PIDCommand(
            pid,
            () -> encoderPosition,
            setpoint,
            output -> setPower(output, Constants.Turret.NERF),
            Robot.getTurret()
        );
    }
}

