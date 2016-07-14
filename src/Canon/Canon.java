package Canon;

public class Canon
{

  static boolean isNop(Tree.Stm a)
  {
    return a instanceof Tree.EXP1 && ((Tree.EXP1) a).exp instanceof Tree.CONST;
  }

  static Tree.Stm seq(Tree.Stm a, Tree.Stm b)
  {
    if (isNop(a))
      return b;
    else if (isNop(b))
      return a;
    else
      return new Tree.SEQ(a, b);
  }

  static boolean commute(Tree.Stm a, Tree.Exp b)
  {
    return isNop(a) || b instanceof Tree.NAME || b instanceof Tree.CONST;
  }

  static Tree.Stm do_stm(Tree.SEQ s)
  {
    return seq(do_stm(s.left), do_stm(s.right));
  }

  static Tree.Stm do_stm(Tree.MOVE s)
  {
    if (s.dst instanceof Tree.TEMP && s.src instanceof Tree.CALL)
      return reorder_stm(new MoveCall((Tree.TEMP) s.dst, (Tree.CALL) s.src));
    else if (s.dst instanceof Tree.ESEQ)
      return do_stm(new Tree.SEQ(((Tree.ESEQ) s.dst).stm, new Tree.MOVE(((Tree.ESEQ) s.dst).exp, s.src)));
    else
      return reorder_stm(s);
  }

  static Tree.Stm do_stm(Tree.EXP1 s)
  {
    if (s.exp instanceof Tree.CALL)
      return reorder_stm(new ExpCall((Tree.CALL) s.exp));
    else
      return reorder_stm(s);
  }

  static Tree.Stm do_stm(Tree.Stm s)
  {
    if (s instanceof Tree.SEQ)
      return do_stm((Tree.SEQ) s);
    else if (s instanceof Tree.MOVE)
      return do_stm((Tree.MOVE) s);
    else if (s instanceof Tree.EXP1)
      return do_stm((Tree.EXP1) s);
    else
      return reorder_stm(s);
  }

  static Tree.Stm reorder_stm(Tree.Stm s)
  {
    StmExpList x = reorder(s.kids());
    return seq(x.stm, s.build(x.exps));
  }

  static Tree.ESEQ do_exp(Tree.ESEQ e)
  {
    Tree.Stm stms = do_stm(e.stm);
    Tree.ESEQ b = do_exp(e.exp);
    return new Tree.ESEQ(seq(stms, b.stm), b.exp);
  }

  static Tree.ESEQ do_exp(Tree.Exp e)
  {
    if (e instanceof Tree.ESEQ)
      return do_exp((Tree.ESEQ) e);
    else
      return reorder_exp(e);
  }

  static Tree.ESEQ reorder_exp(Tree.Exp e)
  {
    StmExpList x = reorder(e.kids());
    return new Tree.ESEQ(x.stm, e.build(x.exps));
  }

  static StmExpList nopNull = new StmExpList(new Tree.EXP1(new Tree.CONST(0)), null);

  static StmExpList reorder(Tree.ExpList exps)
  {
    if (exps == null)
      return nopNull;
    else
      {
        Tree.Exp a = exps.head;
        if (a instanceof Tree.CALL)
          {
            Temp.Temp t = new Temp.Temp();
            Tree.Exp e = new Tree.ESEQ(new Tree.MOVE(new Tree.TEMP(t), a), new Tree.TEMP(t));
            return reorder(new Tree.ExpList(e, exps.tail));
          }
        else
          {
            Tree.ESEQ aa = do_exp(a);
            StmExpList bb = reorder(exps.tail);
            if (commute(bb.stm, aa.exp))
              return new StmExpList(seq(aa.stm, bb.stm), new Tree.ExpList(aa.exp, bb.exps));
            else
              {
                Temp.Temp t = new Temp.Temp();
                return new StmExpList(seq(aa.stm, seq(new Tree.MOVE(new Tree.TEMP(t), aa.exp), bb.stm)),
                    new Tree.ExpList(new Tree.TEMP(t), bb.exps));
              }
          }
      }
  }

  static Tree.StmList linear(Tree.SEQ s, Tree.StmList l)
  {
    return linear(s.left, linear(s.right, l));
  }

  static Tree.StmList linear(Tree.Stm s, Tree.StmList l)
  {
    if (s instanceof Tree.SEQ)
      return linear((Tree.SEQ) s, l);
    else
      return new Tree.StmList(s, l);
  }

  static public Tree.StmList linearize(Tree.Stm s)
  {
    return linear(do_stm(s), null);
  }
}
