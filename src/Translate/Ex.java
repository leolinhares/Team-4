package Translate;

public class Ex extends Exp
{

  Tree.Exp exp;

  public Ex(Tree.Exp e)
  {
    exp = e;
  }

  public Tree.Exp unEx()
  {
    return exp;
  }

  public Tree.Stm unNx()
  {
    return new Tree.EXP1(exp);
  }

  public Tree.Stm unCx(Temp.Label t, Temp.Label f)
  {
    return new Tree.CJUMP(Tree.CJUMP.NE, exp, new Tree.CONST(0), t, f);
  }
}
