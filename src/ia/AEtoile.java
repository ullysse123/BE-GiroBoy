package ia;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class AEtoile {
	
	private static int dernierSens;//Permet de recuperer le sens dans aetoile et pour aetoile
	
	private static List <Sommet> insererLesFils (List <Sommet> listAttentePrec, List <Sommet> listFils, List<Sommet> listVu) {
		List <Sommet> listAttente= listAttentePrec;
		Iterator<Sommet>filsIterator=listFils.iterator();
		
		//Parcours des fils
		while(filsIterator.hasNext()) {
			Sommet filsATraiter=filsIterator.next();
			Boolean find=false;
			
			//Si il n'est présent dans la liste Vu
			if(!estPresent(filsATraiter,listVu)) {
				
				//Parcours de la liste d'attente à trier
				for(int i=0;i<listAttente.size() && !find; i++) {
					Sommet attenteATraiter=listAttente.get(i);
					
					//si on a trouvé la place pour le fils, le placer dans la liste
					if(find = filsATraiter.getGH()<attenteATraiter.getGH()) {
						listAttente.add(i, filsATraiter);
					}
				}
				
				if(!find) {
					listAttente.add(filsATraiter);
				}
			}
		}
		return listAttente;
	}
	
	private static Sommet creationFils (Graph graph,Sommet sommetPere,int cout,Heuristique heur) {
		Sommet pere=sommetPere;
		List <Sommet> sommetFils=new ArrayList<Sommet>();
		
		for(Sommet fils:pere.getFilsList()) {
			int nomFils=fils.getNom();
			fils.setG(pere.getG()+cout);
			fils.setGh(heur.fonction(fils)+fils.getG());
			
			//Permet de savoir le trajet à la fin
			if(!pere.getListDirection().isEmpty())
				fils.getListDirection().addAll(pere.getListDirection());
			
			fils.getListDirection().add(fils.getDirection());
			List <Sommet> sommetPetitFils=new ArrayList<Sommet>();
			int j=1;
			
			//Creation des petits fils pour creer le fils
			//Direction:1er j=1 droite, 2nd j=0 gauche, 3nd j=2 demi-tour (dans la liste)
			dernierSens=graph.whereDoYouCome(pere.getNom(),nomFils);
			
			for(int i:graph.voisin(nomFils,dernierSens)) {
				//Le sens du petit fils est determiné par le nom du fils et du nom du petit fils
				sommetPetitFils.add(new Sommet(i,j,graph.whereDoYouCome(nomFils, i)));
				j--;
			}
			
			fils.setFilsList(sommetPetitFils);
			sommetFils.add(fils);
		}
		
		pere.setFilsList(sommetFils);
		return pere;
	}
	
	private static Boolean estPresent (Sommet sommet, List <Sommet> listVu ) {
		Boolean estPresent=false;
		int i;
		for(i=0;i<listVu.size() && !estPresent;i++) {
			Sommet s=listVu.get(i);
			estPresent=sommet.getNom()==s.getNom() && sommet.getGH()>s.getGH();
		}
		return estPresent;
	}
	//Fonction d'initialisation du A*
	private static List<Integer> initAEtoile (Graph graph,Sommet sommetDepart,int cout,int but,Heuristique heur) {
		List <Integer> directionList=new ArrayList<Integer>();
		List <Sommet> listVu=new ArrayList<>();
		Boolean estBut=false;
		List <Sommet> listAttente=new ArrayList <Sommet>();
		dernierSens=sommetDepart.getSens();
		directionList = aEtoile(graph, sommetDepart, cout, but, heur, directionList, listVu, estBut, listAttente);
		return directionList;
	}

	//Implementation de A*
	private static List<Integer> aEtoile(Graph graph, Sommet sommetDepart, int cout, int but, Heuristique heur,
			List<Integer> directionList, List<Sommet> listVu, Boolean estBut, List<Sommet> listAttente) {
		Sommet pereActuelle=creationFils(graph,sommetDepart,cout,heur);
		listAttente=insererLesFils(listAttente , pereActuelle.getFilsList(),listVu);
		listVu.add(pereActuelle);
		//Parcours de la liste d'attente
		while(!listAttente.isEmpty() && !estBut) {
			estBut=pereActuelle.getNom()==but;
			//si but alors chemin trouvé
			if(estBut) {
				//rajoute la direction
				directionList=pereActuelle.getListDirection();
				directionList.add(0,pereActuelle.getGH());//permet de savoir le cout du chemin
				dernierSens=pereActuelle.getSens();
			//Sinon continue de parcourir
			}else {
				pereActuelle=creationFils(graph,listAttente.get(0),cout,heur);
				listAttente.remove(0);
				listVu.add(pereActuelle);
				listAttente=insererLesFils(listAttente , pereActuelle.getFilsList(),listVu);
			}
		}
		return directionList;
	}
	
	
	
	private static List <Integer> chemin (int numDepart,int numFin,Graph graph, Heuristique h,int sens){
		//Initialisation necessaire pour l'initialisation de AEtoile
		List <Integer> listA,listB;
		List <Integer> listFinal=new ArrayList<Integer>();
		
		Sommet depart=new Sommet(numDepart,2,sens);//Un sommet prend 3 parametres le nom du sommet (int), 
		//la direction du robot par rapport à une intersection(int)(ici 2 car initialisateur),
		//et le sens du robot sur la ligne (ou sommet) (int)
		
		int dernierSensOppose=(sens+1)%2;//on regarde aussi du sens oppose
		Sommet departOppose=new Sommet(numDepart,2,dernierSensOppose);
		
		List <Sommet> sommetPetitFils=new ArrayList<Sommet>();
		List <Sommet> sommetPetitFilsOppose=new ArrayList<Sommet>();
		
		sommetPetitFilsConstruct(graph, depart, sommetPetitFils);
		sommetPetitFilsConstruct(graph, departOppose, sommetPetitFilsOppose);
		
		depart.setFilsList(sommetPetitFils);
		departOppose.setFilsList(sommetPetitFilsOppose);
		
		listA=initAEtoile(graph,depart,1,numFin,h);
		int dernierSensA=dernierSens;//dernierSens en global car on ne peux pas renvoyer deux variables en java
		
		listB=initAEtoile(graph,departOppose,1,numFin,h);
		int dernierSensB=dernierSens;
		
		//On prend le cout present dans la liste et on dépile
		int coutA=listA.get(0);
		listA.remove(0);
		
		int coutB=listB.get(0)+h.fonction(depart);//On prend en compte le cout d'un demi-tour
		listB.remove(0);
		
		if(coutA>coutB) {
			listFinal.add(dernierSensB);//Rajouter le sens pour l'utiliser dans la fonction ou on retourne la liste
			listFinal.add(coutB);//Rajouter le cout 
			listFinal.add(2);//Pour le demi-tour
			listFinal.addAll(listB);//Rajouter la liste de direction
	    }else {//Faire pareil avec A si c'est le plus avantageux
	    	listFinal.add(dernierSensA);
	    	listFinal.add(coutA);
	    	listFinal.addAll(listA);
		}
		
		return listFinal;
	}
	
	//TODO: Sens à entrer en paramètre
	public static List <Integer> mainProgram (int nbVictimeTransportable,int debut, List <Integer> hopitaux, List<Integer> victimes,Graph graph, Heuristique h,int sens){
		int pointDeDepart=debut;
		int sensActuelle;
		int meilleurSens=0;
		List <Integer> retour=new ArrayList<Integer>();
		List <Integer >listSave=new ArrayList<Integer>();
		sensActuelle=sens;
		parcoursVictimes(nbVictimeTransportable, hopitaux, victimes, graph, h, pointDeDepart, sensActuelle,
				meilleurSens, retour, listSave);
		return retour;
	}

	private static void parcoursVictimes(int nbVictimeTransportable, List<Integer> hopitaux, List<Integer> victimes,
			Graph graph, Heuristique h, int pointDeDepart, int sensActuelle, int meilleurSens, List<Integer> retour,
			List<Integer> listSave) {
		Boolean hopitalPlusProche;
		int pointDeDepartSuivant;
		int indexVictimeSauve;
		int meilleurCout;
		int coutActuelle;
		int sensFils;
		List<Integer> listActuelle;
		
		//Tant qu'on n'a pas sauve tous les victimes
		while(!victimes.isEmpty()) {
			pointDeDepartSuivant=-1;
			hopitalPlusProche=false;
			
			//Permet le transport de plusieurs victimes
			for(int i=0;i<nbVictimeTransportable && !victimes.isEmpty() && !hopitalPlusProche ;i++) {
				meilleurCout=-1;
				indexVictimeSauve=0;
				
				//Parcours des victimes à sauver
				for(int j=0;j<victimes.size();j++) {
					int victime=victimes.get(j);
					listActuelle=chemin(pointDeDepart,victime,graph,h,sensActuelle);
					
					//Recuperer le sens dans le premier
					sensFils=listActuelle.get(0);
					listActuelle.remove(0);
					
					//Le second nombre de la liste est le cout
					coutActuelle=listActuelle.get(0);
					listActuelle.remove(0);
					
					//La premiere victime est l'initialisateur
					if(meilleurCout==-1) {
						meilleurCout=coutActuelle;
						listSave=listActuelle;
						pointDeDepartSuivant=victime;
						indexVictimeSauve=j;
						meilleurSens=sensFils;
					}else {
						//Sinon on compare et on change si le cout actuelle est meilleur
						if(meilleurCout>coutActuelle) {
							meilleurCout=coutActuelle;
							listSave=listActuelle;
							pointDeDepartSuivant=victime;
							indexVictimeSauve=j;
							meilleurSens=sensFils;
						}
					}
				}
				
				//Si il reste une victime et qu'il peut transporter ou que c'est la premiere victime (debut ou apres un hopital)
				if(victimes.size()==1 && (i+1)==nbVictimeTransportable || i==0) {
					//Le sauvegarde
					meilleurCout=-1;
					//Rajoute la liste sur la list de direction retourne
					retour.addAll(listSave);
					victimes.remove(indexVictimeSauve);
					pointDeDepart=pointDeDepartSuivant;
					sensActuelle=meilleurSens;
				}
				
				//Si ce n'est pas le premier ou qu'il a atteint la capacite max de transport
				if(i!=0 || (i+1)==nbVictimeTransportable) {
					
					//Regarde l'hopital le plus proche
					for(int hopital:hopitaux) {
						listActuelle=chemin(pointDeDepart,hopital,graph,h,sensActuelle);
						
						//Recuperer le sens dans le premier
						sensFils=listActuelle.get(0);
						listActuelle.remove(0);
						
						//Le second nombre de la liste est le cout
						coutActuelle=listActuelle.get(0);
						listActuelle.remove(0);
						
						//Le premier hopital initialise si il reste une victime et qu'il peut transporter ou que c'est la premiere victime (debut ou apres un hopital)
						if(hopitalPlusProche=meilleurCout==-1) {
							meilleurCout=coutActuelle;
							listSave=listActuelle;
							pointDeDepartSuivant=hopital;
							meilleurSens=sensFils;
						}else {
							
							//Sinon on compare et on change si le cout actuelle est meilleur (victime ou hopital)
							if(hopitalPlusProche=meilleurCout>coutActuelle) {
								meilleurCout=coutActuelle;
								listSave=listActuelle;
								pointDeDepartSuivant=hopital;
								meilleurSens=sensFils;
							}
						}
					}
					
					//Si ce n'est pas un hopital le plus proche dans le cas ou il n'a pas atteint le nombre de victime max (les autres sont la en cas d'erreur)
					if(!hopitalPlusProche && (i+1)!=nbVictimeTransportable && !victimes.isEmpty() && !(victimes.size()==indexVictimeSauve)) 
						victimes.remove(indexVictimeSauve);//on dit que la victime est sauve
					retour.addAll(listSave);
					pointDeDepart=pointDeDepartSuivant;
					sensActuelle=meilleurSens;
				}
			}
				
		}
		
		//-1 pour dire la fin
		retour.add(-1);
	}

	//Constructeur des petits fils d'un sommet
	private static void sommetPetitFilsConstruct(Graph graph, Sommet depart, List<Sommet> sommetPetitFils) {
		int j=1;
		for(int i:graph.voisin(depart.getNom(),depart.getSens())) {
			sommetPetitFils.add(new Sommet(i,j,graph.whereDoYouCome(depart.getNom(), i)));
			j--;
		}
	}
	
	//Fonction Test pour voir les directions
	public static void main(String[] args) {
		
		Heuristique h=new HeuristiqueGraph3();
		Graph graph=new Graph3();
		int sens=1;
		List<Integer>list;
		List<Integer>victimes=new ArrayList<>();
		List<Integer>hopitaux=new ArrayList<>();
		victimes.add(7);
		victimes.add(12);
		hopitaux.add(8);
		list=mainProgram(1,1,hopitaux,victimes,graph,h,sens);
		System.out.println("List direction:");
		for(int i:list) {
			System.out.println(i);
		}
	}
}
