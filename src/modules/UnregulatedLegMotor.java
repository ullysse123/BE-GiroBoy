package modules;

import lejos.hardware.motor.UnregulatedMotor;
import lejos.hardware.port.MotorPort;
import lejos.hardware.port.Port;

public class UnregulatedLegMotor {
	private UnregulatedMotor motor;
	public UnregulatedLegMotor (int num) {
		Port port;
		if(num==0) port=MotorPort.A;
		else port=MotorPort.D;
		motor=new UnregulatedMotor (port);
	}
	
	public void setPower(int power) {
		motor.setPower(power);
	}
	
	public int getPower() {
		return motor.getPower();
	}
	
	public void reset() {
		motor.resetTachoCount();
	}
}
