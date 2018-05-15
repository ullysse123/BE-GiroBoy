package ia;

import java.util.List;

public interface Graph {

	public int whereDoYouCome (int numGrandPere, int numPere);
	public List<Integer> voisin(int i,int direction);
}
