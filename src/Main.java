import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;

public class Main {
   
   public static void main(String [] args) throws FileNotFoundException {
      try {
  		 File file = new File("src/file.txt");
         new MiniJavaParser(new FileInputStream(file)).Program();
         System.out.println("Lexical analysis successfull");
      }
      catch (ParseException e) {
         System.out.println("Lexer Error : \n"+ e.toString());
      }
   }
}


