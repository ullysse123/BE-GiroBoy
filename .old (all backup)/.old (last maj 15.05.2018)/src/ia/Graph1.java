package ia;

import java.util.ArrayList;
import java.util.List;

public class Graph1 implements Graph{
	
	
	//whereDoYouCome et voisin sont liées
	public int whereDoYouCome (int numGrandPere, int numPere) {
		int direction=0;
		switch (numPere) {//Convention: 0 si Nord ou Ouest , 1 sinon (si confusion interpretation selon le chemin)
			case 1:
				if(numGrandPere!=3 && numGrandPere != 5)
					direction=1;
				break;
			case 2:
				if(numGrandPere!=3 && numGrandPere != 6)
					direction=1;
				break;
			case 3:
				if(numGrandPere!=1 && numGrandPere != 5)
					direction=1;
				break;
			case 4:
				if(numGrandPere!=6 && numGrandPere != 5)
					direction=1;
				break;
			case 5:
				if(numGrandPere!=4 && numGrandPere != 6)
					direction=1;
				break;
			default:
				if(numGrandPere!=4 && numGrandPere != 5)
					direction=1;
		}
		return direction;
	}
	
	private Boolean directionIs0 (int direction) {
		return direction==0;
	}
	
	public List<Integer> voisin(int i,int direction) {//Direction est utile pour savoir d'ou on vient pour choisir les sommets voisins
		List<Integer>v=new ArrayList<Integer>();
		switch (i) {//1er j=0 droite, 2nd j=1 gauche, 3nd j=2 demi-tour (dans la liste)
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
			default:voisinCaseDefault(direction, v);
			break;
		}
		return v;
	}

	private void voisinCaseDefault(int direction, List<Integer> v) {
		if(directionIs0(direction)) {
			v.add(3);
			v.add(2);
		}else {
			v.add(4);
			v.add(5);
		}
	}

	private void voisinCase5(int direction, List<Integer> v) {
		if(directionIs0(direction)) {
			v.add(1);
			v.add(3);
		}else {
			v.add(6);
			v.add(4);
		}
	}

	private void voisinCase4(int direction, List<Integer> v) {
		if(directionIs0(direction)) {
			v.add(2);
			v.add(1);
		}else {
			v.add(5);
			v.add(6);
		}
	}

	private void voisinCase3(int direction, List<Integer> v) {
		if(directionIs0(direction)) {
			v.add(2);
			v.add(6);
		}else{
			v.add(5);
			v.add(1);
		}
	}

	private void voisinCase2(int direction, List<Integer> v) {
		if(directionIs0(direction)) {
			v.add(1);
			v.add(4);
		}else {
			v.add(6);
			v.add(3);
		}
	}

	private void voisinCase1(int direction, List<Integer> v) {
		if(directionIs0(direction)) {
			v.add(4);
			v.add(2);
		}else{
			v.add(3);
			v.add(5);
		}
	}
}
