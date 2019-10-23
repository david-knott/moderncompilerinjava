package test;
import static org.junit.Assert.assertEquals;
import org.junit.Test;
import Parse.*;

public class Chap3Test {
  @Test
  public void letTest1() {
      String filename = "./test/fixtures/let.tig";
      new Parse(filename);
  }

  @Test
  public void letTest2() {
      String filename = "./test/fixtures/let_dec.tig";
      new Parse(filename);
  }
  
  @Test
  public void letTest3() {
      String filename = "./test/fixtures/let_dec2.tig";
      new Parse(filename);
  }
  
  @Test
  public void letTest4() {
      String filename = "./test/fixtures/let_dec3.tig";
      new Parse(filename);
  }
  
  @Test
  public void letTest5() {
      String filename = "./test/fixtures/let_dec4.tig";
      new Parse(filename);
  }
  
  @Test
  public void letTest6() {
      String filename = "./test/fixtures/let_dec5.tig";
      new Parse(filename);
  }
  

  @Test
  public void letTest7() {
      String filename = "./test/fixtures/let_dec6.tig";
      new Parse(filename);
  }
  
  @Test
  public void letDecSeqExp1() {
      String filename = "./test/fixtures/let_dec_seq_exp.tig";
      new Parse(filename);
  }
  
  @Test
  public void letDecSeqExp2() {
      String filename = "./test/fixtures/let_dec_seq_exp2.tig";
      new Parse(filename);
  }

  @Test
  public void letDecCall1() {
      String filename = "./test/fixtures/let_dec_call1.tig";
      new Parse(filename);
  }

  @Test
  public void letDecCallFunction() {
      String filename = "./test/fixtures/let_dec_call_function.tig";
      new Parse(filename);
  }

  @Test
  public void assignment() {
      String filename = "./test/fixtures/assignment.tig";
      new Parse(filename);
  }

  @Test
  public void ifThen() {
      String filename = "./test/fixtures/ifThen.tig";
      new Parse(filename);
  }

  @Test
  public void ifThenElse() {
      String filename = "./test/fixtures/ifThenElse.tig";
      new Parse(filename);
  }

  @Test
  public void whileL() {
      String filename = "./test/fixtures/while.tig";
      new Parse(filename);
  }

  @Test
  public void forL() {
      String filename = "./test/fixtures/for.tig";
      new Parse(filename);
  }




}