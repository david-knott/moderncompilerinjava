package test;
import static org.junit.Assert.assertEquals;
import org.junit.Test;
import Parse.*;

public class Chap3Test {
  @Test
  public void lexerTest1() {
      String filename = "./test/fixtures/let.tig";
      new Parse(filename);
  }
}