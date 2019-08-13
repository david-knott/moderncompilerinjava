package chap1.exercises;

import static org.junit.jupiter.api.Assertions.*;

import java.lang.reflect.Array;
import java.util.Arrays;

import org.junit.jupiter.api.Test;

class TreeVisitorTest {

	public Tree getTree() {
		var t1 = new Tree(null, "e", null);
        var t2 = Ex1_1.insert("z", t1);
        var t3 = Ex1_1.insert("h", t2);
        var t4 = Ex1_1.insert("a", t3);
        var t5 = Ex1_1.insert("f", t4);
        var t6 = Ex1_1.insert("u", t5);
        var t7 = Ex1_1.insert("w", t6);
        return t7;
	}

	public Tree getTree2() {
		var t1 = new Tree(null, "e", null);
        var t2 = Ex1_1.insert("a", t1);
        var t3 = Ex1_1.insert("b", t2);
        var t4 = Ex1_1.insert("c", t3);
        var t5 = Ex1_1.insert("f", t4);
        var t6 = Ex1_1.insert("g", t5);
        var t7 = Ex1_1.insert("h", t6);
        return t7;
	}

	public class SimpleDisplay
	{
		private String[][] display;
		
		public SimpleDisplay() {
			display = new String[20][11];
			for(int i = 0; i < display.length; i++) {
				for(int j = 0; j < display[i].length; j++) {
					display[i][j] = " ";
				}
			}
		}
		
		public void insertLeft(String currentNodeValue, String leftNodeValue, int level) {
			var index = java.util.Arrays.asList(display[level * 2 - 2]).indexOf(currentNodeValue);
	//		var i = Arrays.b display[level - 1]
			display[level * 2 - 1][index - 1] = "/";
			display[level * 2][index - 2] = leftNodeValue;	
		}

		public void insertRight(String currentNodeValue, String rightNodeValue, int level) {
			var index = java.util.Arrays.asList(display[level * 2 - 2]).indexOf(currentNodeValue);
			display[level * 2 - 1][index + 1] = "\\";
			display[level * 2][index + 2] = rightNodeValue;
		}
		
		public void display() {
			for(int i = 0; i < display.length; i++) {
				for(int j = 0; j < display[i].length; j++) {
					System.out.print(display[i][j]);
				}
				System.out.println();
			}
		}

		public void insertRoot(String key) {
			display[0][4] = key;
			// TODO Auto-generated method stub
		}
	}

	@Test
	void test() {
		var t7 = getTree();
		final SimpleDisplay simpleDisplay = new SimpleDisplay();
    	simpleDisplay.insertRoot(t7.key);

        t7.accept(new TreeVisitor() {
        	//what level of recursion are we at ?
        	int level = 0;
			@Override
			public void visit(Tree tree) {
				var currentNodeValue = tree.key;
				//if present, insert left node value at level n + 1
				var leftNodeValue = tree.left != null ? tree.left.key : null;
				if(leftNodeValue != null) {
					simpleDisplay.insertLeft(currentNodeValue, leftNodeValue, level + 1);
				}
				
				//if present, insert right node value at level n + 1
				var rightNodeValue = tree.right!= null ? tree.right.key : null;
				if(rightNodeValue != null)
					simpleDisplay.insertRight(currentNodeValue, rightNodeValue, level + 1);
				
			}
			@Override
			public void decLevel() {
				level--;
				
			}
			@Override
			public void incLevel() {
				level++;		
			}
        	
        });
        
        simpleDisplay.display();
	}
	

}
