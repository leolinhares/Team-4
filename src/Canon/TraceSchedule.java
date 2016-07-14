/* Copyright (c) 1997 Andrew W. Appel.  Licensed software: see LICENSE file */
/* Revised 2004 by Bradford G. Nickerson to use visitors and LinkedList     */
/*   with Generic Java compiler (gjc) (following Vids Samanta's approach).  */
package Canon;
import java.util.LinkedList;

public class TraceSchedule {

  public LinkedList<Tree.Stm> stms;
  BasicBlocks theBlocks;
  java.util.Dictionary table = new java.util.Hashtable();

  void getLast(LinkedList<Tree.Stm> block) {
     LinkedList<Tree.Stm> l = new LinkedList<Tree.Stm>(block);
     while (!l.isEmpty()) stms.add(l.removeFirst());
  }

  void trace(LinkedList<Tree.Stm> l) {
   for(;;) {
     Tree.LABEL lab = (Tree.LABEL)l.getFirst();
     table.remove(lab.label.toString());
     getLast(l);
     Tree.Stm s = stms.getLast();
     if (s instanceof Tree.JUMP) {
	Tree.JUMP j = (Tree.JUMP)s;
        Temp.Label tlab = j.targets.getFirst();
        LinkedList<Tree.Stm> target = (LinkedList)table.get(tlab.toString());
	if (j.targets.size() == 1 && target != null) {
          l=target;
        }
	else {
	  getNext();
	  return;
        }
     }
     else if (s instanceof Tree.CJUMP) {
	Tree.CJUMP j = (Tree.CJUMP)s;
        LinkedList<Tree.Stm> t = (LinkedList)table.get(j.iftrue.toString());
        LinkedList<Tree.Stm> f = (LinkedList)table.get(j.iffalse.toString());
        if (!f.isEmpty()) {
	  l=f;
	}
        else if (!t.isEmpty()) {
	  Tree.Stm snew = new Tree.CJUMP(Tree.CJUMP.notRel(j.relop),
					j.left,j.right,
					j.iffalse,j.iftrue);
	  stms.removeLast();
          stms.add(snew);
	  l=t;
        }
        else {
	  Temp.Label ff = new Temp.Label();
          stms.removeLast();
	  stms.add(new Tree.CJUMP(j.relop,j.left,j.right,
					j.iftrue,ff));
          stms.add(new Tree.LABEL(ff));
          stms.add(new Tree.JUMP(j.iffalse));
	  getNext();
	  return;
        }
     }
     else throw new Error("Bad basic block in TraceSchedule");
    }
  }

  void getNext() {
      if (theBlocks.blocks==null) {
	  stms.add(new Tree.LABEL(theBlocks.done));
	  return;
      }
      else {
	 LinkedList<Tree.Stm> s = theBlocks.blocks.head;
	 Tree.LABEL lab = (Tree.LABEL)s.getFirst();
	 if (table.get(lab.label.toString()) != null) {
          trace(s);
         }
         else {
	   theBlocks.blocks = theBlocks.blocks.tail;
           getNext();
         }
      }
  }

  public TraceSchedule(BasicBlocks b) {
    theBlocks=b;
    for(StmListList l = b.blocks; l!=null; l=l.tail) {
       table.put(((Tree.LABEL)l.head.getFirst()).label.toString(), l.head);
    }
    stms = new LinkedList<Tree.Stm>();
    getNext();
    table=null;
  }        
}


