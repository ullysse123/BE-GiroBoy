package ia;

import java.util.ArrayList;
import java.util.List;

public class Graph {
	
	public List<Integer> voisin(int i) {
		List<Integer>v=new ArrayList<Integer>();
		switch (i) {
			case 1:v.add(2);
			v.add(3);
			break;
			case 2:v.add(7);
			v.add(6);
			break;
			case 3:v.add(4);
			v.add(5);
			break;
			case 4:v.add(6);
			v.add(5);
			break;
			case 5:v.add(4);
			v.add(6);
			break;
			case 6:v.add(2);
			v.add(7);
			break;
			default:v.add(6);
			v.add(2);
			break;
		}
		return v;
	}
}
