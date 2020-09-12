package Absyn;

import Symbol.Symbol;

/**
 * A NameTy class represents a named type within the AST.
 * This could be a primitive string or int, or it could be
 * a custom type, created using the type contruct.
 */
public class NameTy extends Ty {
   public Symbol name;
   public Absyn def;

   public NameTy(int p, Symbol n) {
      pos = p;
      name = n;
   }

   public String toString() {
      return "[NameTy: {name=" + name + "}]";
   }

   @Override
   public void accept(AbsynVisitor visitor) {
      visitor.visit(this);

   }


   public void setDef(Absyn exp) {
      this.def = exp;
   }

}
