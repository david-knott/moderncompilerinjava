package Inlining;

import java.util.ArrayList;
import java.util.List;

import Absyn.CallExp;
import Absyn.DecList;
import Absyn.Exp;
import Absyn.ExpList;
import Absyn.FieldList;
import Absyn.FunctionDec;
import Absyn.LetExp;
import Absyn.SeqExp;
import Absyn.SimpleVar;
import Absyn.VarDec;
import Absyn.VarExp;
import Cloner.AbsynCloner;
import Symbol.Symbol;


import Cloner.AbsynCloner;

public class Pruner extends AbsynCloner {

    @Override
    public void visit(LetExp exp) {
        exp.decs = null;

        super.visit(exp);
    }
}