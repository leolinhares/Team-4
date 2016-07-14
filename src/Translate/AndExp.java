package Translate;

public class AndExp extends Exp
{

  Temp.Label label = new Temp.Label();
  Exp        left;
  Exp        right;

  public AndExp(Exp l, Exp r)
  {
    left = l;
    right = r;
  }

  public Tree.Exp unEx()
  {
    Temp.Temp r = new Temp.Temp();
    Temp.Label t = new Temp.Label();
    Temp.Label f = new Temp.Label();

    return new Tree.ESEQ(new Tree.SEQ(new Tree.MOVE(new Tree.TEMP(r), new Tree.CONST(1)), new Tree.SEQ(left.unCx(label,
        f), new Tree.SEQ(new Tree.LABEL(label), new Tree.SEQ(right.unCx(t, f), new Tree.SEQ(new Tree.LABEL(f),
        new Tree.SEQ(new Tree.MOVE(new Tree.TEMP(r), new Tree.CONST(0)), new Tree.LABEL(t))))))), new Tree.TEMP(r));
  }

  public Tree.Stm unCx(Temp.Label t, Temp.Label f)
  {
    return new Tree.SEQ(left.unCx(label, f), new Tree.SEQ(new Tree.LABEL(label), right.unCx(t, f)));
  }

  public Tree.Stm unNx()
  {
    System.err.println("ERROR:  In well-typed MiniJava, (AndExp a).unNx() should never be used.");
    return null;
  }
}
