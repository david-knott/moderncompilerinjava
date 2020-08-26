package Intel;

import Tree.Exp;

import Temp.Temp;
import Tree.BINOP;
import Tree.IR;

public abstract class BaseCodeGenTest {
    public CodeGen createCodeGen(Emitter emitter) {
        CodeGen codeGen = new CodeGen();
        Reducer reducer = new Reducer(emitter);
        codeGen.setReducer(reducer);
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
    
    public Exp getTemp() {
        return new Tree.TEMP(Temp.create());
    }
}