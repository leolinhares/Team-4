import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;

import syntaxtree.Program;
import visitor.BuildSymbolTableVisitor;
import visitor.PrettyPrintVisitor;
import visitor.TypeCheckVisitor;

public class Main {

	public static void main(String [] args) throws FileNotFoundException {
		try {
			
			File file = new File("src/file3.txt");

			Program root = new MiniJavaParser(new FileInputStream(file)).Goal();
			System.out.println("Lexical analysis successfull");

			// Print the original source code from the abstact syntax tree:
			// root.accept(new PrettyPrintVisitor());
			// Print the abstract syntax tree:
			// root.accept(new ASTPrintVisitor());
			// Should this have been called "UglyPrintVisitor"? :)
			
			
			BuildSymbolTableVisitor bstv = new BuildSymbolTableVisitor();
		    root.accept(bstv);
		    System.out.println(bstv.getSymTab());
		    root.accept(new TypeCheckVisitor(bstv.getSymTab()));
		    System.out.println("Type-checking successful.");
		}
		catch (ParseException e) {
			System.out.println(e.toString());
		}
	}
}


