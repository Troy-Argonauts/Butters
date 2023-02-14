package QuinticPathFollower;

public class Polynomial {
    
    private double values[][];

    public Polynomial(double values[][]) {
        this.values = values;
    }

    public double interpolation(double value) {
        double sum = 0;
        for (int i = 0; i < 6; i++) {
            double partsum = 1;
            for (int j = 0; j < 6; j++) {
                if (i != j) {
                    partsum *= (value - values[j][0]) / (values[i][0] - values[j][0]);
                }
            }
            sum += partsum * values[i][1];
        }
        return sum;
    }

    public double derivative(double value) {
        return (interpolation(value + 0.0001) - interpolation(value)) / 0.0001;
    }

}
