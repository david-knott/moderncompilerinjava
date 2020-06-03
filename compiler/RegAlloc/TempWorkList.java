package RegAlloc;

import Graph.Node;
import Temp.Temp;

class TempWorkList {

    public Temp me;
    public TempWorkList next;
    public TempWorkList prev;


    TempWorkList(Temp me) {
		this.me = me;
    }

	public static TempWorkList or(TempWorkList spillWorkList, TempWorkList instructionWorkList) {
		return null;
	}

	public static TempWorkList andOr(TempWorkList spillWorkList, TempWorkList nodeWorkList) {
		return null;
	}

	public static boolean contains(TempWorkList coalescedNodes, Temp node) {
		return false;
	}

	public static TempWorkList append(TempWorkList precoloured, Temp tnode) {
		return null;
	}

	public static Temp last(TempWorkList nodeWorkList) {
		return null;
	}

	public static TempWorkList or(TempWorkList spilledNodes, Temp n) {
		return null;
	}

}