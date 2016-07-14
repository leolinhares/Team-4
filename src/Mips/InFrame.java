package Mips;

import Frame.Access;
import Tree.Exp;

public class InFrame extends Access {
	int offset;
	
	
	public InFrame(int offset) {
		super();
		this.offset = offset;
	}

	@Override
	public String toString() {
		// TODO Auto-generated method stub
		return Integer.toString(offset);
	}

	@Override
	public Exp exp(Exp e) {
		// TODO Auto-generated method stub
		return null;
	}

}
