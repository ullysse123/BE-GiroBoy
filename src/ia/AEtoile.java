package ia;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class AEtoile {
	static Graph graph=new Graph();
	
	private static List <Sommet> insererLesFils (List <Sommet> listAttentePrec, List <Sommet> listFils) {
		List <Sommet> listAttente= listAttentePrec;
		Iterator<Sommet>filsIterator=listFils.iterator();
		//Parcours des fils
		while(filsIterator.hasNext()) {
			Sommet filsATraiter=filsIterator.next();
			Boolean find=false;
			//Parcours de la liste d'attente � trier
			for(int i=0;i<listAttente.size() && !find; i++) {
				Sommet attenteATraiter=listAttente.get(i);
				//si on a trouv� la place pour le fils
				if(find = filsATraiter.getGH()<attenteATraiter.getGH()) {
					listAttente.add(i, filsATraiter);
				}
			}
			if(!find) {
				listAttente.add(filsATraiter);
			}
		}
		return listAttente;
	}
	
	private static Sommet creationFils (Sommet sommetPere,int cout,Heuristique heur) {
		Sommet pere=sommetPere;
		List <Sommet> sommetFils=new ArrayList<Sommet>();
		for(Sommet fils:pere.getFilsList()) {
			fils.setGh(heur.fonction(fils));
			if(!pere.getListDirection().isEmpty())
				fils.getListDirection().addAll(pere.getListDirection());
			fils.getListDirection().add(fils.getDirection());
			List <Sommet> sommetPetitFils=new ArrayList<Sommet>();
			int j=0;
			for(int i:graph.voisin(fils.getNom())) {
				sommetPetitFils.add(new Sommet(i,cout,j));
				j++;
			}
			fils.setFilsList(sommetPetitFils);
			sommetFils.add(fils);
		}
		pere.setFilsList(sommetFils);
		return pere;
	}
	
	//Fonction aetoile
	public static List<Integer> fonction (Sommet sommetDepart,int cout,int but,Heuristique heur) {
		//Initialisation du A*
		List <Integer> directionList=new ArrayList<Integer>();
		Boolean estBut=false;
		List <Sommet> listAttente=new ArrayList <Sommet>();
		Sommet pereActuelle=creationFils(sommetDepart,cout,heur);
		listAttente=insererLesFils(listAttente , pereActuelle.getFilsList());
		//Parcours de la liste d'attente
		while(!listAttente.isEmpty() && !estBut) {
			estBut=pereActuelle.getNom()==but;
			//si but alors chemin trouv�
			if(estBut) {
				directionList=pereActuelle.getListDirection();
			//Sinon continue de parcourir
			}else {
				pereActuelle=creationFils(listAttente.get(0),cout,heur);
				listAttente.remove(0);
				listAttente=insererLesFils(listAttente , pereActuelle.getFilsList());
			}
		}
		return directionList;
	}
	
	public static void main(String[] args) {
		Sommet depart=new Sommet(1,0,2);
		HeuristiqueBase hb=new HeuristiqueBase();
		List <Sommet> sommetPetitFils=new ArrayList<Sommet>();
		int j=0;
		for(int i:graph.voisin(depart.getNom())) {
			sommetPetitFils.add(new Sommet(i,0,j));
			j++;
		}
		depart.setFilsList(sommetPetitFils);
		List <Integer> list=fonction(depart,1,7,hb);
		for(int i:list) {
			System.out.println(i);
		}
	}
}
