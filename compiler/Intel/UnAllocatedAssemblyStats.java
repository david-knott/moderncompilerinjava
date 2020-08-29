package Intel;

import java.io.OutputStream;
import java.io.PrintStream;

import Assem.DataFrag;
import Assem.FragmentVisitor;
import Assem.InstrList;
import Assem.LABEL;
import Assem.MOVE;
import Assem.OPER;
import Assem.ProcFrag;

final class UnAllocatedAssemblyStats implements FragmentVisitor {
    private int moveCount = 0;
    private int labelCount = 0;
    private int operCount = 0;
    private int dataFragCount = 0;

	@Override
	public void visit(ProcFrag procFrag) {
	    for(InstrList instrList = procFrag.body; instrList != null; instrList = instrList.tail) {
            if(instrList.head instanceof LABEL) {
                labelCount++;
            }
            if(instrList.head instanceof OPER) {
                operCount++;
            }
            if(instrList.head instanceof MOVE) {
                moveCount++;
            }
	    }
	}

	@Override
	public void visit(DataFrag dataFrag) {
        dataFragCount++;
    }
    
    public void dump(OutputStream outputStream) {
        try(PrintStream ps = new PrintStream(outputStream)) {
            ps.println("Assmembly Count");
            ps.println("Moves: " + this.moveCount);
            ps.println("Labels: " + this.labelCount);
            ps.println("Opers: " + this.operCount);
            ps.println("Data: " + this.dataFragCount);
        }
    }
}