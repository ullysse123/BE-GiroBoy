package robot;

import lejos.hardware.Button;
import lejos.hardware.Sound;
import lejos.utility.Delay;
import modules.ColorSensor;
import modules.LightSensor;

public class Robot2 {

	static ColorSensor color;
	static LightSensor light;
	
	public static boolean lightSurLigne(){
		float seuil = 0.4f;
		return seuil<light.getModeRouge();
	}
	
	public static boolean colorSurLigne(){
		float seuil = 0.0385f;
		return seuil<color.getRedMode();
	}
	
	public static int sensLigne(){
		
		int i = 0;
		
		if(lightSurLigne()){
			i++;
		}
		if(colorSurLigne()){
			i+=2;
		}
		return i;	
	}
	
	public static boolean finCarrefour(){
		return ( lightSurLigne() && colorSurLigne() );
	}
	
	//x = 1 prendre la branche droite du carrefourc || x = 0 brandre la branche gauche du carrefour
	public static void carrefour(int x){
		
		long tInit = System.nanoTime()/1000000000;
		long t = 0;
		long timeLimit = 3000;
		
		if (x==0 || x==1){
			//Tant que on n'est pas sur la double ligne de fin et que x milisecondes ne sont pas passées
			while(!finCarrefour() && (t-tInit)<=timeLimit){
				//Passage d'un carrefour
				if(x==1){
					//Passage a droite
					//TODO Implementer passage à droite. Utiliser un seul capteur et zigzaguer sur la ligne
				}else{
					//Passage a gauche
					//TODO Implementer passage a gauche. Utiliser un seul capteur et zigzaguer sur la ligne 
				}
				t = System.nanoTime()/1000000000;
			}
		}else{
			Sound.playTone(800, 10, 10);
			System.out.println("/!\\ ERREUR CARREFOUR /!\\ \n\n\n");
		}
		
	}
	
	//TODO Passage a niveau : Si jamais on a deux double lignes, passage a niveau est appelé via carefour et il faut choisir ce qu'on fait.
	//Aller tout droit ou eviter un robot
	
	public static void suiveurDeLigne(){
		
		//Initialisation de nos objets
		Equilibre eq = new Equilibre();
		color = new ColorSensor();
		light = new LightSensor();
		
		//Ensemble de nos variables permettant de fixer la vitesse et la direction 
		double vitesse = 3;
		double direction = 0;
		int nbPassageVirageDroite = 0;
		int nbPassageVirageGauche = 0;
		int nbPassageLigneDroite = 0;
		
		//Lancement de l'équilibre avec une vitesse initiale pour démarrer le circuit
		eq.start();
		eq.setVitesse(3);
		
		while(!Button.ESCAPE.isDown()){
			
			switch(sensLigne()){
				
				case 0: //Case ou aucun des capteur ne capte la ligne on avance
						//Remise a 0 des compteur et de la direction
						//System.out.println("Entre les lignes");
						nbPassageVirageDroite = 0;
						nbPassageVirageGauche = 0;
						direction = 0;
						//Vitesse fixé a 4 au depart
						if(nbPassageLigneDroite == 0) {
							vitesse = 3.4;
						}
						//Vitesse incrémentale pour augmenter la fluidité du déplacement
						nbPassageLigneDroite++;
						if(nbPassageLigneDroite<=10){
							vitesse+=0.2;
						}
						eq.setVitesse(vitesse);
						Delay.msDelay(15);
						eq.setDirection(direction);
						break;
				case 1: //Cas ou seul le capteur gauche capte la ligne
						//Remise a 0 des compteur
						//System.out.println("Gauche sur la ligne");
						nbPassageLigneDroite = 0;
						nbPassageVirageDroite = 0;
						//Vitesse fixé a 4 pour les virages et direction a 3
						vitesse = 2.2;
						if(nbPassageVirageGauche == 0){
							direction = 5;
						}
						//Augmentation incrémentale de la direction
						nbPassageVirageGauche++;
						if(nbPassageVirageGauche<=10){
							direction++;
						}
						eq.setVitesse(vitesse);
						Delay.msDelay(15);
						eq.setDirection(-direction);
						break;
						
				case 2: //Cas ou seul le capteur droite capte la ligne 
						//Remise a 0 des compteur
						//System.out.println("Droit sur la ligne");
						nbPassageVirageGauche = 0;
						nbPassageLigneDroite = 0;
						//Vitesse fixé a 4 pour les virage et direction a 3
						vitesse = 2.2;
						if(nbPassageVirageDroite == 0){
							direction = 5;
						}
						//Augmentation incrémentale de la direction
						nbPassageVirageDroite++;
						if(nbPassageVirageDroite<=10){
							direction++;
						}
						eq.setVitesse(vitesse);
						Delay.msDelay(15);
						eq.setDirection(direction);
						break;
						
				case 3: //Cas ou es deux capteur captent la ligne on s'arrette
						//System.out.println("Deux sur la ligne");	
						nbPassageLigneDroite = 0;
						nbPassageVirageDroite = 0;
						nbPassageVirageGauche = 0;
						vitesse = 3;
						direction = 25;
						eq.setVitesse(vitesse);
						Delay.msDelay(15);
						eq.setDirection(direction);
						Sound.buzz();
						break;
						
				default : //Ne sortira jamais de [0;3] mais si c'est le cas alors stop vitesse/direction et beep sonore
						//System.out.println("Others");
						  eq.setVitesse(0);
						  Delay.msDelay(15);
						  eq.setDirection(0);
						  Sound.beepSequenceUp();
						  Delay.msDelay(50);
						  Sound.beepSequenceUp();
						  Delay.msDelay(50);
						  Sound.beepSequenceUp();
						  Delay.msDelay(50); 
			}
			
			//Frequence d'échantillonage fixé pour les capteurs
			Delay.msDelay(75);
		}
		
	}
	
	public static void main(String[] args) {
		
		suiveurDeLigne();

	}

}
