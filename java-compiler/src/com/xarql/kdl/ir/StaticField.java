package com.xarql.kdl.ir;

import com.xarql.kdl.Actor;
import com.xarql.kdl.names.Details;
import com.xarql.kdl.names.TypeDescriptor;
import org.objectweb.asm.Opcodes;

import java.util.Objects;

public class StaticField extends Details implements Assignable, Member {

	public final TypeDescriptor ownerType;

	public StaticField(Details details, TypeDescriptor ownerType) {
		super(details);
		this.ownerType = ownerType;
	}

	public StaticField(String name, TypeDescriptor ownerType) {
		this(new Details(name), ownerType);
	}

	@Override
	public Pushable push(Actor actor) throws Exception {
		if(descriptor == null) {
			StaticField proper = actor.unit.type.getFields().equivalentKey(this);
			return proper.push(actor);
		} else {
			actor.visitFieldInsn(Opcodes.GETSTATIC, ownerType.qualifiedName(), name.text, descriptor.arrayName());
			return this;
		}
	}

	@Override
	public StaticField assign(TypeDescriptor incomingType, Actor actor) throws Exception {
		actor.visitFieldInsn(Opcodes.PUTSTATIC, ownerType.qualifiedName(), name.text, descriptor.arrayName());
		return this;
	}

	@Override
	public StaticField assignDefault(Actor actor) throws Exception {
		if(isBaseType())
			toBaseType().getDefaultValue().push(actor);
		else
			actor.visitInsn(Opcodes.ACONST_NULL);
		return assign(descriptor, actor);
	}

	@Override
	public boolean equals(Object object) {
		if(this == object)
			return true;
		else if(object != null && object instanceof StaticField) {
			final StaticField other = (StaticField) object;
			return other.ownerType.equals(ownerType) && other.name.equals(name);
		} else
			return false;
	}

	@Override
	public int hashCode() {
		return Objects.hash(ownerType, name);
	}

	public String toString() {
		return ownerType.qualifiedName() + " " + super.toString();
	}

	@Override
	public Details details() {
		return this;
	}

}
