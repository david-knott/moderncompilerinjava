package Codegen;

public class Assert {
    public static void assertNotNull(Object o) {
        if(o == null) throw new Error("Object is null:" + o);
    }
}