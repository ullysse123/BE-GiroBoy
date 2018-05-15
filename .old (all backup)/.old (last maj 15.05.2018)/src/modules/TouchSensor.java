package modules;

import lejos.hardware.sensor.EV3TouchSensor;
import lejos.hardware.port.SensorPort;

public class TouchSensor {

	EV3TouchSensor sensor;
	
	public TouchSensor(){
		sensor = new EV3TouchSensor(SensorPort.S2);
	}
	
	//Non utilisé pour le moment
	
}
