package org.emftext.sdk.codegen.generators;

import java.io.InputStream;
import java.io.PrintWriter;
import java.util.Collection;

import org.eclipse.emf.codegen.ecore.genmodel.GenClass;
import org.eclipse.emf.ecore.EClass;
import org.emftext.runtime.resource.IReferenceResolverSwitch;
import org.emftext.runtime.resource.ITextParser;
import org.emftext.runtime.resource.ITokenResolverFactory;
import org.emftext.runtime.resource.impl.AbstractTextResourcePluginMetaInformation;
import org.emftext.sdk.codegen.GenerationContext;
import org.emftext.sdk.codegen.composites.JavaComposite;
import org.emftext.sdk.codegen.composites.StringComposite;
import org.emftext.sdk.codegen.util.GenClassUtil;
import org.emftext.sdk.codegen.util.GeneratorUtil;
import org.emftext.sdk.concretesyntax.ConcreteSyntax;

public class PluginMetaInformationGenerator extends BaseGenerator {

	private final static GeneratorUtil generatorUtil = new GeneratorUtil();
	private final static GenClassUtil genClassUtil = new GenClassUtil();
	
	private String parserClassName;
	private String resolverSwitchClassName;
	private ConcreteSyntax syntax;
	private String tokenResolverFactoryClassName;

	public PluginMetaInformationGenerator(GenerationContext context) {
		super(context.getPackageName(), context.getMetaInformationClassName());
		this.parserClassName = context.getQualifiedParserClassName();
		this.resolverSwitchClassName = context.getQualifiedReferenceResolverSwitchClassName();
		this.tokenResolverFactoryClassName = context.getQualifiedTokenResolverFactoryClassName();
		this.syntax = context.getConcreteSyntax();
	}
	
	@Override
	public boolean generate(PrintWriter out) {
		StringComposite sc = new JavaComposite();
		
        sc.add("package " + getResourcePackageName() + ";");
		sc.addLineBreak();
        
        sc.add("public class " + getResourceClassName()+ " extends " + AbstractTextResourcePluginMetaInformation.class.getName() + " {");
        sc.addLineBreak();
		addCreateParserMethod(sc);
		addGetClassesWithSyntaxMethod(sc);
        addGetReferenceResolverSwitchMethod(sc);
        addGetTokenResolverFactoryMethod(sc);
		sc.add("}");
    	
		out.print(sc.toString());
    	return true;	
	}

	private void addCreateParserMethod(StringComposite sc) {
		sc.add("public " + ITextParser.class.getName() + " createParser(" + InputStream.class.getName() + " inputStream, " + String.class.getName() + " encoding) {");
		sc.add("return new " + parserClassName + "().createInstance(inputStream, encoding);");
		sc.add("}");
        sc.addLineBreak();
	}

	private void addGetClassesWithSyntaxMethod(StringComposite sc) {
		Collection<GenClass> classesWithSyntax = generatorUtil.getClassesWithSyntax(syntax);
		sc.add("public " + EClass.class.getName() + "[] getClassesWithSyntax() {");
		sc.add("return new " + EClass.class.getName() + "[] {");
		for (GenClass classWithSyntax : classesWithSyntax) {
			sc.add(genClassUtil.getAccessor(classWithSyntax) + ",");
		}
		sc.add("};");
		sc.add("}");
        sc.addLineBreak();
	}

	private void addGetReferenceResolverSwitchMethod(StringComposite sc) {
		sc.add("public " + IReferenceResolverSwitch.class.getName() + " getReferenceResolverSwitch() {");
		sc.add("return new " + resolverSwitchClassName + "();");
		sc.add("}");
        sc.addLineBreak();
	}

	private void addGetTokenResolverFactoryMethod(StringComposite sc) {
		sc.add("public " + ITokenResolverFactory.class.getName() + " getTokenResolverFactory() {");
		sc.add("return new " + tokenResolverFactoryClassName + "();");
		sc.add("}");
        sc.addLineBreak();
	}
}
