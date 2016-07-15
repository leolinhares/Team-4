package Mips;

import Assem.Instr;
import Assem.InstrList;
import Assem.LABEL;
import Assem.MOVE;
import Assem.OPER;
import IR_visitor.TempVisitor;
import Temp.Temp;
import Temp.TempList;
import Tree.CONST;

public class Codegen implements TempVisitor {

	private InstrList codeList, listTail;
	private MipsFrame frame;

	public Codegen(MipsFrame f) {
		frame = f;
		codeList = listTail = null;
	}

	public InstrList codegen(Tree.Stm s) {
		s.accept(this);
		InstrList result = codeList;
		codeList = listTail = null;
		return result;
	}

	/* Assumes instructions are generated in reverse order. */
	private void emit(Instr instr) {
		if (listTail == null)
			listTail = codeList = new InstrList(instr, null);
		else
			listTail = listTail.tail = new InstrList(instr, null);
	}

	public void visit(Tree.SEQ n) {
		throw new Error("There should be no SEQ nodes in a canonical IR tree.");
	}

	public void visit(Tree.LABEL n) {
		emit(new LABEL(n.label.toString() + ":\n", n.label));
	}

	public void visit(Tree.JUMP n) {
		// JUMP(Tree.NAME, List<Label>)
		// JUMP(Exp, List<Label>)

		if (n.exp instanceof Tree.NAME) {
			Tree.NAME name = (Tree.NAME)n.exp;
			emit(new OPER("b " + name.label.toString(), null, null, n.targets));
			return;
		}
		emit(new OPER("jr `s0", null, null, n.targets));
	}

	public void visit(Tree.CJUMP s) {
		// CJUMP(op, Exp, CONST, Label, Label)
		// CJUMP(op, Exp, Exp, Label, Label)
		Tree.CONST left = (CONST) s.left;
		Tree.CONST right = (CONST) s.right;
		if (left == null)
			return;
		if (right == null) {
			s.left = s.right;
			s.right = left;
			switch (s.relop) {
			case Tree.CJUMP.EQ:
			case Tree.CJUMP.NE:
				break;
			case Tree.CJUMP.LT:
				s.relop = Tree.CJUMP.GT;
				break;
			case Tree.CJUMP.GE:
				s.relop = Tree.CJUMP.LE;
				break;
			case Tree.CJUMP.GT:
				s.relop = Tree.CJUMP.LT;
				break;
			case Tree.CJUMP.LE:
				s.relop = Tree.CJUMP.GE;
				break;
			case Tree.CJUMP.ULT:
				s.relop = Tree.CJUMP.UGT;
				break;
			case Tree.CJUMP.UGE:
				s.relop = Tree.CJUMP.ULE;
				break;
			case Tree.CJUMP.UGT:
				s.relop = Tree.CJUMP.ULT;
				break;
			case Tree.CJUMP.ULE:
				s.relop = Tree.CJUMP.UGE;
				break;
			default:
				throw new Error("");
			}
		}
	}

	public void visit(Tree.MOVE n) {
		// MOVE(MEM, Exp)
		// MOVE(MEM(+ Exp CONST), Exp)
		// MOVE(MEM(CONST), Exp)
		// MOVE(MEM(TEMP), Exp)
		// MOVE(MEM(Exp), Exp)
		// MOVE(TEMP, MEM)
		// MOVE(TEMP, MEM(+ Exp CONST))
		// MOVE(TEMP, MEM(CONST))
		// MOVE(TEMP, MEM(TEMP))
		// MOVE(TEMP, MEM(Exp))
		// MOVE(TEMP, Exp)

	}

	public void visit(Tree.EXP1 n) {
		n.exp.accept(this);
	}

	public Temp visit(Tree.BINOP e) {
		TempList t = new TempList(null, null);

		int right = ((Tree.CONST)e.right).value;
		switch (e.binop) {
		case Tree.BINOP.PLUS:
		{
			Temp left = (e.left instanceof Tree.TEMP) ?
					((Tree.TEMP)e.left).temp : e.left.accept(this);
					String off = Integer.toString(right);
					if (left == frame.FP()) {
						left = frame.SP;
						off += "+" + frame.name + "_framesize";
					}
					emit(new OPER("add `d0 `s0 " + off, t,null));
					return null;
		}
		}
		return null;
	}

	public Temp visit(Tree.MEM n) {
		// MEM(+ Exp CONST)
		// MEM(CONST)
		// MEM(TEMP)
		// MEM(Exp)

		return null;
	}

	public Temp visit(Tree.TEMP n) {


		return n.temp;
	}


	public Temp visit(Tree.ESEQ n) {
		throw new Error("There should be no ESEQ nodes in a canonical IR tree.");
	}

	/* Assumes NAME node is handled in visit methods for JUMP and CALL. */
	public Temp visit(Tree.NAME n) {
		throw new Error("In well-formed MiniJava program, NAME node is never visited outside of JUMP and CALL.");
	}

	public Temp visit(Tree.CONST n) {
		// TO DO: fill in
		return null;
	}

	public Temp visit(Tree.CALL n) {
		// TO DO: fill in
		return null;
	}
}
