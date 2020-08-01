package com.xarql.kdl;

import com.xarql.kdl.names.*;
import  com.xarql.kdl.antlr4.kdlParser;

public class Literal<Type> implements ToName, StringOutput, Resolvable, CommonNames {
	Type value;

	public Literal(Type value) {
		if(!BaseType.isBaseType(value))
			throw new IllegalArgumentException("Literal may only have Types defined in the BaseType enum, but the type was " + value.getClass());
		else
			this.value = value;
	}

	@Override
	public boolean isBaseType() {
		return true;
	}

	@Override
	public BaseType toBaseType() {
		return BaseType.matchValue(value);
	}

	@Override
	public String stringOutput() {
		switch(toBaseType()) {
			case INT:
			case BOOLEAN:
				return value + "";
			case STRING:
				return (String) value;
			default:
				SourceListener.standardHandle(new UnimplementedException(SWITCH_BASETYPE));
				return null;
		}
	}

	@Override
	public String toString() {
		return "Literal: " + toBaseType().name() + " --> " + value;
	}

	@Override
	public InternalName toInternalName() {
		return toBaseType().toInternalName();
	}

	@Override
	public InternalObjectName toInternalObjectName() {
		return toInternalName().toInternalObjectName();
	}

	@Override
	public void push(LinedMethodVisitor lmv) {
		lmv.visitLdcInsn(value);
	}

	public static Literal<?> parseLiteral(final kdlParser.LiteralContext ctx) {
		if(ctx.bool() != null)
			return new Literal<Boolean>(ctx.bool().TRUE() != null);
		else if(ctx.number() != null)
			return new Literal<Integer>(Integer.parseInt(ctx.number().toString()));
		else if(ctx.STRING_LIT() != null)
			return new Literal<String>(crush(ctx.STRING_LIT().getText()));
		else {
			System.err.println(new UnimplementedException(SWITCH_BASETYPE));
			return null;
		}
	}

	public static String crush(final String s) {
		return s.substring(1, s.length() - 1);
	}
}
