package ia;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class AEtoile {
	
	private static List <Sommet> insererLesFils (List <Sommet> listAttentePrec, List <Sommet> listFils, List<Sommet> listVu) {
		List <Sommet> listAttente= listAttentePrec;
		Iterator<Sommet>filsIterator=listFils.iterator();
		//Parcours des fils
		while(filsIterator.hasNext()) {
			Sommet filsATraiter=filsIterator.next();
			Boolean find=false;
			if(!listVu.contains(filsATraiter)) {
				//Parcours de la liste d'attente à trier
				for(int i=0;i<listAttente.size() && !find; i++) {
					Sommet attenteATraiter=listAttente.get(i);
					//si on a trouvé la place pour le fils
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
			fils.setGh(heur.fonction(fils));
			if(!pere.getListDirection().isEmpty())
				fils.getListDirection().addAll(pere.getListDirection());
			fils.getListDirection().add(fils.getDirection());
			List <Sommet> sommetPetitFils=new ArrayList<Sommet>();
			int j=0;
			//j pour la direction: 0 pour droite, 1 pour gauche, 2 pour demi-tour ( ou 2 et 3 pour dans le sens inverse, conversion de 2/3 en 0/1 avec modulo 2)
			for(int i:graph.voisin(fils.getNom())) {
				sommetPetitFils.add(new Sommet(i,cout,j));
				j++;
				if(j>2)j=2;
			}
			fils.setFilsList(sommetPetitFils);
			sommetFils.add(fils);
		}
		pere.setFilsList(sommetFils);
		return pere;
	}
	
	//Fonction aetoile
	public static List<Integer> fonction (Graph graph,Sommet sommetDepart,int cout,int but,Heuristique heur) {
		//Initialisation du A*
		List <Integer> directionList=new ArrayList<Integer>();
		List <Sommet> listVu=new ArrayList<Sommet>();
		Boolean estBut=false;
		List <Sommet> listAttente=new ArrayList <Sommet>();
		Sommet pereActuelle=creationFils(graph,sommetDepart,cout,heur);
		listAttente=insererLesFils(listAttente , pereActuelle.getFilsList(),listVu);
		listVu.addAll(pereActuelle.getFilsList());
		//Parcours de la liste d'attente
		while(!listAttente.isEmpty() && !estBut) {
			estBut=pereActuelle.getNom()==but;
			//si but alors chemin trouvé
			if(estBut) {
				directionList=pereActuelle.getListDirection();
			//Sinon continue de parcourir
			}else {
				pereActuelle=creationFils(graph,listAttente.get(0),cout,heur);
				//System.out.println(pereActuelle.getNom());
				for(Sommet somFils:pereActuelle.getFilsList()) {
					if(!listVu.contains(somFils))
						listVu.add(somFils);
				}
				listAttente.remove(0);
				listAttente=insererLesFils(listAttente , pereActuelle.getFilsList(),listVu);
			}
		}
		return directionList;
	}
	
	public static void main(String[] args) {
		Sommet depart=new Sommet(1,0,2);
		HeuristiqueBase hb=new HeuristiqueBase();
		List <Sommet> sommetPetitFils=new ArrayList<Sommet>();
		int j=0;
		Graph graph=new Graph();
		for(int i:graph.voisin(depart.getNom())) {
			sommetPetitFils.add(new Sommet(i,0,j));
			j++;
		}
		depart.setFilsList(sommetPetitFils);
		//System.out.println("List Sommet traité:");
		List <Integer> list=fonction(graph,depart,1,7,hb);
		System.out.println("List direction:");
		for(int i:list) {
			System.out.println(i);
		}
	}
}
