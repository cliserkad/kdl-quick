package com.xarql.kdl.ir;

import com.xarql.kdl.Actor;
import com.xarql.kdl.names.Details;
import com.xarql.kdl.names.InternalName;
import org.objectweb.asm.Opcodes;

import java.util.Objects;

public class StaticField extends Details implements Assignable, Member {

	public final InternalName ownerType;

	public StaticField(Details details, InternalName ownerType) {
		super(details);
		this.ownerType = ownerType;
	}

	public StaticField(String name, InternalName ownerType) {
		this(new Details(name), ownerType);
	}

	@Override
	public Pushable push(Actor actor) throws Exception {
		if(type == null) {
			StaticField proper = actor.unit.type.fields.equivalentKey(this);
			return proper.push(actor);
		} else {
			actor.visitFieldInsn(Opcodes.GETSTATIC, ownerType.nameString(), name.text, type.objectString());
			return this;
		}
	}

	@Override
	public StaticField assign(InternalName incomingType, Actor actor) throws Exception {
		actor.visitFieldInsn(Opcodes.PUTSTATIC, ownerType.nameString(), name.text, type.objectString());
		return this;
	}

	@Override
	public StaticField assignDefault(Actor actor) throws Exception {
		if(isBaseType())
			toBaseType().defaultValue.push(actor);
		else
			actor.visitInsn(Opcodes.ACONST_NULL);
		return assign(type, actor);
	}

	@Override
	public boolean equals(Object object) {
		if(object != null && object instanceof StaticField) {
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
		return ownerType.nameString() + " " + super.toString();
	}

	@Override
	public Details details() {
		return this;
	}
}
