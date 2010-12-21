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
package org.emftext.sdk.codegen.resource;

import static org.emftext.sdk.codegen.antlr.Constants.DEFAULT_ANTLR_PLUGIN_NAME;
import static org.emftext.sdk.codegen.composites.IClassNameConstants.LIST;
import static org.emftext.sdk.codegen.resource.generators.IClassNameConstants.ADAPTER;
import static org.emftext.sdk.codegen.resource.generators.IClassNameConstants.COLLECTION;
import static org.emftext.sdk.codegen.resource.generators.IClassNameConstants.E_MAP;
import static org.emftext.sdk.codegen.resource.generators.IClassNameConstants.E_OBJECT;
import static org.emftext.sdk.codegen.resource.generators.IClassNameConstants.E_REFERENCE;
import static org.emftext.sdk.codegen.resource.generators.IClassNameConstants.E_STRUCTURAL_FEATURE;
import static org.emftext.sdk.codegen.resource.generators.IClassNameConstants.INTERNAL_E_OBJECT;
import static org.emftext.sdk.codegen.resource.generators.IClassNameConstants.MAP;
import static org.emftext.sdk.codegen.resource.generators.IClassNameConstants.TOKEN;

import java.io.File;
import java.util.Collection;
import java.util.Map;

import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.Path;
import org.eclipse.emf.codegen.ecore.genmodel.GenClass;
import org.eclipse.emf.codegen.ecore.genmodel.GenClassifier;
import org.eclipse.emf.codegen.ecore.genmodel.GenEnum;
import org.eclipse.emf.codegen.ecore.genmodel.GenEnumLiteral;
import org.eclipse.emf.codegen.ecore.genmodel.GenFeature;
import org.eclipse.emf.codegen.ecore.genmodel.GenModel;
import org.eclipse.emf.codegen.ecore.genmodel.GenPackage;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.emftext.sdk.Constants;
import org.emftext.sdk.IPluginDescriptor;
import org.emftext.sdk.OptionManager;
import org.emftext.sdk.codegen.composites.JavaComposite;
import org.emftext.sdk.codegen.composites.StringComposite;
import org.emftext.sdk.codegen.util.NameUtil;
import org.emftext.sdk.concretesyntax.ConcreteSyntax;
import org.emftext.sdk.concretesyntax.Import;
import org.emftext.sdk.concretesyntax.OptionTypes;
import org.emftext.sdk.util.ConcreteSyntaxUtil;

/**
 * A utility class used by all generators to reuse code fragments that are
 * generated by multiple generators.
 */
public class GeneratorUtil {
	
	private ConcreteSyntaxUtil csUtil = new ConcreteSyntaxUtil();
	private NameUtil nameUtil = new NameUtil();

	public String createGetFeatureCall(GenClass genClass, GenFeature genFeature) {
		return "getEStructuralFeature(" + getFeatureConstant(genClass, genFeature) + ")";
	}

	public String getFeatureConstant(GenClass genClass, GenFeature genFeature) {
		GenPackage genPackage = genClass.getGenPackage();
		return genPackage.getQualifiedPackageInterfaceName() + "." + genClass.getFeatureID(genFeature);
	}

	public String getFeatureAccessor(GenClass genClass, GenFeature genFeature) {
		return getClassifierAccessor(genClass) + "." + createGetFeatureCall(genClass, genFeature);
	}

	public String getEnumLiteralInstanceAccessor(GenEnum genEnum, GenEnumLiteral genLiteral) {
		return getClassifierAccessor(genEnum) + ".getEEnumLiteral(" + genEnum.getQualifiedName() + "." + genLiteral.getEnumLiteralValueConstantName() + ").getInstance()";
	}

	public String getClassifierAccessor(GenClassifier classifier) {
		GenPackage genPackage = classifier.getGenPackage();
		return genPackage.getReflectionPackageName() + "."
								+ genPackage.getPackageInterfaceName()
								+ ".eINSTANCE.get" + classifier.getClassifierAccessorName()
								+ "()";
	}

	public GenFeature findGenFeature(GenClass genClass, String name) {
		for (GenFeature genFeature : genClass.getAllGenFeatures()) {
			if (genFeature.getName().equals(name)) {
				return genFeature;
			}
		}
		return null;
	}

	public void addCodeToSetFeature(
			StringComposite sc,
			GenClass genClass, 
			String featureConstant,
			EStructuralFeature eFeature, 
			String expressionToBeSet, 
			boolean isContainment,
			boolean addCodeToCompleteElement) {
		
		sc.add("Object value = " + expressionToBeSet + ";");
		if (eFeature.getUpperBound() == 1) {
			if (Map.Entry.class.getName().equals(eFeature.getEType().getInstanceClassName())) {
				sc.add("addMapEntry(element, element.eClass().getEStructuralFeature("
								+ featureConstant
								+ "), value);");
			} else {
				sc.add("element.eSet(element.eClass().getEStructuralFeature("
						+ featureConstant
						+ "), value);");
			}
		} else {
			if (Map.Entry.class.getName().equals(eFeature.getEType().getInstanceClassName())) {
				sc.add("addMapEntry(element, element.eClass().getEStructuralFeature("
								+ featureConstant
								+ "), " + expressionToBeSet + ");");
			} else {
				sc.add("addObjectToList(element, "
						+ featureConstant
						+ ", value);");
			}
		}
		if (addCodeToCompleteElement) {
			sc.add("completedElement(value, " + isContainment + ");");
		}
	}

	public void addAddMapEntryMethod(StringComposite sc, GenerationContext context) {
		String dummyEObjectClassName = context.getQualifiedClassName(TextResourceArtifacts.DUMMY_E_OBJECT);
		String mapUtilClassName = context.getQualifiedClassName(TextResourceArtifacts.MAP_UTIL);

		sc.add("protected void addMapEntry(" + E_OBJECT + " element, " + E_STRUCTURAL_FEATURE + " structuralFeature, " + dummyEObjectClassName + " dummy) {");
		sc.add("Object value = element.eGet(structuralFeature);");
		sc.add("Object mapKey = dummy.getValueByName(\"key\");");
		sc.add("Object mapValue = dummy.getValueByName(\"value\");");
		sc.add("if (value instanceof " + E_MAP + "<?, ?>) {");
		sc.add(E_MAP + "<Object, Object> valueMap = " + mapUtilClassName  + ".castToEMap(value);");
		sc.add("if (mapKey != null && mapValue != null) {");
		sc.add("valueMap.put(mapKey, mapValue);");
		sc.add("}");
		sc.add("}");
		sc.add("}");
		sc.addLineBreak();
	}

	public void addAddObjectToListMethod1(StringComposite sc) {
		sc.add("@SuppressWarnings(\"unchecked\")");
		sc.addLineBreak();
        sc.add("public boolean addObjectToList(" + E_OBJECT + " container, int featureID, Object object) {");
        sc.add("return ((" + LIST + "<Object>) container.eGet(container.eClass().getEStructuralFeature(featureID))).add(object);");
        sc.add("}");
        sc.addLineBreak();
	}

	public void addAddObjectToListMethod2(StringComposite sc) {
		sc.add("@SuppressWarnings(\"unchecked\")");
		sc.addLineBreak();
        sc.add("public boolean addObjectToList(" + E_OBJECT + " container, " + E_STRUCTURAL_FEATURE + " feature, Object object) {");
        sc.add("return ((" + LIST + "<Object>) container.eGet(feature)).add(object);");
        sc.add("}");
        sc.addLineBreak();
	}

	public void addGetFreshTokenResolveResultMethod(StringComposite sc, String qualifiedTokenResolveResultClassName) {
		sc.add("private " + qualifiedTokenResolveResultClassName + " getFreshTokenResolveResult() {");
        sc.add("tokenResolveResult.clear();");
        sc.add("return tokenResolveResult;");
        sc.add("}");
        sc.addLineBreak();
	}

	public void addRegisterContextDependentProxyMethod(JavaComposite sc, boolean addTypeParameters, GenerationContext context) {
		String iCommandClassName = context.getQualifiedClassName(TextResourceArtifacts.I_COMMAND);
		String iTextResourceClassName = context.getQualifiedClassName(TextResourceArtifacts.I_TEXT_RESOURCE);
		String contextDependentURIFragmentFactoryClassName = context.getQualifiedClassName(TextResourceArtifacts.CONTEXT_DEPENDENT_URI_FRAGMENT_FACTORY);

		String typeParameters = "";
		if (addTypeParameters) {
			typeParameters = "<ContainerType extends " + E_OBJECT + ", ReferenceType extends " + E_OBJECT + "> ";
		}
		sc.add("protected " + typeParameters + "void registerContextDependentProxy(final " + contextDependentURIFragmentFactoryClassName + "<ContainerType, ReferenceType> factory, final ContainerType container, final " + E_REFERENCE + " reference, final String id, final " + E_OBJECT + " proxy) {");

    	sc.add("final int position;");
    	sc.add("if (reference.isMany()) {");
    	sc.add("position = ((" + LIST + "<?>) container.eGet(reference)).size();");
    	sc.add("} else {");
    	sc.add("position = -1;");
    	sc.add("}");
    	sc.addLineBreak();
		sc.add("postParseCommands.add(new " + iCommandClassName + "<" + iTextResourceClassName + ">() {");
		sc.add("public boolean execute(" + iTextResourceClassName + " resource) {");
		sc.add("if (resource == null) {");
		sc.addComment("the resource can be null if the parser is used for code completion");
		sc.add("return true;");
		sc.add("}");
		sc.add("resource.registerContextDependentProxy(factory, container, reference, id, proxy, position);");
		sc.add("return true;");
		sc.add("}");
		sc.add("});");
		sc.add("}");
		sc.addLineBreak();
	}

	public void addGetReferenceResolverSwitchMethod(GenerationContext context, StringComposite sc) {
		final String qualifiedReferenceResolverSwitchClassName = context.getQualifiedClassName(TextResourceArtifacts.REFERENCE_RESOLVER_SWITCH);
		sc.add("protected " + qualifiedReferenceResolverSwitchClassName + " getReferenceResolverSwitch() {");
        sc.add("return (" + qualifiedReferenceResolverSwitchClassName + ") getMetaInformation().getReferenceResolverSwitch();");
        sc.add("}");
        sc.addLineBreak();
	}

	public void addAddErrorToResourceMethod(JavaComposite sc, GenerationContext context) {
		String iCommand = context.getQualifiedClassName(TextResourceArtifacts.I_COMMAND);
		String iTextResource = context.getQualifiedClassName(TextResourceArtifacts.I_TEXT_RESOURCE);
		String iProblem = context.getQualifiedClassName(TextResourceArtifacts.I_PROBLEM);
		String iQuickFix = context.getQualifiedClassName(TextResourceArtifacts.I_QUICK_FIX);
		String eProblemSeverity = context.getQualifiedClassName(TextResourceArtifacts.E_PROBLEM_SEVERITY);
		String eProblemType = context.getQualifiedClassName(TextResourceArtifacts.E_PROBLEM_TYPE);

		sc.add("protected void addErrorToResource(final String errorMessage, final int column, final int line, final int startIndex, final int stopIndex) {");

		sc.add("postParseCommands.add(new " + iCommand + "<" + iTextResource + ">() {");
		sc.add("public boolean execute(" + iTextResource + " resource) {");
		sc.add("if (resource == null) {");
		sc.addComment("the resource can be null if the parser is used for code completion");
		sc.add("return true;");
		sc.add("}");
		sc.add("resource.addProblem(new " + iProblem + "() {");
		sc.add("public " + eProblemSeverity + " getSeverity() {");
		sc.add("return " + eProblemSeverity + ".ERROR;");
		sc.add("}");
		sc.add("public " + eProblemType + " getType() {");
		sc.add("return " + eProblemType + ".SYNTAX_ERROR;");
		sc.add("}");
		sc.add("public String getMessage() {");
		sc.add("return errorMessage;");
		sc.add("}");
		sc.add("public " + COLLECTION + "<" + iQuickFix + "> getQuickFixes() {");
		sc.add("return null;");
		sc.add("}");
		sc.add("}, column, line, startIndex, stopIndex);");
		sc.add("return true;");
		sc.add("}");
		sc.add("});");
		sc.add("}");
		sc.addLineBreak();
	}

	public void addSetOptionsMethod(JavaComposite sc, String body, String comment) {
		sc.add("public void setOptions(" + MAP + "<?,?> options) {");
		if (comment != null) {
			sc.addComment(comment);
		}
		if (body != null) {
			sc.add(body);
		}
		sc.add("}");
	    sc.addLineBreak();
	}

	public void addGetLayoutInformationAdapterMethod(StringComposite sc, String layoutInformationAdapterClassName) {
		sc.add("protected " + layoutInformationAdapterClassName + " getLayoutInformationAdapter(" + E_OBJECT + " element) {");
		sc.add("for (" + ADAPTER + " adapter : element.eAdapters()) {");
		sc.add("if (adapter instanceof " + layoutInformationAdapterClassName + ") {");
		sc.add("return (" + layoutInformationAdapterClassName + ") adapter;");
		sc.add("}");
		sc.add("}");
		sc.add(layoutInformationAdapterClassName + " newAdapter = new " + layoutInformationAdapterClassName + "();");
		sc.add("element.eAdapters().add(newAdapter);");
		sc.add("return newAdapter;");
		sc.add("}");
		sc.addLineBreak();
	}
	
	public void addCodeToDeresolveProxyObject(StringComposite sc, String iContextDependentUriFragmentClassName, String proxyVariable) {
		sc.add("String deresolvedReference = null;");
		sc.add("if (" + proxyVariable + " instanceof " + E_OBJECT + ") {");
		sc.add(E_OBJECT + " eObjectToDeResolve = (" + E_OBJECT + ") " + proxyVariable + ";");
		sc.add("if (eObjectToDeResolve.eIsProxy()) {");
		sc.add("deresolvedReference = ((" + INTERNAL_E_OBJECT + ") eObjectToDeResolve).eProxyURI().fragment();");
		sc.add("if (deresolvedReference != null && deresolvedReference.startsWith(" + iContextDependentUriFragmentClassName + ".INTERNAL_URI_FRAGMENT_PREFIX)) {");
		sc.add("deresolvedReference = deresolvedReference.substring(" + iContextDependentUriFragmentClassName + ".INTERNAL_URI_FRAGMENT_PREFIX.length());");
		sc.add("deresolvedReference = deresolvedReference.substring(deresolvedReference.indexOf(\"_\") + 1);");
		sc.add("}");
		sc.add("}");
		sc.add("}");
	}

	public File getResolverFile(ConcreteSyntax syntax, GenFeature proxyReference, String projectFolder) {
		OptionTypes overrideOption = OptionTypes.OVERRIDE_REFERENCE_RESOLVERS;
		boolean doOverride = overrideOption == null || OptionManager.INSTANCE.getBooleanOptionValue(syntax, overrideOption);
		File resolverFile = new File(csUtil.getSourceFolder(syntax, doOverride, false, projectFolder) + File.separator + getResolverPackagePath(syntax) + File.separator + nameUtil.getReferenceResolverClassName(proxyReference) + Constants.JAVA_FILE_EXTENSION);
		return resolverFile;
	}

	public File getResolverPackageFile(ConcreteSyntax syntax, boolean doOverride, String pluginProjectFolder) {
		return new File(csUtil.getSourceFolder(syntax, doOverride, false, pluginProjectFolder).getAbsolutePath() + File.separator + getResolverPackagePath(syntax));
	}

	public IPath getResolverPackagePath(ConcreteSyntax syntax) {
		return new Path(nameUtil.getResolverPackageName(syntax).replaceAll("\\.","/"));
	}

	public IPluginDescriptor getAntlrPluginDescriptor(ConcreteSyntax syntax) {
		final String pluginName = OptionManager.INSTANCE.getStringOptionValue(syntax, OptionTypes.ANTLR_PLUGIN_ID, DEFAULT_ANTLR_PLUGIN_NAME);
		IPluginDescriptor antlrPlugin = new IPluginDescriptor() {

			public String getName() {
				return pluginName;
			}
		};
		return antlrPlugin;
	}
	
	public void addImports(GenerationContext context, Collection<String> requiredBundles, ConcreteSyntax syntax) {
		// first add the syntax itself
		String syntaxPluginID = nameUtil.getResourcePluginDescriptor(syntax).getName();
		requiredBundles.add(syntaxPluginID);
		String antlrPluginID = context.getAntlrPlugin().getName();
		requiredBundles.add(antlrPluginID);
		
		// second add the main generator package
		GenPackage mainPackage = syntax.getPackage();
		addImports(requiredBundles, mainPackage);
		
		// third add imported generator packages and syntaxes recursively
		for (Import nextImport : syntax.getImports()) {
			GenPackage importedPackage = nextImport.getPackage();
			addImports(requiredBundles, importedPackage);

			ConcreteSyntax importedSyntax = nextImport.getConcreteSyntax();
			if (importedSyntax != null) {
				addImports(context, requiredBundles, importedSyntax);
			}
		}
	}

	/**
	 * Adds imports for the given generator package and all used
	 * generator packages.
	 * 
	 * @param requiredBundles
	 * @param genPackage
	 * @return
	 */
	private GenModel addImports(Collection<String> requiredBundles, GenPackage genPackage) {
		// add the package itself
		addImport(requiredBundles, genPackage);
		
		// add used generator packages
		GenModel genModel = genPackage.getGenModel();
		for (GenPackage usedGenPackage : genModel.getUsedGenPackages()) {
			addImport(requiredBundles, usedGenPackage);
		}
		return genModel;
	}

	/**
	 * Adds one import for the given generator package.
	 * 
	 * @param requiredBundles
	 * @param genPackage
	 * @return
	 */
	private void addImport(Collection<String> requiredBundles, GenPackage genPackage) {
		GenModel genModel = genPackage.getGenModel();
		String modelPluginID = genModel.getModelPluginID();
		requiredBundles.add(modelPluginID);
	}

	public void addCanBeUsedForSyntaxHighlightingMethod(JavaComposite sc) {
		sc.add("public boolean canBeUsedForSyntaxHighlighting(int tokenType) {");
		sc.add("if (tokenType < 0 || tokenType == " + TOKEN + ".EOF) {");
		sc.add("return false;");
		sc.add("}");
		sc.add("if (tokenType == " + TOKEN + ".UP) {");
		sc.add("return false;");
		sc.add("}");
		sc.add("if (tokenType == " + TOKEN + ".DOWN) {");
		sc.add("return false;");
		sc.add("}");
		sc.add("if (tokenType == " + TOKEN + ".EOR_TOKEN_TYPE) {");
		sc.add("return false;");
		sc.add("}");
		sc.add("if (tokenType == " + TOKEN + ".INVALID_TOKEN_TYPE) {");
		sc.add("return false;");
		sc.add("}");
		sc.add("return true;");
		sc.add("}");
		sc.addLineBreak();
	}
}
