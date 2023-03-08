package org.troyargonauts.robot.subsystems;

import com.revrobotics.CANSparkMax;
import com.revrobotics.CANSparkMaxLowLevel;
import edu.wpi.first.math.controller.PIDController;
import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj2.command.PIDCommand;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import org.troyargonauts.common.util.motorcontrol.LazyCANSparkMax;
import org.troyargonauts.robot.Constants;
import org.troyargonauts.robot.Robot;

/**
 * Class representing the Turret. Includes PID control and limit switches.
 *
 * @author ASH-will-WIN, Aizakkuno, TeoElRey, SolidityContract
 */

public class Turret extends SubsystemBase {
    private final LazyCANSparkMax turretMotor;
    public double encoderPosition, turretSetpoint;
    public final DigitalInput rightLimitSwitch, leftLimitSwitch;
    private PIDController pid;


    /**
     * Constructor for Turret Class. Instantiates motor, magnetic limit switches, PID Controller, and encoder.
     */
    public Turret() {
        turretMotor = new LazyCANSparkMax(Constants.Turret.PORT, CANSparkMaxLowLevel.MotorType.kBrushless);
        turretMotor.setIdleMode(CANSparkMax.IdleMode.kBrake);
        rightLimitSwitch = new DigitalInput(Constants.Turret.RIGHT_PORT);
        leftLimitSwitch = new DigitalInput(Constants.Turret.LEFT_PORT);

        pid = new PIDController(Constants.Turret.kP, Constants.Turret.kI ,Constants.Turret.kD, Constants.Turret.PERIOD);
        pid.setTolerance(Constants.Turret.TOLERANCE);
        turretSetpoint = Constants.Turret.DEFAULT_SETPOINT;
    }

    /**
     * This sets motor to given power. Will not allow turret to go past the two magnetic limit switches.
     * @param power Desired motor power.
     **/
    public void setPower(double power){
        if (power > 0) {
            if (!leftLimitSwitch.get()) {
                turretMotor.set(0);
            } else{
                turretMotor.set(power * Constants.Elevator.NERF);
            }
        } else {
            if (!rightLimitSwitch.get()) {
                turretMotor.set(0);
            } else {
                turretMotor.set(power * Constants.Elevator.NERF);

            }
        }
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
        encoderPosition = turretMotor.getEncoder().getPosition();

        if (!leftLimitSwitch.get()) {
            turretMotor.getEncoder().setPosition(36);
        }

//        setPower(pid.calculate(encoderPosition, turretSetpoint));
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
                this::setPower,
            Robot.getTurret()
        );
    }

    public void turretManual(double speed) {
        turretSetpoint += -speed;
    }

    public void setTurretSetpoint(double setpoint) {
        turretSetpoint = setpoint;
    }
}

