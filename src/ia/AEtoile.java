package ia;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class AEtoile {
	
	private List <Sommet> insererLesFils (List <Sommet> listAttentePrec, List <Sommet> listFils) {
		List <Sommet> listAttente= listAttentePrec;
		Iterator<Sommet>filsIterator=listFils.iterator();
		//Parcours des fils
		while(filsIterator.hasNext()) {
			Sommet filsATraiter=filsIterator.next();
			Boolean find=false;
			//Parcours de la liste d'attente à trier
			for(int i=0;i<listAttente.size() && !find; i++) {
				Sommet attenteATraiter=listAttente.get(i);
				if(find = filsATraiter.getGH()<attenteATraiter.getGH()) {
					listAttente.add(i, filsATraiter);
				}
			}
		}
		return listAttente;
	}
	
	private Sommet creationFils (Sommet sommetPere,int cout) {
		Sommet pere=sommetPere;
		List <Sommet> sommetFils=new ArrayList<Sommet>();
		for(Sommet fils:pere.getFilsList()) {
			fils.setGh(fils.getGH());//heuristique a rajouter
			fils.getListDirection().addAll(pere.getListDirection());
			fils.getListDirection().add(fils.getDirection());
			sommetFils.add(fils);
		}
		pere.setFilsList(sommetFils);
		return pere;
	}
	
	//Fonction aetoile
	public List<Integer> fonction (Sommet sommetDepart,int cout,String but) {
		List <Integer> directionList=new ArrayList<Integer>();
		Boolean estBut=false;
		List <Sommet> listAttente=new ArrayList <Sommet>();
		Sommet pereActuelle=creationFils(sommetDepart,cout);
		listAttente=insererLesFils(listAttente , pereActuelle.getFilsList());
		//Parcours de la liste d'attente
		while(!listAttente.isEmpty() && !estBut) {
			estBut=pereActuelle.getNom().equals(but);
			if(estBut) {
				directionList=pereActuelle.getListDirection();
			}else {
				pereActuelle=creationFils(listAttente.get(0),cout);
				listAttente=insererLesFils(listAttente , pereActuelle.getFilsList());
				listAttente.remove(0);
			}
		}
		return directionList;
	}
}
