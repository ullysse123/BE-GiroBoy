package modules;

import lejos.hardware.sensor.EV3UltrasonicSensor;
import lejos.hardware.port.SensorPort;
import lejos.robotics.SampleProvider;

public class UltraSoundSensor {
	
	EV3UltrasonicSensor sensor;
	
	public UltraSoundSensor(){
		sensor = new EV3UltrasonicSensor(SensorPort.S2);
	}
	
}
