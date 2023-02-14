package org.troyargonauts.commands;

public class PIDController {
    double kP, kI, kD, tolerence;
    int iterations = 0;
    double prevError = 0;
    double Error = 0;

    double TotalError = 0;
    public PIDController(double kP, double kI, double kD, double tolerence){
        this.kP = kP;
        this.kI = kI;
        this.kD = kD;
        this.tolerence = tolerence;
    }

    public double getOutput(double input, double setpoint){

        Error = setpoint - input;
        double output = kP*Error + kI*TotalError*iterations + kP*(Error - prevError)/iterations;

        if (Error < tolerence){
            iterations = 0;
        } else {
            iterations++;
        }

        TotalError += Error;
        prevError = Error;



        return output;
    }

    public void reset() {
        Error = 0;
        prevError = 0;
        iterations = 0;
    }
}
