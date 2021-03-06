/*******************************************************************************
 * Copyright (c) 2014 Nathan Ridge.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Nathan Ridge - initial API and implementation
 *******************************************************************************/
package org.eclipse.cdt.internal.core.dom.parser.cpp.semantics;

import org.eclipse.cdt.core.CCorePlugin;
import org.eclipse.cdt.core.dom.ast.IType;
import org.eclipse.cdt.internal.core.dom.parser.ISerializableType;
import org.eclipse.cdt.internal.core.dom.parser.ITypeMarshalBuffer;
import org.eclipse.cdt.internal.core.dom.parser.cpp.CPPUnknownMember;
import org.eclipse.cdt.internal.core.dom.parser.cpp.ICPPUnknownType;
import org.eclipse.cdt.internal.core.index.IIndexFragment;
import org.eclipse.core.runtime.CoreException;

/**
 * Represents the type of an unknown member.
 */
public class TypeOfUnknownMember implements ICPPUnknownType, ISerializableType {
	private final CPPUnknownMember fMember;

	public TypeOfUnknownMember(CPPUnknownMember member) {
		fMember = member;
	}

	public CPPUnknownMember getUnknownMember() {
		return fMember;
	}

	@Override
	public boolean isSameType(IType type) {
		return type instanceof TypeOfUnknownMember
				&& fMember == ((TypeOfUnknownMember) type).fMember;
	}

	

	@Override
	public void marshal(ITypeMarshalBuffer buffer) throws CoreException {
		buffer.putShort(ITypeMarshalBuffer.UNKNOWN_MEMBER_TYPE);
		fMember.marshal(buffer);
	}

	public static IType unmarshal(IIndexFragment fragment, short firstBytes, ITypeMarshalBuffer buffer) throws CoreException {
		short firstBytesForMember = buffer.getShort();
		if ((firstBytesForMember & ITypeMarshalBuffer.KIND_MASK) != ITypeMarshalBuffer.UNKNOWN_MEMBER)
			throw new CoreException(CCorePlugin.createStatus("Expected an unknown memebr, first bytes=" + firstBytesForMember)); //$NON-NLS-1$
		return new TypeOfUnknownMember((CPPUnknownMember) CPPUnknownMember.unmarshal(fragment, firstBytesForMember, buffer));
	}

	@Override
	public Object clone() {
		try {
			return super.clone();
		} catch (CloneNotSupportedException e) {
			return null;
		}
	}
}
