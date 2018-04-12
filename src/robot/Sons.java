package robot;

import lejos.hardware.Sound;
import java.io.File;


public class Sons extends Thread {

	public void laboulette(){
		//Fonction permettant de jouer "A la boulette"
		Sound.playSample(new File("sons/boulette"), 10);
	}
	
	public void banzai(){
		Sound.playSample(new File("sons/banzai"), 5);
	}
	
	public void run(){
		
		while(true){
			
		}
		
	}
	
}
