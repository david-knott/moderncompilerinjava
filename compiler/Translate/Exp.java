package Translate;

import Temp.Label;

abstract public class Exp {
    abstract Tree.Exp unEx();

    abstract Tree.Stm unNx();

    abstract Tree.Stm unCx(Label t, Label f);
}