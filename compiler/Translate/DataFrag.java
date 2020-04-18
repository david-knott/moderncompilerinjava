package Translate;

import java.io.PrintStream;

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
	public void process(FragProcessor processor) {
		PrintStream out = processor.getOut();
		out.println("section .data");
		out.println(this);
	}
}