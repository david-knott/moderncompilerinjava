package Codegen;

public class Validator {
    public static void assertNotNull(Object o) {
        if(o == null) throw new Error("Object is null:" + o);
    }
}