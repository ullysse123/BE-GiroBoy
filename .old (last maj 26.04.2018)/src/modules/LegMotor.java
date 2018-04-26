package modules;

import lejos.hardware.motor.EV3LargeRegulatedMotor;
import lejos.hardware.port.MotorPort;
import lejos.hardware.port.Port;

public class LegMotor {
	private EV3LargeRegulatedMotor motor;
	public LegMotor (int num) {
		Port port;
		if(num==0) port=MotorPort.A;
		else port=MotorPort.D;
		motor=new EV3LargeRegulatedMotor (port);
	}
	
	public int getSpeed() {
		return motor.getSpeed();
	}
	
	public void setSpeed(int speed) {
		motor.setSpeed(speed);
	}
	
	public int getAcceleration() {
		return motor.getAcceleration();
	}
	
	public void setAcceleration(int acceleration) {
		motor.setAcceleration(acceleration);
	}
	
	public void forward() {
		motor.forward();
	}
	
	public void backward() {
		motor.backward();
	}
	
	public void rotate(int angle) {
		motor.rotate(angle);
	}
	
	public void rotateImmediate(int angle) {
		motor.rotate(angle,true);
	}
	
	public int getRotationSpeed() {
		return motor.getRotationSpeed();
	}
	
	public void stop() {
		motor.setSpeed(0);
	}
	
	public void close() {
		motor.close();
	}
	
	
}
