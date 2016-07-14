package IR_visitor;

import Temp.Temp;
import Tree.*;

public interface TempVisitor
{
  public void visit(SEQ n);

  public void visit(LABEL n);

  public void visit(JUMP n);

  public void visit(CJUMP n);

  public void visit(MOVE n);

  public void visit(EXP1 n);

  public Temp visit(BINOP n);

  public Temp visit(MEM n);

  public Temp visit(TEMP n);

  public Temp visit(ESEQ n);

  public Temp visit(NAME n);

  public Temp visit(CONST n);

  public Temp visit(CALL n);
}
