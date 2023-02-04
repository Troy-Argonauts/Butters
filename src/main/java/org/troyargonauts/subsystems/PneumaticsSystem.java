package org.troyargonauts.subsystems;

import org.troyargonauts.Constants.*;

import edu.wpi.first.wpilibj.DoubleSolenoid;
import edu.wpi.first.wpilibj.PneumaticsModuleType;
import edu.wpi.first.wpilibj2.command.SubsystemBase;

public class PneumaticsSystem extends SubsystemBase {

    private final DoubleSolenoid manipulatorSolenoid;
    private final DoubleSolenoid alignSolenoid;
    private final DoubleSolenoid grabSolenoid;
    private final DoubleSolenoid rotateSolenoid;

    public enum ManipulatorState {
        GRAB, RELEASE;
    }

    public enum AlignIntakeState {
        ALIGN, UNALIGN;
    }

    public enum GrabIntakeState {
        GRAB, RELEASE;
    }
    public enum RotateIntakeState {
        FLAT, ROTATED;
    }

    public PneumaticsSystem() {
        manipulatorSolenoid = new DoubleSolenoid(PneumaticsModuleType.CTREPCM, Manipulator.kManipulatorForwardChannel, Manipulator.kManipulatorReverseChannel);
        manipulatorSolenoid.set(DoubleSolenoid.Value.kForward);
        
        alignSolenoid = new DoubleSolenoid(PneumaticsModuleType.CTREPCM, IntakeConstants.kAlignForwardChannel, IntakeConstants.kAlignReverseChannel);
        alignSolenoid.set(DoubleSolenoid.Value.kForward);

        grabSolenoid = new DoubleSolenoid(PneumaticsModuleType.CTREPCM, IntakeConstants.kGrabForwardChannel, IntakeConstants.kGrabReverseChannel);
        grabSolenoid.set(DoubleSolenoid.Value.kForward);
        
        rotateSolenoid = new DoubleSolenoid(PneumaticsModuleType.CTREPCM, IntakeConstants.kRotateForwardChannel, IntakeConstants.kRotateReverseChannel);
        rotateSolenoid.set(DoubleSolenoid.Value.kForward);
    }

    public void setManipulatorState(ManipulatorState state) {
        switch (state) {
            case GRAB:
                manipulatorSolenoid.set(DoubleSolenoid.Value.kForward);
                break;
            case RELEASE:
                manipulatorSolenoid.set(DoubleSolenoid.Value.kReverse);
                break;
        }
    }

    public void setAlignState(AlignIntakeState state) {
        switch (state) {
            case ALIGN:
                alignSolenoid.set(DoubleSolenoid.Value.kForward);
                break;
            case UNALIGN:
                alignSolenoid.set(DoubleSolenoid.Value.kReverse);
                break;
        }
    }

    public void setGrabState(GrabIntakeState state) {
        switch (state) {
            case GRAB:
                grabSolenoid.set(DoubleSolenoid.Value.kForward);
                break;
            case RELEASE:
                grabSolenoid.set(DoubleSolenoid.Value.kReverse);
                break;
        }
    }

    public void setRotateState(RotateIntakeState state) {
        switch (state) {
            case FLAT:
                grabSolenoid.set(DoubleSolenoid.Value.kForward);
                break;
            case ROTATED:
                grabSolenoid.set(DoubleSolenoid.Value.kReverse);
                break;
        }
    }
}