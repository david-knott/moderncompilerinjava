package Assem;

import Frame.Frame;

public class DataFrag extends Fragment {

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