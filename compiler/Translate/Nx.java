package Translate;

import Temp.Label;
import Tree.Stm;

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