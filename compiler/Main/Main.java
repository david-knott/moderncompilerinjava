package Main;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintStream;

import ErrorMsg.ErrorMsg;
import FindEscape.FindEscape;
import Frame.Frame;
import Intel.IntelFrame;
import Parse.Grm;
import Parse.Program;
import Parse.Yylex;
import Semant.Semant;
import Temp.Label;
import Translate.FragList;
import Translate.Level;
import Translate.Translator;

interface CompilerSource {

}

interface CompilerDestination {

}

interface FileSourceReader {
    public void read(FileInputStream fileInputStream);
}
class FileSource implements CompilerSource {

    private FileInputStream fileInputStream = null;

    public FileSource(String fileName) {
        try {
            this.fileInputStream = new FileInputStream(fileName);

        } catch (FileNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    void read(FileSourceReader reader) {

    }

    void closeFile() {

        try {
            this.fileInputStream.close();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
    
    void read() {
        
    }
}

class FileDestination implements CompilerDestination {
    
    private FileInputStream fileInputStream = null;

    public FileDestination(String fileName) {
        try {
            this.fileInputStream = new FileInputStream(fileName);

        } catch (FileNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
    
    void openFile() {
        
    }
    
    public void println(String str) {
        
    }
    
    public void print(String str) {
        
    }
    
    void closeFile() {
        
    }
}

/**
 * Main class that executes the compiler.
 */
public class Main {

    public static void main(final String[] args) throws FileNotFoundException {
        new Main(args[0]);
    }

    private InputStream inputStream;
    private String name;
    private Program ast;
    private Semant semant;
    private ErrorMsg errorMsg;
    private Grm parser;
    // frame implementation
    private Frame frame = new IntelFrame(Label.create("tigermain"), null);
    private Level topLevel = new Level(frame);
    private Translator translate = new Translator();
    private boolean allVarsEscape = false;
    private FindEscape findEscape = new FindEscape(allVarsEscape);

    /**
     * Compiles the program. Using the parser we parse the source code and create an
     * abstract syntax tree. The abstract syntax tree is traversed and we mark
     * variables as escaping ( in frame ) or non escaping ( in register ). Semantic
     * analysis is performed to check for type consistency and semantic meaning that
     * cannot be infered from the grammar. The semantic analysis phase then produces
     * an internediate form of the language that can be converted into assembly. The
     * intermediate form is a list of fragments, either data fragment for strings or
     * code fragment for a function.
     * 
     * @return
     */
    public Main(final String name) throws FileNotFoundException {
        this.name = name;
        /*
        FileSource compilerSource = new FileSource(name);
        compilerSource.read(x -> {
            this.parser = new Grm(new Yylex(x, this.errorMsg), this.errorMsg);
        });*/
        
        this.inputStream = new java.io.FileInputStream(this.name);
        this.errorMsg = new ErrorMsg(this.name);
        this.parser = new Grm(new Yylex(this.inputStream, this.errorMsg), this.errorMsg);
        this.semant = new Semant(errorMsg, topLevel, this.translate);

        try {
            java_cup.runtime.Symbol rootSymbol = parser.parse();
            this.ast = (Program) rootSymbol.value;
        } catch (Throwable e) {
            throw new Error("Unable to parse, syntax error", e);
        } finally {
            try {
                this.inputStream.close();
            } catch (final java.io.IOException e) {
                throw new Error(e.toString());
            }
        }
        
        findEscape.traverse(this.ast.absyn);
        FragList frags = FragList.reverse(this.semant.getTreeFragments(this.ast.absyn));
        if(this.semant.getEnv().getErrorMsg().getCompilerErrors().size() != 0) {
            System.out.println("semant check error");
            System.exit(1);
        }
        PrintStream out = null;
        try {
            out = new PrintStream(new java.io.FileOutputStream(this.name + ".s"));
            //out = System.out;
            out.println(".global tigermain");
            for (; frags != null; frags = frags.tail) {
                frags.head.process(out);
            }
            out.close();
        } catch (FileNotFoundException e1) {
            e1.printStackTrace();
            throw new Error(e1.toString());
        } finally {
            if (out != null) {
                out.close();
            }
        }

    }

    /*
     * public Main(final String name, final InputStream inputStream) { this.name =
     * name; this.inputStream = inputStream; this.errorMsg = new
     * ErrorMsg(this.name); this.parser = new Grm(new Yylex(this.inputStream,
     * this.errorMsg), this.errorMsg); this.semant = new Semant(errorMsg, topLevel,
     * this.translate); }
     */

}
