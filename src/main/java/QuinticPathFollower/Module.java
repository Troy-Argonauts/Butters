package QuinticPathFollower;

import java.util.ArrayList;
import java.util.List;

public class Module {

    double timeSeconds, velocityMetersPerSecond, accelerationMetersPerSecondSq, curvatureRadPerMeter;
    Position position;
    
    public Module(double timeSeconds, double velocityMetersPerSecond, double accelerationMetersPerSecondSq, Position position, double curvatureRadPerMeter) {
        this.timeSeconds = timeSeconds;
        this.velocityMetersPerSecond = velocityMetersPerSecond;
        this.accelerationMetersPerSecondSq = accelerationMetersPerSecondSq;
        this.position = position;
        this.curvatureRadPerMeter = curvatureRadPerMeter;
    }

    public Module interpolate(Module endValue, double i) {
        double newTime = timeSeconds + (endValue.timeSeconds - timeSeconds) * i;

        double changeTime = newTime - timeSeconds;
  
        if (changeTime < 0) {
          return endValue.interpolate(this, 1 - i);
        }

        double newVelocity = velocityMetersPerSecond + (accelerationMetersPerSecondSq * changeTime);
        double changePositon = (velocityMetersPerSecond * changeTime + 0.5 * accelerationMetersPerSecondSq * Math.pow(changeTime, 2));
        double interpolationFrac = changePositon / endValue.position.getDistance(position);
  
        return new Module(
            changeTime,
            newVelocity,
            accelerationMetersPerSecondSq,
            position.plus((endValue.position.minus(position)).times(interpolationFrac)),
            curvatureRadPerMeter + (endValue.curvatureRadPerMeter - curvatureRadPerMeter) * interpolationFrac
        );
    }

    public static class Path {
        private final double totalTimeSeconds;
        private final List<Module> modules;

        public Path() {
            modules = new ArrayList<>();
            totalTimeSeconds = 0.0;
        }
      
        public Path(List<Module> states) {
            modules = states;
            totalTimeSeconds = modules.get(modules.size() - 1).timeSeconds;
        }
      
        public double getTotalTimeSeconds() {
            return totalTimeSeconds;
        }

        public Module sample(double timeSeconds) {
            if (timeSeconds <= modules.get(0).timeSeconds) {
                return modules.get(0);
            }
            if (timeSeconds >= totalTimeSeconds) {
                return modules.get(modules.size() - 1);
            }

            int low = 1;
            int high = modules.size() - 1;
        
            while (low != high) {
                int mid = (low + high) / 2;
                if (modules.get(mid).timeSeconds < timeSeconds) {
                    low = mid + 1;
                } else {
                    high = mid;
                }
            }

            Module sample = modules.get(low);
            Module prevSample = modules.get(low - 1);

            if (Math.abs(sample.timeSeconds - prevSample.timeSeconds) < 1E-9) {
                return sample;
            }

            return prevSample.interpolate(
                sample,
                (timeSeconds - prevSample.timeSeconds) / (sample.timeSeconds - prevSample.timeSeconds)
            );
        }

        public List<Module> generate(double[][] values) {
            List<Module> m_states = new ArrayList<>();
            Polynomial poly = new Polynomial(values);
            for (double i = values[0][0]; i <= values[5][0]; i += (values[5][0] - values[0][0]) / 100) {
                m_states.add(
                    new Module(
                        0.1 * ((i - values[0][0]) / (values[5][0] - values[0][0]) + 1), 
                        Math.hypot((values[5][0] - values[0][0]) / 100, poly.interpolation(i + (values[5][0] - values[0][0]) / 100) - poly.interpolation(i)) / 0.1, 
                        (Math.hypot((values[5][0] - values[0][0]) / 100, poly.interpolation(i + 2 * (values[5][0] - values[0][0]) / 100) - poly.interpolation(i + (values[5][0] - values[0][0]) / 100)) / 0.1 - Math.hypot((values[5][0] - values[0][0]) / 100, poly.interpolation(i + (values[5][0] - values[0][0]) / 100) - poly.interpolation(i)) / 0.1) / 0.1, 
                        new Position(i, poly.interpolation(i), Math.atan(poly.derivative(i))),
                        (Math.atan(poly.derivative(i + (values[5][0] - values[0][0]) / 100)) - Math.atan(poly.derivative(i))) / 0.1
                    )
                );
                poly.interpolation(i);
            }
            return m_states;
        }
    }

}
