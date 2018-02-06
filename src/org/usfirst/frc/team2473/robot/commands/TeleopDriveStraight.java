package org.usfirst.frc.team2473.robot.commands;

import org.usfirst.frc.team2473.framework.Database;
import org.usfirst.frc.team2473.framework.Devices;
import org.usfirst.frc.team2473.robot.ControlsMap;
import org.usfirst.frc.team2473.robot.Robot;
import org.usfirst.frc.team2473.robot.RobotMap;

import edu.wpi.first.wpilibj.command.Command;

public class TeleopDriveStraight extends Command {

	private double maxPow = 0.8;
	private double wheelDeadzone = 0.04;
	private double throttleDeadzone = 0.05;
	// WARNING!!!!!!!!!!!!!!!!!!
	// Cannot run code without initializing maxWheelX value correctly
	private double maxWheelX = 1;
	// determine the values of a and b by testing
	private final double b = 1; // b should be half the length between the two wheels in meters
	private final double powToSpeedRatio = 35; // the ratio converting power to actual speed while driving straight
	private final double speedMaxThreshold = 15; // the threshold determining the max pow while turning
	private final double powCapMod = 0.7; // the modifier for reducing the power while turning
	
	private float angle = 0;
	//private double encoders = 0;
	
	public TeleopDriveStraight() {
		requires(Robot.piDriveTrain);
		angle = Devices.getInstance().getNavXGyro().getYaw();
		//this.encoders = encoders; 
	}

	@Override
	protected void initialize() {
		Robot.piDriveTrain.enable();
	}

	@Override
	protected void execute() {
		if(Math.abs(Database.getInstance().getNumeric(ControlsMap.THROTTLE_Z)) < throttleDeadzone){
			Robot.piDriveTrain.drive(0, Database.getInstance().getNumeric(ControlsMap.STEERING_WHEEL_X));
		}else{
			Robot.piDriveTrain.setTargetAngle( Database.getInstance().getNumeric(ControlsMap.STEERING_WHEEL_X)*90);
			Robot.piDriveTrain.drive(Database.getInstance().getNumeric(ControlsMap.THROTTLE_Z), Robot.piDriveTrain.getAngleRate());
			//turn(Database.getInstance().getNumeric(ControlsMap.THROTTLE_Z), Robot.piDriveTrain.getAngleRate());
		}
//		Robot.piDriveTrain.setTargetAngle(angle);
//		this.turn(Database.getInstance().getNumeric(ControlsMap.THROTTLE_Z), Robot.piDriveTrain.getAngleRate());
	}

	private double squareWithSign(double d) {
		if (d >= 0.04) {
			return -d * d;
		} else if (d <= -0.04) {
			return d * d;
		} else {
			return 0;
		}
	}

	private void turn(double pow, double turn) {
		if (Math.abs(pow) > maxPow) {
			pow = maxPow * Math.signum(pow);
		}
		
		if (Math.abs(pow) > throttleDeadzone) {
			double approxSpeed = getApproxSpeed(pow, turn); // the approximated speed in meters per second
			approxSpeed = this.cap(approxSpeed, turn);
			approxSpeed = approxSpeed / powToSpeedRatio;
			if (approxSpeed > 0) {
				setLeftPow(approxSpeed + b * turn); 
				setRightPow(approxSpeed - b * turn); 
			} else if (pow < 0) {
				setLeftPow(-(approxSpeed + b * turn));
				setRightPow(-(approxSpeed - b * turn));
			}
		}
	}
	
	private final double minTurnThreshold = 0.2;
	private final double minSpeedThreshold = 5;
	private final double a = 5; // a is the ratio of the value of power to the actual velocity
	// return the approximated current SPEED under the influence of friction and skidding
	private double getApproxSpeed(double pow, double turn) {
		/*
		 * TODO determine if this is the best fit. All we know now is that the higher the pow and the greater 
		 * the turn, the lower the actual speed.
		 */
		double speed = pow * powToSpeedRatio;
		if (Math.abs(turn) < minTurnThreshold || speed < minSpeedThreshold)
			return speed;
		return speed - (a / Math.abs(turn)) / speed;
	}
	
	// lower the power if the speed is too high
	private double cap(double speed, double turn) {
		/*
		 * TODO determine if this is the best fit. All we know now is that the higher the pow the greater the
		 * pow reduction, and the greater the turn the less the pow reduction
		 */
		if (speed > speedMaxThreshold && turn > minTurnThreshold)
			return powCapMod * speed / Math.abs(turn);
		return speed;
	}

	// the experimental version of turn
	private void turnExp(double pow, double turn) {
		
	}
	
//	private void record(double pow, double turn, double actualV) {
//		try {
//			fw.write(pow + " ");
//			fw.write(turn + " ");
//			fw.write(actualV + "\n");
//		} catch (IOException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//	}

	private void setRightPow(double pow) {
		double temp = pow;
		if (temp > maxPow)
			temp = maxPow;
		else if (temp < -maxPow)
			temp = -maxPow;
		Devices.getInstance().getTalon(RobotMap.FR).set(-temp);
		Devices.getInstance().getTalon(RobotMap.BR).set(-temp);

	}

	private void setLeftPow(double pow) {
		double temp = pow;
		if (temp > maxPow)
			temp = maxPow;
		else if (temp < -maxPow)
			temp = -maxPow;
		Devices.getInstance().getTalon(RobotMap.FL).set(temp);
		Devices.getInstance().getTalon(RobotMap.BL).set(temp);

	}

	@Override
	protected boolean isFinished() {
		//return ((Database.getInstance().getNumeric(RobotMap.FRONT_RIGHT_ENC) + Database.getInstance().getNumeric(RobotMap.FRONT_LEFT_ENC))/2)<=encoders;
		return false;
	}

	@Override
	protected void end() {
		Robot.piDriveTrain.disable();
		Devices.getInstance().getTalon(RobotMap.BL).set(0);
		Devices.getInstance().getTalon(RobotMap.BR).set(0);
		Devices.getInstance().getTalon(RobotMap.FR).set(0);
		Devices.getInstance().getTalon(RobotMap.FL).set(0);
		System.out.println("DriveStraight ended. 🙂");
	}

	@Override
	protected void interrupted() {
		Robot.piDriveTrain.disable();
	}

}