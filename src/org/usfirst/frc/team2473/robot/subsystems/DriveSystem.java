package org.usfirst.frc.team2473.robot.subsystems;

import org.usfirst.frc.team2473.framework.Devices;
import org.usfirst.frc.team2473.robot.RobotMap;

import com.kauailabs.navx.frc.AHRS;

import edu.wpi.first.wpilibj.SpeedControllerGroup;
import edu.wpi.first.wpilibj.command.PIDSubsystem;
import edu.wpi.first.wpilibj.drive.DifferentialDrive;

public class DriveSystem extends PIDSubsystem {

	// KP, KI, and KD values used for PID
	// P: 0.018, D: 0.03
	private static final double KP = 0; // 0.0179;
	private static final double KI = 0; // 0
	private static final double KD = 0; // 0.03;

	private DifferentialDrive driver;

	private double rotateToAngleRate; // the value changed by PID

	// private CANTalon frontLeft, frontRight, backLeft, backRight;

	private static final double K_TOLERANCE_DEGREES = 2.0f; // the absolute
															// error that is
															// tolerable in the
															// PID system

	public DriveSystem() {
		super(KP, KI, KD); // creates a PID controller with the KP, KI, and KD
							// values

		rotateToAngleRate = 0;

		Devices.getInstance().getNavXGyro().zeroYaw();

		setInputRange(-180.0f, 180.0f); // sets the maximum and minimum values
										// expected from the gyro
		setOutputRange(-1.0, 1.0); // sets the maximum and minimum output values
		setAbsoluteTolerance(K_TOLERANCE_DEGREES); // sets the absolute error
													// that is tolerable in the
													// PID system
		getPIDController().setContinuous(true); // sets the PID Controller to
												// think of the gyro input as
												// continuous

		// Creates a robot driver
		// Devices.getInstance().getTalon(RobotMap.FL).

//		Devices.getInstance().getTalon(RobotMap.FL).follow(Devices.getInstance().getTalon(RobotMap.BL));
//		Devices.getInstance().getTalon(RobotMap.FR).follow(Devices.getInstance().getTalon(RobotMap.BR));
		SpeedControllerGroup left_side = new SpeedControllerGroup(Devices.getInstance().getTalon(RobotMap.FL),
				Devices.getInstance().getTalon(RobotMap.BL));
		SpeedControllerGroup right_side = new SpeedControllerGroup(Devices.getInstance().getTalon(RobotMap.FR),
				Devices.getInstance().getTalon(RobotMap.BR));
		driver = new DifferentialDrive(left_side, right_side);
		// Devices.getInstance().getTalon(RobotMap.FRONT_LEFT).changeControlMode(TalonControlMode.);
	}

	public void initDefaultCommand() {
		// setDefaultCommand(new DriveStraight(1000));
	}

	/**
	 * Returns the input used in the PID calculations
	 * 
	 * @return the input used in the PID calculations
	 */
	protected double returnPIDInput() {
		return Devices.getInstance().getNavXGyro().getYaw();
	}

	/**
	 * Uses KP, KI, KD, and the input from returnPIDInput() to change and output
	 * value
	 */
	protected void usePIDOutput(double output) {
		double out = output;

		if (Math.abs(out) < 0.01) {
			out = 0;
		} else if (Math.abs(out) < 0.2) {
			out = 0.2 * Math.signum(out);
		}

		rotateToAngleRate = out;
	}

	/**
	 * Drives the robot given the speed and the rotation angle.
	 */
	public void drive(double speed, double rotation) {
		//System.out.println("THROTTLE: " + speed);
		//System.out.println("TWIST: " + rotation);
		driver.arcadeDrive(speed, rotation);
	}

	/**
	 * Stops the robot
	 */
	public void stop() {
		driver.arcadeDrive(0, 0);
	}

	/**
	 * @return the gyro object
	 */
	public AHRS getGyro() {
		return Devices.getInstance().getNavXGyro();
	}

	/**
	 * @return the value changed by the PID system
	 */
	public double getAngleRate() {
		return rotateToAngleRate;
	}

	/**
	 * Sets the desired angle in the PID system
	 * 
	 * @param angle
	 */
	public void setTargetAngle(double angle) {
		setSetpoint(angle);
		this.disable();
		this.enable();
	}
}