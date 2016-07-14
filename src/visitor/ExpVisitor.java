package visitor;

import syntaxtree.*;

public interface ExpVisitor
{
  public Translate.Exp visit(Program n);

  public Translate.Exp visit(MainClass n);

  public Translate.Exp visit(ClassDeclSimple n);

  public Translate.Exp visit(ClassDeclExtends n);

  public Translate.Exp visit(VarDecl n);

  public Translate.Exp visit(MethodDecl n);

  public Translate.Exp visit(Formal n);

  public Translate.Exp visit(IntArrayType n);

  public Translate.Exp visit(BooleanType n);

  public Translate.Exp visit(IntegerType n);

  public Translate.Exp visit(IdentifierType n);

  public Translate.Exp visit(Block n);

  public Translate.Exp visit(If n);

  public Translate.Exp visit(While n);

  public Translate.Exp visit(Print n);

  public Translate.Exp visit(Assign n);

  public Translate.Exp visit(ArrayAssign n);

  public Translate.Exp visit(And n);

  public Translate.Exp visit(LessThan n);

  public Translate.Exp visit(Plus n);

  public Translate.Exp visit(Minus n);

  public Translate.Exp visit(Times n);

  public Translate.Exp visit(ArrayLookup n);

  public Translate.Exp visit(ArrayLength n);

  public Translate.Exp visit(Call n);

  public Translate.Exp visit(IntegerLiteral n);

  public Translate.Exp visit(True n);

  public Translate.Exp visit(False n);

  public Translate.Exp visit(IdentifierExp n);

  public Translate.Exp visit(This n);

  public Translate.Exp visit(NewArray n);

  public Translate.Exp visit(NewObject n);

  public Translate.Exp visit(Not n);

  public Translate.Exp visit(Identifier n);
}
