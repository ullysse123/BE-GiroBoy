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
	
	//Obtention de la vitesse du moteur
	public int getSpeed() {
		return motor.getSpeed();
	}
	
	//Appliquer une vitesse au moteur
	public void setSpeed(int speed) {
		motor.setSpeed(speed);
	}
	
	//Obtention de l'cceleration du moteur
	public int getAcceleration() {
		return motor.getAcceleration();
	}
	
	//Appliquer une acceleration au moteur
	public void setAcceleration(int acceleration) {
		motor.setAcceleration(acceleration);
	}
	
	//Avance
	public void forward() {
		motor.forward();
	}
	
	//Recule
	public void backward() {
		motor.backward();
	}
	
	//Fait tourner le moteur de x degré
	public void rotate(int angle) {
		motor.rotate(angle);
	}
	
	//Fait tourner le moteur immediatement de x degré
	public void rotateImmediate(int angle) {
		motor.rotate(angle,true);
	}
	
	//Obtention de la vitesse de rotation
	public int getRotationSpeed() {
		return motor.getRotationSpeed();
	}
	
	//Stop le moteur
	public void stop() {
		motor.setSpeed(0);
	}
	
	//Arrette le moteur
	public void close() {
		motor.close();
	}
	
	
}
