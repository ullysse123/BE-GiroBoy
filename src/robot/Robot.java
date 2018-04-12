package robot;

import lejos.hardware.Button;
import lejos.utility.Delay;
import modules.ColorSensor;
import modules.LightSensor;

public class Robot {
	static ColorSensor colorSensor=new ColorSensor();
	static LightSensor lightSensor=new LightSensor();
	static int direction = 5;
	static int vitesse = 5;
	static int ralenti = 2;
	static int nul = 0;
	static float minColor = 0.085f;
	static float minLight = 0.4f;
	

	public static int lignFollower() {
		int result = 0;
		//Detecte la ligne à droite 
		if(colorSensor.getColor()>minColor) {
			result++;
		}
		//Detecte la ligne à gauche
		if(lightSensor.getModeRouge()>minLight) {
			result+=2;
		}
		return result;
	}
	
	public static void lignFollowerRun(Equilibre eq) {
		int result;
		eq.setVitesse(ralenti);
		while(!Button.ESCAPE.isDown()) {
			result=lignFollower();
			switch(result) {
				//Rectifie à droite
				case 1:eq.setVitesse(ralenti);
					Delay.msDelay(5);
					eq.setDirection(direction);
					break;
				//Rectifie à gauche
				case 2:eq.setVitesse(ralenti);
					Delay.msDelay(5);
					eq.setDirection(-direction);
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
	}
	
	public static void main(String[] args) {
		Equilibre eq=new Equilibre();
		eq.run();
		lignFollowerRun(eq);
		colorSensor.close();
		lightSensor.close();
	}

}
