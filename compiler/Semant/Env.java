package Semant;

import Types.RECORD;
import Types.Type;

import Symbol.GenericTable;
import Symbol.Symbol;
import Symbol.SymbolTable;

public class Env {
    GenericTable<Entry> venv;
    GenericTable<Type> tenv;
    ErrorMsg.ErrorMsg errorMsg;

    Env(ErrorMsg.ErrorMsg err) {
        errorMsg = err;
        // initialize function values
        venv = new GenericTable<Entry>();
        venv.put(Symbol.symbol("print"),
                new FunEntry(new RECORD(Symbol.symbol("s"), Semant.STRING, null), Semant.VOID));
        venv.put(Symbol.symbol("flush"), new FunEntry(null, Semant.VOID));
        venv.put(Symbol.symbol("getchar"), new FunEntry(null, Semant.STRING));
        venv.put(Symbol.symbol("ord"),
                new FunEntry(new RECORD(Symbol.symbol("s"), Semant.STRING, null), Semant.STRING));
        venv.put(Symbol.symbol("size"), new FunEntry(new RECORD(Symbol.symbol("s"), Semant.STRING, null), Semant.INT));
        venv.put(Symbol.symbol("substring"), new FunEntry(new RECORD(Symbol.symbol("s"), Semant.STRING,
                new RECORD(Symbol.symbol("first"), Semant.INT, new RECORD(Symbol.symbol("n"), Semant.INT, null))),
                Semant.STRING));
        venv.put(Symbol.symbol("concat"), new FunEntry(
                new RECORD(Symbol.symbol("s1"), Semant.STRING, new RECORD(Symbol.symbol("s2"), Semant.STRING, null)),
                Semant.STRING));
        venv.put(Symbol.symbol("not"), new FunEntry(new RECORD(Symbol.symbol("i"), Semant.INT, null), Semant.INT));
        venv.put(Symbol.symbol("exit"), new FunEntry(new RECORD(Symbol.symbol("i"), Semant.INT, null), Semant.VOID));
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