package Main;
import Parse.*;
import java.io.InputStream;

public class Main {

    public static void main(String[] args) {
        new Main(args[0]);
    }

    private ErrorMsg.ErrorMsg errorMsg;
    private InputStream inputStream;
    private String name;
    private Program ast;

    public Main(String filename) {
        try {
            this.inputStream = new java.io.FileInputStream(filename);
            this.name = filename;
        } catch (java.io.FileNotFoundException e) {
            throw new Error("File not found: " + filename);
        }
    }

    public Main(String name, InputStream inputStream) {
        this.name = name;
        this.inputStream = inputStream;
    }

    public boolean hasErrors(){
        return this.errorMsg.anyErrors;
    }

    public void buildAst() {
        this.errorMsg = new ErrorMsg.ErrorMsg(this.name);
        Grm parser = new Grm(new Yylex(this.inputStream, this.errorMsg), this.errorMsg);
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
        new Semant.Semant(errorMsg).transExp(this.ast.absyn);
    }

    public void print(){
        new Absyn.Print(System.out).prExp(this.ast.absyn, 0);
    }
}