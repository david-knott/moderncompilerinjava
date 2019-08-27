package chap2.programming;

import static org.junit.jupiter.api.Assertions.*;

import java.io.IOException;

import org.junit.jupiter.api.Test;

class MainTest {

	@Test
	void test() throws IOException {
		
		for(int i = 1; i < 50; i++) {
			var myIntArray = new String[]{"D:\\Workspace\\moderncompilerinjava\\reference\\tiger\\testcases\\test" + i + ".tig"};
			Main.maintest(myIntArray);			
		}
		
	}

	
}
