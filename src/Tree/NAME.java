package Tree;

import IR_visitor.*;
import Temp.Temp;
import Temp.Label;


public class NAME extends Exp
{
  public Label label;

  public NAME(Label l)
  {
    label = l;
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

  public Temp accept(TempVisitor v)
  {
    return v.visit(this);
  }

}
