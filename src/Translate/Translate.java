package Translate;

import syntaxtree.And;
import syntaxtree.ArrayAssign;
import syntaxtree.ArrayLength;
import syntaxtree.ArrayLookup;
import syntaxtree.Assign;
import syntaxtree.Block;
import syntaxtree.BooleanType;
import syntaxtree.Call;
import syntaxtree.ClassDeclExtends;
import syntaxtree.ClassDeclSimple;
import syntaxtree.False;
import syntaxtree.Formal;
import syntaxtree.Identifier;
import syntaxtree.IdentifierExp;
import syntaxtree.IdentifierType;
import syntaxtree.If;
import syntaxtree.IntArrayType;
import syntaxtree.IntegerLiteral;
import syntaxtree.IntegerType;
import syntaxtree.LessThan;
import syntaxtree.MainClass;
import syntaxtree.MethodDecl;
import syntaxtree.Minus;
import syntaxtree.NewArray;
import syntaxtree.NewObject;
import syntaxtree.Not;
import syntaxtree.Plus;
import syntaxtree.Print;
import syntaxtree.Program;
import syntaxtree.This;
import syntaxtree.Times;
import syntaxtree.True;
import syntaxtree.VarDecl;
import syntaxtree.While;
import visitor.ExpVisitor;

import java.util.HashMap;

import Frame.Access;

public class Translate implements ExpVisitor
{

  /* linked list of accumulated procedure fragments */
  private Frag                     frags     = null;
  private Frag                     frags_tail = null;

  /* current frame */
  private Frame.Frame              currFrame = null;

  /* pointer to address in heap at which instance variables are 
   stored, for current class (i.e., "this") */
  private Tree.Exp                 objPtr    = null;

  /* current offset from pointer to class object 
   (set to 0 in visit(ClassDecl), used in visit(VarDecl)) */
  private int                      offset;

  /* hashtable of field (instance) variables--key is variable name 
   string, value is offset from pointer to class object */
  private HashMap<String, Integer> fieldVars = null;

  /* hashtable of local and formal variables--key is variable name
   string, value is Access object to describe location of 
   variable (InFrame or InReg) */
  private HashMap<String, Access>  vars      = null;

  /* constructor: set currFrame and start visitor */
  public Translate(Program p, Frame.Frame f)
  {
    currFrame = f;
    p.accept(this);
  }

  /* add function with frame=currFrame and body to list of 
   ProcFrag objects, frags. */
  public void procEntryExit(Tree.Stm body)
  {
    ProcFrag newfrag = new ProcFrag(body, currFrame);
    if (frags == null)
      frags = newfrag;
    else
      frags_tail.next = newfrag;
    frags_tail = newfrag;
    
    /* original code
    newfrag.next = frags;
    frags = newfrag; */
  }

  /* public method for retrieving linked list of accumulated 
   procedure fragments */
  public Frag getResults()
  {
    return frags;
  }

  /* public method for printing body (IR tree) of each procedure */
  public void printResults()
  {
    Tree.Print p = new Tree.Print(System.out);
    Frag f = frags;
    while (f != null)
      {
        System.out.println();
        System.out.println("Function: " + ((ProcFrag) f).frame.name.toString());
        p.prStm(((ProcFrag) f).body);
        f = f.next;
      }
  }

  /* simply visit all class declarations */
  public Exp visit(Program n)
  {
    n.m.accept(this);
    for (int i = 0; i < n.cl.size(); i++)
      n.cl.elementAt(i).accept(this);
    return null;
  }

  /* setup new frame and visit statement in body */
  public Exp visit(MainClass n)
  {
    /* setup new frame for main method */
    Frame.Frame newFrame = currFrame.newFrame("main", 1);
    Frame.Frame oldFrame = currFrame;
    currFrame = newFrame;

    /* visit single statement in method body */
    Tree.Stm s = (n.s.accept(this)).unNx();

    /* there is no return expression, so return 0
     then create Tree.MOVE to store return value */
    Tree.Exp retExp = new Tree.CONST(0);
    Tree.Stm body = new Tree.MOVE(new Tree.TEMP(currFrame.RV()), new Tree.ESEQ(s, retExp));

    /* create new procedure fragment for method and add to list */
    procEntryExit(body);
    currFrame = oldFrame;

    return null;
  }

  /* prepare HashMap fieldVars so we can add to it in visit(VarDecl),
   set back to null at end */
  public Exp visit(ClassDeclSimple n)
  {
    fieldVars = new HashMap<String, Integer>();
    offset = -1 * currFrame.wordSize(); 
    for (int i = 0; i < n.vl.size(); i++)
      n.vl.elementAt(i).accept(this);
    for (int i = 0; i < n.ml.size(); i++)
      n.ml.elementAt(i).accept(this);
    fieldVars = null;
    return null;
  }

  /* do not bother with this node, since we are not handling
   inheritance */
  public Exp visit(ClassDeclExtends n)
  {
    return null;
  }

  /* if VarDecl in ClassDecl, add offset from class object pointer to 
   HashMap fieldVars
   else if VarDecl in MethodDecl, allocate local and add to HashMap vars */
  public Exp visit(VarDecl n)
  {
    if (vars == null)
      fieldVars.put(n.i.toString(), new Integer(offset += currFrame.wordSize()));
    else
      vars.put(n.i.toString(), currFrame.allocLocal(false));

    return null;
  }

  public Exp visit(MethodDecl n)
  {
    /* setup new frame for method */
    Frame.Frame newFrame = currFrame.newFrame(n.i.toString(), n.fl.size() + 1);
    Frame.Frame oldFrame = currFrame;
    currFrame = newFrame;

    /* add locals to HashMap vars */
    for (int i = 0; i < n.vl.size(); i++)
      n.vl.elementAt(i).accept(this);

    /* ADD CODE: move formals to fresh temps and add them to the HashMap vars */
    
    /* ADD CODE: set value of Tree.Exp objPtr
     Recall that objPtr is a pointer to the address in memory at which 
     instance variables are stored for the current class 
     (i.e., it is "this").
     In the MiniJava compiler, it is passed as an argument during all
     calls to MiniJava methods. */

    /* ADD CODE: visit each statement in method body, 
     creating new Tree.SEQ nodes as needed */
    Tree.Stm body = null; // FILL IN

    /* ADD CODE: get return expression and group with statements of body,
     then create Tree.MOVE to store return value */

    /* create new procedure fragment for method and add to list */
    procEntryExit(body);
    currFrame = oldFrame;
    vars = null;
    objPtr = null;

    return null;
  }

  /* these nodes are never reached by visitor */
  public Exp visit(Formal n)
  {
    return null;
  }

  public Exp visit(IntArrayType n)
  {
    return null;
  }

  public Exp visit(BooleanType n)
  {
    return null;
  }

  public Exp visit(IntegerType n)
  {
    return null;
  }

  public Exp visit(IdentifierType n)
  {
    return null;
  }

  public Exp visit(Block n)
  {
    /* ADD CODE -- don't return null */
    return null;
  }

  public Exp visit(If n)
  {
    /* ADD CODE -- don't return null */
    return null;
  }

  public Exp visit(While n)
  {
    /* ADD CODE -- don't return null */
    return null;
  }

  /* call external print function, passing integer expression as argument */
  public Exp visit(Print n)
  {
    Tree.Exp e = (n.e.accept(this)).unEx();
    return new Ex(currFrame.externalCall("printInt", new Tree.ExpList(e, null)));
  }

  /* get appropriate Tree.Exp node for identifier (set as Tree.MOVE dst),
   set expression as Tree.MOVE src */
  public Exp visit(Assign n)
  {
    Tree.Exp e = (n.e.accept(this)).unEx();
    return new Nx(new Tree.MOVE(getIdTree(n.i.toString()), e));
  }

  public Exp visit(ArrayAssign n)
  {
    /* ADD CODE -- don't return null */
    return null;
  }

  public Exp visit(And n)
  {
    /* ADD CODE --don't return null */
    return null;
  }

  public Exp visit(LessThan n)
  {
    /* ADD CODE -- don't return null */
    return null;
  }

  public Exp visit(Plus n)
  {
    /* ADD CODE -- don't return null */
    return null;
  }

  public Exp visit(Minus n)
  {
    /* ADD CODE -- don't return null */
    return null;
  }

  public Exp visit(Times n)
  {
    /* ADD CODE -- don't return null */
    return null;
  }

  public Exp visit(ArrayLookup n)
  {
    /* ADD CODE -- don't return null */
    return null;
  }

  public Exp visit(ArrayLength n)
  {
    /* ADD CODE -- don't return null */
    return null;
  }

  public Exp visit(Call n)
  {
    /* ADD CODE -- don't return null */
    return null;
  }

  public Exp visit(IntegerLiteral n)
  {
    /* ADD CODE -- don't return null */
    return null;
  }

  public Exp visit(True n)
  {
    /* ADD CODE -- don't return null */
    return null;
  }

  public Exp visit(False n)
  {
    /* ADD CODE -- don't return null */
    return null;
  }

  /* get appropriate Tree.Exp node for identifier */
  public Exp visit(IdentifierExp n)
  {
    return new Ex(getIdTree(n.s));
  }

  /* return pointer to address in heap at which instance variables are 
   stored, for current class */
  public Exp visit(This n)
  {
    return new Ex(objPtr);
  }

  /* call external "alloc" function, pass the number of bytes
   needed ((array length + 1) * wordsize) as the argument,
   initialize the value of each array element to 0,
   store the array length with the array */ 
  public Exp visit(NewArray n)
  {
    /* ADD CODE
     (Note: use currFrame.externalCall("alloc", new Tree.ExpList(...))) 
     -- don't return null */
    return null;
  }

  /* call external "alloc" function, pass the number of bytes
   needed (number of instance variables * wordsize) as
   the argument, initialize each instance variable to 0 */  
  public Exp visit(NewObject n)
  {
    /* ADD CODE
     (Note: you will need to get the number of field variables from your
     symbol table)  -- don't return null */
    return null;
  }

  public Exp visit(Not n)
  {
    /* ADD CODE -- don't return null */
    return null;
  }

  /* node never reached by visitor */
  public Exp visit(Identifier n)
  {
    return null;
  }

  /* if id is a local or formal variable (i.e., vars.get(id) returns an Access
   object), return result of calling exp()--Tree.MEM if InFrame, 
   Tree.TEMP if InReg
   else if id is a field (instance) variable, get its offset from class
   object pointer and return Tree.MEM 
   **For demonstration of this method, see visit(Assign) and visit(Identifier). */
  private Tree.Exp getIdTree(String id)
  {
    Frame.Access a = vars.get(id);
    if (a == null)
      {
        int offset = fieldVars.get(id).intValue();
        return new Tree.MEM(new Tree.BINOP(Tree.BINOP.PLUS, objPtr, new Tree.CONST(offset)));
      }

    return a.exp(new Tree.TEMP(currFrame.FP()));
  }
}
