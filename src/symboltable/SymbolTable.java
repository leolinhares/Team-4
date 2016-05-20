package symboltable;

import java.util.Hashtable;

import syntaxtree.BooleanType;
import syntaxtree.IdentifierType;
import syntaxtree.IntArrayType;
import syntaxtree.IntegerType;
import syntaxtree.Type;

class SymbolTable {
	   public Hashtable hashtable;
	   
	   public SymbolTable() {
		    hashtable = new Hashtable();
	    }
		
	    public boolean addClass(String id, String parent) {
		    if(containsClass(id)) 
	                    return false;	    
		    else 
			hashtable.put(id, new Class(id, parent));
		    return true;	    
	    }
		
	    public Class getClass(String id) {
		    if(containsClass(id)) 
			return (Class)hashtable.get(id);	    
		    else 
			return null;
	    }

	    public boolean containsClass(String id) {
		    return hashtable.containsKey(id);
	    }

	    
		
	    public Type getVarType(Method method, Class clss, String id) {
	      if(method != null) {
		  if(method.getVar(id) != null) {
		      return method.getVar(id).type();
		  }
		  if(method.getParam(id) != null){
		     return method.getParam(id).type();
		  }
	      }
	      
	      while(clss != null) {
		  if(clss.getVar(id) != null) {
		      return clss.getVar(id).type();
		  }
		  else {
		      if(clss.parent() == null) {
			  clss = null;
		      }
		      else {
			  clss = getClass(clss.parent());
		      }
		  }
	      }
	      
	      
	      System.out.println("Variable " + id 
				 + " not defined in current scope");
	      System.exit(0);
	      return null;
	  }

	  public Method getMethod(String id, String classScope) {
		if(getClass(classScope)==null) {
		    System.out.println("Class " + classScope 
				       + " not defined");  
		    System.exit(0);
		}

		Class clss = getClass(classScope);
		while(clss != null) {
		    if(clss.getMethod(id) != null) {
			return clss.getMethod(id);
		    }
		    else {
			if(clss.parent() == null) {
			    clss = null;
			}
			else {
			    clss = getClass(clss.parent());
			}
		    }
		}

		
		System.out.println("Method " + id + " not defined in class " + classScope);
		
		System.exit(0);
		return null;
	    }

	    public Type getMethodType(String id, String classScope) {
		if(getClass(classScope)==null) {
		    System.out.println("Class " + classScope 
				       + " not defined");  
		    System.exit(0);
		}

		Class clss = getClass(classScope);
		while(clss != null) {
		    if(clss.getMethod(id) != null) {
			return clss.getMethod(id).type();
		    }
		    else {
			if(clss.parent() == null) {
			    clss = null;
			}
			else {
			    clss = getClass(clss.parent());
			}
		    }
		}
		
		System.out.println("Method " + id + " not defined in class " + classScope);	
		System.exit(0);
		return null;
	    }

	    public boolean compareTypes(Type t1, Type t2){
		
		if (t1 == null || t2 == null) return false;
		
		if (t1 instanceof IntegerType && t2 instanceof  IntegerType)
		    return true;
		if (t1 instanceof BooleanType && t2 instanceof  BooleanType)
		    return true;
		if (t1 instanceof IntArrayType && t2 instanceof IntArrayType)
		    return true;
		if (t1 instanceof IdentifierType && t2 instanceof IdentifierType){
		    IdentifierType i1 = (IdentifierType)t1;
		    IdentifierType i2 = (IdentifierType)t2;
		    
		    Class clss = getClass(i2.s);
		    while(clss != null) {
			if (i1.s.equals(clss.getId())) return true;
			else {
			    if(clss.parent() == null) return false;
			    clss = getClass(clss.parent());
			}
		    }
		}
		return false;	
	    }

	}
