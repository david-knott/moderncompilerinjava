package Assem;

import Temp.TempList;

public class TEST extends Instr {

   public Temp.TempList dst;
   public Temp.TempList src;
   public Targets jump;
   public String comments;


   public TEST(Temp.TempList d, Temp.TempList s) {
      if (d == null)
         throw new Error("d");
      if (s == null)
         throw new Error("s");
      dst = d;
      src = s;
      jump = null;
   }

   public TEST(Temp.TempList d, Temp.TempList s, Temp.LabelList j) {
      if (d == null)
         throw new Error("d");
      if (s == null)
         throw new Error("s");
      if (j == null)
         throw new Error("j");
      dst = d;
      src = s;
      jump = new Targets(j);
   }

   @Override
   public TempList use() {
      return this.src;
   }

   @Override
   public TempList def() {
      return this.dst;
   }

   @Override
   public Targets jumps() {
      return this.jump;
   }
}