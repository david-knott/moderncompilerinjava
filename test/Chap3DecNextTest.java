
import static org.junit.Assert.assertEquals;
import org.junit.Test;
import Parse.*;

public class Chap3DecNextTest {

    @Test
    public void associativeTest() {
        String filename = "./test/fixtures/decnext.tig";
        new Parse(filename);
    }
}