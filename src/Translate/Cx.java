package Translate;

public abstract class Cx extends Exp
{

  public Tree.Exp unEx()
  {
    Temp.Temp r = new Temp.Temp();
    Temp.Label t = new Temp.Label();
    Temp.Label f = new Temp.Label();

    return new Tree.ESEQ(new Tree.SEQ(new Tree.MOVE(new Tree.TEMP(r), new Tree.CONST(1)), new Tree.SEQ(unCx(t, f),
        new Tree.SEQ(new Tree.LABEL(f), new Tree.SEQ(new Tree.MOVE(new Tree.TEMP(r), new Tree.CONST(0)),
            new Tree.LABEL(t))))), new Tree.TEMP(r));
  }

  public abstract Tree.Stm unCx(Temp.Label t, Temp.Label f);

  public Tree.Stm unNx()
  {
    System.err.println("ERROR:  In well-typed MiniJava, (Cx c).unNx() should never be used.");
    return null;
  }
}
