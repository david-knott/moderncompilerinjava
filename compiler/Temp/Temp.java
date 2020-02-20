package Temp;

public class Temp {
  private static int count;
  private int num;

  @Override
  public String toString() {
    return "t" + num;
  }

  public Temp() {
    num = count++;
  }

  @Override
  public int hashCode() {
    return num;
  }
}
