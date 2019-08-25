package chap2.programming;

import static org.junit.jupiter.api.Assertions.*;

import java.io.IOException;

import org.junit.jupiter.api.Test;

class MainTest {

	@Test
	void test() throws IOException {
		
	//	Process process = Runtime.getRuntime().exec("cmd /c start \"\" D:\\Workspace\\moderncompilerinjava\\src\\chap2\\programming\\test.bat");
		var myIntArray = new String[]{"D:\\Workspace\\moderncompilerinjava\\reference\\tiger\\testcases\\simple.tig"};
		Main.maintest(myIntArray);
		
	}

}
