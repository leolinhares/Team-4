import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.LinkedList;
import java.util.List;

import Assem.InstrList;
import Translate.ProcFrag;
import Translate.Translate;
import Tree.StmList;
import syntaxtree.Program;
import visitor.ASTPrintVisitor;
import symboltable.BuildSymbolTableVisitor;
import visitor.PrettyPrintVisitor;
import visitor.TypeCheckVisitor;

public class Main {

	public static void main(String [] args) throws FileNotFoundException {
		try {
			
			File file = new File("src/file3.txt");

			Program root = new MiniJavaParser(new FileInputStream(file)).Goal();
			System.out.println("Lexical analysis successfull");

			// root.accept(new PrettyPrintVisitor());
			// Print the abstract syntax tree:
			// root.accept(new ASTPrintVisitor());
			
			
			BuildSymbolTableVisitor bstv = new symboltable.BuildSymbolTableVisitor();
		    root.accept(bstv);
		    System.out.println(bstv.getSymTab());
		    root.accept(new TypeCheckVisitor(bstv.getSymTab()));
		    System.out.println("Type-checking successful.");

		    /*
		    // tradução da AST para IR Tree
		    Translate translate = new Translate(root, new Mips.MipsFrame());
		    
		    // linearizacao da IR tree (arvore canonica)
		    StmList stms = Canon.Canon.linearize(((ProcFrag)translate.getResults()).body);
		    
		    // agrupamento em blocos basicos
		    Canon.BasicBlocks b = new Canon.BasicBlocks(stms);
		    
		    // ordenacao de blocos basicos em tracos
		    Canon.TraceSchedule trace = new Canon.TraceSchedule(b);
		    
		    // lista de instrucoes (selecao de instrucoes feita pelo codegen)
		    InstrList instrs = ((ProcFrag)translate.getResults()).frame.codegen(trace.stms.head);
		    */    
		}
		catch (ParseException e) {
			System.out.println(e.toString());
		}

	}
}