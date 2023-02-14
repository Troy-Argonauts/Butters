package QuinticPathFollower;

import java.util.function.DoubleSupplier;
import java.util.function.Supplier;

import org.troyargonauts.Robot;
import org.troyargonauts.Constants.DriveConstants;

import QuinticPathFollower.Module.Path;
import edu.wpi.first.math.controller.PIDController;
import edu.wpi.first.math.controller.SimpleMotorFeedforward;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj2.command.CommandBase;

public class PathGeneratorCommand extends CommandBase {
    private final Timer m_timer = new Timer();
    private final PIDController m_leftController = new PIDController(1, 0, 0, 0);
    private final PIDController m_rightController = new PIDController(1, 0, 0, 0);
    private final Path path;
    private final Supplier<Position> pos;
    private final SimpleMotorFeedforward feedforward;
    private final DoubleSupplier leftSpeed, rightSpeed;
    private double prevLeftSpeed, prevRightSpeed;
    private double prevTime;

    public PathGeneratorCommand(Path path, Supplier<Position> pos, SimpleMotorFeedforward feedforward, DoubleSupplier leftSpeed, DoubleSupplier rightSpeed) {
        this.path = path;
        this.pos = pos;
        this.feedforward = feedforward;
        this.leftSpeed = leftSpeed;
        this.rightSpeed = rightSpeed;
    }

    @Override
    public void initialize() {
        prevTime = -1;
        Module initialState = path.sample(0);
        prevLeftSpeed = initialState.velocityMetersPerSecond - DriveConstants.kTrackwidthMeters / 2 * (initialState.velocityMetersPerSecond * initialState.velocityMetersPerSecond);
        prevRightSpeed = initialState.velocityMetersPerSecond + DriveConstants.kTrackwidthMeters / 2 * (initialState.velocityMetersPerSecond * initialState.velocityMetersPerSecond);
        m_timer.reset();
        m_timer.start();
        m_leftController.reset();
        m_rightController.reset();
    }

    @Override
    public void execute() {
        double curTime = m_timer.get();
        double dt = curTime - prevTime;

        if (prevTime < 0) {
            Robot.getDrivetrain().tankVolts(0, 0);
            prevTime = curTime;
            return;
        }

        double leftSpeedSetpoint = Position.calc(pos.get(), path.sample(curTime), false);
        double rightSpeedSetpoint = Position.calc(pos.get(), path.sample(curTime), true);

        double leftOutput = feedforward.calculate(leftSpeedSetpoint, (leftSpeedSetpoint - prevLeftSpeed) / dt) + m_leftController.calculate(leftSpeed.getAsDouble(), leftSpeedSetpoint);
        double rightOutput = feedforward.calculate(rightSpeedSetpoint, (rightSpeedSetpoint - prevRightSpeed) / dt) + m_rightController.calculate(rightSpeed.getAsDouble(), rightSpeedSetpoint);

        Robot.getDrivetrain().tankVolts(leftOutput, rightOutput);
        prevLeftSpeed = leftSpeedSetpoint;
        prevRightSpeed = rightSpeedSetpoint;

        prevTime = curTime;
    }

    @Override
    public void end(boolean interrupted) {
        m_timer.stop();

        if (interrupted) {
            Robot.getDrivetrain().tankVolts(0, 0);
        }
    }

    @Override
    public boolean isFinished() {
        return m_timer.hasElapsed(path.getTotalTimeSeconds());
    }
}
