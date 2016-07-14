package Tree;

import IR_visitor.*;

public class MOVE extends Stm
{
  public Exp dst, src;

  public MOVE(Exp d, Exp s)
  {
    dst = d;
    src = s;
  }

  public ExpList kids()
  {
    if (dst instanceof MEM)
      return new ExpList(((MEM) dst).exp, new ExpList(src, null));
    else
      return new ExpList(src, null);
  }

  public Stm build(ExpList kids)
  {
    if (dst instanceof MEM)
      return new MOVE(new MEM(kids.head), kids.tail.head);
    else
      return new MOVE(dst, kids.head);
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
