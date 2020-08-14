package Intel;

import Tree.Exp;

import Temp.Temp;
import Tree.BINOP;
import Tree.IR;

public abstract class BaseCodeGenTest {
    public CodeGen createCodeGen(Emitter emitter) {
        CodeGen codeGen = new CodeGen();
        codeGen.setEmitter(emitter);
        return codeGen;
    }

    public Exp getNestedBinopExp() {
        return new Tree.BINOP(
            BINOP.PLUS,
            new Tree.CONST(3),
            new Tree.BINOP(
                BINOP.PLUS,
                new Tree.CONST(3),
                new Tree.CONST(3)
            )
        );
    }

	public Exp getBinopExp() {
        return new Tree.BINOP(
            BINOP.PLUS,
            new Tree.CONST(3),
            new Tree.CONST(3)
        );
	}
}