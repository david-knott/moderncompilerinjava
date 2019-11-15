package Semant;

import Symbol.Table;
import Types.Type;
import Symbol.GenericTable;
import Symbol.Symbol;
import Symbol.SymbolTable;

public class Env {
   // Table venv;
   // Table tenv;
    GenericTable<Entry> venv;
    GenericTable<Type> tenv;
    ErrorMsg.ErrorMsg errorMsg;

    Env(ErrorMsg.ErrorMsg err) {
        errorMsg = err;
        // initialize function values
        venv = new GenericTable<Entry>();
        // venv.put(Symbol.Symbol.symbol("getPath"), new FunEntry(t, Types.INT) );
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