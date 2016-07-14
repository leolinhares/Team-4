package Mips;

import java.util.ListIterator;

import Assem.Instr;
import Temp.Temp;
import Tree.BINOP;
import Tree.CALL;
import Tree.CJUMP;
import Tree.CONST;
import Tree.ESEQ;
import Tree.Exp1;
import Tree.ExpCall;
import Tree.JUMP;
import Tree.LABEL;
import Tree.MEM;
import Tree.MOVE;
import Tree.MoveCall;
import Tree.NAME;
import Tree.SEQ;
import Tree.TEMP;

public class Codegen implements Tree.CodeVisitor{
	private MipsFrame mipsFrame;
    private ListIterator<Instr> listIterator;
	public Codegen(MipsFrame mipsFrame, ListIterator<Instr> listIterator) {
		// TODO Auto-generated constructor stub
		this.mipsFrame = mipsFrame;
		this.listIterator = listIterator;
	}
	@Override
	public void visit(SEQ n) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void visit(LABEL n) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void visit(JUMP n) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void visit(CJUMP n) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void visit(MOVE n) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void visit(Exp1 n) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public Temp visit(BINOP n) {
		// TODO Auto-generated method stub
		return null;
	}
	@Override
	public Temp visit(MEM n) {
		// TODO Auto-generated method stub
		return null;
	}
	@Override
	public Temp visit(TEMP n) {
		// TODO Auto-generated method stub
		return null;
	}
	@Override
	public Temp visit(ESEQ n) {
		// TODO Auto-generated method stub
		return null;
	}
	@Override
	public Temp visit(NAME n) {
		// TODO Auto-generated method stub
		return null;
	}
	@Override
	public Temp visit(CONST n) {
		// TODO Auto-generated method stub
		return null;
	}
	@Override
	public Temp visit(CALL n) {
		// TODO Auto-generated method stub
		return null;
	}
	@Override
	public Temp visit(MoveCall n) {
		// TODO Auto-generated method stub
		return null;
	}
	@Override
	public Temp visit(ExpCall n) {
		// TODO Auto-generated method stub
		return null;
	}

}
