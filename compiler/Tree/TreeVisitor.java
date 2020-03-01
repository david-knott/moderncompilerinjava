package Tree;

public interface TreeVisitor {

    void visit(BINOP op);

    void visit(CALL op);

    void visit(CONST op);

    void visit(ESEQ op);

    void visit(EXP op);

    void visit(JUMP op);

    void visit(LABEL op);

    void visit(MEM op);

    void visit(MOVE op);

    void visit(NAME op);

    void visit(SEQ op);

    void visit(TEMP op);

	void visit(CJUMP cjump);
}
