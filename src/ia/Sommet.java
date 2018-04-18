package ia;

import java.util.List;

public class Sommet {

	private String nom;
	private int gh;
	private List <Sommet> filsList;
	private int [] direction;
	
	public Sommet(String nom, int gh, List <Sommet> filsList, int [] direction) {
		this.nom = nom;
		this.gh = gh;
		this.filsList = filsList;
		this.direction=direction;
	}
	
	

	public void setDirection(int[] direction) {
		this.direction = direction;
	}



	public int [] getDirection() {
		return direction;
	}

	public String getNom() {
		return nom;
	}

	public int getGH() {
		return gh;
	}

	public List <Sommet> getFilsList() {
		return filsList;
	}
	
	
	
	
}
