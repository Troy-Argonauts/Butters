package org.troyargonauts.subsystems;


import com.revrobotics.SparkMaxLimitSwitch;
import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import org.troyargonauts.Constants;


import com.revrobotics.CANSparkMax;
import com.revrobotics.CANSparkMaxLowLevel.MotorType;

import edu.wpi.first.wpilibj2.command.SubsystemBase;



public class Intake extends SubsystemBase {

    private static CANSparkMax squeezeMotor;
    private static CANSparkMax rotateMotor;
    private static String intakeSqueezeState;
    private static String intakeRotateState;

    public static SparkMaxLimitSwitch squeezeLimitSwitch;
    public static SparkMaxLimitSwitch rotateLimitSwitch;

    public Intake() {
        squeezeMotor = new CANSparkMax(Constants.Intake.SQUEEZE_MOTOR_PORT, MotorType.kBrushless);
        rotateMotor = new CANSparkMax(Constants.Intake.ROTATE_MOTOR_PORT, MotorType.kBrushless);
        squeezeLimitSwitch = squeezeMotor.getForwardLimitSwitch(SparkMaxLimitSwitch.Type.kNormallyOpen);
        rotateLimitSwitch = rotateMotor.getReverseLimitSwitch(SparkMaxLimitSwitch.Type.kNormallyOpen);

   }

   public void intakeLimitSwitchState() {
        if((squeezeLimitSwitch.isLimitSwitchEnabled() && Constants.Intake.SQUEEZE_MOTOR_SPEED > 0)){
            squeezeMotor.set(0.0);
            intakeSqueezeState = "STOP";
        } else if((rotateLimitSwitch.isLimitSwitchEnabled() && Constants.Intake.ROTATE_MOTOR_SPEED > 0)) {
            rotateMotor.set(0.0);
            intakeRotateState = "STOP";
       }
   }
   public enum squeezeStates {
        OPEN, CLOSE, STOP
   }
   public enum rotateStates {
        UP, DOWN, STOP
   }

    public static void setSqueezeIntakeState(squeezeStates state) {
        switch (state) {
            case OPEN:
                squeezeMotor.set(Constants.Intake.SQUEEZE_MOTOR_SPEED);
                intakeSqueezeState = "OPEN";
                break;
            case CLOSE:
                squeezeMotor.set(-Constants.Intake.SQUEEZE_MOTOR_SPEED);
                intakeSqueezeState = "CLOSE";
                break;
            case STOP:
                squeezeMotor.set(0.0);
                intakeSqueezeState = "STOP";
                break;
       }
   }

    public static void setRotateIntakeState(rotateStates state) {
        switch (state) {
            case UP:
                rotateMotor.set(Constants.Intake.ROTATE_MOTOR_SPEED);
                intakeRotateState = "UP";
                break;
            case DOWN:
                rotateMotor.set(-Constants.Intake.ROTATE_MOTOR_SPEED);
                intakeRotateState = "DOWN";
                break;
            case STOP:
                rotateMotor.set(0.0);
                intakeRotateState = "STOP";
                break;
       }
   }



    @Override
    public void periodic() {
        SmartDashboard.putString("Intake Squeeze State", intakeSqueezeState);
        SmartDashboard.putString("Intake Squeeze State", intakeRotateState);
        SmartDashboard.putBoolean("Squeeze Limit Switch", squeezeLimitSwitch.isLimitSwitchEnabled());
        SmartDashboard.putBoolean("Rotate Limit Switch", rotateLimitSwitch.isLimitSwitchEnabled());

   }
}