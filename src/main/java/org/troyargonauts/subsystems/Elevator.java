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
    }

    @Override
    public void periodic() {
        if(elevatorLimitSwitch.isPressed()) {
           setElevatorPower(0);
        }
        super.periodic();
    }

    public enum setElevatorMotors {
        FORWARD, BACKWARD, STOP
    }

//    public void extendElevator(setElevatorMotors state) {
//        switch (state) {
//            case FORWARD:
//                leftElevatorMotor.set(0.1);
//                rightElevatorMotor.set(0.1);
//                break;
//
//            case BACKWARD:
//                leftElevatorMotor.set(-0.1);
//                rightElevatorMotor.set(-0.1);
//                break;
//
//            case STOP:
//                leftElevatorMotor.set(0);
//                rightElevatorMotor.set(0);
//                break;
//
//        }
//    }

    public void setElevatorPower(double rightJoystick) {
        leftElevatorMotor.set(rightJoystick);
        rightElevatorMotor.set(rightJoystick);
        if(rightJoystick > 0) {
            periodic();
        }
    }
}
