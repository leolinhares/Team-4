package Tree;

import IR_visitor.*;
import Temp.Label;

public class LABEL extends Stm
{
  public Label label;

  public LABEL(Label l)
  {
    label = l;
  }

  public ExpList kids()
  {
    return null;
  }

  public Stm build(ExpList kids)
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

  public void accept(TempVisitor v)
  {
    v.visit(this);
  }

}
