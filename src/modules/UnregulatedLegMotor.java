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
	
	//Applique une puissance au moteur
	public void setPower(int power) {
		motor.setPower(power);
	}
	
	//Obtention de la puissance du moteur
	public int getPower() {
		return motor.getPower();
	}
	
	//Obtention du nombre de tour que fait le moteur
	public int getTacho() {
		return motor.getTachoCount();
	}
	
	//Reset le tachoCount
	public void reset() {
		motor.resetTachoCount();
	}
	
	//Ferme le moteur
	public void close(){
		motor.close();
	}
}
