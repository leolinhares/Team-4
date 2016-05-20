package visitor;

public class CompilerExceptions{

	public static void printError(String errorMessage){
		System.out.println(errorMessage);
		System.exit(-1);
	}

}
