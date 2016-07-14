package Tree;

import IR_visitor.*;
import Temp.Temp;

public class CALL extends Exp
{

  public Exp     func;
  public ExpList args;

  public CALL(Exp f, ExpList a)
  {
    func = f;
    args = a;
  }

  public ExpList kids()
  {
    return new ExpList(func, args);
  }

  public Exp build(ExpList kids)
  {
    return new CALL(kids.head, kids.tail);
  }

  public String accept(StringVisitor v)
  {
    return v.visit(this);
  }

  public void accept(IntVisitor v, int d)
  {
    v.visit(this, d);
  }

  public Temp accept(TempVisitor v)
  {
    return v.visit(this);
  }
}
