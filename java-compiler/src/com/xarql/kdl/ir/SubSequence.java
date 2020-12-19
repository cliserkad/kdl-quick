package com.xarql.kdl.ir;

import com.xarql.kdl.Actor;
import com.xarql.kdl.MethodHeader;
import com.xarql.kdl.UnimplementedException;
import com.xarql.kdl.antlr.kdl;
import com.xarql.kdl.names.BaseType;
import com.xarql.kdl.names.InternalName;
import com.xarql.kdl.names.ReturnValue;
import org.objectweb.asm.Opcodes;

import static com.xarql.kdl.BestList.list;

public class SubSequence implements Pushable {
	public static final MethodHeader SUB_STRING = new MethodHeader(InternalName.STRING, "substring", MethodHeader.toParamList(InternalName.INT, InternalName.INT), ReturnValue.STRING,
			Opcodes.ACC_PUBLIC);

	public final Pushable operand;
	public final Range range;

	public SubSequence(final kdl.SubSequenceContext ctx, final Actor actor) throws Exception {
		this(new Range(ctx.range(), actor), actor);
	}

	public SubSequence(final Range range, final Actor actor) {
		operand = actor.unit.operandStack.pop();
		this.range = range;
	}

	@Override
	public SubSequence push(Actor actor) throws Exception {
		if(!operand.toInternalName().isArray() && operand.toBaseType() == BaseType.STRING) {
			operand.push(actor);
			range.min.push(actor);
			range.max.push(actor);
			SUB_STRING.push(actor);
		} else {
			throw new UnimplementedException("Subsequence only implemented for strings");
		}
		return this;
	}

	@Override
	public InternalName toInternalName() {
		return operand.toInternalName();
	}

	@Override
	public boolean isBaseType() {
		return operand.isBaseType();
	}

	@Override
	public BaseType toBaseType() {
		return operand.toBaseType();
	}

}
