/* Copyright (c) 1997 Andrew W. Appel.  Licensed software: see LICENSE file */
/* Revised 2004 by Bradford G. Nickerson to use visitors and LinkedList     */
/*   with Generic Java compiler (gjc) (following Vids Samanta's approach)   */
package Canon;
import java.util.Iterator;
import java.util.List;

import Tree.Exp1;

import java.util.LinkedList;

class StmExpList {
	Tree.Stm stm;
	LinkedList<Tree.Exp> exps;
	StmExpList(Tree.Stm s, LinkedList<Tree.Exp> e) {
		stm=s; exps=e;}
}

public class Canon {

	static boolean isNop(Tree.Stm a) {
		return a instanceof Exp1
				&& ((Exp1)a).exp instanceof Tree.CONST;
	}

	static Tree.Stm seq(Tree.Stm a, Tree.Stm b) {
		if (isNop(a)) return b;
		else if (isNop(b)) return a;
		else return new Tree.SEQ(a,b);
	}

	static boolean commute(Tree.Stm a, Tree.Exp b) {
		return isNop(a)
				|| b instanceof Tree.NAME
				|| b instanceof Tree.CONST;
	}

	static Tree.Stm do_stm(Tree.SEQ s) { 
		return seq(do_stm(s.left), do_stm(s.right));
	}

	static Tree.Stm do_stm(Tree.MOVE s) { 
		if (s.dst instanceof Tree.TEMP 
				&& s.src instanceof Tree.CALL) 
			return reorder_stm(new Tree.MoveCall((Tree.TEMP)s.dst,
					(Tree.CALL)s.src));
		else if (s.dst instanceof Tree.ESEQ) 
			return do_stm(new Tree.SEQ(((Tree.ESEQ)s.dst).stm,
					new Tree.MOVE(((Tree.ESEQ)s.dst).exp, s.src)));
		else return reorder_stm(s);
	}

	static Tree.Stm do_stm(Exp1 s) { 
		if (s.exp instanceof Tree.CALL) 
			return reorder_stm(new Tree.ExpCall((Tree.CALL)s.exp));
		else return reorder_stm(s);
	}

	static Tree.Stm do_stm(Tree.Stm s) {
		if (s instanceof Tree.SEQ) return do_stm((Tree.SEQ)s);
		else if (s instanceof Tree.MOVE) return do_stm((Tree.MOVE)s);
		else if (s instanceof Exp1) return do_stm((Exp1)s);
		else return reorder_stm(s);
	}

	static Tree.Stm reorder_stm(Tree.Stm s) {
		StmExpList x = reorder(s.kids());
		return seq(x.stm, s.build(x.exps));
	}

	static Tree.ESEQ do_exp(Tree.ESEQ e) {
		Tree.Stm stms = do_stm(e.stm);
		Tree.ESEQ b = do_exp(e.exp);
		return new Tree.ESEQ(seq(stms,b.stm), b.exp);
	}

	static Tree.ESEQ do_exp (Tree.Exp e) {
		if (e instanceof Tree.ESEQ) return do_exp((Tree.ESEQ)e);
		else return reorder_exp(e);
	}

	static Tree.ESEQ reorder_exp (Tree.Exp e) {
		StmExpList x = reorder(e.kids());
		return new Tree.ESEQ(x.stm, e.build(x.exps));
	}

	static StmExpList nopNull = new StmExpList(new Exp1(new Tree.CONST(0)),
			new LinkedList<Tree.Exp>());

	static StmExpList reorder(LinkedList<Tree.Exp> exps) {
		if (exps.isEmpty()) return nopNull;
		else {
			Tree.Exp a = exps.removeFirst();
			if (a instanceof Tree.CALL) {
				Temp.Temp t = new Temp.Temp();
				Tree.Exp e = new Tree.ESEQ(new Tree.MOVE(new Tree.TEMP(t), a),
						new Tree.TEMP(t));
				exps.addFirst(e);
				return reorder(new LinkedList<Tree.Exp>(exps));
			} 
			else {
				Tree.ESEQ aa = do_exp(a);
				StmExpList bb = reorder(exps);
				if (commute(bb.stm, aa.exp)){
					bb.exps.addFirst(aa.exp);
					return new StmExpList(seq(aa.stm,bb.stm), 
							new LinkedList<Tree.Exp>(bb.exps));
				}
				else {
					Temp.Temp t = new Temp.Temp();
					Tree.Stm cc = seq(aa.stm, 
							seq(new Tree.MOVE(new Tree.TEMP(t), aa.exp), bb.stm));
					bb.exps.addFirst(new Tree.TEMP(t));
					return new StmExpList( cc, new LinkedList<Tree.Exp>(bb.exps));
				}
			}
		}
	}

	static LinkedList<Tree.Stm> linear(Tree.SEQ s, LinkedList<Tree.Stm> l) {
		return linear(s.left,linear(s.right,l));
	}

	static LinkedList<Tree.Stm> linear(Tree.Stm s, LinkedList<Tree.Stm> l) {
		if (s instanceof Tree.SEQ) return linear((Tree.SEQ)s, l);
		else {
			l.addFirst(s);  
			return new LinkedList<Tree.Stm>(l);
		}
	}

	static public LinkedList<Tree.Stm> linearize(Tree.Stm s) {
		return linear(do_stm(s), new LinkedList<Tree.Stm>());
	}
}
