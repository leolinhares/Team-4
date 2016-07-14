package Translate;

public abstract class Exp
{
  
  public abstract Tree.Exp unEx();

  public abstract Tree.Stm unNx();

  public abstract Tree.Stm unCx(Temp.Label t, Temp.Label f);
}
