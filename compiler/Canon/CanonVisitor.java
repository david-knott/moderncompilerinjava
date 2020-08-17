package Canon;

import Translate.DataFrag;
import Translate.FragList;
import Translate.FragmentVisitor;
import Translate.ProcFrag;
import Tree.StmList;

/**
 * Produces a new FragList that contains the HIR converted
 * to LIR trees. We reuse the Fragment data structure as it
 * ties a particular fragment to its activation record.
 */
public class CanonVisitor implements FragmentVisitor {

    final Canonicalization canonicalization;
    FragList fragList = null;

    public CanonVisitor(Canonicalization canonicalization) {
        this.canonicalization = canonicalization;
	}

	@Override
    public void visit(ProcFrag procFrag) {
        StmList stmList = canonicalization.canon(procFrag.body);
        ProcFrag lirProcFrag = new ProcFrag(stmList.toSEQ(), procFrag.frame);
        this.fragList = new FragList(lirProcFrag, this.fragList);
    }

    @Override
    public void visit(DataFrag dataFrag) {
        this.fragList = new FragList(dataFrag, this.fragList);
    }
}