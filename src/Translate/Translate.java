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
import Tree.*;

import java.util.HashMap;
import java.util.LinkedList;

import Frame.Access;
import Temp.Label;
import Temp.Temp;

public class Translate implements ExpVisitor
{

	private Frag frags     = null;
	private Frag frags_tail = null;

	private Frame.Frame currFrame = null;

	private Tree.Exp objPtr = null;

	private int offset;

	private HashMap<String, Integer> fieldVars = null;

	private HashMap<String, Access> vars = null;

	public Translate(Program p, Frame.Frame f)
	{
		currFrame = f;
		p.accept(this);
	}

	public void procEntryExit(Tree.Stm body)
	{
		ProcFrag newfrag = new ProcFrag(body, currFrame);
		if (frags == null)
			frags = newfrag;
		else
			frags_tail.next = newfrag;
		frags_tail = newfrag;

	}

	public Frag getResults()
	{
		return frags;
	}

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

	public Exp visit(Program n)
	{
		n.m.accept(this);
		for (int i = 0; i < n.cl.size(); i++)
			n.cl.elementAt(i).accept(this);
		return null;
	}

	public Exp visit(MainClass n)
	{
		Frame.Frame newFrame = currFrame.newFrame("main", 1);
		Frame.Frame oldFrame = currFrame;
		currFrame = newFrame;

		Tree.Stm s = (n.s.accept(this)).unNx();

		Tree.Exp retExp = new Tree.CONST(0);
		Tree.Stm body = new Tree.MOVE(new Tree.TEMP(currFrame.RV()), new Tree.ESEQ(s, retExp));

		procEntryExit(body);
		currFrame = oldFrame;

		return null;
	}


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

	public Exp visit(ClassDeclExtends n)
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
		Frame.Frame newFrame = currFrame.newFrame(n.i.toString(), n.fl.size() + 1);
		Frame.Frame oldFrame = currFrame;
		currFrame = newFrame;

		for (int i = 0; i < n.vl.size(); i++)
			n.vl.elementAt(i).accept(this);


		Tree.Stm body = null; 

		procEntryExit(body);
		currFrame = oldFrame;
		vars = null;
		objPtr = null;

		return null;
	}

	public Exp visit(Formal n)
	{
		n.i.accept(this);
		n.t.accept(this);
		return null;
	}

	public Exp visit(IntArrayType n)
	{
		n.accept(this);
		return null;
	}

	public Exp visit(BooleanType n)
	{
		n.accept(this);
		return null;
	}

	public Exp visit(IntegerType n)
	{
		n.accept(this);
		return null;
	}

	public Exp visit(IdentifierType n)
	{
		n.accept(this);
		return null;
	}

	public Exp visit(Block n)
	{
		n.accept(this);
		return null;
	}

	public Exp visit(If n)
	{
		Label T = new Label();
		Label F = new Label();
		Label D = new Label();
		Exp exp =  n.e.accept(this);
		Exp stmT =  n.s1.accept(this);
		Exp stmF =  n.s2.accept(this);
		return new Nx(new SEQ
				(new SEQ
						(new SEQ
								(new SEQ
										(new CJUMP(Tree.CJUMP.EQ,exp.unEx(),new CONST(1), T,F),
												new SEQ(new LABEL(T),stmT.unNx())),
										new JUMP(D)),
								new SEQ(new LABEL(F),stmF.unNx())),
						new LABEL(D)));
	}

	public Exp visit(While n)
	{
		Label test = new Label();
		Label T = new Label();
		Label F = new Label();
		Exp exp = n.e.accept(this);
		Exp body = n.s.accept(this);

		return new Nx(new SEQ
				(new SEQ
						(new SEQ(new LABEL(test),
								(new CJUMP(Tree.CJUMP.EQ, exp.unEx(),
										new CONST(1),T,F))),
								(new SEQ( new LABEL(T),body.unNx()))),
						new LABEL(F)));
	}

	public Exp visit(Print n)
	{
		Tree.Exp e = (n.e.accept(this)).unEx();
		return new Ex(currFrame.externalCall("printInt", new Tree.ExpList(e, null)));
	}


	public Exp visit(Assign n)
	{
		Tree.Exp e = (n.e.accept(this)).unEx();
		return new Nx(new Tree.MOVE(getIdTree(n.i.toString()), e));
	}

	public Exp visit(ArrayAssign n)
	{
		Exp _ret=null;

		Tree.Exp e1 = n.e1.accept(this).unEx();

		if (!(e1 instanceof Tree.TEMP)){
			Temp taux1= new Temp();
			Temp taux2= new Temp();
			e1= new ESEQ
					(new SEQ
							(new MOVE(new TEMP(taux1),new BINOP(Tree.BINOP.MUL,e1,new CONST(4))),
									new MOVE(new TEMP(taux2),
											new MEM(new BINOP(Tree.BINOP.PLUS,new TEMP(taux2),new TEMP(taux1))))),
							new TEMP(taux2));
		}

		Tree.Exp e2 = n.e2.accept(this).unEx();
		Temp t = new Temp();
		Temp t_index = new Temp();
		Temp t_size = new Temp();
		ExpList args1 = null;
		Label T = new Label();
		Label F = new Label();

		e2 = new ESEQ
				(new SEQ
						(new SEQ
								(new SEQ
										(new SEQ
												(new SEQ
														(new MOVE(new TEMP(t_index),
																new BINOP(Tree.BINOP.MUL,e2,new CONST(4))),
																new MOVE(new TEMP(t_size),new MEM(e1))),
														new CJUMP(Tree.CJUMP.GE,new TEMP(t_index),new TEMP(t_size),T,F)),
												new LABEL(T)),
										new MOVE(new TEMP(new Temp()),
												new CALL(new NAME(new Label("_error")), args1))),
								new LABEL(F)),
						new TEMP(t_index));

		Tree.Exp e3 = n.i.accept(this).unEx();

		return new Nx
				(new MOVE
						(new MEM
								(new BINOP
										(Tree.BINOP.PLUS,e1,new BINOP
												(Tree.BINOP.PLUS,e2,new CONST(4)))),
								e3));
	}

	public Exp visit(And n)
	{
		return n.accept(this);
	}

	public Exp visit(LessThan n)
	{
		return n.accept(this);
	}

	public Exp visit(Plus n)
	{
		return n.accept(this);
	}

	public Exp visit(Minus n)
	{
		return n.accept(this);
	}

	public Exp visit(Times n)
	{
		return n.accept(this);
	}

	public Exp visit(ArrayLookup n)
	{
		Temp t_index = new Temp();
		Temp t_size = new Temp();
		Tree.Exp e1 = n.e1.accept(this).unEx();
		Tree.Exp e2 = n.e2.accept(this).unEx();

		Label F = new Label();
		Label T = new Label();

		ExpList args1 = null;

		Tree.Stm s1 =
				new SEQ
				(new SEQ
						(new SEQ
								(new SEQ
										(new SEQ
												(new MOVE(new TEMP(t_index),new BINOP(Tree.BINOP.MUL,e2,new CONST(4))),
														new MOVE(new TEMP(t_size),new MEM(e1))),
												new CJUMP(Tree.CJUMP.GE,new TEMP(t_index),new TEMP(t_size),T,F)),
										new LABEL(T)),
								new MOVE(new TEMP(new Temp()),
										new CALL(new NAME(new Label("_error")),args1))),
						new LABEL(F));

		Temp t = new Temp();
		Tree.Stm s2 = new SEQ
				(s1,new MOVE(new TEMP(t),new MEM
						(new BINOP(Tree.BINOP.PLUS,e1,new BINOP
								(Tree.BINOP.PLUS,
										new BINOP(Tree.BINOP.MUL,e2,new CONST(4))
										,new CONST(4))))));
		return new Ex(new ESEQ(s2,new TEMP(t)));
	}

	public Exp visit(ArrayLength n)
	{
		return n.e.accept(this);
	}

	public Exp visit(Call n)
	{
		return null;
	}

	public Exp visit(IntegerLiteral n)
	{
		return new Ex(new CONST(n.i));
	}

	public Exp visit(True n)
	{
		return new Ex(new CONST(1));
	}

	public Exp visit(False n)
	{
		return new Ex(new CONST(0));
	}

	public Exp visit(IdentifierExp n)
	{
		return new Ex(getIdTree(n.s));
	}


	public Exp visit(This n)
	{
		return new Ex(objPtr);
	}


	public Exp visit(NewArray n)
	{
		Temp t1 = new Temp();
		Temp t2 = new Temp();
		Label cj = new Label();
		Label F = new Label();
		Label T = new Label();

		Exp exp1 = n.e.accept(this);

		Tree.Exp size =new BINOP
				(Tree.BINOP.MUL,
						new BINOP(Tree.BINOP.PLUS,exp1.unEx(),new CONST(1)),
						new CONST(4));
		ExpList args1 = null;
		Tree.Stm s1 =new MOVE
				(new TEMP(t1),
						new CALL(new NAME(new Label("_halloc")),args1));

		Tree.Stm s2 =
				new SEQ
				(new SEQ
						(new SEQ
								(new SEQ
										(new SEQ
												(new SEQ
														(new MOVE(new TEMP(t2),new CONST(4)),
																new SEQ (new LABEL(cj),new CJUMP(Tree.CJUMP.LT,new TEMP(t2),size,F,T))),
														new LABEL(T)),
												new MOVE(new MEM(new BINOP(Tree.BINOP.PLUS,new TEMP(t1),new TEMP(t2))),new CONST(0))),
										new MOVE(new TEMP(t2),new BINOP(Tree.BINOP.PLUS,new TEMP(t2),new CONST(4)))),
								new JUMP(cj)),
						new SEQ(new LABEL(F),new MOVE(new MEM(new TEMP(t1)),new BINOP(Tree.BINOP.MUL,exp1.unEx(),new CONST(4)))));

		return new Ex(new ESEQ(new SEQ(s1,s2),new TEMP(t1)));
	}

	public Exp visit(NewObject n)
	{
		return null;
	}

	public Exp visit(Not n)
	{
		return new Ex
				   (new BINOP(Tree.BINOP.MINUS, new CONST(1),
					  (n.e.accept(this)).unEx()));
	}

	public Exp visit(Identifier n)
	{
		return null;
	}

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
