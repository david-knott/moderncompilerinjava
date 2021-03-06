package Translate;

import Temp.Label;

/**
 * Translate.Exp is a base class that all translated
 * expresssion must extend from. It provides 3 methods
 * that contextualise a tree expression
 * 1) unEx - an expression that returns a result
 * 2) unNx - an expression wihout a result
 * 3) unCx - an expression used in a conditional, arguments contain
 * true and false labels.
 */
abstract public class Exp {
    abstract Tree.Exp unEx();

    abstract Tree.Stm unNx();

    abstract Tree.Stm unCx(Label t, Label f);
}