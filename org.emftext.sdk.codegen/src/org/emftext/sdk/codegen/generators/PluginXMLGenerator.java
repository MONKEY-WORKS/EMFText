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

import java.io.PrintWriter;
import java.util.Collection;
import java.util.Collections;

import org.eclipse.emf.codegen.ecore.genmodel.GenPackage;
import org.emftext.sdk.codegen.EArtifact;
import org.emftext.sdk.codegen.GenerationContext;
import org.emftext.sdk.codegen.GenerationProblem;
import org.emftext.sdk.codegen.IGenerator;
import org.emftext.sdk.codegen.OptionManager;
import org.emftext.sdk.codegen.composites.StringComposite;
import org.emftext.sdk.codegen.composites.XMLComposite;
import org.emftext.sdk.concretesyntax.ConcreteSyntax;
import org.emftext.sdk.concretesyntax.OptionTypes;

/**
 * A generator for the plugin.xml file.
 */
public class PluginXMLGenerator implements IGenerator {

	private GenerationContext context;
	private String pluginID;

	public PluginXMLGenerator() {
		super();
	}

	private PluginXMLGenerator(GenerationContext context) {
		super();
		this.context = context;
		pluginID = context.getPluginName();
	}

	public boolean generate(PrintWriter out) {
		out.write(getContentOfPluginXML());
		out.flush();
		return true;
	}

	/**
	 * Generate the XML file describing the plug-in.
	 * 
	 * @return Generated code.
	 */
	private String getContentOfPluginXML() {
		final ConcreteSyntax concreteSyntax = context.getConcreteSyntax();
		final String primaryConcreteSyntaxName = getPrimarySyntaxName(concreteSyntax);
		final String secondaryConcreteSyntaxName = getSecondarySyntaxName(concreteSyntax);
		final String qualifiedResourceFactoryClassName;
		if (secondaryConcreteSyntaxName == null) {
			qualifiedResourceFactoryClassName = context.getQualifiedClassName(EArtifact.RESOURCE_FACTORY_DELEGATOR);
		}
		else {
			qualifiedResourceFactoryClassName = context.getQualifiedClassName(EArtifact.RESOURCE_FACTORY);
		}

		StringComposite sc = new XMLComposite();
		sc.add("<?xml version=\"1.0\" encoding=\"UTF-8\"?>");
		sc.add("<?eclipse version=\"3.2\"?>");
		sc.add("<plugin>");

		// register the syntax meta information
		String metaInformationClass = context.getQualifiedClassName(EArtifact.META_INFORMATION);
		sc.add("<extension point=\"org.emftext.access.syntax\">");
		sc.add("<metaInformationProvider class=\"" + metaInformationClass + "\" id=\"" + metaInformationClass + "\">");
		sc.add("</metaInformationProvider>");
		sc.add("</extension>");
		sc.addLineBreak();
		
		String editorName = context.getQualifiedClassName(EArtifact.EDITOR);

		// registers the editor itself
		sc.add("<extension point=\"org.eclipse.ui.editors\">");
		sc.add("<editor class=\"" + editorName + 
				"\" contributorClass=\"org.eclipse.ui.texteditor.BasicTextEditorActionContributor\" " +
				"extensions=\"" + primaryConcreteSyntaxName + "\" " + 
				"icon=\"icons/editor_icon.gif\" " +
				"id=\"" + editorName + "\" " + 
				"name=\"EMFText " + concreteSyntax.getName() + " Editor\">");
		sc.add("</editor>");
		sc.add("</extension>");
		sc.addLineBreak();
		
		sc.add("<extension point=\"org.eclipse.core.runtime.preferences\">");
		sc.add("<initializer class=\"" + context.getQualifiedClassName(EArtifact.PREFERENCE_INITIALIZER) + "\">");
		sc.add("</initializer>");
		sc.add("</extension>");
		sc.addLineBreak();
		
		sc.add("<extension point=\"org.eclipse.ui.preferencePages\">");
		//main page
		sc.add("<page name=\"" + context.getCapitalizedConcreteSyntaxName() + " Text Editor\" " + 
				"id=\"" + context.getQualifiedClassName(EArtifact.PREFERENCE_PAGE) + "\" " +
				"class=\"" + context.getQualifiedClassName(EArtifact.PREFERENCE_PAGE) + "\">");
		sc.add("</page>");
		//sub pages
		sc.add("<page name=\"Syntax Coloring\" " + 
				"id=\"" + context.getQualifiedClassName(EArtifact.SYNTAX_COLORING_PREFERENCE_PAGE)  + "\" " +
				"class=\"" + context.getQualifiedClassName(EArtifact.SYNTAX_COLORING_PREFERENCE_PAGE) + "\" " + 
				"category=\"" + context.getQualifiedClassName(EArtifact.PREFERENCE_PAGE) + "\">");
		sc.add("</page>");
		sc.add("<page name=\"Brackets\" " + 
				"id=\"" + context.getQualifiedClassName(EArtifact.BRACKET_PREFERENCE_PAGE) + "\" " +
				"class=\"" + context.getQualifiedClassName(EArtifact.BRACKET_PREFERENCE_PAGE) + "\" " + 
				"category=\"" + context.getQualifiedClassName(EArtifact.PREFERENCE_PAGE) + "\">");
		sc.add("</page>");
		sc.add("<page name=\"Occurrence\" " + 
				"id=\"" + context.getQualifiedClassName(EArtifact.OCCURRENCE_PREFERENCE_PAGE)  + "\" " +
				"class=\"" + context.getQualifiedClassName(EArtifact.OCCURRENCE_PREFERENCE_PAGE) + "\" " + 
				"category=\"" + context.getQualifiedClassName(EArtifact.PREFERENCE_PAGE) + "\">");
		sc.add("</page>");
		sc.add("</extension>");
		sc.addLineBreak();

		sc.add("<extension point=\"org.eclipse.ui.newWizards\">");
		sc.add("<category id=\"org.emftext.runtime.ui.EMFTextFileCategory\" name=\"EMFText File\">");
		sc.add("</category>");
		String newFileWizard = context.getQualifiedClassName(EArtifact.NEW_FILE_WIZARD);
		sc.add("<wizard category=\"org.emftext.runtime.ui.EMFTextFileCategory\" icon=\"" + context.getProjectRelativeNewIconPath() + "\" class=\"" + newFileWizard + "\" id=\"" + newFileWizard + "\" name=\"EMFText ." + context.getConcreteSyntax().getName() + " file\">");
		sc.add("</wizard>");
		sc.add("</extension>");
		sc.addLineBreak();
		
		sc.add("<extension id=\"" + pluginID + ".problem\" name=\"EMFText Problem\" point=\"org.eclipse.core.resources.markers\">");
		sc.add("<persistent value=\"true\">");
		sc.add("</persistent>");
		sc.add("<super type=\"org.eclipse.core.resources.problemmarker\">");
		sc.add("</super>");
		sc.add("<super type=\"org.eclipse.core.resources.textmarker\">");
		sc.add("</super>");
		sc.add("</extension>");
		sc.addLineBreak();
		
		sc.add("<extension-point id=\"" + pluginID + ".default_load_options\" name=\"Default Load Options\" schema=\"schema/default_load_options.exsd\"/>");
		sc.addLineBreak();
		
		if (secondaryConcreteSyntaxName == null) {
			// register the generated resource factory
			sc.add("<extension point=\"org.eclipse.emf.ecore.extension_parser\">");
			sc.add("<parser class=\"" + qualifiedResourceFactoryClassName + "\" type=\"" + primaryConcreteSyntaxName + "\">");
			sc.add("</parser>");
			sc.add("</extension>");
			sc.addLineBreak();
			
			sc.add("<extension-point id=\"" + pluginID + ".additional_extension_parser\" name=\"Additional Extension Parser\" schema=\"schema/additional_extension_parser.exsd\"/>");
			sc.addLineBreak();
		}
		else {
			String qualifiedBasePluginName = OptionManager.INSTANCE.getStringOptionValue(concreteSyntax, OptionTypes.BASE_RESOURCE_PLUGIN);
			sc.add("<extension point=\""+  qualifiedBasePluginName + ".additional_extension_parser\">");
			sc.add("<parser class=\"" + qualifiedResourceFactoryClassName + "\" type=\"" + secondaryConcreteSyntaxName + "\">");
			sc.add("</parser>");
			sc.add("</extension>");
			sc.addLineBreak();
		}
		
		if (context.isGenerateTestActionEnabled()) {
			sc.add(generateTestActionExtension());
		}

		sc.add("</plugin>");

		return sc.toString();
	}

	private String getPrimarySyntaxName(ConcreteSyntax concreteSyntax) {
		 String fullConcreteSyntaxName = concreteSyntax.getName();
		 int idx = fullConcreteSyntaxName.lastIndexOf(".");
		 if (idx != -1) {
			 return fullConcreteSyntaxName.substring(idx + 1);
		 }
		 return fullConcreteSyntaxName;
	}
	
	private String getSecondarySyntaxName(ConcreteSyntax concreteSyntax) {
		 String fullConcreteSyntaxName = concreteSyntax.getName();
		 int idx = fullConcreteSyntaxName.lastIndexOf(".");
		 if (idx != -1) {
			 return fullConcreteSyntaxName.substring(0, idx);
		 }
		 return null;
	}
	
	private StringComposite generateTestActionExtension() {

		final ConcreteSyntax concreteSyntax = context.getConcreteSyntax();
		final String concreteSyntaxName = concreteSyntax.getName();
		final GenPackage concreteSyntaxPackage = concreteSyntax.getPackage();
		final String concreteSyntaxBasePackage = concreteSyntaxPackage.getBasePackage();
		final String baseId = (concreteSyntaxBasePackage == null ? ""
				: concreteSyntaxBasePackage + ".")
				+ concreteSyntaxName;

		StringComposite s = new XMLComposite();
		s.add("<extension point=\"org.eclipse.ui.popupMenus\">");
		s.add("<objectContribution id=\"" + baseId + ".contributions\" objectClass=\"org.eclipse.core.resources.IFile\" nameFilter=\"*." + concreteSyntaxName + "\">");
		s.add("<action class=\"org.emftext.sdk.ui.actions.ValidateParserPrinterAction\" enablesFor=\"1\" id=\"" + baseId + ".validate\" label=\"Validate\" menubarPath=\"org.emftext.sdk.ui.menu1/group1\">");
		s.add("</action>");
		s.add("</objectContribution>");
		s.add("</extension>");
		return s;
	}

	public Collection<GenerationProblem> getCollectedErrors() {
		return Collections.emptyList();
	}

	public Collection<GenerationProblem> getCollectedProblems() {
		return Collections.emptyList();
	}

	public IGenerator newInstance(GenerationContext context) {
		return new PluginXMLGenerator(context);
	}
}
