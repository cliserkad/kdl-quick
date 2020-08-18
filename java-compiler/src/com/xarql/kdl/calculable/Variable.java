package com.xarql.kdl.calculable;

import com.xarql.kdl.Scope;
import com.xarql.kdl.Text;
import com.xarql.kdl.UnimplementedException;
import com.xarql.kdl.names.*;
import org.objectweb.asm.MethodVisitor;

public class Variable implements Resolvable, CommonNames {
	public final String             name;
	public final InternalObjectName type;
	public final int                localIndex;
	public final Scope owner;

	public Variable(final Scope owner, final String name, final InternalObjectName type) {
		this.name = Text.nonNull(name);
		this.type = InternalObjectName.checkNonNull(type);
		this.localIndex = owner.nextIndex();
		this.owner = owner;
		owner.addLocalVariable(this);
	}

	@Override
	public boolean equals(Object obj) {
		if(obj instanceof Variable) {
			Variable other = (Variable) obj;
			return other.name.equals(name);
		}
		else
			return false;
	}

	@Override
	public String toString() {
		return "LocalVariable: " + name + " --> " + type + " @ " + localIndex;
	}

	@Override
	public boolean isBaseType() {
		return type.isBaseType();
	}

	@Override
	public BaseType toBaseType() {
		return type.toBaseType();
	}

	@Override
	public InternalName toInternalName() {
		return type.inName;
	}

	@Override
	public InternalObjectName toInternalObjectName() {
		return type;
	}

	public boolean isArray() {
		return toInternalObjectName().isArray();
	}

	@Override
	public Resolvable push(final MethodVisitor visitor) throws UnimplementedException {
		if(type.isBaseType() && type.toBaseType() != STRING) {
			if(type.toBaseType() == INT)
				visitor.visitVarInsn(ILOAD, localIndex);
			else if(type.toBaseType() == BOOLEAN)
				visitor.visitVarInsn(ILOAD, localIndex);
			else
				throw new UnimplementedException(SWITCH_BASETYPE);
		}
		else
			visitor.visitVarInsn(ALOAD, localIndex);
		return this;
	}

	@Override
	public Resolvable calc(final MethodVisitor visitor) throws Exception {
		push(visitor);
		return this;
	}
}
