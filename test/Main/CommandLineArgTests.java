package Main;

import static org.junit.Assert.assertThrows;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import org.junit.Test;

public class CommandLineArgTests {

    @Test
    public void parseLongForm() {

        String[] args = new String[]{"--argone", "--argtwo", "--argthree"};
        this.findArgs(args);
    }

    public void findArgs(String[] args) {
        for(int i = 0; i < args.length; i++) {
            if(args[i].startsWith("--")) {
                System.out.println("larg=" + args[i].substring(2));
                if(i + 1 < args.length  && args[i + 1].length() > 2 && !args[i + 1].substring(0, 2).equals("--")) {
                    System.out.println("lvalue =" + args[i + 1]);
                    i++;
                }
            } else if(args[i].startsWith("-")) {
                for(int j = 1; j < args[i].length(); j++) {
                    System.out.println("sarg=" + args[i].charAt(j));
                }

            } else {
                throw new Error("Invalid argument syntax, show help..");
            }
        }
    }
    
    @Test
    public void parseLongFormWithValues() {

        String[] args = new String[]{"--argone", "valueone", "--argtwo", "valuetwo", "--argthree"};
        this.findArgs(args);

    }
 
    @Test
    public void parseShortFormSingle() {

        String[] args = new String[]{"-a"};
        this.findArgs(args);

    }
 
    @Test
    public void parseShortFormMulitple() {

        String[] args = new String[]{"-abcd"};
        this.findArgs(args);

    }
 
    @Test
    public void parseShortAndLongForm() {

        String[] args = new String[]{"-daxd", "--argone", "valueone", "--argtwo", "valuetwo", "--argthree"};
        this.findArgs(args);

    }
 
    @Test
    public void invalidSyntax() {

        String[] args = new String[]{"daxd", "--argone", "valueone", "--argtwo", "valuetwo", "--argthree"};
        this.findArgs(args);

    }
 
}
