package Semant;

import Symbol.Table;
import Symbol.Symbol;

public class Env {
    Table venv;
    Table tenv;
    ErrorMsg.ErrorMsg errorMsg;

    Env(ErrorMsg.ErrorMsg err) {
        errorMsg = err;
        // initialize function values
        venv = new Table();
        // venv.put(Symbol.Symbol.symbol("getPath"), new FunEntry(t, Types.INT) );
        // initialize types table
        tenv = new Table();
        tenv.put(Symbol.symbol("int"), Semant.INT);
        tenv.put(Symbol.symbol("string"), Semant.STRING);

    }

    public Table getTEnv() {
        return this.tenv;
    }

    public Table getVEnv() {
        return this.venv;
    }

}