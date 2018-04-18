package ia;

public class Sommet {

	private String nom;
	private int h;
	private Sommet[] voisinList;
	
	public Sommet(String nom, int h, Sommet[] voisinList) {
		this.nom = nom;
		this.h = h;
		this.voisinList = voisinList;
	}

	public String getNom() {
		return nom;
	}

	public int getH() {
		return h;
	}

	public Sommet[] getVoisinList() {
		return voisinList;
	}
	
	
	
	
}
