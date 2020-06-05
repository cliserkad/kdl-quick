package com.xarql.kdl;

public class Constant {
	public final String name;
	public       Value  value;

	public Constant(final String name, final Value value) {
		if(name == null || name.isEmpty())
			throw new IllegalArgumentException("Constant name may not be empty");
		this.name = name;
	}

	public Constant(final String name) {
		this(name, null);
	}

	@Override
	public String toString() {
		return name + " : " + value;
	}

	public boolean isEmpty() {
		return value == null;
	}

	@Override
	public boolean equals(Object o) {
		if(o instanceof Constant) {
			Constant other = (Constant) o;
			return other.name.equals(name);
		}
		return false;
	}

	public static abstract class Value {
		public abstract Class<?> valueType();

		public abstract Object value();

		@Override
		public String toString() {
			return value().toString();
		}
	}
}