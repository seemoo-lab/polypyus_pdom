/*******************************************************************************
 * Copyright (c) 2006, 2012 Wind River Systems, Inc. and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Markus Schorn - initial API and implementation
 *******************************************************************************/ 
package org.eclipse.cdt.internal.core.pdom.dom.cpp;

import org.eclipse.cdt.core.CCorePlugin;
import org.eclipse.cdt.core.dom.ast.DOMException;
import org.eclipse.cdt.core.dom.ast.IASTName;
import org.eclipse.cdt.core.dom.ast.IASTNode;
import org.eclipse.cdt.core.dom.ast.cpp.ICPPConstructor;
import org.eclipse.cdt.internal.core.dom.parser.cpp.ICPPExecution;
import org.eclipse.cdt.internal.core.index.IIndexCPPBindingConstants;
import org.eclipse.cdt.internal.core.pdom.db.Database;
import org.eclipse.cdt.internal.core.pdom.dom.PDOMLinkage;
import org.eclipse.cdt.internal.core.pdom.dom.PDOMNode;
import org.eclipse.core.runtime.CoreException;

public class PDOMCPPConstructor extends PDOMCPPMethod implements ICPPConstructor {

	/** Offset of the constructor chain execution for constexpr constructors. */
	private static final int CONSTRUCTOR_CHAIN = PDOMCPPMethod.RECORD_SIZE; // Database.EXECUTION_SIZE
	
	/**
	 * The size in bytes of a PDOMCPPConstructor record in the database.
	 */
	@SuppressWarnings("hiding")
	protected static final int RECORD_SIZE = CONSTRUCTOR_CHAIN + Database.EXECUTION_SIZE;
	
	public PDOMCPPConstructor(PDOMCPPLinkage linkage, PDOMNode parent, ICPPConstructor method, IASTNode point)
			throws CoreException, DOMException {
		super(linkage, parent, method, point);
		linkage.new ConfigureConstructor(method, this);
	}

	public PDOMCPPConstructor(PDOMLinkage linkage, long record) {
		super(linkage, record);
	}
	
	@Override
	protected int getRecordSize() {
		return RECORD_SIZE;
	}

	@Override
	public int getNodeType() {
		return IIndexCPPBindingConstants.CPP_CONSTRUCTOR;
	}
	
	@Override
	public int getAdditionalNameFlags(int standardFlags, IASTName name) {
		return 0;
	}
	
	public void initData(ICPPExecution constructorChain) {
		try {
			getLinkage().storeExecution(record + CONSTRUCTOR_CHAIN, constructorChain);
		} catch (CoreException e) {
			CCorePlugin.log(e);
		}
	}
	
	@Override
	public ICPPExecution getConstructorChainExecution(IASTNode point) {
		if (!isConstexpr())
			return null;

		try {
			return getLinkage().loadExecution(record + CONSTRUCTOR_CHAIN);
		} catch (CoreException e) {
			CCorePlugin.log(e);
			return null;
		}
	}
}
