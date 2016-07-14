/* Copyright (c) 1997 Andrew W. Appel.  Licensed software: see LICENSE file */
/* Revised 2004 by Bradford G. Nickerson to use visitors and LinkedList     */
/*   with Generic Java compiler (gjc) (following Vids Samanta's approach).  */
package Canon;
import java.util.LinkedList;

public class BasicBlocks {
  public StmListList blocks;
  public Temp.Label done;

  private StmListList lastBlock;
  private LinkedList<Tree.Stm> lastStm;

  private void addStm(Tree.Stm s) {
        lastStm.add(s);
  }

  private void doStms(LinkedList<Tree.Stm> l) {
      if (l.isEmpty()) {
	l.add(new Tree.JUMP(done));
	doStms(new LinkedList<Tree.Stm>(l));
      }
      else if (l.getFirst() instanceof Tree.JUMP 
	      || l.getFirst() instanceof Tree.CJUMP) {
	addStm(l.removeFirst());
	mkBlocks(l);
      } 
      else if (l.getFirst() instanceof Tree.LABEL) {
        l.addFirst(new Tree.JUMP(((Tree.LABEL)l.getFirst()).label));
        doStms(new LinkedList<Tree.Stm>(l));
      }
      else {
	addStm(l.removeFirst());
	doStms(l);
      }
  }

  void mkBlocks(LinkedList<Tree.Stm> l) {
     if (l.isEmpty()) return;
     else if (l.getFirst() instanceof Tree.LABEL) {
        lastStm = new LinkedList<Tree.Stm>();
        lastStm.add(l.getFirst());
        if (lastBlock==null)
  	   lastBlock= blocks= new StmListList(lastStm,null);
        else
  	   lastBlock = lastBlock.tail = new StmListList(lastStm,null);
        Tree.Stm t = l.removeFirst();
	doStms(l);
     }
     else {
	l.addFirst(new Tree.LABEL(new Temp.Label()));
        mkBlocks(new LinkedList<Tree.Stm>(l));
     }
  }
   
  public BasicBlocks(LinkedList<Tree.Stm> stms) {
    done = new Temp.Label();
    mkBlocks(stms);
  }

}
