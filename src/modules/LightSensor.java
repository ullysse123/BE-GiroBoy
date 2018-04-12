package modules;

import lejos.hardware.sensor.NXTLightSensor;
import lejos.hardware.port.SensorPort;
import lejos.robotics.SampleProvider;

public class LightSensor {

	NXTLightSensor sensor;
	
	public LightSensor(){
		
		sensor = new NXTLightSensor(SensorPort.S4);
		
	}
	
	public int getReflexionLumiere(){
		return sensor.getFloodlight();
	}
	
	public float getModeRouge(){
		SampleProvider sample = sensor.getRedMode();
		float [] res = new float [sample.sampleSize()];
		sample.fetchSample(res, 0);
		return res[0];
	}
	
	public void allumerLedReflexion(boolean onOff){
		sensor.setFloodlight(onOff);
	}
}
