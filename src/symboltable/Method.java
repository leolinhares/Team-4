package symboltable;

import java.util.Enumeration;
import java.util.HashMap;
import java.util.Vector;

import syntaxtree.Type;

public class Method {

	Vector<Object> parametros;
	HashMap<String, Object> variaveisLocais;
	String metodoID;
	Type tipo;

	public Method(String id, Type type) {
		this.metodoID = id;
		this.tipo = type;
		variaveisLocais = new HashMap<>();
		parametros = new Vector<>();
	}

	public String getId() {
		return metodoID;
	}

	public Type type() {
		return tipo; 
	}

	public boolean addParam(String id, Type type) {
		if(containsParam(id)) 
			return false;       
		else {
			parametros.add(new Variable(id, type));
			return true;
		}
	}

	public Enumeration<Object> getParams(){
		return parametros.elements();
	}

	public Variable getParamAt(int i){
		if (i < parametros.size())
			return (Variable)parametros.get(i);
		else
			return null;
	}

	public boolean addVar(String id, Type type) {
		if(variaveisLocais.containsKey(id)) 
			return false;
		else{
			variaveisLocais.put(id, new Variable(id, type));
			return true;
		}
	}

	public boolean containsParam(String id) {
		for (int i = 0; i< parametros.size(); i++)
			if (((Variable)parametros.get(i)).id.equals(id))
				return true;
		return false;
	}

	public Variable getVar(String id) {
		if(variaveisLocais.containsKey(id)) 
			return (Variable)variaveisLocais.get(id);
		else 
			return null;
	}

	public Variable getParam(String id) {

		for (int i = 0; i< parametros.size(); i++)
			if (((Variable)parametros.get(i)).id.equals(id))
				return (Variable)(parametros.get(i));
		return null;
	}
} 