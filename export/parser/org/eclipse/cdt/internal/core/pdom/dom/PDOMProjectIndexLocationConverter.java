/*******************************************************************************
 * Copyright (c) 2006, 2015 Symbian Software Ltd. and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Andrew Ferguson (Symbian) - initial API and implementation
 *     Markus Schorn (Wind River Systems)
 *     Karsten Thoms (itemis) - Bug 471103
 *******************************************************************************/
 package org.eclipse.cdt.internal.core.pdom.dom;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.HashMap;
import java.util.Map;

import org.eclipse.cdt.core.index.IIndexFileLocation;
import org.eclipse.cdt.core.index.IIndexLocationConverter;
import org.eclipse.cdt.internal.core.index.IndexFileLocation;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IWorkspaceRoot;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.Path;

/**
 * The standard location converter used by the per-project PDOM
 */
public class PDOMProjectIndexLocationConverter implements IIndexLocationConverter {
	private static final String EXTERNAL = "<EXT>"; //$NON-NLS-1$
	private static final String WS = "<WS>"; //$NON-NLS-1$

	final private IWorkspaceRoot fRoot;
	final private String fFullPathPrefix;
	final private boolean fIgnoreExternal;
	// Cache for results of method fromInternalFormat (bug 471103).
	final Map<String, IIndexFileLocation> fromInternalFormatCache = new HashMap<>();

	public PDOMProjectIndexLocationConverter() {
		this(false);
	}

	public PDOMProjectIndexLocationConverter(boolean ignoreWSExternal) {
		fRoot= null;
		fFullPathPrefix= "./"; //$NON-NLS-1$
		fIgnoreExternal= ignoreWSExternal;
	}

	@Override
	public IIndexFileLocation fromInternalFormat(String raw) {
	
		// Fast return when 'raw' was queried before (bug 471103).
		IIndexFileLocation cachedResult = fromInternalFormatCache.get(raw);
		if (cachedResult != null) {
			return cachedResult;
		}

		
		//My Shit
		IndexFileLocation location;
		try {
			location = new IndexFileLocation(new URI(raw.substring(EXTERNAL.length())), raw);
			fromInternalFormatCache.put(raw, location);
			return location;
		} catch (URISyntaxException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}

		
		
		String fullPath = null;
		URI uri= null;
		if (raw.startsWith(EXTERNAL)) {
			try {
				uri = new URI(raw.substring(EXTERNAL.length()));
			} catch (URISyntaxException e) {
			}
		} else {
			if (raw.startsWith(WS)) {
				fullPath= raw.substring(WS.length());
			} else {
				fullPath= fFullPathPrefix + raw;
			}
			final IPath path= new Path(fullPath);
			if (path.segmentCount() > 1) {
				IResource member= fRoot.getFile(path);
				uri = member.getLocationURI();
			}
		}
		if (uri == null)
			return null;

		location = new IndexFileLocation(uri, fullPath);
		fromInternalFormatCache.put(raw, location);
		return location;
	}

	@Override
	public String toInternalFormat(IIndexFileLocation location) {
		String fullPath= location.getFullPath();
		if (fullPath != null) {
			if (fullPath.startsWith(fFullPathPrefix)) {
				return fullPath.substring(fFullPathPrefix.length());
			}
			return WS + fullPath;
		}

		if (fIgnoreExternal)
			return null;

		return EXTERNAL + location.getURI().toString();
	}
}
