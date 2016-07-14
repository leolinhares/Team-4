package Mips;

import Frame.Access;
import Temp.Temp;
import Tree.Exp;

public class InReg extends Access {
	Temp temp;

	
	public InReg(Temp temp) {
		super();
		this.temp = temp;
	}

	@Override
	public String toString() {
		// TODO Auto-generated method stub
		return temp.toString();
	}

	@Override
	public Exp exp(Exp e) {
		return new Tree.TEMP(temp);
	}

}
