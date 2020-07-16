package chap1.exercises;

import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.junit.jupiter.api.Test;

class BinaryTreeVisualiserTest {

	public BBinaryTreeNode getTree() {
     	return new BBinaryTreeNode(null, "o", "o", null)
     			.insert("e", "e")
     			.insert("b", "b")
     			.insert("g", "g")   
     			.insert("a", "a")   
     			.insert("f", "f")   
     			.insert("r", "r")
     			.insert("p", "p")
     			.insert("s", "s")   
     			.insert("h", "h")   
         			
     			
     			
    		;
	}
	
	public char[] shuffle(String input){
        List<Character> characters = new ArrayList<Character>();
        for(char c:input.toCharArray()){
            characters.add(c);
        }
        StringBuilder output = new StringBuilder(input.length());
        while(characters.size()!=0){
            int randPicker = (int)(Math.random()*characters.size());
            output.append(characters.remove(randPicker));
        }
        return output.toString().toCharArray();
    }
	
	static Random random = new Random();
	public BBinaryTreeNode getRandomTree() {
		String alpha = "abcdefghi";//hijkl";//mnopqrstuvwxyz";
		BBinaryTreeNode b = null; 
		for(char c: shuffle(alpha)) {
			if(b == null)
				b = new BBinaryTreeNode(null, String.valueOf(c), String.valueOf(c), null);
			else
				b = b.insert(String.valueOf(c), String.valueOf(c));
		}
		return b;
	}
	@Test
	void test() {
		var test = new BinaryTreeVisualiser(getRandomTree());
		test.visualise();
	}

}
