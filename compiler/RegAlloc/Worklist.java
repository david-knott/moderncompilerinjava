package RegAlloc;

import Assem.Instr;
import Assem.InstrList;
import Graph.Node;

/**
 * Double linked list implementation for register allocation.
 * @param <T>
 */
public class Worklist<T> {

    public T me;
    public Worklist<T> next;
    public Worklist<T> prev;

    Worklist(T me) {
        this.me = me;
        this.next = null;
        this.prev = null;
    }

	public static InstructionWorkList or(InstructionWorkList workListMoves, InstructionWorkList instructionWorkList) {
		return null;
	}
}


class InstructionWorkList {

    public Instr me;
    public InstructionWorkList next;
    public InstructionWorkList prev;

    InstructionWorkList(Instr me) {
    }

	public static InstructionWorkList and(InstructionWorkList instrList, InstructionWorkList or) {
		return null;
    }
    
    public static boolean contains(InstructionWorkList activeMoves, Instr instr) {
        return false;
    }

	public static InstructionWorkList andOr(InstructionWorkList workListMoves,
			InstructionWorkList instructionWorkList) {
		return null;
	}

	public static InstructionWorkList or(InstructionWorkList constrainedMoves,
			InstructionWorkList instructionWorkList) {
		return null;
	}

}

class NodeWorkList {

    public Node me;
    public NodeWorkList next;
    public NodeWorkList prev;


    NodeWorkList(Node me) {
    }

	public static NodeWorkList or(NodeWorkList spillWorkList, Object instructionWorkList) {
		return null;
	}

	public static NodeWorkList andOr(NodeWorkList spillWorkList, NodeWorkList nodeWorkList) {
		return null;
	}

	public static boolean contains(NodeWorkList coalescedNodes, Node node) {
		return false;
	}

	public static NodeWorkList append(NodeWorkList precoloured, Node tnode) {
		return null;
	}

}

