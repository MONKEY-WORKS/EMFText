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
package org.emftext.sdk.syntax_analysis;

import java.util.ArrayList;
import java.util.List;

import org.emftext.sdk.AbstractPostProcessor;
import org.emftext.sdk.concretesyntax.ConcreteSyntax;
import org.emftext.sdk.concretesyntax.TokenDefinition;
import org.emftext.sdk.concretesyntax.resource.cs.mopp.CsResource;
import org.emftext.sdk.concretesyntax.resource.cs.mopp.ECsProblemType;

/**
 * A analyser that checks that all token names start with a capital letter.
 */
public class TokenNameAnalyser extends AbstractPostProcessor {

	@Override
	public void analyse(CsResource resource, ConcreteSyntax syntax) {
		List<TokenDefinition> wrongDefinitions = getTokenDefinitionsWithInvalidNames(syntax);
		for (TokenDefinition next : wrongDefinitions) {
			addProblem(resource, ECsProblemType.INVALID_TOKEN_NAME, "Token names must start with a capital letter.", next);
		}
	}

	public List<TokenDefinition> getTokenDefinitionsWithInvalidNames(ConcreteSyntax syntax) {
		List<TokenDefinition> result = new ArrayList<TokenDefinition>();
		for (TokenDefinition definition : syntax.getActiveTokens()) {
			char firstLetter = definition.getName().charAt(0);
			if (!(firstLetter >= 'A' && firstLetter <= 'Z')) {
				result.add(definition);
			}
		}
		return result;
	}
}
