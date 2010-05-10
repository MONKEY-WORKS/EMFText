/*******************************************************************************
 * Copyright (c) 2006-2010 
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

import java.io.File;
import java.util.Collection;

import org.emftext.sdk.IPluginDescriptor;
import org.emftext.sdk.codegen.ArtifactDescriptor;
import org.emftext.sdk.codegen.IContext;
import org.emftext.sdk.codegen.IGenerator;

public class GenericArtifactCreator<ContextType extends IContext, ParameterType> extends AbstractArtifactCreator<ContextType, ParameterType> {

	private ArtifactDescriptor<ContextType, ParameterType> artifact;

	public GenericArtifactCreator(ArtifactDescriptor<ContextType, ParameterType> artifact) {
		this(artifact, null);
	}

	public GenericArtifactCreator(ArtifactDescriptor<ContextType, ParameterType> artifact, ParameterType parameters) {
		super(artifact.getClassNamePrefix() + artifact.getClassNameSuffix(), parameters);
		this.artifact = artifact;
	}

	@Override
	public Collection<IArtifact> getArtifactsToCreate(IPluginDescriptor plugin, ContextType context, ParameterType parameters) {
	    File file = context.getFile(plugin, artifact);
		IGenerator<ContextType, ParameterType> generator = artifact.createGenerator(context, parameters);
		
	    return createArtifact(
	    		context,
	    		generator,
	    		file,
	    		"Exception while generating " + getArtifactDescription() + "."
	    );
	}
	
	public ArtifactDescriptor<ContextType, ParameterType> getArtifact() {
		return artifact;
	}

	@Override
	protected boolean doOverride(ContextType context) {
		return true;
	}
}
