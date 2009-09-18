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
package org.emftext.sdk.codegen.generators;

import static org.emftext.sdk.codegen.generators.IClassNameConstants.E_CLASS;
import static org.emftext.sdk.codegen.generators.IClassNameConstants.STRING;

import java.io.OutputStream;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.eclipse.emf.codegen.ecore.genmodel.GenClass;
import org.emftext.runtime.ui.new_wizard.AbstractNewFileWizard;
import org.emftext.sdk.codegen.EArtifact;
import org.emftext.sdk.codegen.GenerationContext;
import org.emftext.sdk.codegen.GenerationProblem;
import org.emftext.sdk.codegen.IGenerator;
import org.emftext.sdk.codegen.composites.JavaComposite;
import org.emftext.sdk.codegen.composites.StringComposite;
import org.emftext.sdk.codegen.util.GenClassUtil;
import org.emftext.sdk.concretesyntax.ConcreteSyntax;

/**
 * The NewFileContentGenerator can be used to create a NewFileWizard that 
 * creates a minimal sample file from a concrete syntax when it is invoked.
 */
public class NewFileWizardGenerator extends BaseGenerator {
	
	private final static GenClassUtil genClassUtil = new GenClassUtil();
	private GenerationContext context;

	public NewFileWizardGenerator() {
		super();
	}

	private NewFileWizardGenerator(GenerationContext context) {
		super(context, EArtifact.NEW_FILE_WIZARD);
		this.context = context;
	}

	public boolean generate(PrintWriter out) {
		StringComposite sc = new JavaComposite();
		sc.add("package " + context.getPackageName(EArtifact.NEW_FILE_WIZARD) + ";");
		sc.add("public class " + context.getClassName(EArtifact.NEW_FILE_WIZARD) + " extends " + AbstractNewFileWizard.class.getName() + " {");
		sc.addLineBreak();
		
		sc.add("public " + STRING + " getFileExtension() {");
		ConcreteSyntax syntax = context.getConcreteSyntax();
		sc.add("return \"" + syntax.getName() + "\";");
		sc.add("}");
		sc.addLineBreak();

		List<GenClass> startSymbols = syntax.getActiveStartSymbols();

		sc.add("public " + getClassNameHelper().getI_TEXT_RESOURCE_PLUGIN_META_INFORMATION() + " getMetaInformation() {");
		sc.add("return new " + context.getQualifiedClassName(EArtifact.META_INFORMATION) + "();");
		sc.add("}");
		sc.addLineBreak();

		sc.add("public " + STRING + " getExampleContent() {");
		sc.add("return getExampleContent(new " + E_CLASS + "[] {");
		for (GenClass startSymbol : startSymbols) {
			sc.add(genClassUtil.getAccessor(startSymbol) + ",");
		}
		sc.add("}, getMetaInformation().getClassesWithSyntax());");
		sc.add("}");
		sc.addLineBreak();
		
		sc.add("public " + getClassNameHelper().getI_TEXT_PRINTER() + " getPrinter(" + OutputStream.class.getName() + " outputStream) {");
		sc.add("return new " + context.getQualifiedClassName(EArtifact.PRINTER) + "(outputStream, new " + context.getQualifiedClassName(EArtifact.RESOURCE) + "());");
		sc.add("}");

		sc.add("}");
		
		out.write(sc.toString());
		out.flush();
		return true;
	}

	public Collection<GenerationProblem> getCollectedErrors() {
		return new ArrayList<GenerationProblem>();
	}

	public Collection<GenerationProblem> getCollectedProblems() {
		return new ArrayList<GenerationProblem>();
	}

	public IGenerator newInstance(GenerationContext context) {
		return new NewFileWizardGenerator(context);
	}
}