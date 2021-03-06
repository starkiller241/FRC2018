package org.usfirst.frc.team2473.robot.subsystems;

import org.usfirst.frc.team2473.framework.Devices;
import org.usfirst.frc.team2473.framework.TrackableSubsystem;
import org.usfirst.frc.team2473.robot.Controls;
import org.usfirst.frc.team2473.robot.Robot;
import org.usfirst.frc.team2473.robot.RobotMap;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.can.WPI_TalonSRX;

import edu.wpi.first.wpilibj.DoubleSolenoid.Value;
import edu.wpi.first.wpilibj.RobotController;
import edu.wpi.first.wpilibj.command.Subsystem;

/**
 *
 */
public class ClimbSystem extends TrackableSubsystem {
	public final double FASTER_UP = -3.5 / RobotController.getBatteryVoltage();
	public final double SLOWER_UP = -0.15;
	public final double ARMPOWDOWN = 0.25;
	public final double CLIMBPOW = 0.9;
	public final double CLIMBASSISTPOW = 0.45;
	public final double THRESHOLD = 100;
	public final double ENCCOUNT2 = 2052;
	public final double ENCCOUNT3 = 150;

	private boolean hungUp = false;

	// 2052

	public ClimbSystem() {

	}

	public double getSlowerSpeed(boolean up) {
		return (up) ? -2.8 / RobotController.getBatteryVoltage() : 2.8 / RobotController.getBatteryVoltage();
	}

	public double getFasterSpeed(boolean up) {
		return (up) ? -3.5 / RobotController.getBatteryVoltage() : 3.5 / RobotController.getBatteryVoltage();
	}

	public void initDefaultCommand() {
		// Set the default command for a subsystem here.
		// setDefaultCommand(new MySpecialCommand());
	}

	public void setArmPow() {
		// Devices.getInstance().getTalon(RobotMap.climbArmMotor).set(ControlMode.PercentOutput,
		// ARMPOWUP);
	}

	public void stopArmMotor() {
		Devices.getInstance().getTalon(RobotMap.CLIMB_ARM_MOTOR).stopMotor();
	}

	public void updateToHung() {
		hungUp = true;
		System.out.println("update to hung status");
	}

	public void notYet() {
		hungUp = false;
		System.out.println("not yet...");
	}

	public void climbPrimary() {
		if (hungUp) {
			Devices.getInstance().getTalon(RobotMap.climbMotorR).set(ControlMode.PercentOutput, CLIMBPOW);
			Devices.getInstance().getTalon(RobotMap.climbMotorR2).set(ControlMode.PercentOutput, CLIMBPOW);
		} else {
			System.out.println("NOT YET U LITTLE SHITE - VICE PRESIDENT");
		}
	}

	public void climbAssist() {
		if (hungUp) {
			Devices.getInstance().getTalon(RobotMap.climbMotorL).set(ControlMode.PercentOutput, -CLIMBASSISTPOW);
			Devices.getInstance().getTalon(RobotMap.climbMotorL2).set(ControlMode.PercentOutput, -CLIMBASSISTPOW);
		} else {
			System.out.println("NOT YET U LITTLE SHITE - VICE PRESIDENT");
		}		
	}

	public void climbAssistDown() {
		Devices.getInstance().getTalon(RobotMap.climbMotorL).set(ControlMode.PercentOutput, CLIMBASSISTPOW/2);
		Devices.getInstance().getTalon(RobotMap.climbMotorL2).set(ControlMode.PercentOutput, CLIMBASSISTPOW/2);
	}
	
	public void climbPrimaryReverse() {
		Devices.getInstance().getTalon(RobotMap.climbMotorR).set(ControlMode.PercentOutput, -CLIMBPOW/2);
		Devices.getInstance().getTalon(RobotMap.climbMotorR2).set(ControlMode.PercentOutput, -CLIMBPOW/2);
	}

	public void climbPrimarySlow() {
		if (hungUp) {
			Devices.getInstance().getTalon(RobotMap.climbMotorR).set(ControlMode.PercentOutput, CLIMBPOW * .5);
			Devices.getInstance().getTalon(RobotMap.climbMotorR2).set(ControlMode.PercentOutput, CLIMBPOW * .5);
			System.out.println("climbing up");
		} else {
			System.out.println("NOT YET U LITTLE SHITE - VICE PRESIDENT");
		}
	}

	public void climbAlly() {
		// Devices.getInstance().getTalon(RobotMap.climbMotorR).set(ControlMode.PercentOutput,
		// -CLIMBPOW);
		Devices.getInstance().getTalon(RobotMap.climbMotorL2).set(ControlMode.PercentOutput, -CLIMBPOW);
		// Devices.getInstance().getTalon(RobotMap.climbMotorR2).set(ControlMode.PercentOutput,
		// -CLIMBPOW);
		// System.out.println("climb down");
		Devices.getInstance().getTalon(RobotMap.climbMotorL).set(ControlMode.PercentOutput, -CLIMBPOW);
	}

	public void stopClimbMotor() {
		Devices.getInstance().getTalon(RobotMap.climbMotorR).stopMotor();
		Devices.getInstance().getTalon(RobotMap.climbMotorL2).stopMotor();
		Devices.getInstance().getTalon(RobotMap.climbMotorR2).stopMotor();
		System.out.println("stopped");
		Devices.getInstance().getTalon(RobotMap.climbMotorL).stopMotor();
	}

	@Override
	public void stop() {

	}

	@Override
	public String getState() {
		return "" + Devices.getInstance().getTalon(RobotMap.climbMotorL).get() + " "
				+ Devices.getInstance().getTalon(RobotMap.climbMotorR).get() + " "
				+ Devices.getInstance().getTalon(RobotMap.CLIMB_ARM_MOTOR).get();
	}

	public void setPistonR() {
		Devices.getInstance().getDoubleSolenoid(RobotMap.solenoidClimbF, RobotMap.solenoidClimbR).set(Value.kReverse);

	}

	public void setPistonOff() {
		Devices.getInstance().getDoubleSolenoid(RobotMap.solenoidClimbF, RobotMap.solenoidClimbR).set(Value.kOff);

	}

	public void setPistonF() {
		Devices.getInstance().getDoubleSolenoid(RobotMap.solenoidClimbF, RobotMap.solenoidClimbR).set(Value.kForward);

	}
}
