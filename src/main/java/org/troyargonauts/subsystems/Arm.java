package org.troyargonauts.subsystems;
import com.revrobotics.*;
import edu.wpi.first.math.controller.PIDController;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.PIDCommand;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import org.troyargonauts.Constants;
import org.troyargonauts.Robot;

/**
 * Class representing arm. Includes PID control and absolute encoders
 * @author TeoElRey, ASH-will-WIN, SolidityContract
 */
public class Arm extends SubsystemBase {
    private final CANSparkMax armMotor;
    private final CANSparkMax manipulatorMotor;
//    private final AbsoluteEncoder elbowEncoder;
//    private final AbsoluteEncoder wristEncoder;
    private final PIDController wristPID, armPID;
    private double armEncoderValue;
    private double armSetpoint;

    /**
     * Here, the motors, absolute encoders, and PID Controller are instantiated.
     */
    public Arm() {
        armMotor = new CANSparkMax(Constants.Arm.ELBOW, CANSparkMaxLowLevel.MotorType.kBrushless);
        manipulatorMotor = new CANSparkMax(Constants.Arm.MANIPULATOR, CANSparkMaxLowLevel.MotorType.kBrushless);

        armMotor.setIdleMode(CANSparkMax.IdleMode.kBrake);
        manipulatorMotor.setIdleMode(CANSparkMax.IdleMode.kCoast);

//        elbowEncoder = elbowMotor.getAbsoluteEncoder(SparkMaxAbsoluteEncoder.Type.kDutyCycle);
//        wristEncoder = wristMotor.getAbsoluteEncoder(SparkMaxAbsoluteEncoder.Type.kDutyCycle);

        wristPID = new PIDController(Constants.Arm.WRIST_P, Constants.Arm.WRIST_I, Constants.Arm.WRIST_D, Constants.Arm.WRIST_PERIOD);
        wristPID.setTolerance(Constants.Arm.WRIST_TOLERANCE);

        armPID = new PIDController(Constants.Arm.ARM_P, Constants.Arm.ARM_I, Constants.Arm.ARM_D, Constants.Arm.ARM_PERIOD);
        armPID.setTolerance(Constants.Arm.ARM_TOLERANCE);
        armSetpoint = Constants.Arm.ARM_DEFAULT;

    }
    @Override
    public void periodic() {
        armEncoderValue = armMotor.getEncoder().getPosition();


        setArmPower(armPID.calculate(armEncoderValue, armSetpoint));


        SmartDashboard.putNumber("Arm Encoder", armEncoderValue);
        SmartDashboard.putBoolean("Arm Finished", armPID.atSetpoint());
    }

    /**
     * Enums are the states of the intake rollers (FORWARD, OFF, BACKWARD).
     */
    public enum IntakeState {
        FORWARD, OFF, BACKWARD
    }

    /**
     * Intake roller state is set here.
     * @param state desired state of intake rollers to a motor speed.
     */
    public void setIntakeState(IntakeState state) {
        switch(state) {
            case FORWARD:
                manipulatorMotor.set(Constants.Arm.FORWARD_INTAKE_SPEED);
                break;
            case OFF:
                manipulatorMotor.set(0);
                break;
            case BACKWARD:
                manipulatorMotor.set(Constants.Arm.REVERSE_INTAKE_SPEED);
                break;
        }
    }

    /**
     * Sets Elbow to set speed given it is within encoder limits.
     * @param speed sets elbow motor to desired speed given that it is within the encoder limits.
     */
    public void setArmPower(double speed) {
        armMotor.set(speed * 0.5);
        SmartDashboard.putNumber("Arm Speed", speed);
    }

    public void armTeleOp(double speed) {
        armSetpoint += (speed * 0.5);
    }






    /**
     * Returns elbow position
     * @return returns the position of elbow
     */
    public double getArmPosition() {
        return armEncoderValue;
    }


    public void resetEncoders() {
        armMotor.getEncoder().setPosition(0);
    }

    /**
     * Using a PID command, elbow will rotate to a setpoint using the PID Controller.
     * @param setpoint the point desired to be reached by the elbow.
     */
    public PIDCommand armPID(double setpoint) {
        armPID.setSetpoint(setpoint);
        return new PIDCommand(
                armPID,
                this::getArmPosition,
                setpoint,
                this::setArmPower, 
                Robot.getArm()
        );
    }



}
