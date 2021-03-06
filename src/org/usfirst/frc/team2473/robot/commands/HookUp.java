package org.usfirst.frc.team2473.robot.commands;

import org.usfirst.frc.team2473.framework.Devices;
import org.usfirst.frc.team2473.robot.Robot;
import org.usfirst.frc.team2473.robot.RobotMap;
import org.usfirst.frc.team2473.robot.subsystems.ClimbSystem;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.FeedbackDevice;

import edu.wpi.first.wpilibj.command.Command;

/**
 *
 */
public class HookUp extends Command {
	private ClimbSystem sub;

	public HookUp() {
		sub = (ClimbSystem) Robot.getSubsystem(ClimbSystem.class);
		requires(sub);
	}

	// Called just before this Command runs the first time
	protected void initialize() {
		// Devices.getInstance().getTalon(RobotMap.climbArmMotor).set(ControlMode.PercentOutput,
		// sub.getArmUpPow());
		Devices.getInstance().getTalon(RobotMap.CLIMB_ARM_MOTOR)
				.configSelectedFeedbackSensor(FeedbackDevice.QuadEncoder, 0, 5); // do once at start to define encoder
		Devices.getInstance().getTalon(RobotMap.CLIMB_ARM_MOTOR).setSelectedSensorPosition(0, 0, 10); // whenever
																										// resetting
		Devices.getInstance().getTalon(RobotMap.CLIMB_ARM_MOTOR).set(ControlMode.PercentOutput, sub.FASTER_UP);
		
	}

	// Called repeatedly when this Command is scheduled to run
	protected void execute() {
		double pow = (Devices.getInstance().getTalon(RobotMap.CLIMB_ARM_MOTOR).getSelectedSensorPosition(0) <= sub.THRESHOLD
				? sub.getFasterSpeed(true)
				: sub.getSlowerSpeed(true));
		Devices.getInstance().getTalon(RobotMap.CLIMB_ARM_MOTOR).set(pow);
	}

	// Make this return true when this Command no longer needs to run execute()
	protected boolean isFinished() {
		return Math.abs(
				Devices.getInstance().getTalon(RobotMap.CLIMB_ARM_MOTOR).getSelectedSensorPosition(0)) >= sub.ENCCOUNT2;
	}

	// Called once after isFinished returns true
	protected void end() {
		sub.stopArmMotor();
		sub.notYet();
	}

	// Called when another command which requires one or more of the same
	// subsystems is scheduled to run
	protected void interrupted() {
		sub.stopArmMotor();

	}
}
