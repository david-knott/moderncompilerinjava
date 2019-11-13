import static org.junit.Assert.assertEquals;
import org.junit.Test;
import Parse.*;

public class Chap1Test {
  @Test
  public void lexerTest() {
      String filename = "./test/fixtures/lex.tig";
      Parse.Lex(filename);
  }
}