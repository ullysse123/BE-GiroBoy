package robot;

import lejos.hardware.Button;
import lejos.utility.Delay;
import modules.ColorSensor;
import modules.LightSensor;

public class Robot {
	static ColorSensor colorSensor=new ColorSensor();
	static LightSensor lightSensor=new LightSensor();
	static int direction = 6;
	static int vitesse = 2;
	static int ralenti = 1;
	static int nul = 0;
	//static float minColor = 0.075f;
	//static float minColor = 0.080f;
	//static float minColor = 0.090f;
	//static float minColor = 0.095f;
	static float minColor = 0.0385f;
	//static float minLight = 0.35f;
	//static float minLight = 0.30f;
	//static float minLight = 0.45f;
	//static float minLight = 0.50f;
	static float minLight = 0.4f;
	

	public static int lignFollower() {
		int result = 0;
		//Detecte la ligne à droite 
		if(colorSensor.getRedMode()>minColor) {
			result++;
		}
		//Detecte la ligne à gauche
		if(lightSensor.getModeRouge()>minLight) {
			result+=2;
		}
		return result;
	}
	
	public static void lignFollowerRun() {
		Equilibre eq=new Equilibre();
		eq.start();
		eq.setVitesse(vitesse);
		int result;
		while(!Button.ESCAPE.isDown()) {
			result=lignFollower();
			switch(result) {
				//Rectifie à droite
				case 1:eq.setVitesse(nul);
					Delay.msDelay(5);
					eq.setDirection(direction);
					Delay.msDelay(100);
					while(lignFollower()!=2)
						Delay.msDelay(100);
					eq.setDirection(-direction);
					break;
				//Rectifie à gauche
				case 2:eq.setVitesse(nul);
					Delay.msDelay(5);
					eq.setDirection(-direction);
					Delay.msDelay(100);
					while(lignFollower()!=1)
						Delay.msDelay(100);
					eq.setDirection(direction);
					break;
				//S'arrete puis reflechit
				case 3:eq.setVitesse(nul);
					break;
				//Continue Tout droit
				default:eq.setDirection(nul);
					Delay.msDelay(5);
					eq.setVitesse(vitesse);
			}
			
			Delay.msDelay(100);
		}
		colorSensor.close();
		lightSensor.close();
	}
	
	public static void main(String[] args) {
		lignFollowerRun();
	}

}
