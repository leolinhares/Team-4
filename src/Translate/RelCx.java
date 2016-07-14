package Translate;

public class RelCx extends Cx
{

  private int relop;
  Tree.Exp    left;
  Tree.Exp    right;

  public RelCx(int o, Tree.Exp l, Tree.Exp r)
  {
    relop = o;
    left = l;
    right = r;
  }

  public Tree.Stm unCx(Temp.Label t, Temp.Label f)
  {
    return new Tree.CJUMP(relop, left, right, t, f);
  }
}
