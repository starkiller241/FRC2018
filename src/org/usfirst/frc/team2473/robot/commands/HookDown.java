package org.usfirst.frc.team2473.robot.commands;

import org.usfirst.frc.team2473.framework.Devices;
import org.usfirst.frc.team2473.robot.Controls;
import org.usfirst.frc.team2473.robot.Robot;
import org.usfirst.frc.team2473.robot.RobotMap;
import org.usfirst.frc.team2473.robot.subsystems.ClimbSystem;

import com.ctre.phoenix.motorcontrol.ControlMode;

import edu.wpi.first.wpilibj.command.Command;

/**
 *
 */
public class HookDown extends Command {
	private ClimbSystem sub;
	
    public HookDown() {
    	sub = (ClimbSystem) Robot.getSubsystem(ClimbSystem.class);
    	requires(sub);
    }

    // Called just before this Command runs the first time
    protected void initialize() {
    	Devices.getInstance().getTalon(RobotMap.climbArmMotor).set(ControlMode.PercentOutput, sub.ARMPOWDOWN);
    	System.out.println("down");
    }

    // Called repeatedly when this Command is scheduled to run
    protected void execute() {
    	
    }

    // Make this return true when this Command no longer needs to run execute()
    protected boolean isFinished() 
    {
    	return Devices.getInstance().getTalon(RobotMap.climbArmMotor).getSelectedSensorPosition(0)<=sub.ENCCOUNT3;
    }

    // Called once after isFinished returns true
    protected void end() 
    {
    	sub.stopArmMotor();
    }

    // Called when another command which requires one or more of the same
    // subsystems is scheduled to run
    protected void interrupted() {
    	sub.stopArmMotor();
    }
}
