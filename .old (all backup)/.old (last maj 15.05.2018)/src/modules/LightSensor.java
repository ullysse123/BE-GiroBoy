package modules;

import lejos.hardware.sensor.NXTLightSensor;
import lejos.hardware.port.SensorPort;
import lejos.robotics.SampleProvider;

public class LightSensor {

	NXTLightSensor sensor;
	
	public LightSensor(){
		
		sensor = new NXTLightSensor(SensorPort.S4);
		
	}
	
	//Obtention de la valeur de la lumière reflechis
	public int getReflexionLumiere(){
		return sensor.getFloodlight();
	}
	
	//Obtention de la valeur via le red mode
	public float getModeRouge(){
		SampleProvider sample = sensor.getRedMode();
		float [] res = new float [sample.sampleSize()];
		sample.fetchSample(res, 0);
		return res[0];
	}
	
	//Allume la led pour la reflexion de la lumière
	public void allumerLedReflexion(boolean onOff){
		sensor.setFloodlight(onOff);
	}
	
	//Ferme le capteur
	public void close() {
		sensor.close();
	}
}
