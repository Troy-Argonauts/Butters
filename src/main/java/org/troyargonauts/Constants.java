package org.troyargonauts;

public final class Constants {

    public interface IntakeConstants {
        int kRightIntakeID = 8;
        int kLeftIntakeID = 7;

        int kAlignForwardChannel = 2;
        int kAlignReverseChannel = 3;

        int kGrabForwardChannel = 4;
        int kGrabReverseChannel = 5;

        int kRotateForwardChannel = 6;
        int kRotateReverseChannel = 7;
    }

    public interface Manipulator {
        int kManipulatorForwardChannel = 0;
        int kManipulatorReverseChannel = 1;
    }

}
