import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;

import syntaxtree.*;
import visitor.*;
import symboltable.*;
import symboltable.BuildSymbolTableVisitor;

public class Main {
    public static void main(String [] args) {
	try {
		
		File file = new File("src/file2.txt");
		Program root = new MiniJavaParser(new FileInputStream(file)).Goal();
		
	    BuildSymbolTableVisitor bstv = new BuildSymbolTableVisitor();
	    root.accept(bstv);
	    System.out.println(bstv.symbolTable);
	}
	catch (ParseException e) {
	    System.out.println(e.toString());
	} catch (FileNotFoundException e) {
		e.printStackTrace();
	}
    }
}