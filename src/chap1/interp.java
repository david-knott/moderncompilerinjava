public class interp {
  static void interp(Stm s) {
    /* you write this part */ 
  
  }

  static int maxargs(Exp e){
    if(e instanceof EseqExp){
      EseqExp ee = (EseqExp)e;
      System.out.println("maxargs EseqExp");
      return maxargs(ee.stm);
    }
    return 0;
  }
  
  static int maxargs(ExpList el){
    if(el instanceof PairExpList){
      PairExpList pel = (PairExpList)el;
      System.out.println("maxargs PairExpList");
      return maxargs(pel.tail) + maxargs(pel.head);
    }
    if(el instanceof LastExpList){
      LastExpList lel = (LastExpList)el;
      System.out.println("maxargs LastExpList");
      return maxargs(lel.head);
    }
    
    return 0;
  }

  static int maxargs(Stm s) { /* you write this part */
    if(s instanceof CompoundStm){
      CompoundStm cs = (CompoundStm)s;
      System.out.println("maxargs compound statement");
      return maxargs(cs.stm2) + maxargs(cs.stm1);
    }
    if(s instanceof AssignStm){
      AssignStm cs = (AssignStm)s;
      System.out.println("maxargs assign statement");
      return maxargs(cs.exp);    
    }
    if(s instanceof PrintStm){
      PrintStm cs = (PrintStm)s;
      System.out.println("maxargs print statement");
      return 1 + maxargs(cs.exps);
    }
    return 0;
  }

  public static void main(String args[]) throws java.io.IOException {
    System.out.println(maxargs(prog.prog));
    interp(prog.prog);
  }
}