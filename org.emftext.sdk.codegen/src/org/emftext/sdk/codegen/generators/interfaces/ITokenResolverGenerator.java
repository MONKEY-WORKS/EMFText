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
package org.emftext.sdk.codegen.generators.interfaces;

import static org.emftext.sdk.codegen.generators.IClassNameConstants.E_OBJECT;
import static org.emftext.sdk.codegen.generators.IClassNameConstants.E_STRUCTURAL_FEATURE;
import static org.emftext.sdk.codegen.generators.IClassNameConstants.OBJECT;

import org.emftext.sdk.codegen.EArtifact;
import org.emftext.sdk.codegen.GenerationContext;
import org.emftext.sdk.codegen.IGenerator;
import org.emftext.sdk.codegen.composites.JavaComposite;
import org.emftext.sdk.codegen.generators.JavaBaseGenerator;

public class ITokenResolverGenerator extends JavaBaseGenerator {

	private String iConfigurableClassName;
	private String iTokenResolveResultClassName;

	public ITokenResolverGenerator() {
		super();
	}

	private ITokenResolverGenerator(GenerationContext context) {
		super(context, EArtifact.I_TOKEN_RESOLVER);
		iConfigurableClassName = getContext().getQualifiedClassName(EArtifact.I_CONFIGURABLE);
		iTokenResolveResultClassName = getContext().getQualifiedClassName(EArtifact.I_TOKEN_RESOLVE_RESULT);
	}

	public IGenerator newInstance(GenerationContext context) {
		return new ITokenResolverGenerator(context);
	}

	public boolean generateJavaContents(JavaComposite sc) {
		
		sc.add("package " + getResourcePackageName() + ";");
		sc.addLineBreak();
		
		sc.addJavadoc(
			"A basic interface to convert parsed tokens to the attribute type in the meta model. " +
			"All generated TokenResolvers per default delegate requests to an instance of " + getContext().getClassName(EArtifact.DEFAULT_TOKEN_RESOLVER) + " which performs " +
			"a standard conversion based on the EMF type conversion. This includes conversion of registered EDataTypes.\n\n" +
			"@see " + getClassNameHelper().getDEFAULT_TOKEN_RESOLVER()
		);
		sc.add("public interface " + getResourceClassName() + " extends " + iConfigurableClassName + " {");
		sc.addLineBreak();
		
		sc.addJavadoc(
			"Converts a token into an " + OBJECT + " (the value of the attribute).\n\n" +
			"@param lexem the text of the parsed token\n" +
			"@param feature the corresponding feature in the meta model\n" +
			"@param result the result of resolving the lexem, can be used to add processing errors"
		);
		sc.add("public void resolve(String lexem, " + E_STRUCTURAL_FEATURE + " feature, " + iTokenResolveResultClassName + " result);");
		sc.addLineBreak();
		
		sc.addJavadoc(
			"Converts an " + OBJECT + " (the value of an attribute) to a string which can be printed. " +
			"This is the inverse of resolving a token with a call to resolve().\n\n" +
			"@param value the " + OBJECT + " to be printed as String\n" +
			"@param feature the corresponding feature (EAttribute)\n" +
			"@param container the container of the object\n" +
			"@return the String representation or null if a problem occurred");
		sc.add("public String deResolve(" + OBJECT + " value, " + E_STRUCTURAL_FEATURE + " feature, " + E_OBJECT + " container);");
		sc.addLineBreak();
		
		sc.add("}");
		return true;
	}
}
