package Translate;

import Assem.InstrList;
import Codegen.CodeGeneratorFacade;
import Frame.Frame;
import RegAlloc.CodeFrag;
import Tree.StmList;

/**
 * Represents a canonicalized IR Tree Fragment. Also
 * functions as a linked list.
 */
public class ProcessedProcFrag extends ProcessedFrag {

    private Frame frame;

    public ProcessedProcFrag(StmList stmList, Frame frame) {
        super(stmList);
        this.frame = frame;
    }

    @Override
    public CodeFrag process(CodeGeneratorFacade codeGeneratorFacade) {
        InstrList instrList = codeGeneratorFacade.generateCode(this.frame, this.stmList);
        return new CodeFrag(instrList, this.frame);
    }
}