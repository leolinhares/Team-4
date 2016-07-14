package syntaxtree;
import visitor.Visitor;
import visitor.ExpVisitor;
import visitor.TypeVisitor;

public abstract class Type {
  public abstract void accept(Visitor v);
  public abstract Type accept(TypeVisitor v);
  public abstract Translate.Exp accept(ExpVisitor v);
}
