package Intel;

import org.junit.Test;

import Temp.Label;
import Temp.Temp;
import Tree.IR;


public class CodeGenTest {

    

    @Test
    public void call0Args() throws Exception {
        IR tree = new Tree.EXP(
            new Tree.CALL( new Tree.NAME(Label.create()), null)
        );
        /*
            new Tree.TEMP(Temp.create()),
            new MEM(
                new Tree.BINOP(
                    Tree.BINOP.PLUS,
                    new Tree.TEMP(Temp.create()),
                    new Tree.BINOP(
                        Tree.BINOP.MUL,
                        new Tree.TEMP(Temp.create()),
                        new Tree.CONST(1)
                    )
                )
            )
        );*/
        var cg = new Intel.CodeGen();
        cg.burm(tree);
    }

    @Test
    public void call1Args() throws Exception {
        IR tree = new Tree.EXP(
            new Tree.CALL(
                new Tree.NAME(
                    Label.create()
                ), 
                new Tree.ExpList(
                    new Tree.BINOP(
                        Tree.BINOP.PLUS,
                        new Tree.TEMP(Temp.create()),
                        new Tree.BINOP(
                            Tree.BINOP.MUL,
                            new Tree.TEMP(Temp.create()),
                            new Tree.CONST(1)
                        )
                    )
                )
            )
        );
        var cg = new Intel.CodeGen();
        cg.burm(tree);
    }

    @Test
    public void call2Args() throws Exception {
        IR tree = new Tree.EXP(
            new Tree.CALL(
                new Tree.NAME(
                    Label.create()
                ), 
                new Tree.ExpList(
                    new Tree.BINOP(
                        Tree.BINOP.PLUS,
                        new Tree.TEMP(Temp.create()),
                        new Tree.BINOP(
                            Tree.BINOP.MUL,
                            new Tree.TEMP(Temp.create()),
                            new Tree.CONST(1)
                        )
                    ),
                    new Tree.ExpList(
                        new Tree.BINOP(
                            Tree.BINOP.AND,
                            new Tree.TEMP(Temp.create()),
                            new Tree.BINOP(
                                Tree.BINOP.DIV,
                                new Tree.TEMP(Temp.create()),
                                new Tree.CONST(1)
                            )
                        )
                    )
                )
            )
        );
        var cg = new Intel.CodeGen();
        cg.burm(tree);
    }





}