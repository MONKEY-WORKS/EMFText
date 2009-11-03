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
package org.emftext.sdk.codegen.generators.interfaces;

import java.io.PrintWriter;

import org.emftext.sdk.codegen.EArtifact;
import org.emftext.sdk.codegen.GenerationContext;
import org.emftext.sdk.codegen.IGenerator;
import org.emftext.sdk.codegen.generators.BaseGenerator;

public class ITokenResolverFactoryGenerator extends BaseGenerator {

	private String iTokenResolverClassName;

	public ITokenResolverFactoryGenerator() {
		super();
	}

	private ITokenResolverFactoryGenerator(GenerationContext context) {
		super(context, EArtifact.I_TOKEN_RESOLVER_FACTORY);
		iTokenResolverClassName = getContext().getQualifiedClassName(EArtifact.I_TOKEN_RESOLVER);
	}

	public IGenerator newInstance(GenerationContext context) {
		return new ITokenResolverFactoryGenerator(context);
	}

	public boolean generate(PrintWriter out) {
		org.emftext.sdk.codegen.composites.StringComposite sc = new org.emftext.sdk.codegen.composites.JavaComposite();
		sc.add("package " + getResourcePackageName() + ";");
		sc.addLineBreak();
		
		sc.add("// A TokenResolverFactory creates TokenResolvers for a given token name.");
		sc.add("// They may be implemented like a registry.");
		sc.add("public interface " + getResourceClassName() + " {");
		sc.addLineBreak();
		
		sc.add("// Creates a token resolver for normal tokens of type 'tokenName'.");
		sc.add("public " + iTokenResolverClassName + " createTokenResolver(String tokenName);");
		sc.addLineBreak();
		
		sc.add("// Creates a token resolver for COLLECT-IN tokens that are stores in");
		sc.add("// feature 'featureName'.");
		sc.add("public " + iTokenResolverClassName + " createCollectInTokenResolver(String featureName);");
		sc.add("}");
		out.print(sc.toString());
		return true;
	}
}
