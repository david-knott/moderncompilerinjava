package Intel;

import Assem.FragList;
import Translate.DataFrag;
import Translate.FragmentVisitor;
import Translate.ProcFrag;
import Tree.StmList;

/**
 * 
 */
class AssemblyGeneratorVisitor implements FragmentVisitor {

    final CodeGen codeGen;
    FragList fragList;
    Emitter emitter;

    public AssemblyGeneratorVisitor(CodeGen codeGen) {
        this.codeGen = codeGen;
    }

    @Override
    public void visit(ProcFrag procFrag) {
        emitter = new EmitterImpl();
        codeGen.setEmitter(emitter);
        codeGen.setReducer(new Reducer(null));
        // yuck, this smells.
        StmList stmList = (StmList)procFrag.body;
        for(; stmList != null; stmList = stmList.tail) {
            try {
                this.codeGen.burm(stmList.head);
                this.fragList = new FragList(new Assem.ProcFrag(emitter.getInstrList(), procFrag.frame), this.fragList);
            } catch (Exception e) {
                throw new Error(e);
            }
        }
    }

    @Override
    public void visit(DataFrag dataFrag) {
        this.fragList = new FragList(new Assem.DataFrag(dataFrag.toString()));
    }

    public FragList getAssemFragList() {
        return this.fragList;
    }
}