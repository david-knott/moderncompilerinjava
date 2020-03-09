package Translate;

import Temp.Label;
import Temp.Temp;
import Tree.Stm;

abstract public class Exp {
    abstract Tree.Exp unEx();

    abstract Tree.Stm unNx();

    abstract Tree.Stm unCx(Label t, Label f);
}

class Ex extends Exp {
    Tree.Exp exp;

    Ex(Tree.Exp e) {
        exp = e;
    }

    @Override
    Tree.Exp unEx() {
        return exp;
    }

    @Override
    Stm unNx() {
        return new Tree.EXP(exp);
    }

    @Override
    Stm unCx(Label t, Label f) {
        return new Tree.CJUMP(Tree.CJUMP.EQ, this.exp, new Tree.CONST(1), t, f);
    }
}

/**
 * Represents an expression that does not yield a value, this is referred to as
 * a statement.
 */
class Nx extends Exp {
    Tree.Stm stm;

    Nx(Tree.Stm s) {
        stm = s;
    }

    @Override
    Tree.Exp unEx() {
        throw new RuntimeException("Not implemented, this should never occur");
    }

    @Override
    Stm unNx() {
        return stm;
    }

    @Override
    Stm unCx(Label t, Label f) {
        throw new RuntimeException("Not implemented, this should never occur");
    }
}

/**
 * An abstract class that represents an expression that yields a boolean value.
 * This is used to control logic flow in the program.
 * 
 * This class is subclassed by RelCx which is used for comparision operations
 */
abstract class Cx extends Exp {

    @Override
    Tree.Exp unEx() {
        Label t = new Label();
        Label f = new Label();
        Temp r = new Temp();
        // move const 1 into temp r
        // call conditional statement with labels
        // add label false
        // move const 0 into register r
        // add label true
        // create emit temp with register r
        return new Tree.ESEQ(
                new Tree.SEQ(new Tree.MOVE(new Tree.TEMP(r), new Tree.CONST(1)), new Tree.SEQ(unCx(t, f),
                        new Tree.SEQ(new Tree.LABEL(f),
                                new Tree.SEQ(new Tree.MOVE(new Tree.TEMP(r), new Tree.CONST(0)), new Tree.LABEL(t))))),
                new Tree.TEMP(r));
    }

    @Override
    Tree.Stm unNx() {
        Label a = new Label();
        return new Tree.SEQ(unCx(a, a), new Tree.LABEL(a));
    }
}

/**
 * This class is used to represent a conditional expression that yields a
 * boolean value. It is typically used in control flow structures such as loops
 * and branches. It can also be assigned to a variable.
 * 
 * IR Code is just a simple CJUMP to the supplied labels.
 */
class RelCx extends Cx {

    Tree.Exp right;
    Tree.Exp left;
    int operator;

    RelCx(Tree.Exp l, Tree.Exp r, int op) {
        right = r;
        left = l;
        operator = op;
    }

    @Override
    Stm unCx(Label t, Label f) {
        return new Tree.CJUMP(operator, left, right, t, f);
    }
}

/**
 * A subclass to handle if then else expressions. Where condition is the if
 * condition, aa this the expression in the then block and bb is the optional
 * expression in the else block
 * 
 * IR Code
 */
class IfThenElseExp extends Exp {

    Exp cond, a, b;
    // begining of the the clause
    Label t = new Label();
    // begining of the else clause
    Label f = new Label();
    // both branches jump to this when the complete
    Label join = new Label();

    IfThenElseExp(Exp tst, Exp aa, Exp bb) {
        cond = tst;
        a = aa;
        b = bb;
        if(b == null)
        b = new Ex(new Tree.CONST(0));
    }

    /**
     * Returns the result where the then and else have the same type that is not
     * void.
     */
    @Override
    Tree.Exp unEx() {
        var r = new Temp();
        return new Tree.ESEQ(
            new Tree.SEQ(
                new Tree.SEQ(
                    cond.unCx(t, f), // eval cx with labels t and f
                    new Tree.SEQ(
                        new Tree.LABEL(t), // add label t
                        new Tree.SEQ( // eval then expression
                                new Tree.MOVE( // move ex result into r
                                    new Tree.TEMP(r), 
                                    a.unEx()
                                ),
                                new Tree.SEQ(
                                    new Tree.JUMP(join), 
                                    new Tree.SEQ(
                                        new Tree.LABEL(f), // add label f
                                        new Tree.SEQ(new Tree.MOVE( // move
                                                new Tree.TEMP(r), // result of b
                                                b.unEx() // into register r
                                        ), 
                                        new Tree.JUMP(join))
                                    )
                                )
                        )
                    )
                ), 
                new Tree.LABEL(join)
            ), 
            new Tree.TEMP(r)
        );
    }

    @Override
    Stm unNx() {
        return new Tree.SEQ(
            new Tree.SEQ(
                cond.unCx(t, f), // eval cx with labels t and f
                new Tree.SEQ(
                    new Tree.LABEL(t), // add label t
                    new Tree.SEQ( // eval then express
                        a.unNx(), 
                        new Tree.SEQ(
                            new Tree.JUMP(join), 
                            new Tree.SEQ(
                                new Tree.LABEL(f), // add label
                                    new Tree.SEQ(
                                        b.unNx(), // into register r
                                        new Tree.JUMP(join))
                            )
                        )
                    )
                )
            ),
            new Tree.LABEL(join)
        );
    }

    /**
     * Used by & and | operators. Label tt is the position where conditions in else
     * or then must jump if they evaluate to true or false Check is this is correct
     */
    @Override
    Stm unCx(Label tt, Label ff) {
        return new Tree.SEQ(
            cond.unCx(t, f), // eval cx with labels t and f
            new Tree.SEQ(
                new Tree.LABEL(t), // add label t
                    new Tree.SEQ( // eval then express
                        a.unCx(tt, ff), 
                        new Tree.SEQ(
                            new Tree.JUMP(join), 
                            new Tree.SEQ(
                                new Tree.LABEL(f), // add
                                new Tree.SEQ(
                                    b.unCx(tt, ff), // into register r
                                    new Tree.JUMP(join)
                                )
                            )
                        )
                    )
                )
        );
    }
}