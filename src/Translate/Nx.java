package Translate;

public class Nx extends Exp
{

  Tree.Stm stm;

  public Nx(Tree.Stm s)
  {
    stm = s;
  }

  public Tree.Stm unNx()
  {
    return stm;
  }

  public Tree.Exp unEx()
  {
    System.err.println("ERROR:  In well-typed MiniJava, (Nx n).unEx() should never be used.");
    return null;
  }

  public Tree.Stm unCx(Temp.Label t, Temp.Label f)
  {
    System.err.println("ERROR:  In well-typed MiniJava, (Nx n).unCx() should never be used.");
    return null;
  }
}
