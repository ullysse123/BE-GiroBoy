package ia;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class AEtoile {
	
	private static int dernierSens=0;
	
	private static List <Sommet> insererLesFils (List <Sommet> listAttentePrec, List <Sommet> listFils, List<Sommet> listVu) {
		List <Sommet> listAttente= listAttentePrec;
		Iterator<Sommet>filsIterator=listFils.iterator();
		//Parcours des fils
		while(filsIterator.hasNext()) {
			Sommet filsATraiter=filsIterator.next();
			Boolean find=false;
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
			dernierSens=graph.whereDoYouCome(pere.getNom(),fils.getNom());
			for(int i:graph.voisin(fils.getNom(),dernierSens)) {
				sommetPetitFils.add(new Sommet(i,0,j,graph.whereDoYouCome(sommetPere.getNom(), i)));
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
		List <Sommet> listVu=new ArrayList<Sommet>();
		Boolean estBut=false;
		List <Sommet> listAttente=new ArrayList <Sommet>();
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
				//rajoute la direction fin
				directionList=pereActuelle.getListDirection();
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
	
	public static List <Integer> mainProgram (int numDepart,int numFin,Graph graph, Heuristique hb){
		//Initialisation necessaire pour l'initialisation de AEtoile
		List <Integer> listA,listB;
		List <Integer> listFinal=new ArrayList<Integer>();
		Sommet depart=new Sommet(numDepart,0,2,dernierSens);
		int dernierSensOppose=(dernierSens+1)%2;
		Sommet departOppose=new Sommet(numDepart,0,2,dernierSensOppose);
		List <Sommet> sommetPetitFils=new ArrayList<Sommet>();
		List <Sommet> sommetPetitFilsOppose=new ArrayList<Sommet>();
		sommetPetitFilsConstruct(graph, depart, sommetPetitFils);
		sommetPetitFilsConstruct(graph, departOppose, sommetPetitFilsOppose);
		depart.setFilsList(sommetPetitFils);
		departOppose.setFilsList(sommetPetitFilsOppose);
		listA=initAEtoile(graph,depart,1,numFin,hb);
		listB=initAEtoile(graph,departOppose,1,numFin,hb);
		if(listA.size()>listB.size()) {
			listFinal.add(2);//Pour le demi-tour
			listFinal.addAll(listB);
	    }else {
	    	listFinal=listA;
		}
		return listA;
	}

	private static void sommetPetitFilsConstruct(Graph graph, Sommet depart, List<Sommet> sommetPetitFils) {
		int j=1;
		for(int i:graph.voisin(depart.getNom(),dernierSens)) {
			sommetPetitFils.add(new Sommet(i,0,j,graph.whereDoYouCome(depart.getNom(), i)));
			j--;
		}
	}
	
	//Fonction Test
	public static void main(String[] args) {
		
		Heuristique hb=new HeuristiqueBase();
		Heuristique hb2=new HeuristiqueBase2();
		Graph graph=new Graph();
		List <Integer> list=mainProgram(1,6,graph,hb);
		List <Integer> list2=mainProgram(6,1,graph,hb2);;
		list.addAll(list2);
		list.add(-1);
		System.out.println("List direction:");
		for(int i:list) {
			System.out.println(i);
		}
	}
}
