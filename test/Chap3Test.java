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
  

}