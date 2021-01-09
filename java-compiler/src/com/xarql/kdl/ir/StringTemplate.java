package com.xarql.kdl.ir;

import com.xarql.kdl.Actor;
import com.xarql.kdl.CompilationUnit;
import com.xarql.kdl.Type;
import com.xarql.kdl.names.BaseType;
import com.xarql.kdl.names.TypeDescriptor;

import java.util.ArrayList;
import java.util.List;

public class StringTemplate implements Pushable {

	private final List<Pushable> elements;

	public StringTemplate() {
		elements = new ArrayList<>();
	}

	public void add(String s) {
		elements.add(new Literal<>(s));
	}

	public void add(Pushable pushable) {
		elements.add(pushable);
	}

	public boolean isTextOnly() {
		for(Pushable p : elements)
			if(!(p instanceof Literal<?>))
				return false;
		return true;
	}

	@Override
	public Type toType() {
		return TypeDescriptor.STRING;
	}

	@Override
	public boolean isBaseType() {
		return true;
	}

	@Override
	public BaseType toBaseType() {
		return BaseType.STRING;
	}

	@Override
	public Pushable push(final Actor actor) throws Exception {
		Expression.createStringBuilder(actor);
		for(Pushable p : elements) {
			if(p instanceof Constant)
				p = actor.unit.getConstant(((Constant) p).name.text);
			TypeDescriptor type = p.push(actor).toType();
			if(!type.equals(TypeDescriptor.STRING))
				CompilationUnit.convertToString(type, actor);
			Expression.SB_APPEND.push(actor);
		}
		Expression.SB_TO_STRING.push(actor);
		return this;
	}

	@Override
	public String toString() {
		String out = "";
		for(Pushable p : elements) {
			if(p instanceof Literal<?>) {
				Literal<?> lit = (Literal<?>) p;
				out += lit.value;
			} else
				out += p;
		}
		return out;
	}

}
