package Assem;

import Temp.TempList;

public class TEST extends Instr {

   public Temp.TempList dst;
   public Temp.TempList src;
   public Targets jump;
   public String comments ;


   public TEST() {
      dst = null;
      src = null;
      jump = null;
      super.assem = "";
   }
   public TEST(Temp.TempList d, Temp.TempList s) {
      dst = d;
      src = s;
      jump = null;
      super.assem = "";
   }

   public TEST(Temp.TempList d, Temp.TempList s, Temp.LabelList j) {
      if (j == null)
         throw new Error("j");
      dst = d;
      src = s;
      jump = new Targets(j);
      super.assem = "";
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