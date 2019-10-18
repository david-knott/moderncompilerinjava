package chap1.programming;

class Table {
  String id; int value; Table tail;
  Table(String i, int v, Table t) { 
    id = i; value = v; tail = t;
  }
  int tailCount(){
    int r = 0;
    Table tt = this;
    do{
      r++;
      tt = tt.tail;
    } while(tt != null);
    return r;
  }
  int lookup(String key){
    Table tt = this;
    do{
      if(key.equals(tt.id))
        return tt.value;
      tt = tt.tail;
    } while(tt != null);
    throw new RuntimeException("This shouldn't happen");
  }
  public String toString(){
    String s = "[table:";
    Table tt = this;
    do{
      s = s + " id=" + tt.id + " value=" + tt.value;//e + " tail=" + tt.tail;
      tt = tt.tail;
    } while(tt != null);
    return s + "]";
  }
}

class IntAndTable {
  int i; Table t;
  IntAndTable(int ii, Table tt){
    i = ii;
    t = tt;
  }
}

public class interp {

  static void interp(Stm s) {
    var table = interpStm(s, null);
    System.out.println(table); 
  }

  static IntAndTable interpExp(Exp e, Table t){
 //   System.out.println("interpExp Exp " + e);
    if(e instanceof IdExp){
      var ne = (IdExp)e;
      var id = ne.id;
      var num = t.lookup(id);
      return new IntAndTable(num, t);
    }
    if(e instanceof NumExp){
      var ne = (NumExp)e;
      var num = ne.num;
      return new IntAndTable(num, t);
    }
    if(e instanceof OpExp){
      var ne = (OpExp)e;
      var left = interpExp(ne.left, t);
      //pass the table from left into right
      var right = interpExp(ne.right, left.t);
      switch(ne.oper){
        case 1: //+
          return new IntAndTable(left.i + right.i, right.t);
        case 2: //-
          return new IntAndTable(left.i - right.i, right.t);
        case 3: //*
          return new IntAndTable(left.i * right.i, right.t);
        case 4: ///
          return new IntAndTable(left.i / right.i, right.t);      
      }
    }
    if(e instanceof EseqExp){
      var ne = (EseqExp)e;
      return interpExp(ne.exp, interpStm(ne.stm, t));
    }
    throw new RuntimeException("This shouldn't happen " + e.getClass().getName());
  }

  static IntAndTable interpExp(ExpList el, Table t){
    if(el instanceof PairExpList){
      PairExpList pel = (PairExpList)el;
      var res = interpExp(pel.head, interpExp(pel.tail, t).t);
      return res;
    }
    if(el instanceof LastExpList){
      LastExpList lel = (LastExpList)el;
      var res = interpExp(lel.head, t);
      return res;
    }
    throw new RuntimeException("This shouldn't happen " + el.getClass().getName());
  }
  
  static Table interpStm(Stm s, Table t){
    if(s instanceof CompoundStm){
      CompoundStm cs = (CompoundStm)s;
      return interpStm(cs.stm2, interpStm(cs.stm1, t));
    }
    if(s instanceof AssignStm){
      //create a new value in the symbol table.
      AssignStm cs = (AssignStm)s;
      var assignResult = interpExp(cs.exp, t);
      //set the assignment value and the result from the expression
      return new Table(cs.id, assignResult.i, assignResult.t);
    }
    if(s instanceof PrintStm){
      //show print the contents of the evaulated expressions in cs.exps
      //we first expand out the exppression list and pass in a reference to 
      //the current table
      PrintStm cs = (PrintStm)s;
      ExpList expList = cs.exps;
      Exp nextExp = null;
      ExpList nextExpList = null;
      Table currentTable = t;
      //keep iterating while exp list is a pair of expressions
      while(expList instanceof PairExpList){
        //expand out the pair expression list to current expression and expression list
        nextExp = ((PairExpList)expList).head;
        nextExpList = ((PairExpList)expList).tail;
        //evalue the current exp
        var intAndTable = interpExp(nextExp, currentTable);
        System.out.print(intAndTable.i);
        //set the current table to the result of the interpExp function
        currentTable = intAndTable.t;
        //set the list to the current expression list, and repeat until no more pair of expresion lists 
        expList = nextExpList;
      }
      //the last item must be a last exp list item.
      nextExp =  ((LastExpList) expList).head;
      IntAndTable it = interpExp(nextExp, currentTable);
      System.out.println(it.i);
      return it.t;
    }
    throw new RuntimeException("This shouldn't happen " + s.getClass().getName());
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
      return maxargs(cs.stm1) + maxargs(cs.stm2);
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
   // System.out.println(maxargs(prog.prog));
    interp(prog.prog);
  }
}
