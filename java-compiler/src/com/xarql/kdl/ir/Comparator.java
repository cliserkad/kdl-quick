package com.xarql.kdl.ir;

public enum Comparator {

	EQUAL("="), MORE_THAN(">"), LESS_THAN("<"), MORE_OR_EQUAL(">="), LESS_OR_EQUAL("<="), NOT_EQUAL("!="), ADDRESS_EQUAL("@"), ADDRESS_NOT_EQUAL("!@"), IS_A("#"), IS_NOT_A("!#");

	String rep;

	Comparator(String rep) {
		this.rep = rep;
	}

	public static boolean isComparator(String rep) {
		return match(rep) != null;
	}

	public static Comparator match(String rep) {
		if(rep == null)
			return null;
		else {
			for(Comparator c : values())
				if(rep.equals(c.rep))
					return c;
			return null;
		}
	}

}
