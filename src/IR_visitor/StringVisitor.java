package IR_visitor;

import Tree.*;

public interface StringVisitor
{
  public String visit(LABEL n);

  public String visit(JUMP n);

  public String visit(CJUMP n);

  public String visit(MOVE n);

  public String visit(EXP1 n);

  public String visit(BINOP n);

  public String visit(MEM n);

  public String visit(TEMP n);

  public String visit(NAME n);

  public String visit(CONST n);

  public String visit(CALL n);

  public String visit(SEQ n);

  public String visit(ESEQ n);
}
