package Mips;

import Assem.Instr;
import Assem.InstrList;
import Assem.LABEL;
import Assem.MOVE;
import Assem.OPER;
import IR_visitor.TempVisitor;

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

	}

	public void visit(Tree.CJUMP n) {
		// CJUMP(op, Exp, CONST, Label, Label)
		// CJUMP(op, Exp, Exp, Label, Label)

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

	public Temp.Temp visit(Tree.BINOP n) {
		// TO DO: fill in
		return null;
	}

	public Temp.Temp visit(Tree.MEM n) {
	    // MEM(+ Exp CONST)
	    // MEM(CONST)
	    // MEM(TEMP)
	    // MEM(Exp)

		return null;
	}

	public Temp.Temp visit(Tree.TEMP n) {
		
		
		return n.temp;
	}
	

	public Temp.Temp visit(Tree.ESEQ n) {
		throw new Error("There should be no ESEQ nodes in a canonical IR tree.");
	}

	/* Assumes NAME node is handled in visit methods for JUMP and CALL. */
	public Temp.Temp visit(Tree.NAME n) {
		throw new Error("In well-formed MiniJava program, NAME node is never visited outside of JUMP and CALL.");
	}

	public Temp.Temp visit(Tree.CONST n) {
		// TO DO: fill in
		return null;
	}

	public Temp.Temp visit(Tree.CALL n) {
		// TO DO: fill in
		return null;
	}
}
