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
package org.emftext.sdk.regex;

import java.util.List;

import org.emftext.sdk.concretesyntax.TokenDefinition;

public class SorterException extends Exception {

	private static final long serialVersionUID = 7500645730266413189L;
	private List<TokenDefinition> errorTokens;

	public SorterException(String message, List<TokenDefinition> errorTokens) {
		super(message);
		this.errorTokens = errorTokens;
	}

	public SorterException(String message) {
		super(message);
	}

	public SorterException(String message, Throwable throwable) {
		super(message, throwable);
	}

	public List<TokenDefinition> getErrorTokens() {
		return errorTokens;
	}
}
