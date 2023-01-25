package org.troyargonauts.subsystems;

import com.revrobotics.CANSparkMax;
import com.revrobotics.CANSparkMaxLowLevel;
import com.revrobotics.SparkMaxLimitSwitch;
import edu.wpi.first.math.controller.PIDController;
import edu.wpi.first.wpilibj.AnalogPotentiometer;
import edu.wpi.first.wpilibj2.command.PIDCommand;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import org.troyargonauts.Robot;

/**
 * Turret Code
 *
 * @author Ashwin Shrivsatav, Isaac Hatfield, Teodor Topan
 */

public class Turret extends SubsystemBase {
    private final CANSparkMax turretMotor;
    private boolean active;
    public final SparkMaxLimitSwitch rightLimitSwitch;
    public final SparkMaxLimitSwitch leftLimitSwitch;
    PIDController pid;

    boolean leftLimitSwitchIsActive;
    boolean rightLimitSwitchIsActive;

    public final AnalogPotentiometer potentiometer;

    public double potentiometerValue;


    /**
     * Constructor for Turret Class. Instantiates motors, magnetic limit switches, PID Controllers, and potentiometer.
     */
    public Turret() {
        turretMotor = new CANSparkMax(0, CANSparkMaxLowLevel.MotorType.kBrushless);
        rightLimitSwitch = turretMotor.getForwardLimitSwitch(SparkMaxLimitSwitch.Type.kNormallyOpen);
        leftLimitSwitch = turretMotor.getReverseLimitSwitch(SparkMaxLimitSwitch.Type.kNormallyOpen);

        pid = new PIDController(1, 0, 0, 0.2);
        potentiometer = new AnalogPotentiometer(0, 180, 30);
    }

    /**
     * Sets motor to given power. Doesn't allow turret to go past the two magnetic limit switches.
     * @param power Power of motors.
     *
     */
    public void setPower(double power){
        if ((leftLimitSwitchIsActive && power < 0) || (rightLimitSwitchIsActive && power > 0)) {
            turretMotor.set(0);
        }
        else {
            turretMotor.set(power);
        }
    }

    /**
     * Periodic will constantly check the encoder and limit switch values.
     * These values will later be used to make sure turrets reach positions and stay within limits.
     */
    @Override
    public void periodic(){
        rightLimitSwitchIsActive = rightLimitSwitch.isPressed();
        leftLimitSwitchIsActive = leftLimitSwitch.isPressed();
        potentiometerValue = potentiometer.get();
    }

    /**
     * Gives status of left limit switch as a boolean value.
     * @return Status of left limit switch.
     */
    public boolean getLeftLimitSwitch(){
        return leftLimitSwitchIsActive;
    }

    /**
     * Gives status of right limit switch as a boolean value.
     * @return Status of right limit switch.
     */
    public boolean getRightLimitSwitch() {
        return rightLimitSwitchIsActive;
    }

    /**
     * Using a PID command, turret will rotate to a setpoint using the PID Controller. Will mainly be used in autonomous.
     * @param setPoint Point that turret wants to reach.
     */
    public void turretPID(double setPoint){
        new PIDCommand (
            pid,
            () -> turretMotor.getEncoder().getPosition(),
            setPoint,
            this::setPower,
            Robot.getTurret()
        );
    }
}

