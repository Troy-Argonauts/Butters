package org.troyargonauts.common.util;

public class Gains {
	private double kP;
	private double kI;
	private double kD;
	private double kF;
	private double tolerance;

	public Gains(double kP, double kI, double kD, double kF, double tolerance) {
		this.kP = kP;
		this.kI = kI;
		this.kD = kD;
		this.kF = kF;
		this.tolerance = tolerance;
	}

	public Gains(double kP, double kI, double kD, double tolerance) {
		this(kP, kI, kD, 0, tolerance);
	}

	public Gains(double kP, double kI, double kD) {
		this(kP, kI, kD, 0);
	}

	public double getP() {
		return kP;
	}

	public double getI() {
		return kI;
	}

	public double getD() {
		return kD;
	}

	public double getF() {
		return kF;
	}

	public double getTolerance() {
		return tolerance;
	}
}
