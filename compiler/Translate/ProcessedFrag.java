package Translate;

import Codegen.CodeGeneratorFacade;
import RegAlloc.CodeFrag;
import Tree.StmList;

/**
 * Base class that all processed frags should extend.
 */
public abstract class ProcessedFrag {

    StmList stmList;
    ProcessedFrag next = null;

    public ProcessedFrag(StmList stmList) {
        this.stmList = stmList;
    }

    public abstract CodeFrag process(CodeGeneratorFacade codeGeneratorFacade);

	public CodeFrag processAll(CodeGeneratorFacade codeGeneratorFacade) {
        CodeFrag result = null;
        for(var me = this; me != null; me = me.next) {
            CodeFrag codeFrag = me.process(codeGeneratorFacade);
            if(result == null) {
                result = codeFrag;
            } else {
                result.append(codeFrag);
            }
        }
        return result;
	}

	public void append(ProcessedFrag process) {
        ProcessedFrag processedFrag = this;
        for(; processedFrag.next != null; processedFrag = processedFrag.next);
        processedFrag.next = process;
	}
}