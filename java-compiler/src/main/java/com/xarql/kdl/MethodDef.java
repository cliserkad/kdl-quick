package com.xarql.kdl;

import com.xarql.kdl.names.CommonNames;
import com.xarql.kdl.names.InternalName;
import com.xarql.kdl.names.InternalObjectName;
import com.xarql.kdl.names.ReturnValue;

import java.util.List;

public class MethodDef extends JavaMethodDef implements CommonNames {
	public static final Type DEFAULT_TYPE   = Type.MTD;
	public static final int  DEFAULT_ACCESS = ACC_PUBLIC;

	public final Type type;

	/**
	 * Creates a .kdl MethodDef
	 * @param type        compiletime restrictions
	 * @param methodName  name of method
	 * @param paramTypes  the type of every parameter
	 * @param returnValue the type that is returned
	 * @param access      access modifiers
	 */
	public MethodDef(InternalName owner, Type type, String methodName, List<InternalObjectName> paramTypes, ReturnValue returnValue, int access) {
		super(owner, methodName, paramTypes, returnValue, access);
		this.type = type;
	}

	/**
	 * Creates a method with methodName that is public, void, and has no parameters
	 * @param methodName
	 */
	public MethodDef(InternalName owner, String methodName) {
		this(owner, DEFAULT_TYPE, methodName, null, VOID, DEFAULT_ACCESS);
	}

	/**
	 * Used to track compiletime restrictions of .kdl methods
	 */
	public enum Type {
		FNC("function"), MTD("method");

		String fullName;

		Type(String fullName) {
			this.fullName = fullName;
		}

		public static Type resolve(String type) {
			for(Type t : values())
				if(t.name().equals(type) || t.fullName.equals(type))
					return t;
			throw new IllegalArgumentException("The method type " + type + " is invalid");
		}
	}
}