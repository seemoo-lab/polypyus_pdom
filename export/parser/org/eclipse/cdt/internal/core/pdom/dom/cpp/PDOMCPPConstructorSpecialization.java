/*******************************************************************************
 * Copyright (c) 2007, 2013 QNX Software Systems and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     QNX - Initial API and implementation
 *******************************************************************************/
package org.eclipse.cdt.internal.core.pdom.dom.cpp;

import org.eclipse.cdt.core.CCorePlugin;
import org.eclipse.cdt.core.dom.ast.IASTNode;
import org.eclipse.cdt.core.dom.ast.cpp.ICPPConstructor;
import org.eclipse.cdt.core.dom.ast.cpp.ICPPConstructorSpecialization;
import org.eclipse.cdt.internal.core.dom.parser.cpp.ICPPExecution;
import org.eclipse.cdt.internal.core.dom.parser.cpp.semantics.CPPTemplates;
import org.eclipse.cdt.internal.core.index.IIndexCPPBindingConstants;
import org.eclipse.cdt.internal.core.pdom.db.Database;
import org.eclipse.cdt.internal.core.pdom.dom.PDOMBinding;
import org.eclipse.cdt.internal.core.pdom.dom.PDOMLinkage;
import org.eclipse.cdt.internal.core.pdom.dom.PDOMNode;
import org.eclipse.core.runtime.CoreException;

/**
 * @author Bryan Wilkinson
 */
public class PDOMCPPConstructorSpecialization extends PDOMCPPMethodSpecialization 
		implements ICPPConstructorSpecialization {
	/** Offset of the constructor chain execution for constexpr constructors. */
	private static final int CONSTRUCTOR_CHAIN = PDOMCPPMethodSpecialization.RECORD_SIZE + 0; // Database.EXECUTION_SIZE
	
	/**
	 * The size in bytes of a PDOMCPPConstructorSpecialization record in the database.
	 */
	@SuppressWarnings("hiding")
	protected static final int RECORD_SIZE = CONSTRUCTOR_CHAIN + Database.EXECUTION_SIZE;

	public PDOMCPPConstructorSpecialization(PDOMCPPLinkage linkage, PDOMNode parent, 
			ICPPConstructor constructor, PDOMBinding specialized, IASTNode point) throws CoreException {
		super(linkage, parent, constructor, specialized, point);
		linkage.new ConfigureConstructorSpecialization(constructor, this, point);
	}

	public PDOMCPPConstructorSpecialization(PDOMLinkage linkage, long bindingRecord) {
		super(linkage, bindingRecord);
	}
	
	public void initConstructorData(ICPPExecution constructorChain) {
		if (constructorChain == null)
			return;
		try {
			getLinkage().storeExecution(record + CONSTRUCTOR_CHAIN, constructorChain);
		} catch (CoreException e) {
			CCorePlugin.log(e);
		}
	}
	
	@Override
	protected int getRecordSize() {
		return RECORD_SIZE;
	}

	@Override
	public int getNodeType() {
		return IIndexCPPBindingConstants.CPP_CONSTRUCTOR_SPECIALIZATION;
	}
	
	@Override
	public ICPPExecution getConstructorChainExecution(IASTNode point) {
		if (!isConstexpr())
			return null;

		try {
			ICPPExecution exec = getLinkage().loadExecution(record + CONSTRUCTOR_CHAIN);
			if (exec == null) {
				exec = CPPTemplates.instantiateConstructorChain(this, point);
			}
			return exec;
		} catch (CoreException e) {
			CCorePlugin.log(e);
			return null;
		}
	}
}
