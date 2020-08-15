package Translate;

public interface FragmentVisitor {

    public void visit(ProcFrag procFrag);

    public void visit(DataFrag dataFrag);
}