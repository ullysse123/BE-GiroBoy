package ia;

public class HeuristiqueVictime2 implements Heuristique {

	@Override
	public int fonction(Sommet s) {
		int gh=s.getGH();
		int nom=s.getNom();
		int h;
		switch(nom) {
			case 1:h=2;
			break;
			case 2:h=2;
			break;
			case 3:h=2;
			break;
			case 4:h=1;
			break;
			case 5:h=1;
			break;
			case 6:h=2;
			break;
			case 7:h=2;
			break;
			case 8:h=1;
			break;
			case 9:h=2;
			break;
			case 10:h=2;
			break;
			case 11:h=1;
			break;
			default: h=0;
			break;
		}
		h*=2;
		return gh+h;
	}
}
