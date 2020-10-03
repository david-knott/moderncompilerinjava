package Types;

public abstract class Type {
   public Type actual() {
      return this;
   }

   public boolean coerceTo(Type t) {
      return false;
   }

   public abstract void accept(GenVisitor genVisitor);

}
