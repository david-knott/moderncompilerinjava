package Translate;

import Temp.Label;
import Tree.Exp;

/**
 * Stores string literal data
 **/
public class DataFrag extends Frag {

    Label label;
    Exp stringFragment;

	public DataFrag(Label lbl, Exp frag) {
        label = lbl;
        stringFragment = frag;
	}
}