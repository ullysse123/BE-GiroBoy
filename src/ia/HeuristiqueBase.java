package ia;

public class HeuristiqueBase implements Heuristique {

	@Override
	public int fonction(Sommet s) {
		int gh=s.getGH();
		int nom=s.getNom();
		int h=0;
		if(nom!=6)h++;
		if((nom>=3 && nom<6) || nom==1)h++;
		return gh+h;
	}

}
