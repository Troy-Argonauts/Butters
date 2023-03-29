package org.troyargonauts.robot.subsystems;

import com.revrobotics.CANSparkMax;
import com.revrobotics.CANSparkMaxLowLevel;
import edu.wpi.first.wpilibj.CAN;
import edu.wpi.first.wpilibj2.command.SubsystemBase;

public class roller extends SubsystemBase {

    CANSparkMax rol, ele, wrist, arm;

    public roller() {
        rol = new CANSparkMax(7, CANSparkMaxLowLevel.MotorType.kBrushless);
        ele = new CANSparkMax(10, CANSparkMaxLowLevel.MotorType.kBrushless);
        wrist = new CANSparkMax(8, CANSparkMaxLowLevel.MotorType.kBrushless);
        arm = new CANSparkMax(9, CANSparkMaxLowLevel.MotorType.kBrushless);

        rol.setIdleMode(CANSparkMax.IdleMode.kBrake);
        ele.setIdleMode(CANSparkMax.IdleMode.kBrake);
        wrist.setIdleMode(CANSparkMax.IdleMode.kBrake);
        arm.setIdleMode(CANSparkMax.IdleMode.kBrake);
    }

    public void setSPeeed(double speed) {
        rol.set(speed);
    }

    public void setarmSPeeed(double speed) {
        arm.set(speed);
    }

    public void setwristSPeeed(double speed) {
        wrist.set(speed);
    }

    public void setEleSpeed(double speed) {
        ele.set(speed);
    }

}
