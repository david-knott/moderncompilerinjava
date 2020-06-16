package Absyn;

import Symbol.Symbol;

public class VarDec extends Dec {
   public Symbol name;
   public boolean escape = false;
   public NameTy typ; /* optional */
   public Exp init;
   public boolean readonly = false;

   public VarDec(int p, Symbol n, NameTy t, Exp i) {
      pos = p;
      name = n;
      typ = t;
      init = i;
   }

   public VarDec(int p, Symbol n, NameTy t, Exp i, boolean es, boolean ro) {
      pos = p;
      name = n;
      typ = t;
      init = i;
      escape = es;
      readonly = false;
   }

   public String toString() {
      return "{ VarDec name='" + name + "', type='" + typ.name + "'}";
   }

   @Override
   public void accept(AbsynVisitor visitor) {
      visitor.visit(this);
      init.accept(visitor);
   }
}
