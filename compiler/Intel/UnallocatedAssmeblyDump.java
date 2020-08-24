package Intel;

import java.io.OutputStream;
import java.io.PrintStream;

import Assem.DataFrag;
import Assem.FragmentVisitor;
import Assem.InstrList;
import Assem.ProcFrag;
import Temp.DefaultMap;

final class UnallocatedAssmeblyDump implements FragmentVisitor {
	private final PrintStream printStream;

	UnallocatedAssmeblyDump(OutputStream out) {
		this.printStream = new PrintStream(out);
		this.printStream.println("#### unallocated assembly dunp ###");
	}

	@Override
	public void visit(ProcFrag procFrag) {
	    this.printStream.println("proc:" + procFrag.frame.name);
	    for(InstrList instrList = procFrag.body; instrList != null; instrList = instrList.tail) {
	        this.printStream.println(instrList.head.format(new DefaultMap()));
	    }
	    this.printStream.println();
	}

	@Override
	public void visit(DataFrag dataFrag) {
	    this.printStream.println("data:" + dataFrag.toString());
	}
}