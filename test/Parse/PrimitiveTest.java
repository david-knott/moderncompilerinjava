package Parse;

import java.io.ByteArrayInputStream;
import java.io.InputStream;

import org.junit.Test;

import ErrorMsg.ErrorMsg;

public class PrimitiveTest {
    
    @Test
    public void wrapExp() {
        ErrorMsg errorMsg = new ErrorMsg("Dec Wrapper", System.out);
        String tiger = "1 = 1 | 2 = 2";
        InputStream targetStream = new ByteArrayInputStream(tiger.getBytes());
        CupParser parser = new CupParser(targetStream, errorMsg);

    }
}
