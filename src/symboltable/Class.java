package symboltable;

import java.util.HashMap;
import java.util.Set;

import syntaxtree.IdentifierType;
import syntaxtree.Type;

public class Class {

	String classID;
	HashMap<String, Object> metodos;
	HashMap<String, Object> variaveisGlobais;
	String pai;
	Type tipo;

	public Class(String id, String pai) {
		this.classID = id;
		this.pai = pai;
		this.tipo = new IdentifierType(id);
		metodos = new HashMap<>();
		variaveisGlobais = new HashMap<>();
	}

	public String getId(){
		return classID; 
	}
	
	//TODO: mudar para getType
	public Type type(){
		return tipo; 
	}
	
	//TODO: mudar para getParent
	public String parent() {
		return pai;
	}
	
	//TODO: mudar para addMetodo
	public boolean addMethod(String id, Type tipo) {
		if(metodos.containsKey(id)) 
			return false;       
		else {
			metodos.put(id, new Method(id, tipo));
			return true;
		}
	}

	public Set<String> getMethods(){
		return metodos.keySet();
	}

	public Method getMethod(String id) {
		if(metodos.containsKey(id)) 
			return (Method)metodos.get(id);
		else 
			return null;
	}

	public boolean addVar(String id, Type type) {
		if(variaveisGlobais.containsKey(id)) 
			return false;
		else{
			variaveisGlobais.put(id, new Variable(id, type));
			return true;
		}
	}

	public Variable getVar(String id) {
		if(variaveisGlobais.containsKey(id)) 
			return (Variable)variaveisGlobais.get(id);
		else 
			return null;
	}
		    
}

