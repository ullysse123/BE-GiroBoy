package ia;

import java.util.ArrayList;
import java.util.List;

public class Graph {
	
	public List<Integer> voisin(int i) {//Rajouter le grand père pour savoir de quel coté on vient TODO
		List<Integer>v=new ArrayList<Integer>();
		switch (i) {//1er 0d, 2nd 1g 
			case 1:v.add(4);
			v.add(2);
			v.add(3);
			v.add(5);
			break;
			case 2:v.add(6);
			v.add(3);
			v.add(1);
			v.add(4);
			break;
			case 3:v.add(5);
			v.add(1);
			v.add(2);
			v.add(6);
			break;
			case 4:v.add(5);
			v.add(6);
			v.add(2);
			v.add(1);
			break;
			case 5:v.add(1);
			v.add(3);
			v.add(6);
			v.add(4);
			break;
			default:v.add(4);
			v.add(5);
			v.add(3);
			v.add(2);
			break;
		}
		return v;
	}
}
