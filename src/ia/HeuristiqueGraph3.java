package ia;

public class HeuristiqueGraph3 implements Heuristique {

	@Override
	public int fonction(Sommet s) {
		int g=s.getGH();
		int nom=s.getNom();
		int h=0;
		int intersection=3;
		int virage=2;
		int ligne=1;
		switch(nom) {
			case 1:h=3*ligne+intersection;
			break;
			case 2:h=1*virage+intersection;
			break;
			case 3:h=intersection;
			break;
			case 4:h=intersection;
			break;
			case 5:h=intersection;
			break;
			case 6:h=intersection;
			break;
			case 7:h=1*ligne+1*virage+intersection;
			break;
			case 8:h=1*ligne+intersection;
			break;
			case 9:h=intersection;
			break;
			case 10:h=1*virage+intersection;
			break;
			case 11:h=intersection;
			break;
			default:h=1*virage+intersection;
			break;
		}
		return g+h;
	}

}
