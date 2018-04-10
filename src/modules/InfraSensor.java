package modules;

import lejos.hardware.sensor.EV3ColorSensor;
import lejos.hardware.port.SensorPort;
import lejos.robotics.SampleProvider;

public class InfraSensor {

	EV3ColorSensor sensor;
	
	public InfraSensor(){
		sensor = new EV3ColorSensor(SensorPort.S1);
		
	}
	
	public float getColor(){
		SampleProvider sample = sensor.getColorIDMode();
		float [] res = new float [sample.sampleSize()];
		sample.fetchSample(res, 0);
		return res[0];
	}
	
	public void close() {
		sensor.close();
	}
	
}
