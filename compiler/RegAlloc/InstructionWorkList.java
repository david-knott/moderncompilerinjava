package RegAlloc;

import Assem.Instr;

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