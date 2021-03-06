package Absyn;

import Symbol.Symbol;

public class FunctionDec extends Dec {
   public Symbol name;
   public DecList params;
   public NameTy result; /* optional */
   public Exp body;
   public FunctionDec next;

   public FunctionDec(int p, Symbol n, DecList a, NameTy r, Exp b, FunctionDec x) {
      pos = p;
      name = n;
      params = a;
      result = r;
      body = b;
      next = x;
   }

   @Override
   public void accept(AbsynVisitor visitor) {
      visitor.visit(this);
   }
}
