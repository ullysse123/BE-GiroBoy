package ia;

import java.util.ArrayList;
import java.util.List;

public class Graph2 implements Graph {

	int directionEnregistre=2;
	@Override
	public int whereDoYouCome(int numGrandPere, int numPere) {
		int direction=0;
		switch (numPere) {//Convention: 0 si Nord ou Ouest , 1 sinon (si confusion interpretation selon le chemin)
			case 1:
				if(numGrandPere!=9 && numGrandPere != 11)
					direction=1;
				break;
			case 2://TODO test sur 2 et 3
				if(numGrandPere == 4 || (numGrandPere==3 && directionEnregistre==0)) {
						direction=1;
				}
				break;
			case 3:
				if(numGrandPere == 4 || (numGrandPere==2 && directionEnregistre==0)) {
					direction=1;
				}
			break;
			case 4:
				if(numGrandPere!=12 && numGrandPere != 5)
					direction=1;
				break;
			case 5:
				if(numGrandPere!=7 && numGrandPere != 6)
					direction=1;
				break;
			case 6:
				if(numGrandPere!=7 && numGrandPere != 5)//problème à resoudre
					direction=0;
				break;
			case 7:
				if(numGrandPere!=6 && numGrandPere != 5)
					direction=1;
				break;
			case 8:
				if(numGrandPere!=10 && numGrandPere != 7)
					direction=1;
				break;
			case 9:
				if(numGrandPere!=10 && numGrandPere != 6)
					direction=1;
				break;
			case 10:
				if(numGrandPere!=8 && numGrandPere != 7)
					direction=1;
				break;
			case 11:
				if(numGrandPere!=8 && numGrandPere != 12)
					direction=1;
				break;
			default:
				if(numGrandPere!=4 && numGrandPere != 5)//problème à resoudre
					direction=0;
				break;
		}
		if(numPere==2 || numPere==3)directionEnregistre=direction;
		return direction;
	}
	
	private Boolean directionIs0 (int direction) {
		return direction==0;
	}

	@Override
	public List<Integer> voisin(int i, int direction) {
		List<Integer>v=new ArrayList<Integer>();
		switch (i) {//1er j=1 droite, 2nd j=0 gauche, 3nd j=2 demi-tour (dans la liste)
			case 1:voisinCase1(direction, v);
			break;
			case 2:voisinCase2(direction, v);
			break;
			case 3:voisinCase3(direction, v);
			break;
			case 4:voisinCase4(direction, v);
			break;
			case 5:voisinCase5(direction, v);
			break;
			case 6:voisinCase6(direction, v);
			break;
			case 7:voisinCase7(direction, v);
			break;
			case 8:voisinCase8(direction, v);
			break;
			case 9:voisinCase9(direction, v);
			break;
			case 10:voisinCase10(direction, v);
			break;
			case 11:voisinCase11(direction, v);
			break;
			default:voisinDefault(direction, v);
			break;
		}
		return v;
	}

	private void voisinDefault(int direction, List<Integer> v) {
		if(directionIs0(direction)) {
			v.add(11);
			v.add(8);
		}else {
			v.add(5);
			v.add(4);
		}
	}

	private void voisinCase11(int direction, List<Integer> v) {
		if(directionIs0(direction)) {
			v.add(1);
			v.add(9);
		}else {
			v.add(8);
			v.add(12);
		}
	}

	private void voisinCase10(int direction, List<Integer> v) {
		if(directionIs0(direction)) {
			v.add(9);
			v.add(6);
		}else {
			v.add(7);
			v.add(8);
		}
		
	}

	private void voisinCase9(int direction, List<Integer> v) {
		if(directionIs0(direction)) {
			v.add(11);
			v.add(1);
		}else {
			v.add(6);
			v.add(10);
		}
		
	}

	private void voisinCase8(int direction, List<Integer> v) {
		if(directionIs0(direction)) {
			v.add(12);
			v.add(11);
		}else {
			v.add(10);
			v.add(7);
		}
		
	}

	private void voisinCase7(int direction, List<Integer> v) {
		if(directionIs0(direction)) {
			v.add(8);
			v.add(10);
		}else {
			v.add(6);
			v.add(5);
		}
		
	}

	private void voisinCase6(int direction, List<Integer> v) {
		if(directionIs0(direction)) {
			v.add(10);
			v.add(9);
		}else {
			v.add(5);
			v.add(7);
		}
		
	}

	private void voisinCase5(int direction, List<Integer> v) {
		if(directionIs0(direction)) {
			v.add(4);
			v.add(12);
		}else {
			v.add(7);
			v.add(6);
		}
		
	}

	private void voisinCase4(int direction, List<Integer> v) {
		if(directionIs0(direction)) {
			v.add(3);
			v.add(2);
		}else {
			v.add(12);
			v.add(5);
		}
		
	}

	private void voisinCase3(int direction, List<Integer> v) {
		if(directionIs0(direction)) {
			v.add(2);
			v.add(4);
		}else {
			v.add(1);
			v.add(2);
		}
		
	}

	private void voisinCase2(int direction, List<Integer> v) {
		if(directionIs0(direction)) {
			v.add(4);
			v.add(3);
		}else {
			v.add(3);
			v.add(1);
		}
		
	}

	private void voisinCase1(int direction, List<Integer> v) {
		if(directionIs0(direction)) {
			v.add(2);
			v.add(3);
		}else {
			v.add(9);
			v.add(11);
		}
	}

}
