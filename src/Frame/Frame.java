package Frame;

import Temp.*;

public abstract class Frame implements TempMap
{
  
  public Label name;

  public AccessList formals;

  public abstract Frame newFrame(String name, int argCount);

  public abstract int wordSize();

  public abstract Access allocLocal(boolean escape);

  public abstract Temp FP();

  public abstract Temp RV();

  public abstract Temp RA();

  public abstract Tree.Exp externalCall(String func, Tree.ExpList args);

  public abstract String tempMap(Temp temp);

  public abstract Assem.InstrList codegen(Tree.Stm s);
}
