package symboltable;

import syntaxtree.Type;

public class Variable {

	String id;
	Type tipo;

	public Variable(String id, Type tipo) {
		this.id = id;
		this.tipo = tipo;
	}

	public String id() { return id; }

	public Type type() { return tipo; }

}
