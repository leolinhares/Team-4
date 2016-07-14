package Tree;

import IR_visitor.*;
import Temp.Temp;

abstract public class Exp
{
  abstract public ExpList kids();

  abstract public Exp build(ExpList kids);

  abstract public String accept(StringVisitor v);
  
  abstract public void accept(IntVisitor v, int d);

  abstract public Temp accept(TempVisitor v);

}
