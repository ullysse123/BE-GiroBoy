package ia;

public class HeuristiqueBase2 implements Heuristique {

	@Override
	public int fonction(Sommet s) {
		int gh=s.getGH();
		int nom=s.getNom();
		int h=0;
		if(nom!=1)h++;
		if((nom>=2 && nom<5) || nom==6)h++;
		return gh+h;
	}

}
