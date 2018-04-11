package robot;

import modules.*;
import lejos.hardware.Button;
import lejos.hardware.Sound;
//import lejos.robotics.SampleProvider;
import lejos.utility.Delay;

public class Equilibre extends Thread{

	//Vitesse et direction ( sera utilisé plus tard )
	static double vitesse = 0; //[-100;100]//5,10
	static double direction = 0; //[-50;50]
	//Angle limite 
	static int angleLimite=45;
	//Modules utilisées pour maintenir le robot en equilibre
	static GyroSensor gyro=new GyroSensor();
	static UnregulatedLegMotor gauche = new UnregulatedLegMotor(1);
	static UnregulatedLegMotor droite = new UnregulatedLegMotor(0);
	
	public void setAngleLimite(int angle){
		Equilibre.angleLimite = angle;
	}
	
	public void setVitesse(int x){
		Equilibre.vitesse = x;
	}
	
	public void setDirection (int x){
		Equilibre.direction = x;
	}
	
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
		
		//Echantillons pour stabiliser
		//SampleProvider gyroReader = gyro.getRateMode();
		//float[] echantillon = new float[gyroReader.sampleSize()];
		
		
		//Signal sonore indiquant que les données sont initialisées
		Sound.beepSequenceUp();
		
		//On donne la priorité maximum a ce thread ( systeme vitale a notre fonctionnement )
		Thread.currentThread().setPriority(MAX_PRIORITY);
		
		//Debut de notre boucle de maintien de l'equilibre
		while(!Button.ESCAPE.isDown() && gyro.getAngle()<angleLimite && gyro.getAngle()>-angleLimite){
			
			//Mise a jour de nos infos temporelles
			chrono = System.nanoTime();
			delta = (chrono - dernierChrono)/1000000000.0;
			dernierChrono = chrono;
			
			//Mise a jour des données sur l'angle du robot
			//gyro.chercherEchantillon(echantillon, 0);
			vitesseAngulaire =-gyro.getAngularSpeed(); //-echantillon[0];
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
			
			//Mise a jour de la puissance(avec test)
			//puissance = (0.08 * vitesseMoteur) + (0.12 * angleMoteur) + (0.8 * vitesseAngulaire) + (15 * angle);
			//puissance = (0.08 * vitesseMoteur) + (0.12 * angleMoteur) + (0.8 * vitesseAngulaire) + (16 * angle);
			//puissance = (0.08 * vitesseMoteur) + (0.12 * angleMoteur) + (0.8 * vitesseAngulaire) + (17 * angle);
			//puissance = (0.06 * vitesseMoteur) + (0.12 * angleMoteur) + (0.8 * vitesseAngulaire) + (18 * angle);
			//puissance = (0.08 * vitesseMoteur) + (0.11 * angleMoteur) + (0.8 * vitesseAngulaire) + (18 * angle);
			//puissance = (0.08 * vitesseMoteur) + (0.12 * angleMoteur) + (0.7 * vitesseAngulaire) + (18 * angle);
			//puissance = (0.08 * vitesseMoteur) + (0.12 * angleMoteur) + (0.8 * vitesseAngulaire) + (18 * angle);//reference base
			//puissance = (0.08 * vitesseMoteur) + (0.12 * angleMoteur) + (0.9 * vitesseAngulaire) + (18 * angle);
			//puissance = (0.08 * vitesseMoteur) + (0.12 * angleMoteur) + (0.8 * vitesseAngulaire) + (19 * angle);
			//puissance = (0.08 * vitesseMoteur) + (0.12 * angleMoteur) + (0.8 * vitesseAngulaire) + (20 * angle);
			//puissance = (0.08 * vitesseMoteur) + (0.12 * angleMoteur) + (0.8 * vitesseAngulaire) + (21 * angle);
			//puissance = (0.08 * vitesseMoteur) + (0.12 * angleMoteur) + (0.8 * vitesseAngulaire) + (23 * angle);
			//puissance = (0.08 * vitesseMoteur) + (0.12 * angleMoteur) + (0.8 * vitesseAngulaire) + (25 * angle);
			
			
			//puissance = (0.08 * vitesseMoteur) + (0.12 * angleMoteur) + (0.8 * vitesseAngulaire) + (15 * angle);
			//puissance = (0.09 * vitesseMoteur) + (0.12 * angleMoteur) + (0.8 * vitesseAngulaire) + (15 * angle);//<
			//puissance = (0.1 * vitesseMoteur) + (0.12 * angleMoteur) + (0.8 * vitesseAngulaire) + (15 * angle);
			//puissance = (0.07 * vitesseMoteur) + (0.12 * angleMoteur) + (0.8 * vitesseAngulaire) + (15 * angle);
			//puissance = (0.06 * vitesseMoteur) + (0.12 * angleMoteur) + (0.8 * vitesseAngulaire) + (15 * angle);
			//puissance = (0.08 * vitesseMoteur) + (0.11 * angleMoteur) + (0.8 * vitesseAngulaire) + (15 * angle);//<
			//puissance = (0.08 * vitesseMoteur) + (0.1 * angleMoteur) + (0.8 * vitesseAngulaire) + (15 * angle);
			//puissance = (0.08 * vitesseMoteur) + (0.13 * angleMoteur) + (0.8 * vitesseAngulaire) + (15 * angle);
			//puissance = (0.08 * vitesseMoteur) + (0.14 * angleMoteur) + (0.8 * vitesseAngulaire) + (15 * angle);
			//puissance = (0.08 * vitesseMoteur) + (0.12 * angleMoteur) + (0.75 * vitesseAngulaire) + (15 * angle);//<
			//puissance = (0.08 * vitesseMoteur) + (0.12 * angleMoteur) + (0.7 * vitesseAngulaire) + (15 * angle);
			//puissance = (0.08 * vitesseMoteur) + (0.12 * angleMoteur) + (0.85 * vitesseAngulaire) + (15 * angle);
			//puissance = (0.08 * vitesseMoteur) + (0.12 * angleMoteur) + (0.9 * vitesseAngulaire) + (15 * angle);
			//puissance = (0.08 * vitesseMoteur) + (0.12 * angleMoteur) + (0.8 * vitesseAngulaire) + (15.5 * angle);
			//puissance = (0.08 * vitesseMoteur) + (0.12 * angleMoteur) + (0.8 * vitesseAngulaire) + (16 * angle);//<
			//puissance = (0.08 * vitesseMoteur) + (0.12 * angleMoteur) + (0.8 * vitesseAngulaire) + (14.5 * angle);
			//puissance = (0.08 * vitesseMoteur) + (0.12 * angleMoteur) + (0.8 * vitesseAngulaire) + (14 * angle);
			puissance = (0.08 * vitesseMoteur) + (0.12 * angleMoteur) + (0.8 * vitesseAngulaire) + (15 * angle);
			
			if (puissance > 100){
				puissance = 100;
			}
			if (puissance<-100){
				puissance = -100;
			}
			
			//Application de la puissance aux moteurs
			if(pret){
				droite.setPower((int)(puissance-direction));
				gauche.setPower((int)(puissance+direction));
				//Mise a jour des données sur l'angle du robot
				//vitesseAngulaire = -gyro.getAngularSpeed();
				//angle = angle + (vitesseAngulaire*delta);
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
		
		Equilibre test = new Equilibre();
		test.start();
		test.setVitesse(10);
		Delay.msDelay(5);
		test.setDirection(5);
		
	}
	
}
