package Temp;

import java.util.Hashtable;

public class Temp implements Comparable<Temp> {

    private static Hashtable<String, Temp> temps = new Hashtable<String, Temp>();
    private static Hashtable<Temp, String> revTemps = new Hashtable<Temp, String>();
    private static int count;

    public static Temp create() {
        Temp temp = new Temp();
        temps.put(temp.toString(), temp);
        revTemps.put(temp, temp.toString());
        return temp;
    }

    public static Temp create(String name) {
        if (temps.containsKey(name)) {
            return temps.get(name);
        }
        Temp temp = new Temp();
        temps.put(name, temp);
        revTemps.put(temp, name);
        return temp;
    }

	public static String name(Temp head) {
		return revTemps.get(head);
	}

    private int num;

    public Temp() {
        num = count++;
    }

    @Override
    public String toString() {
        return "t" + num;
    }

    @Override
    public int hashCode() {
        return num;
    }

    @Override
    public int compareTo(Temp o) {
        return o.num == this.num ? 0 : (o.num < this.num ? -1 : 1);
    }

}
