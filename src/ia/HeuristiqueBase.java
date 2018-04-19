package ia;

public class HeuristiqueBase implements Heuristique {

	@Override
	public int fonction(Sommet s) {
		int gh=s.getGH();
		int nom=s.getNom();
		int h=0;
		if(nom>=2 && nom<=6)
			h++;
		if(nom==2 || nom==5){
			h+=5;
		}
		if(nom==2)h+=10;
		
		return gh+h;
	}

}
