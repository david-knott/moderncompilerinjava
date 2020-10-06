package Sugar;

import Absyn.AssignExp;
import Absyn.BreakExp;
import Absyn.CallExp;
import Absyn.DecList;
import Absyn.ExpList;
import Absyn.ForExp;
import Absyn.IfExp;
import Absyn.IntExp;
import Absyn.LetExp;
import Absyn.OpExp;
import Absyn.SeqExp;
import Absyn.SimpleVar;
import Absyn.VarDec;
import Absyn.VarExp;
import Absyn.WhileExp;
import Cloner.AbsynCloner;
import Symbol.Symbol;
import Types.STRING;

public class Desugar extends AbsynCloner {

    @Override
    public void visit(OpExp exp) {
        if(
            exp.left.getType() instanceof STRING
            &&
            exp.right.getType() instanceof STRING
        ) {
            CallExp callExp = new CallExp(
                exp.pos, 
                Symbol.symbol("strcmp"), 
                new ExpList(
                    exp.left, 
                    new ExpList(
                        exp.right,
                        null
                    )
                )
            );
            this.visitedExp = new OpExp(exp.pos, callExp, exp.oper, new IntExp(exp.pos, 0));
        } else {
            super.visit(exp);
        }
    }

    @Override
    public void visit(ForExp forExp) {
        //convert forexp into a while loop

        //vardec i = 0
        //test condition 
        // - true execute body back to test condition
        // - false exit
        Symbol _lo = Symbol.symbol("%lo");
        Symbol _hi = Symbol.symbol("%hi");
        Symbol _i = forExp.var.name;
        int _ipos = forExp.var.pos;
        int _hipos = forExp.hi.pos;
        this.visitedExp = new LetExp(
            forExp.pos,
            new DecList(
                new VarDec(
                    _hipos, _lo, null, forExp.var.init
                ), 
                new DecList(
                    new VarDec(_hipos, _hi, null, forExp.hi),
                    new DecList(
                        new VarDec(_ipos, _i, null, new VarExp(0, new SimpleVar(0, _lo))), 
                        null
                    ) 
                )
            ), 
            new SeqExp(
                forExp.pos,
                new ExpList(
                    new IfExp(
                        forExp.pos, 
                        new OpExp(
                            forExp.pos, 
                            new VarExp(
                                forExp.pos, 
                                new SimpleVar(
                                    _ipos, 
                                    _i
                                )
                            ), 
                            OpExp.LE, 
                            new VarExp(
                                _hipos, 
                                new SimpleVar(
                                    _hipos, 
                                    _hi
                                )
                            )
                        ), 
                        new WhileExp(
                            forExp.pos, 
                            new IntExp(forExp.pos, 1),
                            new SeqExp(
                               forExp.body.pos,
                               new ExpList(
                                    forExp.body,
                                    new ExpList(
                                        new IfExp(
                                            forExp.pos,
                                            new OpExp(
                                                forExp.pos,
                                                new VarExp(
                                                    _ipos, 
                                                    new SimpleVar(
                                                       _ipos, 
                                                       _i
                                                    )
                                                ), 
                                                OpExp.EQ,
                                                new VarExp(
                                                    _hipos, 
                                                    new SimpleVar(
                                                       _hipos, 
                                                       _hi
                                                    )
                                                )
                                            ),
                                            new BreakExp(0) /* true so break */,
                                            new AssignExp(/* false so increment */
                                                _ipos,
                                                new SimpleVar(
                                                    _ipos, 
                                                    _i
                                                ),
                                                new OpExp(
                                                    _ipos, 
                                                    new VarExp(
                                                        _ipos, 
                                                        new SimpleVar(
                                                            _ipos, 
                                                            _i
                                                        )
                                                    ),
                                                    OpExp.PLUS, 
                                                    new IntExp(
                                                        _ipos, 
                                                        1
                                                    )
                                                )
                                           )
                                        ),
                                        null
                                    )
                               )
                            )
                        )
                    ), 
                    null
                )
            )
        );
    }

}
