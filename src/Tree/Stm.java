package Tree;

import IR_visitor.*;

abstract public class Stm
{
  abstract public ExpList kids();

  abstract public Stm build(ExpList kids);

  abstract public String accept(StringVisitor v);

  abstract public void accept(IntVisitor v, int d);

  abstract public void accept(TempVisitor v);

}
