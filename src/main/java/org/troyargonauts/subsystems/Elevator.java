package org.troyargonauts.subsystems;
import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.can.TalonFX;
import com.ctre.phoenix.motorcontrol.can.TalonFXConfiguration;
import com.revrobotics.CANSparkMax;
import com.revrobotics.CANSparkMaxLowLevel;
import edu.wpi.first.wpilibj.CAN;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import com.revrobotics.SparkMaxLimitSwitch;

public class Elevator extends SubsystemBase {
    private CANSparkMax leftElevatorMotor;
    private CANSparkMax rightElevatorMotor;
    private SparkMaxLimitSwitch elevatorLimitSwitch;

    public Elevator() {
        leftElevatorMotor = new CANSparkMax(0, CANSparkMaxLowLevel.MotorType.kBrushless);
        rightElevatorMotor = new CANSparkMax(1, CANSparkMaxLowLevel.MotorType.kBrushless);

        elevatorLimitSwitch = leftElevatorMotor.getForwardLimitSwitch(SparkMaxLimitSwitch.Type.kNormallyClosed);
        elevatorLimitSwitch = rightElevatorMotor.getForwardLimitSwitch(SparkMaxLimitSwitch.Type.kNormallyClosed);

        rightElevatorMotor.setInverted(true);
    }

    public boolean getLimitSwitchState() {
        return elevatorLimitSwitch.isLimitSwitchEnabled();
    }

    public void setElevatorPower(double speed) {
        leftElevatorMotor.set(speed);
        rightElevatorMotor.set(speed);
    }
}
