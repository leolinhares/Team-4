package IR_visitor;

import IR_visitor.StringVisitor;
import Translate.ProcFrag;
import Tree.*;

public class GenCVisitor implements StringVisitor
{

  private String  fn_decls         = "";
  private String  fn_defs          = "";
  private int     temp_count       = 32;
  private int     fixed_heap_size  = 5000;
  private int     fixed_stack_size = 5000;
  private int     fixed_frame_size = 400;
  private int     word_size;
  private boolean on_stack         = false;
  private boolean main_fn          = true;

  public void codegen(ProcFrag proc, StmList list)
  {
    if (!main_fn)
      {
        String fn_name = proc.frame.name.toString().replace('.', '_');
        fn_name = fn_name.replace('-', '_');
        fn_name = fn_name.replace('$', '_');
        fn_decls += "void " + fn_name + "();\n";
        fn_defs += "void " + fn_name + "() {\n%  fp = sp;\n";
        fn_defs += "  sp -= " + fixed_frame_size + ";\n";
      }
    else
      {
        word_size = proc.frame.wordSize();
        fn_defs += "int main() {\n%  fp = " + (fixed_stack_size - 1) + ";\n";
        fn_defs += "  sp = fp-" + fixed_frame_size + ";\n";
      }

    while (list != null)
      {
        list.head.accept(this);
        list = list.tail;
      }

    String temps_str = "int t32";
    for (int i = 33; i <= temp_count; i++)
      temps_str += ", t" + i;
    int temps_pos = fn_defs.indexOf('%');
    String old_fn_defs = fn_defs;
    fn_defs = old_fn_defs.substring(0, temps_pos - 1);
    fn_defs += "\n  " + temps_str + ";\n";
    fn_defs += old_fn_defs.substring(temps_pos + 1, old_fn_defs.length());
    temp_count = 32;

    fn_defs += "  sp = fp;\n  fp += " + fixed_frame_size + ";\n";

    if (main_fn)
      {
        fn_defs += "  return 0;\n";
        main_fn = false;
      }

    fn_defs += "}\n\n";
  }

  public void print()
  {
    System.out.println("#include <stdio.h>\n");
    System.out.println("void _alloc();");
    System.out.println("void _printInt();");
    System.out.println(fn_decls);
    System.out.println("int stack[" + fixed_stack_size + "];");
    System.out.println("int heap[" + fixed_heap_size + "];");
    System.out.println("int rv, a0, a1, a2, a3, sp, fp, ra, zero=0;\n");
    System.out.print(fn_defs);
    System.out.println("void _alloc() {\n  static int heap_size = 0;\n  int i;\n");
    System.out.println("  rv = heap_size;\n  heap_size += a0;\n");
    System.out.println("  // put \"garbage\" in heap to start");
    System.out.println("  for(i = rv; i < heap_size; i++)");
    System.out.println("    heap[i] =-1;\n  }\n");
    System.out.println("void _printInt() {\n  printf(\"%d\\n\", a0);\n}");
  }

  public String visit(LABEL n)
  {
    String label_str = n.label.toString().replace('.', '_');
    label_str = label_str.replace('-', '_');
    label_str = label_str.replace('$', '_');
    fn_defs += "  " + label_str + ":\n";
    return null;
  }

  public String visit(JUMP n)
  {
    String s = "  goto " + n.exp.accept(this) + ";\n";
    fn_defs += s;
    return null;
  }

  public String visit(CJUMP n)
  {
    String relop;
    switch (n.relop)
      {
        case CJUMP.EQ:
          relop = "==";
          break;
        case CJUMP.NE:
          relop = "!=";
          break;
        case CJUMP.LT:
          relop = "<";
          break;
        case CJUMP.GE:
          relop = ">=";
          break;
        case CJUMP.GT:
          relop = ">";
          break;
        case CJUMP.LE:
          relop = "<=";
          break;
        default:
          throw new Error("Whoa, why are you using that relop?");
      }
    String s = "  if(" + n.left.accept(this) + " " + relop + " " + n.right.accept(this) + ") goto "
        + n.iftrue.toString() + ";\n";
    fn_defs += s;
    return null;
  }

  public String visit(MOVE n)
  {
    String s = "  " + n.dst.accept(this) + " = " + n.src.accept(this) + ";\n";
    fn_defs += s;
    return null;
  }

  public String visit(EXP1 n)
  {
    String s = "  " + n.exp.accept(this) + ";\n";
    fn_defs += s;
    return null;
  }

  public String visit(BINOP n)
  {
    String binop;
    switch (n.binop)
      {
        case BINOP.PLUS:
          binop = "+";
          break;
        case BINOP.MINUS:
          binop = "-";
          break;
        case BINOP.MUL:
          binop = "*";
          break;
        case BINOP.DIV:
          binop = "/";
          break;
        case BINOP.AND:
          binop = "&";
          break;
        case BINOP.OR:
          binop = "|";
          break;
        case BINOP.LSHIFT:
          binop = "<<";
          break;
        case BINOP.RSHIFT:
          binop = ">>";
          break;
        default:
          throw new Error("Whoa, why are you using that binop?");
      }
    return "(" + n.left.accept(this) + " " + binop + " " + n.right.accept(this) + ")";
  }

  public String visit(MEM n)
  {
    String exp_str = n.exp.accept(this);
    if (on_stack)
      {
        on_stack = false;
        return "stack[(" + exp_str + ")/" + word_size + "]";
      }
    return "heap[(" + exp_str + ")/" + word_size + "]";
  }

  public String visit(TEMP n)
  {
    int temp_num = n.temp.getNum();
    if (temp_num > temp_count)
      temp_count = temp_num;
    switch (temp_num)
      {
        case (0):
          return "zero";
        case (2):
          return "rv";
        case (4):
          return "a0";
        case (5):
          return "a1";
        case (6):
          return "a2";
        case (7):
          return "a3";
        case (29):
          on_stack = true;
          return "sp";
        case (30):
          on_stack = true;
          return "fp";
        case (31):
          return "ra";
      }
    return n.temp.toString();
  }

  public String visit(NAME n)
  {
    String label_str = n.label.toString().replace('.', '_');
    label_str = label_str.replace('-', '_');
    label_str = label_str.replace('$', '_');
    return label_str;
  }

  public String visit(CONST n)
  {
    return Integer.toString(n.value, 10);
  }

  public String visit(CALL n)
  {
    ExpList list = n.args;
    if (list != null)
      {
        fn_defs += "  a0 = " + list.head.accept(this) + ";\n";
        list = list.tail;
        if (list != null)
          {
            fn_defs += "  a1 = " + list.head.accept(this) + ";\n";
            list = list.tail;
            if (list != null)
              {
                fn_defs += "  a2 = " + list.head.accept(this) + ";\n";
                list = list.tail;
                if (list != null)
                  {
                    fn_defs += "  a3 = " + list.head.accept(this) + ";\n";
                    list = list.tail;
                  }
              }
          }
        int offset = word_size;
        while (list != null)
          {
            fn_defs += "  stack[(sp + " + offset + ")/" + word_size + "] = " + list.head.accept(this) + ";\n";
            list = list.tail;
            offset += word_size;
          }
      }

    String fn_name = n.func.accept(this);
    if (fn_name.compareTo("_printInt") == 0)
      return fn_name + "()";
    fn_defs += "  " + fn_name + "();\n";
    return "rv";
  }

  public String visit(SEQ n)
  {
    throw new Error("Whoa, there should be no SEQ in a canonical IR tree");
  }

  public String visit(ESEQ n)
  {
    throw new Error("Whoa, there should be no ESEQ in a canonical IR tree");
  }
}
