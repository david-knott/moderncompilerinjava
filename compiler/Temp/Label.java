package Temp;

import java.util.Hashtable;

import Symbol.Symbol;

/**
 * A Label represents an address in assembly language.
 */

public class Label {
   private static Hashtable<String, Label> labels = new Hashtable<String, Label>();

   public static Label create() {
      return new Label();
   }

   public static Label create(Symbol symbol) {
      return create(symbol.toString());
   }

   public static Label create(String string) {
      if(labels.contains(string)){
         return labels.get(string);
      }
      Label label = new Label(string);
      labels.put(string, label);
      return label;
   }

   private String name;
   private static int count;

   /**
    * a printable representation of the label, for use in assembly language output.
    */
   public String toString() {
      return name;
   }

   /**
    * Makes a new label that prints as "name". Warning: avoid repeated calls to
    * <tt>new Label(s)</tt> with the same name <tt>s</tt>.
    */
   private Label(String n) {
      name = n;
   }

   /**
    * Makes a new label with an arbitrary name.
    */
   public Label() {
      this("L" + count++);
   }

   /**
    * Makes a new label whose name is the same as a symbol.
    */
   public Label(Symbol s) {
      this(s.toString());
   }
}
