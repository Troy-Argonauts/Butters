package org.troyargonauts.subsystems;
import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.can.TalonFX;
import com.ctre.phoenix.motorcontrol.can.TalonFXConfiguration;
import com.revrobotics.CANSparkMax;
import com.revrobotics.CANSparkMaxLowLevel;
import edu.wpi.first.math.controller.PIDController;
import edu.wpi.first.wpilibj.CAN;
import edu.wpi.first.wpilibj2.command.PIDCommand;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import com.revrobotics.SparkMaxLimitSwitch;
import org.troyargonauts.Robot;

public class Elevator extends SubsystemBase {
    private CANSparkMax leftMotor;
    private CANSparkMax rightMotor;
    private SparkMaxLimitSwitch upperLimitSwitch;
    private SparkMaxLimitSwitch lowerLimitSwitch;

    PIDController pid;

    public Elevator() {
        leftMotor = new CANSparkMax(0, CANSparkMaxLowLevel.MotorType.kBrushless);
        rightMotor = new CANSparkMax(1, CANSparkMaxLowLevel.MotorType.kBrushless);

        upperLimitSwitch = leftMotor.getForwardLimitSwitch(SparkMaxLimitSwitch.Type.kNormallyClosed);
        upperLimitSwitch = rightMotor.getForwardLimitSwitch(SparkMaxLimitSwitch.Type.kNormallyClosed);

        lowerLimitSwitch = leftMotor.getForwardLimitSwitch(SparkMaxLimitSwitch.Type.kNormallyClosed);
        lowerLimitSwitch = rightMotor.getForwardLimitSwitch(SparkMaxLimitSwitch.Type.kNormallyClosed);

        rightMotor.setInverted(true);

        pid = new PIDController(1, 0 ,0, 0.2);
    }

    public boolean getUpperLimitSwitchState() {
        return upperLimitSwitch.isLimitSwitchEnabled();
    }
    public boolean getLowerLimitSwitchState() {
        return lowerLimitSwitch.isLimitSwitchEnabled();
    }

    public void setElevatorPower(double speed) {
        leftMotor.set(speed);
        rightMotor.set(speed);
    }

    public void elevatorPID(double setpoint) {
        new PIDCommand(
                pid,
                () -> leftMotor.getEncoder().getPosition(),
                setpoint,
                this::setElevatorPower,
                Robot.getElevator()
        );
    }
}
