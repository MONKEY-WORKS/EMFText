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
package org.emftext.sdk.codegen.resource.generators.interfaces;

import static org.emftext.sdk.codegen.resource.generators.IClassNameConstants.E_OBJECT;
import static org.emftext.sdk.codegen.resource.generators.IClassNameConstants.E_REFERENCE;
import static org.emftext.sdk.codegen.resource.generators.IClassNameConstants.INPUT_STREAM;
import static org.emftext.sdk.codegen.resource.generators.IClassNameConstants.IO_EXCEPTION;
import static org.emftext.sdk.codegen.resource.generators.IClassNameConstants.MAP;
import static org.emftext.sdk.codegen.resource.generators.IClassNameConstants.RESOURCE;
import static org.emftext.sdk.codegen.resource.generators.IClassNameConstants.STRING;

import org.emftext.sdk.codegen.IGenerator;
import org.emftext.sdk.codegen.composites.JavaComposite;
import org.emftext.sdk.codegen.resource.GenerationContext;
import org.emftext.sdk.codegen.resource.TextResourceArtifacts;
import org.emftext.sdk.codegen.resource.generators.JavaBaseGenerator;

public class ITextResourceGenerator extends JavaBaseGenerator<Object> {

	public ITextResourceGenerator() {
		super();
	}

	private ITextResourceGenerator(GenerationContext context) {
		super(context, TextResourceArtifacts.I_TEXT_RESOURCE);
	}

	public IGenerator<GenerationContext, Object> newInstance(GenerationContext context, Object parameters) {
		return new ITextResourceGenerator(context);
	}

	public boolean generateJavaContents(JavaComposite sc) {
		
		sc.add("package " + getResourcePackageName() + ";");
		sc.addLineBreak();
		
		sc.addJavadoc(
			"An extended resource that can hold information about the exact positions " +
			"of each element of its content in a text file. This can be used to give " +
			"more detailed error feedback."
		);
		sc.add("public interface " + getResourceClassName() + " extends " + RESOURCE + ", " + iTextResourcePluginPartClassName + " {");
		sc.addLineBreak();
		
		sc.addJavadoc(
			"Try to load the content of this resource from the given stream. If " +
			"loading fails, the state of this resource is kept. If loading is " + 
			"successful, the content of this resource is replaced with the new " +
			"content.",
			"This method can be used to try loading erroneous files, as e.g., " +
			"needed during background parsing in the editor.",
			"@param stream the stream to read from",
			"@param options the load options to use",
			"@throws " + IO_EXCEPTION + " thrown if the stream can not be read"
		);
		sc.add("public void reload(" + INPUT_STREAM + " stream, " + MAP + "<?,?> options) throws " + IO_EXCEPTION + ";");
		sc.addLineBreak();
		
		sc.addJavadoc(
			"Try to cancel a current reload of this resource. It is not guaranteed " +
			"that canceling is successful. If this resource has already finished " +
			"parsing the new content, it will replace its content unconditionally."
		);
		sc.add("public void cancelReload();");
		sc.addLineBreak();
		
		sc.addJavadoc(
			"Returns a map containing information about the location of model elements in the text.",
			"@return the model element to text location mapping"
		);
		sc.add("public " + iLocationMapClassName + " getLocationMap();");
		sc.addLineBreak();
		
		sc.addJavadoc("Adds an error that should be displayed at the position of the given element.");
		sc.add("public void addProblem(" + iProblemClassName + " problem, " + E_OBJECT + " element);");
		sc.addLineBreak();
		
		sc.addJavadoc("Adds an error to be displayed at the indicated position.");
		sc.add("public void addProblem(" + iProblemClassName + " problem, int column, int line, int charStart, int charEnd);");
		sc.addLineBreak();
		
		sc.addJavadoc(
			"Internal method used by the parser to register a context dependent proxy object for later resolution.",
			"@param container",
			"@param reference",
			"@param pos",
			"@param id",
			"@param proxyElement"
		);
		sc.add("public <ContainerType extends " + E_OBJECT + ", ReferenceType extends " + E_OBJECT + "> void registerContextDependentProxy(" + iContextDependentUriFragmentFactoryClassName + "<ContainerType, ReferenceType> factory, ContainerType container, " + E_REFERENCE +" reference, " + STRING + " id, " + E_OBJECT + " proxyElement);");
		sc.addLineBreak();
		
		sc.addJavadoc("Attaches a warning with the given message to object 'cause'.");
		sc.add("public void addWarning(" + STRING + " message, " + E_OBJECT + " cause);");
		sc.addLineBreak();
		
		sc.addJavadoc("Attaches an error with the given message to object 'cause'.");
		sc.add("public void addError(" + STRING + " message, " + E_OBJECT + " cause);");
		sc.addLineBreak();

		sc.add("}");
		return true;
	}
}