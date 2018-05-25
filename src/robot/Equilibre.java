package robot;

import modules.*;
import lejos.hardware.Button;
import lejos.hardware.Sound;
//import lejos.robotics.SampleProvider;
import lejos.utility.Delay;

public class Equilibre extends Thread{

	//Vitesse et direction ( sera utilisé plus tard )
	static double vitesse = 0; //[-10;10]
	static int direction = 0; //[-50;50]
	//Angle limite 
	static int angleLimite=45;
	//Modules utilisées pour maintenir le robot en equilibre
	static GyroSensor gyro=new GyroSensor();
	static UnregulatedLegMotor gauche = new UnregulatedLegMotor(1);
	static UnregulatedLegMotor droite = new UnregulatedLegMotor(0);
	
	public float getAngle() {
		return gyro.getAngle();
	}
	
	public void getAngleReset() {
		gyro.reset();
	}
	
	//Applique un angle limite que ne doit pas dépasser le robot ( non utilisé actuellement )
	public void setAngleLimite(int angle){
		Equilibre.angleLimite = angle;
	}
	
	//Applique une vitesse au robot
	public void setVitesse(double x){
		if (x<=10 && x>=-10){
			Equilibre.vitesse = x;
		}else{
			if(x<-10){
				Equilibre.vitesse = -10;
			}else{
				Equilibre.vitesse = 10;
			}
		}
	}
	
	//Applique une direction que doit suivre le robot
	public void setDirection (int x){
		if (x<=50 && x>=-50){
			Equilibre.direction = x;
		}else{
			if(x<-50){
				Equilibre.direction = -50;
			}else{
				Equilibre.direction = 50;
			}
		}
	}
	
	//Fonction principale permettant le maintiens de l'équilibre du robot
	public void run() {
		
		//Ensemble des variables utilisées
		//Variables des angles
		double angle = 0;
		double vitesseAngulaire = 0;
		//Variable des moteurs
		double vitesseMoteur = 0;
		double angleMoteur = 0;
		double sumMoteur = 0;
		double oldSumMoteur = 0;
		double deriveMoteur = 0;
		double deriveMoteur1 = 0;
		double deriveMoteur2 = 0;
		double deriveMoteur3 = 0;
		int directionC;
		//Variable temporelles
		long chrono = 0;
		long dernierChrono = 0;
		double delta = 0;
		double compteur = 0;
		//Booleen autorisant la maj des moteur
		boolean pret = false;
		//Puissance moteur a ajouter
		double puissance = 0;
		
		//Initialisation de nos données
		droite.reset();
		gauche.reset();
		gyro.reset();
		dernierChrono = System.nanoTime();
		angle = -0.25;
		
		//Signal sonore indiquant que les données sont initialisées
		Sound.beepSequenceUp();
		
		//On donne la priorité maximum a ce thread ( systeme vitale a notre fonctionnement )
		Thread.currentThread().setPriority(MAX_PRIORITY);

		//Debut de notre boucle de maintien de l'equilibre
		while(!Button.ESCAPE.isDown()) {
			
			//Mise a jour de nos infos temporelles
			chrono = System.nanoTime();
			delta = (chrono - dernierChrono)/1000000000.0;
			dernierChrono = chrono;
			
			//Mise a jour des données sur l'angle du robot
			vitesseAngulaire =-gyro.getAngularSpeed();
			angle = angle + (vitesseAngulaire*delta);
			
			//Mise a jour des données moteur
			oldSumMoteur = sumMoteur;
			sumMoteur = gauche.getTacho() + droite.getTacho();
			deriveMoteur = sumMoteur - oldSumMoteur;
			angleMoteur = angleMoteur + deriveMoteur;
			vitesseMoteur = ((deriveMoteur + deriveMoteur1 + deriveMoteur2 + deriveMoteur3)/4.0)/delta;
			deriveMoteur3 = deriveMoteur2;
			deriveMoteur2 = deriveMoteur1;
			deriveMoteur1 = deriveMoteur;
			
			
			angleMoteur = angleMoteur-vitesse;
			
			//Calcule de la puissance nécessaire a la correction d'inclinaison du robot
			puissance = (0.08 * vitesseMoteur) + (0.12 * angleMoteur) + (0.8 * vitesseAngulaire) + (15 * angle);
			
			if (puissance > 100){
				puissance = 100;
			}
			if (puissance<-100){
				puissance = -100;
			}
			//int differenceCourante=droite.getTacho()-gauche.getTacho();
			
			//Application de la puissance aux moteurs
			if(pret){
				/*if(differenceCourante<0 && compteur%3==0)
					directionC=direction+3;
				else*/
					directionC=direction+1;
				//Micro correction de la trajectoire afin de permettre au robot de se deplacer droit
				droite.setPower((int)(puissance)-directionC);
				gauche.setPower((int)(puissance)+directionC);
			}
			
			//Mise a jour des compteurs
			Delay.msDelay(10);
			compteur = compteur + 1;
			if (compteur == 10 ){
				pret = true;
			}
			
		}
		
		//On libere les ressources
		droite.close();
		gauche.close();
		gyro.close();
		
	}
	
	public static void main(String[] args){
		
		//Main permettant de faire nos tests sur l'équilibre
		Equilibre test = new Equilibre();
		test.start();
		test.setVitesse(3.5);
	}
	
}
