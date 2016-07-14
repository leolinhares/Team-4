package Temp;

public class Temp
{
  private static int count;
  private int        num;

  public String toString()
  {
    return "t" + num;
  }

  public Temp()
  {
    num = count++;
  }

  public int getNum()
  {
    return num;
  }

  public static void reset()
  {
    count = 32;
  }
}
