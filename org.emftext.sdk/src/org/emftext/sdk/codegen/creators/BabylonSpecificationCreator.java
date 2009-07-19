/*******************************************************************************
 * Copyright (c) 2006-2009 
 * Software Technology Group, Dresden University of Technology
 * 
 * This program is free software; you can redistribute it and/or modify it under
 * the terms of the GNU Lesser General Public License as published by the Free
 * Software Foundation; either version 2 of the License, or (at your option) any
 * later version. This program is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.
 * 
 * See the GNU Lesser General Public License for more details. You should have
 * received a copy of the GNU Lesser General Public License along with this
 * program; if not, write to the Free Software Foundation, Inc., 59 Temple Place,
 * Suite 330, Boston, MA  02111-1307 USA
 * 
 * Contributors:
 *   Software Technology Group - TU Dresden, Germany 
 *   - initial API and implementation
 ******************************************************************************/
package org.emftext.sdk.codegen.creators;

import java.io.File;
import java.util.Collection;

import org.emftext.sdk.codegen.GenerationContext;
import org.emftext.sdk.codegen.generators.BabylonSpecificationGenerator;
import org.emftext.sdk.concretesyntax.OptionTypes;

/**
 * An experimental (not yet implemented) creator for the Babylon compiler
 * framework.
 */
public class BabylonSpecificationCreator extends AbstractArtifactCreator {

	public BabylonSpecificationCreator() {
		super("Babylon specification");
	}

	public Collection<IArtifact> getArtifactsToCreate(GenerationContext context) {
		
		String specificationName = context.getCapitalizedConcreteSyntaxName();
		String packagePath = context.getPackagePath();
  		File specificationFile = new File(packagePath + specificationName + ".babylon");
		
	    return createArtifact(
	    		context,
	    		new BabylonSpecificationGenerator(context),
	    		specificationFile,
	    		"Exception while generating Babylon specification."
	    );
	}

	public OptionTypes getOverrideOption() {
		return OptionTypes.OVERRIDE_PARSER;
	}
}
