package Tree;

import IR_visitor.*;

public class TEMP extends Exp
{
  public Temp.Temp temp;

  public TEMP(Temp.Temp t)
  {
    temp = t;
  }

  public ExpList kids()
  {
    return null;
  }

  public Exp build(ExpList kids)
  {
    return this;
  }

  public String accept(StringVisitor v)
  {
    return v.visit(this);
  }

  public void accept(IntVisitor v, int d)
  {
    v.visit(this, d);
  }

  public Temp.Temp accept(TempVisitor v)
  {
    return v.visit(this);
  }

}
