package Translate;

import Codegen.CodeGeneratorFacade;
import RegAlloc.CodeFrag;
import Tree.StmList;

/**
 * Represents a string data fragment. This is unaffected by the canonicalisation
 * process.
 */
public class ProcessedDataFrag extends ProcessedFrag {

    public ProcessedDataFrag(StmList stmList) {
        super(stmList);
    }

    @Override
    public CodeFrag process(CodeGeneratorFacade codeGeneratorFacade) {
        return null;
    }
}