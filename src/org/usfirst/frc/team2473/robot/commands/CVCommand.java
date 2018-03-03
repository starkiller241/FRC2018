package org.usfirst.frc.team2473.robot.commands;

import org.usfirst.frc.team2473.framework.Database;
import org.usfirst.frc.team2473.framework.TrackingRobot;
import org.usfirst.frc.team2473.robot.CV;
import org.usfirst.frc.team2473.robot.Robot;
import org.usfirst.frc.team2473.robot.subsystems.BoxSystem;

import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class CVCommand extends Command {
	boolean runningCV = false;
	boolean finished = false;
	boolean opened = false;

	static boolean TIMING_DEBUGGING = false; // Prints when you've reached different parts in the code
	static boolean DEBUG = false; // General debugging
	BoxSystem box;

	public CVCommand() {
		box = Robot.getBox();
	}

	public CVCommand(String name) {
		super(name);
		// TODO Auto-generated constructor stub
	}

	public CVCommand(double timeout) {
		super(timeout);
		// TODO Auto-generated constructor stub
	}

	public CVCommand(String name, double timeout) {
		super(name, timeout);
		// TODO Auto-generated constructor stub
	}

	@Override
	protected void initialize() {
		SmartDashboard.putString("CV Sight", "Press Button 8");
		if (TIMING_DEBUGGING)
			System.out.println("beginning CV drive code...");
	}

	// Called repeatedly when this Command is scheduled to run
	@Override
	protected void execute() {
		if (!runningCV || finished) {
			if (Robot.getControls().getCVButton().get()) {
				if (Robot.drive.isRunning()) {
					Robot.drive.cancel();
				}
				runningCV = true;
			} else {
				if (Robot.drive.isRunning() == false) {
					Robot.drive.start();					
					finished = false;
				}
			}
		}
		if (TIMING_DEBUGGING)
			System.out.println("at cvcommand");
		double distance;
		double bearing;
		if (runningCV && !finished) {			
			Robot.drive.cancel();
			if (Database.getInstance().getNumeric("dist") != CV.NO_BOX) {
				if (TIMING_DEBUGGING) System.out.println("beginning the set");
				
				distance = Database.getInstance().getNumeric("dist");
				bearing = Database.getInstance().getNumeric("ang");
				
				if (DEBUG) System.out.println("Distance: " + distance + " Angle: " + bearing);
				
				if (distance < CV.DIST_THRESHOLD) {
					opened = false;
//					TrackingRobot.getDriveTrain().stop();

					System.out.println("within threshold at " + distance);
					
					SmartDashboard.putString("CV Sight", "Box in sight: Final drive forward");
					if (DEBUG) System.out.println("Distance is less than 20");
					
					// Add claw commands to open claw/ check to make sure the claw is open
					if(DEBUG) System.out.println("drive train not yet enabled");
					
					TrackingRobot.getDriveTrain().enable();
					if(DEBUG) System.out.println("drive train enabled");
					
					// TeleOpDriveStraight simp = new TeleOpDriveStraight(distance + 1, 0.5);
//					if (DEBUG) System.out.println("DRIVE STRAIGHT STARTED");

					// Add claw commands for picking up box
					TrackingRobot.getDriveTrain().disable();
					SmartDashboard.putString("CV Sight", "Finished picking up!");
					if (DEBUG) System.out.println("Drive finished! woot");
					
					finished = true;
					runningCV = false;
//					box.clawStatusNotReady();
					opened = false;
//					break;
				} else {
					if (distance < CV.CLAW_DIST) {
						(new ReadyClaw()).start();
					}
					
					if (Math.abs(bearing) > CV.BEARING_THRESHOLD) {
						SmartDashboard.putString("CV Sight",
								"Box in sight : Turning " + (bearing > 0 ? "Right" : "Left"));
						double sign = Math.signum(bearing);
						if (distance < CV.CLOSE_DIST)
							TrackingRobot.getDriveTrain().drive(CV.POWER, sign * CV.BIG_TURN);
						else
							TrackingRobot.getDriveTrain().drive(CV.POWER, sign * CV.SMALL_TURN);
					} else {
						SmartDashboard.putString("CV Sight", "Box in sight: Driving Straight");
						TrackingRobot.getDriveTrain().drive(CV.POWER, 0.0);
					}
				}
			} else {
				if (DEBUG) System.out.println("No box in sight!");
				SmartDashboard.putString("CV Sight", "No box in sight");
				TrackingRobot.getDriveTrain().stop();
				runningCV = false;
			}
		}
	}

	// Make this return true when this Command no longer needs to run execute()
	@Override
	protected boolean isFinished() {
		return false;
	}

	// Called once after isFinished returns true
	@Override
	protected void end() {
		TrackingRobot.getDriveTrain().drive(0, 0);
	}

	// Called when another command which requires one or more of the same
	// subsystems is scheduled to run
	@Override
	protected void interrupted() {
		Robot.drive.start();
	}
}
