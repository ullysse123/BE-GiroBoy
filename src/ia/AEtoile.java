package ia;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class AEtoile {
	
	private List <Sommet> insererLesFils (List <Sommet> listAttentePrec, List <Sommet> listFils) {
		List <Sommet> listAttente= listAttentePrec;
		Iterator<Sommet>filsIterator=listFils.iterator();
		while(filsIterator.hasNext()) {
			Sommet filsATraiter=filsIterator.next();
			Boolean find=false;
			for(int i=0;i<listAttente.size() && !find; i++) {
				Sommet attenteATraiter=listAttente.get(i);
				if(find = filsATraiter.getGH()<attenteATraiter.getGH()) {
					listAttente.add(i, filsATraiter);
				}
			}
		}
		return listAttente;
	}
	
	//g dans la creation
	public int[] fonction (Sommet sommetDepart) {
		int [] directionList= {};
		List <Sommet> listAttente=new ArrayList <Sommet>();
		listAttente=insererLesFils(listAttente , sommetDepart.getFilsList());
		while(!listAttente.isEmpty()) {
			
		}
		return directionList;
	}
}
