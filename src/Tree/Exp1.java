package Tree;

import IR_visitor.*;

public class EXP1 extends Stm
{
  public Exp exp;

  public EXP1(Exp e)
  {
    exp = e;
  }

  public ExpList kids()
  {
    return new ExpList(exp, null);
  }

  public Stm build(ExpList kids)
  {
    return new EXP1(kids.head);
  }

  public String accept(StringVisitor v)
  {
    return v.visit(this);
  }

  public void accept(IntVisitor v, int d)
  {
    v.visit(this, d);
  }

  public void accept(TempVisitor v)
  {
    v.visit(this);
  }

}
