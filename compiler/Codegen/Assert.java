package Codegen;

public class Assert {
    public static void assertNotNull(Object o) {
        if(o == null) throw new Error("Object is null:" + o);
    }

    public static void assertNotNegative(int i) {
        if(i < 0) throw new Error("Less than zero");
    }

	public static void assertLE(int iterations, int mAX_ITERATIONS) {
        if(iterations > mAX_ITERATIONS) throw new Error("Exceeded max");
	}
}