package Types;

public class NAME extends Type {
   public Symbol.Symbol name;
   private Type binding;

   public NAME(Symbol.Symbol n) {
      name = n;
   }

   public boolean isLoop() {
      Type b = binding;
      boolean any;
      binding = null;
      //if current bind is null, it is bound to any type, return true
      if (b == null)
         any = true;
      else if (b instanceof NAME) //if current bind is name, call isLoop on bind
         any = ((NAME) b).isLoop();
      else //if current bind is not null or is not a nametype, return false
         any = false; 
      binding = b; //set binding back to b
      return any;
   }

   public Type actual() {
      return binding.actual(); //return the actual type 
   }

   public boolean coerceTo(Type t) {
      return this.actual().coerceTo(t); //returnt true if t is equal to the actual type
   }

   public void bind(Type t) {
      binding = t;
   }

   public String toString() {
      return "{Types.NAME: name=" + this.name + ", binding=" + this.binding + "}";
   }
}
