package Canon;

import IR_visitor.IntVisitor;
import IR_visitor.StringVisitor;
import IR_visitor.TempVisitor;

class ExpCall extends Tree.Stm
{
  Tree.CALL call;

  ExpCall(Tree.CALL c)
  {
    call = c;
  }

  public Tree.ExpList kids()
  {
    return call.kids();
  }

  public Tree.Stm build(Tree.ExpList kids)
  {
    return new Tree.EXP1(call.build(kids));
  }

  public String accept(StringVisitor v)
  {

    return null;
  }

  public void accept(IntVisitor v, int d)
  {

  }

  public void accept(TempVisitor v)
  {

  }
}