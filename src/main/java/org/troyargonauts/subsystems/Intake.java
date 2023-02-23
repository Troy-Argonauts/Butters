package org.troyargonauts.subsystems;

import edu.wpi.first.wpilibj.DoubleSolenoid;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import org.troyargonauts.Constants.IntakeConstants;

import com.revrobotics.CANSparkMax;
import com.revrobotics.CANSparkMaxLowLevel.MotorType;

import edu.wpi.first.wpilibj2.command.SubsystemBase;

import static org.troyargonauts.subsystems.PneumaticsSystem.*;

public class Intake extends SubsystemBase {

    private static CANSparkMax intakeMotor;
    private boolean intakeState;
    public Intake(){
        intakeMotor = new CANSparkMax(0,MotorType.kBrushless);
    }
    public enum intakeStates{
        OPEN, CLOSED, STOPPED
    }

    public static void setIntakeState(intakeStates state) {
        switch (state) {
            case OPEN:
                intakeMotor.set(0.2);
                break;
            case CLOSED:
                intakeMotor.set(-0.2);
                break;
            case STOPPED:
                intakeMotor.set(0.0);
                break;
        }
    }

    @Override
    public void periodic(){
        SmartDashboard.putBoolean("Back Intake State", intakeState);
    }
}