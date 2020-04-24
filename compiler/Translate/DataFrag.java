package Translate;

import java.io.PrintStream;

import Canon.Canonicalization;
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
	public StmList process(Canonicalization canonicalization, FragProcessor processor) {
		PrintStream out = processor.getOut();
		out.println("section .data");
		out.println(this);
		return null;
	}
}