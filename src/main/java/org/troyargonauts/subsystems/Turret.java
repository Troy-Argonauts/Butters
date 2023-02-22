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
    public double turretPosition;
    public final SparkMaxLimitSwitch rightLimitSwitch;
    public final SparkMaxLimitSwitch leftLimitSwitch;
    private PIDController pid;

    public boolean leftLimitSwitchIsActive;
    public boolean rightLimitSwitchIsActive;





    /**
     * Constructor for Turret Class. Instantiates motor, magnetic limit switches, PID Controllers, and encoders.
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
     * @param nerf Nerf of motor power.
     */
    public void setPower(double power, double nerf){
//        if ((getLeftLimitSwitchState() && power < 0) || (getRightLimitSwitchState() && power > 0)) {
//            turretMotor.set(0);
//        }
//        else {
//            turretMotor.set(power * nerf);
//        }


        turretMotor.set(power * nerf);
    }

    /**
     * Periodic will constantly check the encoder and limit switch values.
     * These values will later be used to make sure turrets reach positions and stay within limits.
     */
    @Override
    public void periodic(){
        SmartDashboard.putNumber("Position", turretPosition);
        SmartDashboard.putNumber("Motor Rotations", turretMotor.getEncoder().getPosition());
        SmartDashboard.putNumber("Turret Rotations", (turretMotor.getEncoder().getPosition() / 125));
        turretPosition = turretMotor.getEncoder().getPosition();
    }

    /**
     * Gives status of left limit switch as a boolean value.
     * @return Status of left limit switch.
     */

    public boolean getLeftLimitSwitchState(){
        return leftLimitSwitch.isLimitSwitchEnabled();
    }

    /**
     * Gives status of right limit switch as a boolean value.
     * @return Status of right limit switch.
     */

    public boolean getRightLimitSwitchState() {
        return rightLimitSwitch.isLimitSwitchEnabled();
    }


    /**
     * Using a PID command, turret will rotate to a setpoint using the PID Controller. 
     * @param setpoint Setpoint for the Turret
     */
    public void turretPID(double setpoint){
        pid.setSetpoint(setpoint);
        new PIDCommand(
            pid,
            () -> turretPosition,
            () -> turretPosition,
            setpoint,
            output -> setPower(output, 0.5),
            Robot.getTurret()
        );
    }
}

