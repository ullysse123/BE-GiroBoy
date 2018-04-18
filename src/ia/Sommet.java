package ia;

import java.util.ArrayList;
import java.util.List;

public class Sommet {

	private int nom;
	private int gh;
	private List <Sommet> filsList= new ArrayList <Sommet>();
	private List <Integer> listDirection=new ArrayList <Integer>();
	private int direction;
	
	public Sommet(int nom, int gh, int direction) {
		this.nom = nom;
		this.gh = gh;
		this.direction=direction;
	}
	
	


	public void setListDirection(List<Integer> listDirection) {
		this.listDirection = listDirection;
	}




	public void setGh(int gh) {
		this.gh = gh;
	}



	public void setFilsList(List<Sommet> filsList) {
		this.filsList = filsList;
	}


	public int getDirection() {
		return direction;
	}




	public List<Integer> getListDirection() {
		return listDirection;
	}




	public int getNom() {
		return nom;
	}

	public int getGH() {
		return gh;
	}

	public List <Sommet> getFilsList() {
		return filsList;
	}
	
	
	
	
}
