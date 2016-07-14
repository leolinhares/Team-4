package IR_visitor;

import Tree.*;

public interface IntVisitor
{
  public void visit(SEQ n, int d);

  public void visit(LABEL n, int d);

  public void visit(JUMP n, int d);

  public void visit(CJUMP n, int d);

  public void visit(MOVE n, int d);

  public void visit(EXP1 n, int d);

  public void visit(BINOP n, int d);

  public void visit(MEM n, int d);

  public void visit(TEMP n, int d);

  public void visit(ESEQ n, int d);

  public void visit(NAME n, int d);

  public void visit(CONST n, int d);

  public void visit(CALL n, int d);
}
