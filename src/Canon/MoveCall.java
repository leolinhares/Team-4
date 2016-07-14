package Canon;

import IR_visitor.IntVisitor;
import IR_visitor.StringVisitor;
import IR_visitor.TempVisitor;

class MoveCall extends Tree.Stm
{
  Tree.TEMP dst;
  Tree.CALL src;

  MoveCall(Tree.TEMP d, Tree.CALL s)
  {
    dst = d;
    src = s;
  }

  public Tree.ExpList kids()
  {
    return src.kids();
  }

  public Tree.Stm build(Tree.ExpList kids)
  {
    return new Tree.MOVE(dst, src.build(kids));
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