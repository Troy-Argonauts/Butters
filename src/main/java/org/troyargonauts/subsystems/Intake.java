package org.troyargonauts.subsystems;


import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import org.troyargonauts.Constants;


import com.revrobotics.CANSparkMax;
import com.revrobotics.CANSparkMaxLowLevel.MotorType;

import edu.wpi.first.wpilibj2.command.SubsystemBase;



public class Intake extends SubsystemBase{

    private static CANSparkMax squeezeMotor;
    private static CANSparkMax rotateMotor;
    private static String intakeSqueezeState;
    private static String intakeRotateState;

    public Intake(){
        squeezeMotor=new CANSparkMax(Constants.Intake.SQUEEZE_MOTOR_PORT,MotorType.kBrushless);
        rotateMotor=new CANSparkMax(Constants.Intake.ROTATE_MOTOR_PORT,MotorType.kBrushless);
   }
    public enum squeezeStates{
        OPEN,CLOSED,STOPPED
   }
    public enum rotateStates{
        UP,DOWN,STOPPED
   }

    public static void setSqueezeIntakeState(squeezeStates state){
        switch (state){
            case OPEN:
                squeezeMotor.set(Constants.Intake.SQUEEZE_MOTOR_SPEED);
                intakeSqueezeState="OPEN";
                break;
            case CLOSED:
                squeezeMotor.set(-Constants.Intake.SQUEEZE_MOTOR_SPEED);
                intakeSqueezeState="CLOSED";
                break;
            case STOPPED:
                squeezeMotor.set(0.0);
                intakeSqueezeState="CLOSED";
                break;
       }
   }

    public static void setRotateIntakeState(rotateStates state){
        switch (state){
            case UP:
                rotateMotor.set(Constants.Intake.ROTATE_MOTOR_SPEED);
                intakeRotateState="UP";
                break;
            case DOWN:
                rotateMotor.set(-Constants.Intake.ROTATE_MOTOR_SPEED);
                intakeRotateState="DOWN";
                break;
            case STOPPED:
                rotateMotor.set(0.0);
                intakeRotateState="OPEN";
                break;
       }
   }

    @Override
    public void periodic(){
        SmartDashboard.putString("Intake Squeeze State",intakeSqueezeState);
        SmartDashboard.putString("Intake Squeeze State",intakeRotateState);

   }
}