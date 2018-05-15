package modules;

import lejos.hardware.sensor.EV3UltrasonicSensor;
import lejos.hardware.port.SensorPort;
public class UltraSoundSensor {
	
	EV3UltrasonicSensor sensor;
	
	public UltraSoundSensor(){
		sensor = new EV3UltrasonicSensor(SensorPort.S2);
	}
	
	//Non utilisé pour le moment
}
