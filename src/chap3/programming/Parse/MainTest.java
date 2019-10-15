package chap3.programming.Parse;

import static org.junit.jupiter.api.Assertions.*;

import java.io.IOException;

import org.junit.jupiter.api.Test;

import chap3.programming.Parse.Main;

public class MainTest {
	@Test
	void test() throws IOException {
		
		for(int i = 1; i < 2; i++) {
			var myIntArray = new String[]{"D:\\Workspace\\moderncompilerinjava\\reference\\tiger\\testcases\\test" + i + ".tig"};
			Main.main(myIntArray);			
		}
		
	}
}
