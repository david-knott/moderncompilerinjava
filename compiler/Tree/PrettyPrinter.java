package Tree;

import java.io.OutputStream;
import java.io.PrintStream;

/**
 * Prints out the LIR and HIR trees.
 */
public class PrettyPrinter implements TreeVisitor {

    final PrintStream printStream;
    int level = 0;

    void incLevel() {
        level++;
    }

    void decLevel() {
        level--;
    }

    public PrettyPrinter(OutputStream out) {
        this.printStream = new PrintStream(out);
        this.printStream.println("### Tree Pretty Printer ###");
    }

    private void write(String s) {
        for (int i = 0; i < level; ++i) {
            this.printStream.print("  ");
        }
        this.printStream.println(s);
    }

    @Override
    public void visit(BINOP op) {
        this.write("binop(");
        switch (op.binop) {
            case BINOP.PLUS:
                this.write("PLUS");
                break;
            case BINOP.MINUS:
                this.write("MINUS");
                break;
            case BINOP.MUL:
                this.write("MUL");
                break;
            case BINOP.DIV:
                this.write("DIV");
                break;
            default:
                this.write(Integer.toString(op.binop));
                break;
        }
        this.incLevel();
        op.left.accept(this);
        op.right.accept(this);
        this.decLevel();
        this.write(")");
    }

    @Override
    public void visit(CALL op) {
        this.write("call(");
        this.incLevel();
        ;
        op.func.accept(this);
        for (ExpList el = op.args; el != null; el = el.tail) {
            el.head.accept(this);
        }
        this.decLevel();
        this.write(")");
    }

    @Override
    public void visit(CONST op) {
        this.write("const(" + op.value + ")");
    }

    @Override
    public void visit(ESEQ op) {
        this.write("eseq(");
        this.incLevel();
        ;
        op.exp.accept(this);
        op.stm.accept(this);
        this.decLevel();
        this.write(")");
    }

    @Override
    public void visit(EXP op) {
        this.write("sxp(");
        this.incLevel();
        op.exp.accept(this);
        this.decLevel();
        this.write(")");
    }

    @Override
    public void visit(JUMP op) {
        this.write("jump(");
        this.incLevel();
        op.exp.accept(this);
        this.decLevel();
        this.write(")");
        // Targets

    }

    @Override
    public void visit(LABEL op) {
        this.write("label(" + op.label + ")");
    }

    @Override
    public void visit(MEM op) {
        this.write("mem(");
        this.incLevel();
        op.exp.accept(this);
        this.decLevel();
        this.write(")");
    }

    @Override
    public void visit(MOVE op) {
        this.write("move(");
        this.incLevel();
        op.dst.accept(this);
        op.src.accept(this);
        this.decLevel();
        this.write(")");
    }

    @Override
    public void visit(NAME op) {
        this.write("name(" + op.label + ")");

    }

    @Override
    public void visit(SEQ op) {
        this.write("seq(");
        this.incLevel();
        op.left.accept(this);
        op.right.accept(this);
        this.decLevel();
        this.write(")");
    }

    @Override
    public void visit(TEMP op) {
        this.write("temp(" + op.temp.toString() + ")");
    }

    @Override
    public void visit(CJUMP cjump) {
        this.write("cjump(");
        this.incLevel();
        cjump.left.accept(this);
        cjump.right.accept(this);
        this.decLevel();
        this.write(")");
    }
}