package Translate;

import java.io.PrintStream;
import Frame.Frame;
import Tree.StmList;

/**
 * Stores string literal data
 **/
public class DataFrag extends Frag {

	private final String data;

	public DataFrag(String data) {
		this.data = data;
	}

	public String toString() {
		return this.data;
	}

	@Override
	public void process(PrintStream out) {
		out.println("section .data");
		out.println(this.data);
	}

	@Override
	public Frame getFrame() {
		throw new Error();
	}
}