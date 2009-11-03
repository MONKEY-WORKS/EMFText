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
package org.emftext.sdk.codegen.creators;

import java.util.Collection;

import org.emftext.sdk.codegen.GenerationContext;
import org.emftext.sdk.concretesyntax.OptionTypes;

/**
 * An experimental (not yet implemented) creator for the SGLR compiler
 * framework.
 */
//TODO sheyden: implement this creator
public class SGLRGrammarCreator extends AbstractArtifactCreator {

	public SGLRGrammarCreator() {
		super("SGLR grammer");
	}

	@Override
	public Collection<IArtifact> getArtifactsToCreate(GenerationContext context) {
		return null;
	}

	@Override
	public OptionTypes getOverrideOption() {
		return null;
	}
}
