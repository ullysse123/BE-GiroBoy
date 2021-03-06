package modules;

import lejos.hardware.sensor.EV3ColorSensor;
import lejos.hardware.port.SensorPort;
import lejos.robotics.SampleProvider;

public class ColorSensor {

	EV3ColorSensor sensor;
	
	//Si i = 0 alors droite sinon gauche
	public ColorSensor(int i){
		if (i==0){
			sensor = new EV3ColorSensor(SensorPort.S1);
		}else{
			sensor = new EV3ColorSensor(SensorPort.S4);
		}
		
	}
	
	//Obtention de la valeur avec le mode red
	public float getRedMode(){
		SampleProvider sample = sensor.getRedMode();
		float [] res = new float [sample.sampleSize()];
		sample.fetchSample(res, 0);
		return res[0];
	}
	
	//Obtention de la couleur
	public float getColor(){
		SampleProvider sample = sensor.getColorIDMode();
		float [] res = new float [sample.sampleSize()];
		sample.fetchSample(res, 0);
		return res[0];
	}
	
	//Fermeture du capteur
	public void close() {
		sensor.close();
	}
	
}
