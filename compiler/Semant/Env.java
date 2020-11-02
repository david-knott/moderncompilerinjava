package Semant;

import Types.RECORD;
import Types.Type;
import ErrorMsg.ErrorMsg;
import Symbol.GenericTable;
import Symbol.Symbol;
import Symbol.SymbolTable;

public class Env {
    GenericTable<Entry> venv;
    GenericTable<Type> tenv;
    ErrorMsg errorMsg;

    Env(ErrorMsg err) {
        this.errorMsg = err;
        venv = new GenericTable<Entry>();
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