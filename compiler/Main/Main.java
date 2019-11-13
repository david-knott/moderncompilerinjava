package Main;
import Parse.*;
import Semant.Semant;
import Symbol.Table;

import java.io.InputStream;

import ErrorMsg.ErrorMsg;

public class Main {

    public static void main(String[] args) {
        new Main(args[0]);
    }

    private InputStream inputStream;
    private String name;
    private Program ast;
    private Semant semant;
    private ErrorMsg errorMsg;
    private Grm parser;


    public Main(String filename) {
        throw new RuntimeException("Not supported");
        /*
        try {
            this.inputStream = new java.io.FileInputStream(filename);
            this.name = filename;
        } catch (java.io.FileNotFoundException e) {
            throw new Error("File not found: " + filename);
        }
        semant = new Semant(errorMsg);
        */
    }

    public Main(String name, InputStream inputStream) {
        this.name = name;
        this.inputStream = inputStream;
        this.errorMsg = new ErrorMsg(this.name);
        this.parser = new Grm(new Yylex(this.inputStream, this.errorMsg), this.errorMsg);
        this.semant = new Semant(errorMsg);
    }

    /**
     * Returns the symbol table for types.
     * Used for testing
     */
    public Table getTypeSymbolTable(){
        return semant.getEnv().getTEnv();
    }

    /**
     * Returns value symbol table . Used
     * for testing
     * @return
     */
    public Table getValueSymbolTable(){
        return semant.getEnv().getVEnv();
    }

    public boolean hasErrors(){
        return this.errorMsg.anyErrors;
    }

    public void buildAst() {
        try {
            java_cup.runtime.Symbol rootSymbol = parser.parse();
            this.ast = (Program) rootSymbol.value;
        } catch (Throwable e) {
            throw new Error("Unable to translate");
        } finally {
            try {
                this.inputStream.close();
            } catch (java.io.IOException e) {
                throw new Error(e.toString());
            }
        }
    }

    public void typeCheck(){
        this.semant.transExp(this.ast.absyn);
    }

    public void print(){
        new Absyn.Print(System.out).prExp(this.ast.absyn, 0);
    }
}