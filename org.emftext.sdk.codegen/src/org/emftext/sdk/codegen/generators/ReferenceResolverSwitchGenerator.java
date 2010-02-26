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
package org.emftext.sdk.codegen.generators;

import static org.emftext.sdk.codegen.generators.IClassNameConstants.E_OBJECT;
import static org.emftext.sdk.codegen.generators.IClassNameConstants.*;
import static org.emftext.sdk.codegen.generators.IClassNameConstants.E_STRUCTURAL_FEATURE;
import static org.emftext.sdk.codegen.generators.IClassNameConstants.MAP;
import static org.emftext.sdk.codegen.generators.IClassNameConstants.STRING;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.emf.codegen.ecore.genmodel.GenClass;
import org.eclipse.emf.codegen.ecore.genmodel.GenFeature;
import org.emftext.sdk.codegen.EArtifact;
import org.emftext.sdk.codegen.GenerationContext;
import org.emftext.sdk.codegen.GeneratorUtil;
import org.emftext.sdk.codegen.IGenerator;
import org.emftext.sdk.codegen.composites.StringComposite;
import org.emftext.sdk.codegen.util.ConcreteSyntaxUtil;
import org.emftext.sdk.finders.GenClassFinder;
import org.emftext.sdk.util.StringUtil;

/**
 * A generator that creates a multiplexing reference resolver.
 * Depending on the type of the reference that must be resolved,
 * the generated class delegates the resolve call to the appropriate
 * reference resolver.
 */
public class ReferenceResolverSwitchGenerator extends JavaBaseGenerator {
	
	private final GeneratorUtil generatorUtil = new GeneratorUtil();
	private final GenClassFinder genClassFinder = new GenClassFinder();
	private final ConcreteSyntaxUtil csUtil = new ConcreteSyntaxUtil();

	public ReferenceResolverSwitchGenerator() {
		super();
	}

	private ReferenceResolverSwitchGenerator(GenerationContext context) {
		super(context, EArtifact.REFERENCE_RESOLVER_SWITCH);
	}
	
	@Override
	public boolean generateJavaContents(StringComposite sc) {
		generateReferenceResolverSwitch(sc);
		return true;
	}
	
	 /**
     * Generates the reference resolver switch that calls the correct
     * reference resolvers generated by <code>generateReferenceResolver()</code>.
	 * @param sc 
     */
    private void generateReferenceResolverSwitch(StringComposite sc)  {  
    	
        sc.add("package " + getResourcePackageName() + ";");
        sc.addLineBreak();
		sc.add("public class " + getResourceClassName() + " implements " + getClassNameHelper().getI_REFERENCE_RESOLVER_SWITCH() + " {");
        sc.addLineBreak();
		
		generateFields(sc);
		generateGetMethods(sc);
        generateSetOptionsMethod(sc);
		generateResolveFuzzyMethod(sc);
		
		sc.add("}");
    }

	private void generateResolveFuzzyMethod(StringComposite sc) {
		String qualifiedFuzzyResolveResultClassName = getContext().getClassName(EArtifact.FUZZY_RESOLVE_RESULT);
		
		sc.add("public void resolveFuzzy(" + STRING + " identifier, " + E_OBJECT + " container, " + E_REFERENCE + " reference, int position, " + getClassNameHelper().getI_REFERENCE_RESOLVE_RESULT() + "<" + E_OBJECT + "> result) {");
		// TODO this is a temporary workaround to avoid NPEs when this switch is called
		// and not container was available. a better solution would be to pass the resource
		// instead of the container, but that implies a change to the reference resolver
		// interface
		sc.add("if (container == null) {");
		sc.add("return;");
		sc.add("}");
		for (GenFeature proxyReference : getContext().getNonContainmentReferences()) {
			GenClass genClass = proxyReference.getGenClass();
			String accessorName = genClass.getGenPackage().getQualifiedPackageInterfaceName() + ".eINSTANCE.get"  + genClass.getName() + "()";
			String generatedClassName = csUtil.getReferenceResolverClassName(proxyReference);
			GenFeature genFeature = generatorUtil.findGenFeature(genClass, proxyReference.getName());
			String referenceTypeName = genClassFinder.getQualifiedInterfaceName(proxyReference.getTypeGenClass());
			sc.add("if (" + accessorName + ".isInstance(container)) {");
			sc.add(qualifiedFuzzyResolveResultClassName + "<" + genClassFinder.getQualifiedInterfaceName(genFeature.getTypeGenClass()) + "> frr = new " + qualifiedFuzzyResolveResultClassName + "<" + genClassFinder.getQualifiedInterfaceName(genFeature.getTypeGenClass()) + ">(result);");
			sc.add(E_STRUCTURAL_FEATURE + " feature = container.eClass().getEStructuralFeature(reference.getName());");
			sc.add("if (feature != null && feature instanceof " + E_REFERENCE + " && feature.getEType().getInstanceClass().isAssignableFrom(" + referenceTypeName + ".class)) {");
			sc.add(StringUtil.low(generatedClassName) + ".resolve(identifier, (" + genClassFinder.getQualifiedInterfaceName(genClass) + ") container, (" + E_REFERENCE + ") feature, position, true, frr);");
			sc.add("}");
			sc.add("}");
		}
		sc.add("}");
	}

	private void generateSetOptionsMethod(StringComposite sc) {
		sc.add("public void setOptions(" + MAP + "<?, ?> options) {");
		for (GenFeature proxyReference : getContext().getNonContainmentReferences()) {
			String generatedClassName = csUtil.getReferenceResolverClassName(proxyReference);
			sc.add(StringUtil.low(generatedClassName) + ".setOptions(options);");			
		}
		sc.add("}");
		sc.addLineBreak();
	}

	private void generateFields(StringComposite sc) {
    	List<String> generatedResolvers = new ArrayList<String>();

		for (GenFeature proxyReference : getContext().getNonContainmentReferences()) {
			String generatedClassName = csUtil.getReferenceResolverClassName(proxyReference);
			if (!generatedResolvers.contains(generatedClassName)) {
				generatedResolvers.add(generatedClassName);
				String fullClassName = getContext().getQualifiedReferenceResolverClassName(proxyReference, false);
				sc.add("protected " + fullClassName + " " + StringUtil.low(generatedClassName) + " = new " + fullClassName + "();");			
			}
		}
	    sc.addLineBreak();
	}

	private void generateGetMethods(StringComposite sc) {
    	List<String> generatedResolvers = new ArrayList<String>();

		for (GenFeature proxyReference : getContext().getNonContainmentReferences()) {
			String generatedClassName = csUtil.getReferenceResolverClassName(proxyReference);
			if (!generatedResolvers.contains(generatedClassName)) {
				generatedResolvers.add(generatedClassName);
				String fullClassName = getContext().getQualifiedReferenceResolverClassName(proxyReference, false);
				sc.add("public " + fullClassName + " get" + generatedClassName + "() {");
				sc.add("return " + StringUtil.low(generatedClassName) + ";");			
				sc.add("}");
			    sc.addLineBreak();
			}
		}
	}

	public IGenerator newInstance(GenerationContext context) {
		return new ReferenceResolverSwitchGenerator(context);
	}
}
