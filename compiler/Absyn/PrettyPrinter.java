package Absyn;

/**
* Returns a formatted version of the AST. 
* This formatted version can be feed back into the compiler.
* See https://www.lrde.epita.fr/~tiger/assignments.split/TC_002d2-Pretty_002dPrinting-Samples.html#TC_002d2-Pretty_002dPrinting-Samples
*/
class PrettyPrinter {
  private PrintStream out;
  private boolean tabs = false;
  private boolean spaces = true;
  private int indentation = 2;
  
  public PrettyPrinter(PrintStream o) {
    this.out = o;
  }
  
  private String toString(Exp exp) {
    line("/* == Abstract Syntax Tree. == */");
    lineBreak();
    exp.visit(this);
  }
  
  private void lineBreak() {
  
  }
  
  private void line(String line) {
  
  }
  
  private void say(String str) {
  
  }
 
  
  void visit(LetExp e) {
    writeline("let");
    if(e.decs != null)
      e.decs.accept(this);
    writeline("in");
    if(e.body != null)
      e.body.accept(this);
    writeline("end");
  }
  
    void visit(FunctionDec d) {
    say("function ");
    say(d.name.toString());
    writeline("(");
    if(d.params != null)
      d.params.accept(this);
    if (d.result != null)
        d.result.accept(this);
    d.body.accept(this);
    writeline(")");
  }
}
