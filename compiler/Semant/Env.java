package Semant;

import Types.RECORD;
import Types.Type;
import Symbol.GenericTable;
import Symbol.Symbol;
import Symbol.SymbolTable;
import Temp.Label;
import Translate.Level;

public class Env {
    GenericTable<Entry> venv;
    GenericTable<Type> tenv;
    ErrorMsg.ErrorMsg errorMsg;
    Level outerMost;

    Env(ErrorMsg.ErrorMsg err, Level om) {
        errorMsg = err;
        outerMost = om;
        // initialize function values
        venv = new GenericTable<Entry>();
        venv.put(Symbol.symbol("print"),
                new FunEntry(outerMost, Label.create("print"), new RECORD(Symbol.symbol("s"), Semant.STRING, null), Semant.VOID));
        venv.put(Symbol.symbol("flush"), new FunEntry(outerMost, Label.create("flush"), null, Semant.VOID));
        venv.put(Symbol.symbol("getchar"), new FunEntry(outerMost, Label.create("getchar"), null, Semant.STRING));
        venv.put(Symbol.symbol("ord"), new FunEntry(outerMost, Label.create("ord"),
                new RECORD(Symbol.symbol("s"), Semant.STRING, null), Semant.INT));
        venv.put(Symbol.symbol("chr"), new FunEntry(outerMost, Label.create("chr"),
                new RECORD(Symbol.symbol("i"), Semant.INT, null), Semant.STRING));
        venv.put(Symbol.symbol("size"),
                new FunEntry(outerMost, Label.create("size"), new RECORD(Symbol.symbol("s"), Semant.STRING, null), Semant.INT));
        venv.put(Symbol.symbol("substring"), new FunEntry(outerMost, Label.create("substring"), new RECORD(Symbol.symbol("s"),
                Semant.STRING,
                new RECORD(Symbol.symbol("first"), Semant.INT, new RECORD(Symbol.symbol("n"), Semant.INT, null))),
                Semant.STRING));
        venv.put(Symbol.symbol("concat"), new FunEntry(outerMost, Label.create("concat"),
                new RECORD(Symbol.symbol("s1"), Semant.STRING, new RECORD(Symbol.symbol("s2"), Semant.STRING, null)),
                Semant.STRING));
        venv.put(Symbol.symbol("not"),
                new FunEntry(outerMost, Label.create("not"), new RECORD(Symbol.symbol("i"), Semant.INT, null), Semant.INT));
        venv.put(Symbol.symbol("exit"),
                new FunEntry(outerMost, Label.create("exit"), new RECORD(Symbol.symbol("i"), Semant.INT, null), Semant.VOID));
        // initialize types table
        tenv = new GenericTable<Type>();
        tenv.put(Symbol.symbol("int"), Semant.INT);
        tenv.put(Symbol.symbol("string"), Semant.STRING);
    }

    public SymbolTable<Type> getTEnv() {
        return this.tenv;
    }

    public SymbolTable<Entry> getVEnv() {
        return this.venv;
    }
}