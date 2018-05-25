package robot;

import lejos.hardware.Button;
import lejos.hardware.Sound;
import lejos.hardware.lcd.LCD;
import lejos.utility.Delay;
import modules.ColorSensor;
import java.util.List;
import java.util.Random;


import java.util.ArrayList;
import ia.*;

public class RobotV3{

	static final int DROITE = 0;
	static final int GAUCHE = 1;
	static final float SEUIL = 0.020f;
	
	static ColorSensor colorDroite;
	static ColorSensor colorGauche;//Remplacer par Light Sensor si pas de color sensor
	static Equilibre eq= new Equilibre();
	
	//liste d'entier indiquant les direction a prendre
	static List<Integer> listDirection;
	
	//Fonction permettant de savoir si le capteur de couleur est sur la ligne
	public static boolean colorSurLigne(int i){
		if (i==DROITE){
			return SEUIL<colorDroite.getRedMode();
		}else{
			return SEUIL<colorGauche.getRedMode();
		}
	}
	
	//Fonction permettant de savoir si on capteur la ligne sur le capteur droit, ou gauche ou les deux
	public static int sensLigne(){
		
		int i = 0;
		
		if(colorSurLigne(GAUCHE)){
			i++;
		}
		if(colorSurLigne(DROITE)){
			i+=2;
		}
		return i;	
	}
	
	//TODO : Fonction marqueSol
	//Objectif de la fonction : Definir si la marque est simple, double ou pleine
	//pour ensuite lancer le carrefour, l'arret/demi-tour ou le depacement.
		
	//TODO : Fonction depacement
	//Objectif de la fonction : faire emprunter les axes de depacement par le robot
	public static boolean finDepacement(int type,int prevL,int courL,int nbPassageL, int prevR, int courR, int nbPassageR){
		if (type == 0){
			//Cas sans depacement
			return (nbPassageL >= 5) && (prevL == 1) && (courL == 0) && (nbPassageR >= 5) && (prevR ==1) && (courR == 0);
		}else{
			if(type == 1){
				//Cas avec depacement
				return (nbPassageL >= 7) && (prevL == 1) && (courL == 0);
			}else{
				return false;
			}
		}
	}
		
	public static void depacement(int x, Equilibre eq){
		//x = 0 on ne cherche pas a depasser, ligne droite
		//x = 1 on depace
		
		//  /!\ /!\ CODE NON SIMPLIFIE : Le code ci dessous est volontairement non simplifie. Ne pouvant pas le tester j'ai decide de le laisser
		//								 non simplife afin de faciliter le debuggage et la lecture de celui ci.
		
		//J'utilise trop de variable ici volontairement, ce code etant experimental j'ai besoin d'un maximum de clarte et de partitionnement
		int nbPassage = 0;
		int nbIterations = 0;
		int coul = -1;
		int coulL = -1;
		int coulR = -1;
		int etatCourL = -1;
		int etatCourR = -1;
		int etatPrevL = -1;
		int etatPrevR = -1;
		int nbPassageR = 0;
		int nbPassageL = 0;
		int nbIterationsL = 0;
		int nbIterationsR = 0;
		int etatCour = -1;
		int etatPrev = -1;
		
		//Correction applique pour la direction
		int direction = 9;
		int directionR = 4;
		
		if(x == 0){
			//On ne cherche pas a depacer
			eq.setVitesse(5);
			while(!finDepacement(x,etatPrevL,etatCourL,nbPassageL,etatPrevR,etatCourR,nbPassageR)){
				//On va faire un comptage d'etat du cote droit puis du cote gauche
				//Comptage du cote gauche
				if(colorSurLigne(GAUCHE)){
					coulL = 1;
				}else{
					coulL = 0;
				}
				//Si la couleur capte est differente de la couleur courante alors on met a jour
				if(coulL != etatCourL && nbPassageL <= 5){
					if(nbIterationsL >= 3 && nbPassageL <=6){
						nbPassageL++;
						etatPrevL = etatCourL;
						etatCourL = coulL;
						nbIterationsL = 0;
					}else{
						if(nbIterationsL >= 1){
							nbPassageL++;
							etatPrevL = etatCourL;
							etatCourL = coulL;
							nbIterationsL = 0;
						}else{
							nbIterationsL++;
						}
					}
				}
				//Comptage cote droit
				if(colorSurLigne(DROITE)){
					coulR = 1;
				}else{
					coulR = 0;
				}
				//Si la couleur capte est differente de la couleur courante alors on met a jour
				if(coulR != etatCourR && nbPassageR <= 5){
					if(nbIterationsR >= 3 && nbPassageR <=6){
						nbPassageR++;
						etatPrevR = etatCourR;
						etatCourR = coulR;
						nbIterationsR = 0;
					}else{
						if(nbIterationsR >= 1){
							nbPassageR++;
							etatPrevR = etatCourR;
							etatCourR = coulR;
							nbIterationsR = 0;
						}else{
							nbIterationsR++;
						}
					}
				}
				//On fait maintenant les correctifs de trajectoire
				if(colorSurLigne(DROITE)){
					eq.setDirection(directionR);
				}else{
					eq.setDirection(0);
				}
				Delay.msDelay(100);
			}
		}else{
			if (x == 1){
				//On cherche a depasser par la droite. --> Meme problematique que pour tourner a droite sur une intersection
				eq.setVitesse(5);
				while(!finDepacement(x,etatPrev,etatCour,nbPassage,0,0,0)){
					//On regarde sur quel couleur est notre capteur gauche
					if(colorSurLigne(GAUCHE)){
						coul = 1;
					}else{
						coul = 0;
					}
					//Si  la couleur capte est differente de la couleur courante alors on met a jour
					if(coul != etatCour && nbPassage <=7){
						if(nbIterations >= 3 && nbPassage <=6){
							nbPassage++;
							etatPrev = etatCour;
							etatCour = coul;
							nbIterations = 0;
						}else{
							if(nbIterations >= 1){
								nbPassage++;
								etatPrev = etatCour;
								etatCour = coul;
								nbIterations = 0;
							}else{
								nbIterations++;
							}
						}
					}
					//On fait maintenant les correctif de suivie de ligne
					if(colorSurLigne(DROITE)){
						eq.setDirection(direction);
					}else{
						eq.setDirection(0);
					}
					Delay.msDelay(100);
				}
			}
		}
		
		
	}
		
	//Objectif de la fonction : Envoyer la suite de sequence permettant au robot de faire
	//un demi tour.
	public static void demiTour(Equilibre eq){
		eq.setVitesse(0);
		Delay.msDelay(20);
		eq.setDirection(50);
		Delay.msDelay(1050);
		eq.setDirection(0);
		Delay.msDelay(25);
		eq.setVitesse(2);
		Delay.msDelay(25);
	}
	
	//Attention : fort risque d'erreur. Si carrefour marche pas revoir cette fonction
	//Fonction determinant si on sort oui ou non d'un carrefour
	public static boolean sortieCarrefour(int nbPassage, int prev, int cour){
		return (nbPassage >= 5) && (prev == 1) && (cour == 0);
	}
	
	//Fonction permettant au robot de franchir un carrefour
	//x = 1 prendre la branche droite du carrefour || x = 0 prendre la branche gauche du carrefour
	public static void carrefour(int x, Equilibre eq){
		
		//On fixe la vitesse a 3.6 et la direction a 0
		eq.setVitesse(3.6);
		eq.setDirection(0);
		
		//Etat noir = 0 || Etat blanc = 1
		Boolean passageLong=false;
		int etatPrev = -1;
		int etatCour = -1;
		int coul = -1;
		int nbPassage = 0;
		int nbIterations = 0;
		int compteur=0;
		int nbReste=0;
		
		//Correction applique pour la direction
		int direction = 11;
		
		Delay.msDelay(700);
		if (x==0 || x==1){
			while(!(sortieCarrefour(nbPassage,etatPrev,etatCour)) && !Button.ENTER.isDown()){
				
				if (x==0){
					//Si on tourne a gauche
					//On regarde sur quel couleur est notre capteur droit
					if(colorSurLigne(DROITE)){
						coul = 1;
					}else{
						coul = 0;
					}
					//Si la couleur capte est differente de la couleur courante alors on met a jour
					if(coul != etatCour && nbPassage <=6){
						//nbIteration limiteur pour eviter de prendre en compte les corrections de trajectoire comme changement d'etat
						if(nbIterations >=3 && nbPassage<=5){
							nbPassage++;
							etatPrev = etatCour;
							etatCour = coul;
							nbIterations = 0;
						}else{
							if(nbIterations >= 1){
								nbPassage++;
								etatPrev = etatCour;
								etatCour = coul;
								nbIterations = 0;
							}else{
								nbIterations++;
							}
						}
						nbReste=0;
					}else {
						//Si coul constant
						if(coul==etatCour) {
							nbReste++;//On compte depuis combien de temps on est
							passageLong=nbReste>=5;//On est sur un passage long si on depasse un certain seuil
						}else
							nbReste=0;
					}

					
					//On fait maintenant les correctif de suivie de ligne
					if(colorSurLigne(GAUCHE) && !(sortieCarrefour(nbPassage,etatPrev,etatCour))){
						eq.setDirection(-direction);
						nbReste=0;
					}else{
						if(!passageLong && compteur==0)
							eq.setDirection(0);
						else {//Si on est sur un passage long, correction de trajectoire
							compteur++;
							eq.setDirection(7);
							if(compteur>=2) {
								nbReste=0;
								compteur=0;
							}
						}
					}
				}else{
					//Si on tourne a droite
					//On regarde sur quel couleur est notre capteur gauche
					if(colorSurLigne(GAUCHE)){
						coul = 1;
					}else{
						coul = 0;
					}
					//Si  la couleur capte est differente de la couleur courante alors on met a jour
					if(coul != etatCour && nbPassage <=6){
						if(nbIterations >= 3 && nbPassage <=5){
							nbPassage++;
							etatPrev = etatCour;
							etatCour = coul;
							nbIterations = 0;
						}else{
							if(nbIterations >= 1){
								nbPassage++;
								etatPrev = etatCour;
								etatCour = coul;
								nbIterations = 0;
							}else{
								nbIterations++;
							}
						}
						nbReste=0;
					}else {
						//Si coul constant
						if(coul==etatCour) {
							nbReste++;//On compte depuis combien de temps on est
							passageLong=nbReste>=5;//On est sur un passage long si on depasse un certain seuil
						}else
							nbReste=0;
					}
					//On fait maintenant les correctif de suivie de ligne
					if(colorSurLigne(DROITE) && !(sortieCarrefour(nbPassage,etatPrev,etatCour))) {
						eq.setDirection(direction);
						nbReste=0;
					}else{
						if(!passageLong && compteur==0)
							eq.setDirection(0);
						else {//Si on est sur un passage long, correction de trajectoire
							compteur++;
							eq.setDirection(-7);
							if(compteur>=2) {
								nbReste=0;
								compteur=0;
							}
						}
					}
					
				}
				Delay.msDelay(75);
				//compteur=(compteur+1)%3;
			}
			
			//Une fois sur l'embranchement final on laisse avancer le robot pour qu'il sorte de la double ligne
			compteur=0;
			while(compteur<=8) {
				switch(sensLigne()) {
					case 0: //Case ou aucun des capteur ne capte la ligne on avance
						eq.setDirection(0);
						break;
					case 1: //Cas ou seul le capteur gauche capte la ligne
							eq.setDirection(-direction);
							break;
							
					case 2: //Cas ou seul le capteur droite capte la ligne 
							eq.setDirection(direction);
							break;
							
					case 3: //Cas ou les deux capteur captent la ligne 
							eq.setDirection(0);
							break;
							
					default:
				}
				Delay.msDelay(100);
				compteur++;
			}
		}else{
			Sound.playTone(800, 10, 10);
			System.out.println("/!\\ ERREUR CARREFOUR /!\\ \n\n\n");
		}
		
	}
	
	//TODO Passage a niveau : Si jamais on a deux double lignes, passage a niveau est appele via carefour et il faut choisir ce qu'on fait.
	//Aller tout droit ou eviter un robot
	
	public static void suiveurDeLigne(){
		
		
		//Ensemble de nos variables permettant de fixer la vitesse et la direction
		double vitesse = 3.4;
		double vitesseVirage = 3.7;
		double vitesseLigne = 3.7;
		int direction = 0;
		int directionVirage = 6;
		int nbPassageVirageDroite = 0;
		int nbPassageVirageGauche = 0;
		int nbPassageLigneDroite = 0;
		
		//Variable pour le carrefour
		int leftright = -1;
		int par = 0;
		
		//Booleen d'arret
		boolean onContinu = true;
		
		//Lancement de l'equilibre avec une vitesse initiale pour demarrer le circuit
		eq.start();
		eq.setVitesse(vitesse);
		
		while(!Button.ESCAPE.isDown() && onContinu){
			
			//Nb negatif >5 utilise pour les beeps
			leftright = listDirection.get(par).intValue();
			if(leftright<0 && leftright!=-5) {
				for(int i=leftright;i<0;i++) {
					Sound.beepSequenceUp();
					Delay.msDelay(10);
				}
				par++;
			}
			switch(sensLigne()){
				
				case 0: //Case ou aucun des capteur ne capte la ligne on avance
						//Remise a 0 des compteur et de la direction
						nbPassageVirageDroite = 0;
						nbPassageVirageGauche = 0;
						direction=0;
						//Vitesse fixe a 3.7 au depart
						if(nbPassageLigneDroite == 0) {
							vitesse = vitesseLigne;
						}
						eq.setVitesse(vitesse);
						Delay.msDelay(15);
						eq.setDirection(direction);
						break;
				case 1: //Cas ou seul le capteur gauche capte la ligne
						//Remise a 0 des compteur
						nbPassageLigneDroite = 0;
						nbPassageVirageDroite = 0;
						//Vitesse fixe a 3.7 pour les virages et direction a 6
						vitesse = vitesseVirage;
						if(nbPassageVirageGauche == 0){
							direction = directionVirage;
						}
						//Augmentation incrementale de la direction
						nbPassageVirageGauche++;
						if(nbPassageVirageGauche<=10){
							direction+=2;
						}
						eq.setVitesse(vitesse);
						Delay.msDelay(15);
						eq.setDirection(-direction);
						break;
						
				case 2: //Cas ou seul le capteur droite capte la ligne 
						//Remise a 0 des compteur
						nbPassageVirageGauche = 0;
						nbPassageLigneDroite = 0;
						//Vitesse fixe a 3.7 pour les virage et direction a 6
						vitesse = vitesseVirage;
						if(nbPassageVirageDroite == 0){
							direction = directionVirage;
						}
						//Augmentation incrementale de la direction
						nbPassageVirageDroite++;
						if(nbPassageVirageDroite<=10){
							direction+=2;
						}
						eq.setVitesse(vitesse);
						Delay.msDelay(15);
						eq.setDirection(direction);
						break;
						
				case 3: //Cas ou les deux capteur captent la ligne on s'arrette	
						nbPassageLigneDroite = 0;
						nbPassageVirageDroite = 0;
						nbPassageVirageGauche = 0;
						//Reinitialisation de la direction
						eq.setDirection(0);
						//Recuperation de la direction
						leftright = listDirection.get(par).intValue();
						par++;
						if(leftright!=-5){
							if(leftright == 2){
								demiTour(eq);
							}else{
								//Marqueur sonor pour indique l'entree dans un carrefour
								Sound.buzz();
								carrefour(leftright,eq);
								Sound.buzz();
							}
						}else{
							eq.setVitesse(0);
							eq.setDirection(0);
							onContinu = false;
							Sound.beepSequenceUp();
							Delay.msDelay(100);
							Sound.beepSequenceUp();
							Delay.msDelay(100);
						}
						break;
						
				default : //Ne sortira jamais de [0;3] mais si c'est le cas alors stop vitesse/direction et beep sonore
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
			
			//Frequence d'echantillonage fixe pour les capteurs
			Delay.msDelay(75);
		}
		
	}
	
	//Permet d'instancier la liste de direction servant a la premiere competition
	public static List<Integer> instanceListDirectionCompet(){
		List<Integer> list = new ArrayList<>();
		int i=1,j;
		for(j=0;j<25;j++) {
			list.add(i);
			i=(i+1)%2;
		}
		list.add(-5);
		return list;
	}
	
	public static List<Integer> instanceListDirectionCompetInverse(){
		List<Integer> list = new ArrayList<>();
		int i=0,j;
		for(j=0;j<25;j++) {
			list.add(i);
			i=(i+1)%2;
		}
		list.add(-5);
		return list;
	}
	
	//Permet d'instancier une liste de direction aleatoire pour des tests
	public static List<Integer> instanceListDirectionRandom(){
		List<Integer> list = new ArrayList<>();
		Random rand = new Random();
		list.add(2);
		for(int i = 0;i<25;i++){
			if((rand.nextInt(2)%2) == 1){
				list.add(1);
			}else{
				list.add(0);
			}
		}
		list.add(-5);
		return list;
	}
	
	//Permet d'instancier une liste de direction permettant de nous rendre de facon optimise d'un point A a un point B
	public static List<Integer> instanceAEtoile(){
		Heuristique h=new HeuristiqueGraph3();
		Graph graph=new Graph3();
		int sens=1;
		List<Integer>list;
		List<Integer>victimes=new ArrayList<>();
		List<Integer>hopitaux=new ArrayList<>();
		//Test
		victimes.add(7);
		victimes.add(12);
		hopitaux.add(8);
		//Competition
		/*victimes.add(2);
		victimes.add(4);
		victimes.add(5);
		victimes.add(7);
		victimes.add(12);
		hopitaux.add(1);
		hopitaux.add(3);
		hopitaux.add(9);*/
		list=AEtoile.mainProgram(1,1,hopitaux,victimes,graph,h,sens);
		return list;
	}
	
	public static void main(String[] args) {
		Boolean marche=true;
		while (marche){
			if(marche=!choixDirection()) {
				if(!waitToStart()) {
					suiveurDeLigne();
					marche=false;
				}
			}
		}
	}
	
	//Partie Menu

	private static Boolean choixDirection() {
		int choix=1;
		Boolean abort=false;
		affichageDirection();
		while(!Button.ENTER.isDown() && !abort) {
			abort=Button.ESCAPE.isDown();
			//L'utilisateur choisit son programme à partir des fleches puis valide à l'aide de ENTER ou annule à l'aide de ECHAP
			switch(Button.readButtons()) {
				case Button.ID_UP:
					choix=1;
					LCD.clear(0);
					LCD.drawString("Choisir mode:U",1,0);
					break;
				case Button.ID_RIGHT:
					choix=2;
					LCD.clear(0);
					LCD.drawString("Choisir mode:R",1,0);
					break;
				case Button.ID_DOWN:
					choix=3;
					LCD.clear(0);
					LCD.drawString("Choisir mode:D",1,0);
					break;
				case Button.ID_LEFT:
					choix=4;
					LCD.clear(0);
					LCD.drawString("Choisir mode:L",1,0);
					break;
				default:
			}
			Delay.msDelay(150);
		}
		LCD.clear();
		if(!abort)listDirectionSelonChoix(choix);
		Delay.msDelay(150);
		return abort;
	}

	private static void listDirectionSelonChoix(int choix) {
		switch(choix) {
			case 1:
				listDirection = instanceAEtoile();
				break;
			case 2:
				listDirection = instanceListDirectionCompet();
				break;
			case 3:
				listDirection = instanceListDirectionRandom();
				break;
			default:
				listDirection = instanceListDirectionCompetInverse();
				
		}
	}

	private static void affichageDirection() {
		LCD.drawString("Choisir mode:U",1,0);
		LCD.drawString("-UP(U):AEtoile",0,1);
		LCD.drawString("-RIGHT(R):Dr/Gau",0,2);
		LCD.drawString("-LEFT(L):Gau/Dr",0,3);
		LCD.drawString("-DOWN(D):Aleatoire",0,4);
		LCD.drawString("Appuyer ENTER",2,5);
		LCD.drawString("si pret,",4,6);
		LCD.drawString("ESCAPE annule.",2,7);
	}


	private static Boolean waitToStart() {
		Boolean retour=false;
		int i=5;
		eq.getAngleReset();
		affichageWait(i);
		Delay.msDelay(100);
		while(!Button.ENTER.isDown() && !(retour=Button.ESCAPE.isDown())) {
			//L'utilisateur choisit le minuteur à partir des fleches puis valide à l'aide de ENTER ou revient en arrière à l'aide de ECHAP
			switch(Button.readButtons()) {
				case Button.ID_UP:
					i++;
					remplaceTimer(i);
					break;
				case Button.ID_DOWN:
					if(i-1>=1)i--;
					remplaceTimer(i);
					break;
				case Button.ID_RIGHT://Il peut aussi reset l'angle du gyro pour observer tout erreur de celui-ci
					eq.getAngleReset();
					break;
				default:;
			}
			LCD.clear(0);
			LCD.drawString("Angle:"+ eq.getAngle(),4,0);
			Delay.msDelay(150);
		}
		if(!retour) {
			initialisation(i);
		}
		Delay.msDelay(150);
		return retour;
	}

	private static void initialisation(int i) {
		LCD.clear();
		colorDroite = new ColorSensor(DROITE);
		colorGauche = new ColorSensor(GAUCHE);
		while(i>=1) {
			Sound.beep();
			i--;
			LCD.drawString((i+1)+"\n",7,4);
			Delay.msDelay(1000);
			LCD.clear();
		}
	}

	private static void affichageWait(int i) {
		LCD.drawString("Angle:"+ eq.getAngle(),4,0);
		LCD.drawString("Timer:"+i+"s",5,1);
		LCD.drawString("Haut/Bas",5,2);
		LCD.drawString("pour changer.",2,3);
		LCD.drawString("Rig:Reset Angle.",1,4);
		LCD.drawString("Appuyer ENTER",2,5);
		LCD.drawString("si pret,",4,6);
		LCD.drawString("ESCAPE retour.",2,7);
	}

	private static void remplaceTimer(int i) {
		LCD.clear(1);
		LCD.drawString("Timer:"+i+"s",5,1);
	}

}
