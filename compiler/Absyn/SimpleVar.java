package Absyn;

import Symbol.Symbol;

public class SimpleVar extends Var {
   public Symbol name;
   public boolean readonly = false;

   public SimpleVar(int p, Symbol n) {
      pos = p;
      name = n;
   }
   
   public SimpleVar(int p, Symbol n, boolean ro) {
      pos = p;
      name = n;
      readonly = ro;
   }
   
   @Override
   public void accept(AbsynVisitor visitor) {
      visitor.visit(this);
   }
}
