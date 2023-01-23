package org.troyargonauts.subsystems;

import com.revrobotics.CANSparkMax;
import com.revrobotics.CANSparkMaxLowLevel;
import com.revrobotics.SparkMaxLimitSwitch;
import edu.wpi.first.wpilibj2.command.SubsystemBase;

public class Turret extends SubsystemBase {
    private final CANSparkMax turretMotor;
    private boolean active;
    public final SparkMaxLimitSwitch rightLimitSwitch;
    public final SparkMaxLimitSwitch leftLimitSwitch;

    public Turret() {
        turretMotor = new CANSparkMax(0, CANSparkMaxLowLevel.MotorType.kBrushless);
        rightLimitSwitch = turretMotor.getForwardLimitSwitch(SparkMaxLimitSwitch.Type.kNormallyOpen);
        leftLimitSwitch = turretMotor.getReverseLimitSwitch(SparkMaxLimitSwitch.Type.kNormallyOpen);
    }

    public void setPower(double power){
        if ((leftLimitSwitch.isPressed() && power < 0) || (rightLimitSwitch.isPressed() && power > 0)) {
            turretMotor.set(0);
        }
        else {
            turretMotor.set(power);
        }
    }

    public boolean getRightLimitSwitch(){
        return rightLimitSwitch.isPressed();
    }

    public boolean getLeftLimitSwitch() {
        return leftLimitSwitch.isPressed();
    }
}
