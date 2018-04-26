package robot;

import lejos.hardware.Button;
import lejos.hardware.Sound;
import lejos.utility.Delay;
import modules.ColorSensor;
import modules.LightSensor;
import java.util.List;
import java.util.ArrayList;

public class Robot2 {

	static ColorSensor color;
	static LightSensor light;
	
	//liste d'entier indiquant les direction a prendre
	static List<Integer> listDirection;
	
	//Fonction permettant de savoir si le capteur de lumière est sur la ligne
	public static boolean lightSurLigne(){
		float seuil = 0.4f;
		return seuil<light.getModeRouge();
	}
	
	//Fonction permettant de savoir si le capteur de couleur est sur la ligne
	public static boolean colorSurLigne(){
		float seuil = 0.0385f;
		return seuil<color.getRedMode();
	}
	
	//Fonction permettant de savoir si on capteur la ligne sur le capteur droit, ou gauche ou les deux
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
				if(lightSurLigne()){
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
				if(colorSurLigne()){
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
				if(colorSurLigne()){
					eq.setDirection(directionR);
				}else{
					if(lightSurLigne())
						eq.setDirection(-directionR);
					else
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
					if(lightSurLigne()){
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
					if(colorSurLigne()){
						eq.setDirection(direction);
					}else{
						if(lightSurLigne())
							eq.setDirection(-directionR);
						else
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
		Delay.msDelay(950);
		eq.setDirection(0);
	}
	
	//Attention : fort risque d'erreur. Si carrefour marche pas revoir cette fonction
	//Fonction determinant si on sort oui ou non d'un carrefour
	public static boolean sortieCarrefour(int nbPassage, int prev, int cour){
		boolean retour = false;
		retour = (nbPassage >= 5) && (prev == 1) && (cour == 0);
		return retour;
	}
	
	//Fonction permettant au robot de franchir un carrefour
	//x = 1 prendre la branche droite du carrefour || x = 0 brandre la branche gauche du carrefour
	public static void carrefour(int x, Equilibre eq){
		
		//On fixe la vitesse a 3.5 et la direction a 0
		eq.setVitesse(4);
		eq.setDirection(0);
		
		//Etat noir = 0 || Etat blanc = 1
		int etatPrev = -1;
		int etatCour = -1;
		int coul = -1;
		int nbPassage = 0;
		int nbIterations = 0;
		
		//Correction appliqué pour la direction
		int direction = 12;
		int directionR = 4;
		
		if (x==0 || x==1){
			while(!(sortieCarrefour(nbPassage,etatPrev,etatCour)) && !Button.ENTER.isDown()){
				
				if (x==0){
					//Si on tourne a gauche
					//On regarde sur quel couleur est notre capteur droit
					if(colorSurLigne()){
						coul = 1;
					}else{
						coul = 0;
					}
					//Si la couleur capté est differente de la couleur courante alors on met a jour
					if(coul != etatCour && nbPassage <=6){
						//nbIteration limiteur pour eviter de prendre en compte les corrections de trajetoire comme changement d'etat
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
					}
					//On fait maintenant les correctif de suivie de ligne
					if(lightSurLigne()){
						eq.setDirection(-direction);
					}else{
						if(colorSurLigne())
							eq.setDirection(directionR);
						else
							eq.setDirection(0);
					}
				}else{
					//Si on tourne a droite
					//On regarde sur quel couleur est notre capteur gauche
					if(lightSurLigne()){
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
					if(colorSurLigne()){
						eq.setDirection(direction);
					}else{
						if(lightSurLigne())
							eq.setDirection(-directionR);
						else
							eq.setDirection(0);
					}
					
				}
				Delay.msDelay(100);
			}
			
			//Bip sonnor indiquant la sortie d'un carrefour
			//Sound.playTone(600, 10, 10);
			
			//Une fois sur l'embranchement final on laisse avancer le robot pour qu'il sorte de la double ligne
			Delay.msDelay(750);
			
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
		int direction = 0;
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
		eq.setVitesse(3);
		
		while(!Button.ESCAPE.isDown() && onContinu){
			
			switch(sensLigne()){
				
				case 0: //Case ou aucun des capteur ne capte la ligne on avance
						//Remise a 0 des compteur et de la direction
						//System.out.println("Entre les lignes");
						nbPassageVirageDroite = 0;
						nbPassageVirageGauche = 0;
						direction=0;
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
						//Vitesse fixé a 2.2 pour les virages et direction a 5
						vitesse = 2.2;
						if(nbPassageVirageGauche == 0){
							direction = 8;
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
						//Vitesse fixé a 2.2 pour les virage et direction a 5
						vitesse = 2.2;
						if(nbPassageVirageDroite == 0){
							direction = 8;
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
						//Recuperation de la direction
						leftright = listDirection.get(par).intValue();
						par++;
						if(leftright!=-1){
							//Marqueur sonor pour indiqué l'entrée dans un carrefour
							Sound.buzz();
							carrefour(leftright,eq);
							Sound.buzz();
						}else{
							eq.setVitesse(0);
							eq.setDirection(0);
							onContinu = false;
						}
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
	
	public static List<Integer> instanceListDirectionCompet(){
		List<Integer> list = new ArrayList<>();
		list.add(1);
		list.add(0);
		list.add(1);
		list.add(0);
		list.add(-1);
		return list;
	}
	
	public static void main(String[] args) {
		listDirection = instanceListDirectionCompet();
		suiveurDeLigne();

	}

}
