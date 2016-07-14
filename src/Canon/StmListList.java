/* Copyright (c) 1997 Andrew W. Appel.  Licensed software: see LICENSE file */
package Canon;
import java.util.LinkedList;

public class StmListList {
  public LinkedList<Tree.Stm> head;
  public StmListList tail;
  public StmListList(LinkedList<Tree.Stm> h, StmListList t) {head=h; tail=t;}
}

