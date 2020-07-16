package chap1.exercises;

public interface TreeVisitor{
	void visit(BinaryTreeNode tree);
	void decLevel();
	void incLevel();
}

class SimpleDisplay
{
	private String[][] display;
	
	public SimpleDisplay() {
		display = new String[20][20];
		for(int i = 0; i < display.length; i++) {
			for(int j = 0; j < display[i].length; j++) {
				display[i][j] = " ";
			}
		}
	}
	
	public void insertLeft(String currentNodeValue, String leftNodeValue, int level) {
		var index = java.util.Arrays.asList(display[level * 2 - 2]).indexOf(currentNodeValue);
		//width needs to adjust based on the total number of items at a particular level
	//	display[level * 2 - 1][index - 2] = "/";
		//display[level * 2][index - 4] = leftNodeValue;	
	}

	public void insertRight(String currentNodeValue, String rightNodeValue, int level) {
		var index = java.util.Arrays.asList(display[level * 2 - 2]).indexOf(currentNodeValue);
		//width needs to adjust based on the total number of items at a particular level
//		display[level * 2 - 1][index + 2] = "\\";
//		display[level * 2][index + 4] = rightNodeValue;
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

class Ex1_1 {
    
    
}