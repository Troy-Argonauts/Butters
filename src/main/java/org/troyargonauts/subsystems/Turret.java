package org.troyargonauts.subsystems;

import com.revrobotics.*;
import edu.wpi.first.math.controller.PIDController;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.PIDCommand;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import org.troyargonauts.Constants;
import org.troyargonauts.Robot;

/**
 * Turret Code
 *
 * @author Ashwin Shrivastav, Isaac Hatfield, Teodor Topan, Rohan Gajula-Ghosh
 */

public class Turret extends SubsystemBase {
    private final CANSparkMax turretMotor;
//    public final SparkMaxLimitSwitch rightLimitSwitch;
//    public final SparkMaxLimitSwitch leftLimitSwitch;
    private PIDController pid;

    public boolean leftLimitSwitchIsActive;
    public boolean rightLimitSwitchIsActive;

    public double potentiometerValue;


    /**
     * Constructor for Turret Class. Instantiate motors, magnetic limit switches, PID Controllers, and potentiometer.
     */
    public Turret() {
        turretMotor = new CANSparkMax(Constants.Turret.PORT, CANSparkMaxLowLevel.MotorType.kBrushless);
//        rightLimitSwitch = turretMotor.getForwardLimitSwitch(SparkMaxLimitSwitch.Type.kNormallyOpen);
//        leftLimitSwitch = turretMotor.getReverseLimitSwitch(SparkMaxLimitSwitch.Type.kNormallyOpen);

        pid = new PIDController(Constants.Turret.kP, Constants.Turret.kI ,Constants.Turret.kD, Constants.Turret.PERIOD);
    }

    /**
     * Sets motor to given power. Doesn't allow turret to go past the two magnetic limit switches.
     * @param power Power of motors.
     *
     */
    public void setPower(double power, double nerf){
        turretMotor.set(power * nerf);
    }

    /**
     * Periodic will constantly check the encoder and limit switch values.
     * These values will later be used to make sure turrets reach positions and stay within limits.
     */
    @Override
    public void periodic(){
        SmartDashboard.putNumber("Motor Rotations", turretMotor.getEncoder().getPosition());
        SmartDashboard.putNumber("Turret Rotations", (turretMotor.getEncoder().getPosition() / 125));
    }

    /**
     * Gives status of left limit switch as a boolean value.
     * @return Status of left limit switch.
     */

//    public boolean getLeftLimitSwitchState(){
////        return leftLimitSwitch.isLimitSwitchEnabled();
//    }
//
//    /**
//     * Gives status of right limit switch as a boolean value.
//     * @return Status of right limit switch.
//     */
//
//    public boolean getRightLimitSwitchState() {
////        return rightLimitSwitch.isLimitSwitchEnabled();
//    }

    /**
     * Using a PID command, turret will rotate to a setpoint using the PID Controller. Will mainly be used in autonomous.
     * @param setAngle Point that turret wants to reach.
     */
    public void turretPID(double setAngle){
        new PIDCommand (
            pid,
            () -> turretMotor.getEncoder().getPosition(),
            setAngle,
            output -> setPower(output, 0.5),
            Robot.getTurret()
        );
    }
}

