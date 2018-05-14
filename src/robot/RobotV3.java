package robot;

import lejos.hardware.Button;
import lejos.hardware.Sound;
import lejos.hardware.lcd.LCD;
import lejos.utility.Delay;
import modules.ColorSensor;

//import modules.LightSensor;
import java.util.List;
import java.util.Random;


import java.util.ArrayList;
import ia.*;

public class RobotV3{

	static final int DROITE = 0;
	static final int GAUCHE = 1;
	static final float SEUIL = 0.019f;
	
	static ColorSensor colorDroite;
	static ColorSensor colorGauche;//Light Sensor
	static Equilibre eq= new Equilibre();
	
	//liste d'entier indiquant les direction a prendre
	static List<Integer> listDirection;
	
	//Fonction permettant de savoir si le capteur de lumiere est sur la ligne
	/*public static boolean lightSurLigne(){
		return 0.4f<light.getModeRouge();
	}*/
	
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
		//x = 0 on ne cherche pas a depacer, ligne droite
		//x = 1 on depace
		
		//  /!\ /!\ CODE NON SIMPLIFIE : Le code ci dessous est volontairement non simplifié. Ne pouvant pas le testé j'ai décidé de le laisser
		//								 non simplifé afin de faciliter le debuggage et la lecture de celui ci.
		
		//J'utilise trop de variable ici volontairement, ce code etant experimentale j'ai besoin d'un maximum de clarté et de partitionnement
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
		
		//Correction appliqué pour la direction
		int direction = 9;
		int directionR = 4;
		
		if(x == 0){
			//On ne cherche pas a depacer
			eq.setVitesse(5);
			while(!finDepacement(x,etatPrevL,etatCourL,nbPassageL,etatPrevR,etatCourR,nbPassageR)){
				//On va faire un comptage d'etat du coté droit puis du coté gauche
				//Comptage du coté gauche
				if(colorSurLigne(GAUCHE)){
					coulL = 1;
				}else{
					coulL = 0;
				}
				//Si la couleur capté est différente de la couleur courante alors on met a jour
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
				//Comptage coté droit
				if(colorSurLigne(DROITE)){
					coulR = 1;
				}else{
					coulR = 0;
				}
				//Si la couleur capté est différente de la couleur courante alors on met a jour
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
				//On cherche a depacer par la droite. --> Meme problematique que pour tourner a droite sur une intersection
				eq.setVitesse(5);
				while(!finDepacement(x,etatPrev,etatCour,nbPassage,0,0,0)){
					//On regarde sur quel couleur est notre capteur gauche
					if(colorSurLigne(GAUCHE)){
						coul = 1;
					}else{
						coul = 0;
					}
					//Si  la couleur capté est différente de la couleur courante alors on met a jour
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
		
	//TODO : demi-tour
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
		Delay.msDelay(75);
	}
	
	//Attention : fort risque d'erreur. Si carrefour marche pas revoir cette fonction
	//Fonction determinant si on sort oui ou non d'un carrefour
	public static boolean sortieCarrefour(int nbPassage, int prev, int cour){
		return (nbPassage >= 5) && (prev == 1) && (cour == 0);
	}
	
	//Fonction permettant au robot de franchir un carrefour
	//x = 1 prendre la branche droite du carrefour || x = 0 brandre la branche gauche du carrefour
	public static void carrefour(int x, Equilibre eq){
		
		//On fixe la vitesse a 3.5 et la direction a 0
		eq.setVitesse(3.6);
		eq.setDirection(0);
		
		//Etat noir = 0 || Etat blanc = 1
		int etatPrev = -1;
		int etatCour = -1;
		int coul = -1;
		int nbPassage = 0;
		int nbIterations = 0;
		//int compteur=0;
		//int nbMaxCompteur=3;
		
		//Correction applique pour la direction
		int direction = 11;//9

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
							// /!\ LAST MODIF
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
					if(colorSurLigne(GAUCHE)){// && compteur==0)||(compteur>=1 && compteur<nbMaxCompteur)){
						eq.setDirection(-direction);
						//compteur++;
					}else{
						eq.setDirection(0);
						//compteur=0;
					}
				}else{
					//Si on tourne a droite
					//On regarde sur quel couleur est notre capteur gauche
					if(colorSurLigne(GAUCHE)){
						coul = 1;
					}else{
						coul = 0;
					}
					//Si  la couleur capté est différente de la couleur courante alors on met a jour
					if(coul != etatCour && nbPassage <=6){
						if(nbIterations >= 3 && nbPassage <=5){
							nbPassage++;
							etatPrev = etatCour;
							etatCour = coul;
							nbIterations = 0;
						}else{
							// /!\ LAST MODIF
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
					if(colorSurLigne(DROITE)){// && compteur==0)||(compteur>=1 && compteur<nbMaxCompteur)){
						eq.setDirection(direction);
						//compteur++;
					}else{
						eq.setDirection(0);
						//compteur=0;
					}
					
				}
				Delay.msDelay(100);
			}
			
			//Une fois sur l'embranchement final on laisse avancer le robot pour qu'il sorte de la double ligne
			if(x==0){
				eq.setDirection(3);
			}else{
				eq.setDirection(-3);
			}
			/*Delay.msDelay(25);
			eq.setVitesse(3.4);*/
			Delay.msDelay(800);
			
		}else{
			Sound.playTone(800, 10, 10);
			System.out.println("/!\\ ERREUR CARREFOUR /!\\ \n\n\n");
		}
	}
	
	//TODO Passage a niveau : Si jamais on a deux double lignes, passage a niveau est appelé via carefour et il faut choisir ce qu'on fait.
	//Aller tout droit ou eviter un robot
	
	public static void suiveurDeLigne(){
		
		
		//Ensemble de nos variables permettant de fixer la vitesse et la direction
		double vitesse = 3.4;
		double vitesseVirage = 3.7; //3.4
		double vitesseLigne = 3.7; //3.4
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
		
		//Lancement de l'équilibre avec une vitesse initiale pour démarrer le circuit
		eq.start();
		eq.setVitesse(vitesse);
		
		while(!Button.ESCAPE.isDown() && onContinu){
			
			switch(sensLigne()){
				
				case 0: //Case ou aucun des capteur ne capte la ligne on avance
						//Remise a 0 des compteur et de la direction
						nbPassageVirageDroite = 0;
						nbPassageVirageGauche = 0;
						direction=0;
						//Vitesse fixé a 3.4 au depart
						if(nbPassageLigneDroite == 0) {
							vitesse = vitesseLigne;
						}
						//Vitesse incrémentale pour augmenter la fluidité du déplacement
						/*nbPassageLigneDroite++;
						if(nbPassageLigneDroite<=10){
							vitesse+=0.2;
						}
						eq.setVitesse(vitesse);
						Delay.msDelay(15);*/
						eq.setDirection(direction);
						break;
				case 1: //Cas ou seul le capteur gauche capte la ligne
						//Remise a 0 des compteur
						nbPassageLigneDroite = 0;
						nbPassageVirageDroite = 0;
						//Vitesse fixé a 2.2 pour les virages et direction a 8
						vitesse = vitesseVirage;
						if(nbPassageVirageGauche == 0){
							direction = directionVirage;
						}
						//Augmentation incrémentale de la direction
						nbPassageVirageGauche++;
						if(nbPassageVirageGauche<=10){
							direction+=2;
						}
						//eq.setVitesse(vitesse);
						//Delay.msDelay(15);
						eq.setDirection(-direction);
						break;
						
				case 2: //Cas ou seul le capteur droite capte la ligne 
						//Remise a 0 des compteur
						nbPassageVirageGauche = 0;
						nbPassageLigneDroite = 0;
						//Vitesse fixé a 2.2 pour les virage et direction a 8
						vitesse = vitesseVirage;
						if(nbPassageVirageDroite == 0){
							direction = directionVirage;
						}
						//Augmentation incrémentale de la direction
						nbPassageVirageDroite++;
						if(nbPassageVirageDroite<=10){
							direction+=2;
						}
						//eq.setVitesse(vitesse);
						//Delay.msDelay(15);
						eq.setDirection(direction);
						break;
						
				case 3: //Cas ou es deux capteur captent la ligne on s'arrette	
						nbPassageLigneDroite = 0;
						nbPassageVirageDroite = 0;
						nbPassageVirageGauche = 0;
						//Recuperation de la direction
						leftright = listDirection.get(par).intValue();
						par++;
						if(leftright!=-1){
							if(leftright == 2){
								demiTour(eq);
							}else{
								//Marqueur sonor pour indiqué l'entrée dans un carrefour
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
			
			//Frequence d'échantillonage fixé pour les capteurs
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
		list.add(-1);
		return list;
	}
	
	public static List<Integer> instanceListDirectionCompetInverse(){
		List<Integer> list = new ArrayList<>();
		int i=0,j;
		for(j=0;j<25;j++) {
			list.add(i);
			i=(i+1)%2;
		}
		list.add(-1);
		return list;
	}
	
	//Permet d'instancier une liste de direction aleatoire pour des tests
	public static List<Integer> instanceListDirectionRandom(){
		List<Integer> list = new ArrayList<>();
		Random rand = new Random();
		for(int i = 0;i<25;i++){
			if(rand.nextInt(1) >= 0.5f){
				list.add(1);
			}else{
				list.add(0);
			}
		}
		list.add(-1);
		return list;
	}
	
	//Permet d'instancier une liste de direction permettant de nous rendre de façon optimisé d'un point A a un point B
	public static List<Integer> instanceAEtoile(){
		Heuristique hv=new HeuristiqueVictime1();
		Heuristique hv2=new HeuristiqueVictime2();
		Heuristique hh=new HeuristiqueHopital();
		Graph graph=new Graph2();
		List <Integer> list=AEtoile.mainProgram(1,3,graph,hv);
		List <Integer> list2=AEtoile.mainProgram(3,6,graph,hh);
		List <Integer> list3=AEtoile.mainProgram(6,12,graph,hv2);
		List <Integer> list4=AEtoile.mainProgram(12,6,graph,hh);
		list.addAll(list2);
		list.addAll(list3);
		list.addAll(list4);
		list.add(-1);
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
		listDirectionSelonChoix(choix);
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
			switch(Button.readButtons()) {
				case Button.ID_UP:
					i++;
					remplaceTimer(i);
					break;
				case Button.ID_DOWN:
					if(i-1>=1)i--;
					remplaceTimer(i);
					break;
				case Button.ID_RIGHT:
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
			Sound.buzz();
			i--;
			LCD.drawString((i+1)+"\n",0,0);
			Delay.msDelay(1000);
			LCD.clear();
		}
		Sound.beep();
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
