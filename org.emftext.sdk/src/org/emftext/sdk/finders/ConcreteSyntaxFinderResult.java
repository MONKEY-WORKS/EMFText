/*******************************************************************************
 * Copyright (c) 2006-2009 
 * Software Technology Group, Dresden University of Technology
 * 
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0 
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *   Software Technology Group - TU Dresden, Germany 
 *      - initial API and implementation
 ******************************************************************************/
package org.emftext.sdk.finders;

import org.emftext.sdk.concretesyntax.ConcreteSyntax;

/**
 * A basic implementation of the IConcreteSyntaxFinderResult interface.
 */
class ConcreteSyntaxFinderResult implements IConcreteSyntaxFinderResult {

	private ConcreteSyntax concreteSyntax;
	
	public ConcreteSyntaxFinderResult(ConcreteSyntax concreteSyntax) {
		super();
		this.concreteSyntax = concreteSyntax;
	}

	public ConcreteSyntax getConcreteSyntax() {
		return concreteSyntax;
	}
}
