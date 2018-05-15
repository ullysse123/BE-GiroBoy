package modules;

import lejos.hardware.sensor.EV3GyroSensor;
import lejos.hardware.port.SensorPort;
import lejos.robotics.SampleProvider;

public class GyroSensor {
	
	EV3GyroSensor sensor;
	
	public GyroSensor(){
		sensor = new EV3GyroSensor(SensorPort.S3);
	}
	
	//Obtention de l'angle
	public float getAngle(){
		SampleProvider sample= sensor.getAngleMode();
		float [] res = new float [sample.sampleSize()];
		sample.fetchSample(res, 0);
		return res[0];
	}
	
	//Obtention de la vitesse angulaire
	public float getAngularSpeed(){
		SampleProvider sample= sensor.getRateMode();
		float [] res = new float [sample.sampleSize()];
		sample.fetchSample(res,0);
		return res[0];
	}
	
	//Obtention de l'echantillon de donnée obtenue via le capteur
	public void chercherEchantillon (float[] echantillon, int i) {
		sensor.fetchSample(echantillon, i);
	}
	
	//Obtention de la valeur
	public SampleProvider getRateMode() {
		return sensor.getRateMode();
	}
	
	public void reset() {
		sensor.reset();
	}
	
	public void close() {
		sensor.close();
	}
	
}
