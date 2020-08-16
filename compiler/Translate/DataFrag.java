package Translate;

import Frame.Frame;

/**
 * Stores string literal data
 **/
public class DataFrag extends Frag {

	final String data;

	public DataFrag(String data) {
		this.data = data;
	}

	public String toString() {
		return this.data;
	}

	@Override
	public Frame getFrame() {
		throw new Error();
	}

	@Override
	public void accept(FragmentVisitor fragmentVisitor) {
        fragmentVisitor.visit(this);
	}
}