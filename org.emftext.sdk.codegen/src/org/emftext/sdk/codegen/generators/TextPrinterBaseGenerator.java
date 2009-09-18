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

import static org.emftext.sdk.codegen.generators.IClassNameConstants.ARRAYS;
import static org.emftext.sdk.codegen.generators.IClassNameConstants.BUFFERED_OUTPUT_STREAM;
import static org.emftext.sdk.codegen.generators.IClassNameConstants.COLLECTION;
import static org.emftext.sdk.codegen.generators.IClassNameConstants.E_OBJECT;
import static org.emftext.sdk.codegen.generators.IClassNameConstants.E_REFERENCE;
import static org.emftext.sdk.codegen.generators.IClassNameConstants.HASH_MAP;
import static org.emftext.sdk.codegen.generators.IClassNameConstants.INTEGER;
import static org.emftext.sdk.codegen.generators.IClassNameConstants.LIST;
import static org.emftext.sdk.codegen.generators.IClassNameConstants.LIST_ITERATOR;
import static org.emftext.sdk.codegen.generators.IClassNameConstants.MAP;
import static org.emftext.sdk.codegen.generators.IClassNameConstants.NULL_POINTER_EXCEPTION;
import static org.emftext.sdk.codegen.generators.IClassNameConstants.OBJECT;
import static org.emftext.sdk.codegen.generators.IClassNameConstants.OUTPUT_STREAM;
import static org.emftext.sdk.codegen.generators.IClassNameConstants.PRINTER_WRITER;
import static org.emftext.sdk.codegen.generators.IClassNameConstants.STRING;
import static org.emftext.sdk.codegen.generators.IClassNameConstants.STRING_WRITER;

import java.io.PrintWriter;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;
import java.util.Queue;
import java.util.Set;

import org.eclipse.emf.codegen.ecore.genmodel.GenClass;
import org.eclipse.emf.codegen.ecore.genmodel.GenFeature;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.emftext.runtime.resource.ITokenResolver;
import org.emftext.runtime.resource.impl.AbstractEMFTextPrinter;
import org.emftext.runtime.util.StringUtil;
import org.emftext.sdk.CollectInFeatureHelper;
import org.emftext.sdk.codegen.EArtifact;
import org.emftext.sdk.codegen.GenerationContext;
import org.emftext.sdk.codegen.GeneratorUtil;
import org.emftext.sdk.codegen.IGenerator;
import org.emftext.sdk.codegen.OptionManager;
import org.emftext.sdk.codegen.composites.JavaComposite;
import org.emftext.sdk.codegen.composites.StringComponent;
import org.emftext.sdk.codegen.composites.StringComposite;
import org.emftext.sdk.codegen.util.ConcreteSyntaxUtil;
import org.emftext.sdk.concretesyntax.Cardinality;
import org.emftext.sdk.concretesyntax.CardinalityDefinition;
import org.emftext.sdk.concretesyntax.Choice;
import org.emftext.sdk.concretesyntax.CompoundDefinition;
import org.emftext.sdk.concretesyntax.ConcreteSyntax;
import org.emftext.sdk.concretesyntax.Containment;
import org.emftext.sdk.concretesyntax.CsString;
import org.emftext.sdk.concretesyntax.Definition;
import org.emftext.sdk.concretesyntax.LineBreak;
import org.emftext.sdk.concretesyntax.OptionTypes;
import org.emftext.sdk.concretesyntax.PLUS;
import org.emftext.sdk.concretesyntax.Placeholder;
import org.emftext.sdk.concretesyntax.QUESTIONMARK;
import org.emftext.sdk.concretesyntax.Rule;
import org.emftext.sdk.concretesyntax.STAR;
import org.emftext.sdk.concretesyntax.Sequence;
import org.emftext.sdk.concretesyntax.Terminal;
import org.emftext.sdk.concretesyntax.WhiteSpaces;
import org.emftext.sdk.finders.GenClassFinder;

/**
 * A generator that creates the base class for the printer.
 * 
 * @author Sven Karol (Sven.Karol@tu-dresden.de)
 */
public class TextPrinterBaseGenerator extends BaseGenerator {

	private final static String localtabName = "localtab";

	private final GeneratorUtil generatorUtil = new GeneratorUtil();
	private final GenClassFinder genClassFinder = new GenClassFinder();

	private ConcreteSyntax concretSyntax;
	private String qualifiedTokenResolverFactoryClassName;
	private String qualifiedReferenceResolverSwitchClassName;
	
	private int tokenSpace;
	/** maps all choices to a method name */
	private Map<Choice, String> choice2Name;
	/** maps all rules to choices nested somewhere, but not to the root choice! */
	private Map<Rule, Set<Choice>> rule2SubChoice;
	/** 
	 * maps all sequences in all choices to all features which HAVE to be printed in the sequence
	 */
	private Map<Sequence, Set<String>> sequence2NecessaryFeatures;
	private Map<Sequence, Set<String>> sequence2ReachableFeatures;

	private ConcreteSyntaxUtil csUtil = new ConcreteSyntaxUtil();

	public TextPrinterBaseGenerator() {
		super();
	}

	private TextPrinterBaseGenerator(GenerationContext context) {
		super(context, EArtifact.PRINTER_BASE);

		this.concretSyntax = context.getConcreteSyntax();
		this.qualifiedTokenResolverFactoryClassName = context.getQualifiedClassName(EArtifact.TOKEN_RESOLVER_FACTORY);
		this.qualifiedReferenceResolverSwitchClassName = context.getQualifiedClassName(EArtifact.REFERENCE_RESOLVER_SWITCH);
	}

	private void extractChoices(List<Rule> rules,
			Map<Rule, Set<Choice>> ruleMap, Map<Choice, String> choiceMap,
			Map<Sequence, Set<String>> necessaryMap,
			Map<Sequence, Set<String>> reachableMap) {
		for (Rule rule : rules) {
			choiceMap.put(rule.getDefinition(), getMethodName(rule));
			Set<Choice> choices = new HashSet<Choice>();
			ruleMap.put(rule, choices);
			extractChoices(rule.getDefinition(), choices, choiceMap,
					necessaryMap, reachableMap, null, getMethodName(rule) + "_", 0);
		}
	}

	private void extractChoices(Choice choice, Set<Choice> choiceSet,
			Map<Choice, String> choiceMap,
			Map<Sequence, Set<String>> necessaryMap,
			Map<Sequence, Set<String>> reachableMap, Sequence parent,
			String namePrefix, int choiceIndex) {
		for (Sequence seq : choice.getOptions()) {
			Set<String> sequenceNecessaryFeatures = new HashSet<String>();
			Set<String> sequenceReachableFeatures = new HashSet<String>();
			necessaryMap.put(seq, sequenceNecessaryFeatures);
			reachableMap.put(seq, sequenceReachableFeatures);
			for (Definition def : seq.getParts()) {
				final boolean hasMinimalCardinalityOneOrHigher = csUtil.hasMinimalCardinalityOneOrHigher(def);
				if (def instanceof CompoundDefinition) {
					String subChoiceName = namePrefix + choiceIndex++;
					Choice currentChoice = ((CompoundDefinition) def)
							.getDefinitions();
					choiceMap.put(currentChoice, subChoiceName);
					choiceSet.add(currentChoice);
					extractChoices(currentChoice, choiceSet, choiceMap,
							necessaryMap, reachableMap,
							hasMinimalCardinalityOneOrHigher ? seq
									: null, subChoiceName + "_", 0);
				} else if (def instanceof Terminal) {
					if (hasMinimalCardinalityOneOrHigher) {
						sequenceNecessaryFeatures.add(((Terminal) def)
								.getFeature().getName());
					}
					sequenceReachableFeatures.add(((Terminal) def).getFeature()
							.getName());
				}
			}
			if (parent != null) {
				necessaryMap.get(parent).addAll(sequenceNecessaryFeatures);
				reachableMap.get(parent).addAll(sequenceReachableFeatures);
			}

		}
	}

	private List<Rule> prepare() {
		List<Rule> rules = concretSyntax.getAllRules();
		choice2Name = new HashMap<Choice, String>(rules.size());
		sequence2NecessaryFeatures = new HashMap<Sequence, Set<String>>(rules
				.size());
		sequence2ReachableFeatures = new HashMap<Sequence, Set<String>>(rules
				.size());
		rule2SubChoice = new HashMap<Rule, Set<Choice>>(rules.size());
		extractChoices(rules, rule2SubChoice, choice2Name,
				sequence2NecessaryFeatures, sequence2ReachableFeatures);
		
        tokenSpace = OptionManager.INSTANCE.getIntegerOptionValue(concretSyntax, OptionTypes.TOKENSPACE, true, this);
		if (tokenSpace < 0) {
			tokenSpace = 1;
		}
		return rules;
	}

	@Override
	public boolean generate(PrintWriter writer) {
		List<Rule> rules = prepare();
		
		StringComposite sc = new JavaComposite();
		
		sc.add("package " + getResourcePackageName() + ";");
		sc.addLineBreak();
		sc.add("public abstract class " + getResourceClassName()
				+ " extends " + AbstractEMFTextPrinter.class.getName() + " {");
		sc.addLineBreak();
		
		addMembers(sc);
		sc.addLineBreak();
		
		addConstructor(sc);
		sc.addLineBreak();
		
		printMatchRule(sc);
		sc.addLineBreak();
		
		addDoPrintMethod(sc, rules);
		sc.addLineBreak();
		
        addGetReferenceResolverSwitchMethod(sc);
		sc.addLineBreak();
		
		addAddWarningToResourceMethod(sc);
		sc.addLineBreak();

		addSetOptionsMethod(sc);
		sc.addLineBreak();

		addGetOptionsMethod(sc);
		sc.addLineBreak();

		addGetResourceMethod(sc);
		sc.addLineBreak();

		addPrintMethod(sc);
		sc.addLineBreak();
		
        for (Rule rule : rules) {
			addPrintRuleMethod(sc, rule);
			sc.addLineBreak();
		}

		sc.add("}");
		
		writer.write(sc.toString());
		return true;
	}

	private void addPrintMethod(StringComposite sc) {
		sc.add("/** Calls {@link #doPrint(EObject, String)} and writes the result to the underlying output stream. */");
		sc.add("public void print(" + E_OBJECT + " element)  {");
		sc.add(PRINTER_WRITER + " out = new " + PRINTER_WRITER + "(new " + BUFFERED_OUTPUT_STREAM + "(outputStream));");
		sc.add("doPrint(element,out,\"\");");
		sc.add("out.flush();");
		sc.add("out.close();");
		sc.add("}");
	}

	private void addGetResourceMethod(StringComposite sc) {
		sc.add("public " + getClassNameHelper().getI_TEXT_RESOURCE() + " getResource() {");
		sc.add("return resource;");
		sc.add("}");
	}

	private void addGetOptionsMethod(StringComposite sc) {
		sc.add("public " + MAP + "<?,?> getOptions() {");
		sc.add("return options;");
		sc.add("}");
	}

	private void addSetOptionsMethod(StringComposite sc) {
		sc.add("public void setOptions(" + MAP + "<?,?> options) {");
		sc.add("this.options = options;");
		sc.add("}");
	}

	private void addGetReferenceResolverSwitchMethod(StringComposite sc) {
		sc.add("protected " + qualifiedReferenceResolverSwitchClassName + " getReferenceResolverSwitch() {");
		sc.add(getClassNameHelper().getI_TEXT_RESOURCE() + " resource = getResource();");
        sc.add("if (resource == null) {");
        sc.add("return null;");
        sc.add("}");
        sc.add("return (" + qualifiedReferenceResolverSwitchClassName + ") resource.getMetaInformation().getReferenceResolverSwitch();");
        sc.add("}");
	}

	private void addDoPrintMethod(StringComposite sc, List<Rule> rules) {
		sc.add("protected void doPrint(" + E_OBJECT + " element, " + PRINTER_WRITER + " out, " + STRING + " globaltab) {");
		sc.add("if (element == null || out == null) throw new " + NULL_POINTER_EXCEPTION + "(\"Nothing to write or to write on.\");");
		sc.addLineBreak();
		Queue<Rule> ruleQueue = new LinkedList<Rule>(rules);
		while (!ruleQueue.isEmpty()) {
			Rule rule = ruleQueue.remove();
			// check whether all subclass calls have been printed
			if (csUtil.hasSubClassesWithCS(rule.getMetaclass(),
					ruleQueue)) {
				ruleQueue.add(rule);
			} else {
				sc.add("if (element instanceof " + getMetaClassName(rule) + ") {");
				sc.add(getMethodName(rule) + "((" + getMetaClassName(rule)
						+ ") element, globaltab, out);");
				sc.add("return;");
				sc.add("}");
			}
		}
		sc.addLineBreak();
		sc.add("addWarningToResource(\"The cs printer can not handle \" + element.eClass().getName() + \" elements\", element);");
		sc.add("}");
	}

	private void addConstructor(StringComposite sc) {
		sc.add("public " + super.getResourceClassName()
				+ "(" + OUTPUT_STREAM + " outputStream, " + getClassNameHelper().getI_TEXT_RESOURCE() + " resource) {");
		sc.add("super();");
		sc.add("this.outputStream = outputStream;");
		sc.add("this.resource = resource;");
		sc.add("}");
	}

	private void addMembers(StringComposite sc) {
		sc.add("protected final static " + STRING + " NEW_LINE = java.lang.System.getProperties().getProperty(\"line.separator\");");
		sc.add("protected " + getClassNameHelper().getI_TOKEN_RESOLVER_FACTORY() + " tokenResolverFactory = new "
						+ qualifiedTokenResolverFactoryClassName + "();");
		sc.add("protected " +  OUTPUT_STREAM + " outputStream;");
		sc.add("/** Holds the resource that is associated with this printer. may be null if the printer is used stand alone. */");
		sc.add("private " + getClassNameHelper().getI_TEXT_RESOURCE() + " resource;");
		sc.add("private " + MAP + "<?, ?> options;");
	}

	private void addAddWarningToResourceMethod(StringComposite sc) {
		sc.add("protected void addWarningToResource(final " + STRING + " errorMessage, " + E_OBJECT + " cause) {");
		sc.add(getClassNameHelper().getI_TEXT_RESOURCE() + " resource = getResource();");
		sc.add("if (resource == null) {");
		sc.add("// the resource can be null if the printer is used stand alone");
		sc.add("return;");
		sc.add("}");
    	sc.add("resource.addProblem(new " + getContext().getQualifiedClassName(EArtifact.PROBLEM) + "(errorMessage, " + getClassNameHelper().getE_PROBLEM_TYPE() + ".ERROR), cause);");
		sc.add("}");
	}

	private void addPrintRuleMethod(StringComposite sc, Rule rule) {
		
		final GenClass genClass = rule.getMetaclass();
		List<GenFeature> featureList = genClass.getAllGenFeatures();

		sc.add("public void " + getMethodName(rule) + "("
				+ getMetaClassName(rule)
				+ " element, " + STRING + " outertab, " + PRINTER_WRITER + " out) {");

		sc.add(new StringComponent(STRING + " " + localtabName + " = outertab;", localtabName));

		String printCountingMapName = "printCountingMap";
		sc.add(new StringComponent(MAP + "<" + STRING + ", " + INTEGER + "> " + printCountingMapName + " = new " + HASH_MAP + "<" + STRING + ", " + INTEGER + ">("
				+ featureList.size() + ");", printCountingMapName));
		
		if (featureList.size() > 0) {
			sc.add(OBJECT + " temp;");
		}
		for (GenFeature genFeature : featureList) {
			EStructuralFeature feature = genFeature.getEcoreFeature();
			sc.add("temp = element." + getAccessMethod(genClass, genFeature)
					+ ";");

			boolean isMultiple = feature.getUpperBound() > 1 || feature.getUpperBound() == -1;
			String featureSize = isMultiple ? "((" + java.util.Collection.class.getName() + "<?>) temp).size()"
					: "1";
			sc.add("printCountingMap.put(\"" + feature.getName()
					+ "\", temp == null ? 0 : " + featureSize + ");");
			//mapDefinition.enable();
		}
		addPrintCollectedTokensCode(sc, rule);
		
		printChoice(rule.getDefinition(), sc, genClass);
		sc.add("}");
		printChoices(sc, rule);
	}

	private void printChoices(StringComposite sc, Rule rule) {
		for (Choice choice : rule2SubChoice.get(rule)) {
			sc
					.add("public void "
							+ choice2Name.get(choice)
							+ "("
							+ getMetaClassName(rule)
							+ " element, " + STRING + " outertab, " + PRINTER_WRITER + " out, " + MAP + "<" + STRING + ", " + INTEGER + "> printCountingMap){");

			sc.add(new StringComponent(STRING + " " + localtabName + " = outertab;", localtabName));
			printChoice(choice, sc, rule.getMetaclass());
			sc.add("}");
		}
	}

	private String getMetaClassName(Rule rule) {
		if (hasMapType(rule.getMetaclass()) ) {
			return rule.getMetaclass().getQualifiedClassName();
		}
		return genClassFinder.getQualifiedInterfaceName(rule.getMetaclass());
	}

	private String getMethodName(Rule rule) {
		String className = getMetaClassName(rule);
		
		// first escape underscore with their unicode value
		className = className.replace("_", "_005f");
		// then replace package separator with underscore
		className = className.replace(".", "_");
		return "print_" +  className;
	}

	private void addPrintCollectedTokensCode(StringComposite sc, Rule rule) {

		final GenClass genClass = rule.getMetaclass();
		List<GenFeature> featureList = genClass.getAllGenFeatures();

		sc.add("// print collected hidden tokens");
		for (GenFeature genFeature : featureList) {
			EStructuralFeature feature = genFeature.getEcoreFeature();
			if (new CollectInFeatureHelper().isCollectInFeature(rule.getSyntax(), feature)) {
				sc.add("{");
				sc.add(EStructuralFeature.class.getName() + " feature = element.eClass()." + generatorUtil.createGetFeatureCall(genClass, genFeature) + ";");
				sc.add(OBJECT + " value = element.eGet(feature);");
				sc.add("if (value instanceof " + LIST + ") {");
				sc.add("for (" + OBJECT + " next : (" + LIST + "<?>) value) {");
				sc.add("out.print(tokenResolverFactory.createCollectInTokenResolver(\"" + feature.getName() + "\").deResolve(next, feature, element));");
				sc.add("}");
				sc.add("}");
				sc.add("}");
			}
		}
	}

	private void printChoice(Choice choice, StringComposite sc, GenClass genClass) {
		String countName = "count";
		sc.add(new StringComponent("int " + countName + ";", countName));
		
		if (choice.getOptions().size() > 1) {
			sc.add("int alt = -1;");
			Iterator<Sequence> seqIt = choice.getOptions().iterator();
			if (seqIt.hasNext()) {
				Sequence firstSeq = seqIt.next();
				int count = 0;
				StringComposite sc1 = new JavaComposite();
				sc1.add("switch(alt) {");
				sc.add("alt=" + count++ + ";");
				sc.add("int matches=");
				printMatchCall(firstSeq, sc);
				sc.add("int tempMatchCount;");
				while (seqIt.hasNext()) {
					Sequence seq = seqIt.next();
					sc.add("tempMatchCount=");
					printMatchCall(seq, sc);
					sc.add("if (tempMatchCount > matches) {");
					sc.add("alt = " + count + ";");
					sc.add("matches = tempMatchCount;");
					sc.add("}");

					sc1.add("case " + count + ":");
					// extra scope for case begin
					sc1.add("{");
					printSequence(seq, sc1, genClass);
					// extra scope for case end
					sc1.add("}");
					sc1.add("break;");
					count++;
				}

				sc1.add("default:");
				printSequence(firstSeq, sc1, genClass);
				sc1.add("}");
				sc.add(sc1);
			}
		} else if (choice.getOptions().size() == 1) {
			printSequence(choice.getOptions().get(0), sc, genClass);
		}
	}

	private Set<String> getReachableFeatures(Choice choice) {
		Set<String> result = new LinkedHashSet<String>();
		for (Sequence seq : choice.getOptions()) {
			result.addAll(sequence2ReachableFeatures.get(seq));
		}
		return result;
	}

	private void printSequence(Sequence sequence, StringComposite sc,
			GenClass genClass) {
		Set<String> neededFeatures = new LinkedHashSet<String>(
				sequence2NecessaryFeatures.get(sequence));

		//EClass metaClass = genClass.getEcoreClass();
		ListIterator<Definition> definitionIterator = sequence.getParts()
				.listIterator();
		
		String iterateName = "iterate";
		sc.add(new StringComponent("boolean " + iterateName + " = true;", iterateName));
		String sWriteName = "sWriter";
		sc.add(new StringComponent(STRING_WRITER + " " + sWriteName + " = null;", sWriteName));
		String out1Name = "out1";
		sc.add(new StringComponent(PRINTER_WRITER + " " + out1Name + " = null;", out1Name));
		String printCountingMap1Name = "printCountingMap1";
		sc.add(new StringComponent(HASH_MAP + "<" + STRING + ", " + INTEGER + "> " + printCountingMap1Name + " = null;", printCountingMap1Name));
		
		while (definitionIterator.hasNext()) {
			Definition definition = definitionIterator.next();
			
			sc.add("//////////////DEFINITION PART BEGINS ("
					+ definition.eClass().getName() + "):");
			String printPrefix = "out.print(";
			if (definition instanceof LineBreak) {
				int count = ((LineBreak) definition).getTab();
				if (count > 0) {
					String tab = getTabString(count);
					sc.add("localtab += \"" + tab + "\";");
				}
				sc.add("out.println();");
				sc.add("out.print(localtab);");
				//localTabDefinition.enable();
			} else {
				if (definition instanceof WhiteSpaces) {
					int count = ((WhiteSpaces) definition).getAmount();
					if (count > 0) {
						String spaces = getWhiteSpaceString(count);
						sc.add(printPrefix + "\"" + spaces
								+ "\");");
					}
				} else if (definition instanceof CsString) {
					CsString terminal = (CsString) definition;
					sc.add(printPrefix + "\""
							+ StringUtil.escapeToJavaString(terminal.getValue())
							+ "\");");

					// the given tokenSpace (>0) causes an additional
					// print statement to be printed
					if (tokenSpace > 0) {
						Definition lookahead = null;
						if (definitionIterator.hasNext()){
							lookahead = definitionIterator.next();
							definitionIterator.previous();
						}
							
						if (lookahead == null
								|| !(lookahead instanceof WhiteSpaces)) {
							String printSuffix = getWhiteSpaceString(tokenSpace);
							
							sc.add(printPrefix + "\"" + printSuffix + "\");");
						}
							
					}

				} else {
					Cardinality cardinality = null;
					if (definition instanceof CardinalityDefinition) {
						cardinality = ((CardinalityDefinition) definition).getCardinality();
					}
					if (definition instanceof CompoundDefinition) {
						CompoundDefinition compound = (CompoundDefinition) definition;
						String printStatement = 
								choice2Name.get(compound.getDefinitions())
								+ "(element, localtab, out, printCountingMap);";
						//localTabDefinition.enable();
						// enter once
						if (cardinality == null || cardinality instanceof PLUS) {
							sc.add(printStatement);
							// needed features in non optional choice are also
							// needed features in this sequence
							// note: this implementation might remove to much in
							// some cases!
							for (Sequence seq1 : compound.getDefinitions()
									.getOptions()) {
								neededFeatures
										.removeAll(sequence2NecessaryFeatures
												.get(seq1));
							}
						}
						// optional cases
						if (cardinality != null) {

							boolean isMany = !(cardinality instanceof QUESTIONMARK);
							// runtime: iterate as long as no fixpoint is
							// reached
							// make sure that NOT all elements of needed
							// features are printed in optional compounds!
							if (isMany) {
								sc.add("iterate = true;");
								sc.add("while (iterate) {");
								//iterateDeclaration.enable();
							}
							sc.add("sWriter = new " + STRING_WRITER + "();");
							sc.add("out1 = new " + PRINTER_WRITER + "(sWriter);");
							sc.add("printCountingMap1 = new " + HASH_MAP + "<" + STRING + ", " + INTEGER + ">(printCountingMap);");
							//compoundDeclaration.enable();
							
							sc.add(choice2Name.get(compound
													.getDefinitions())
											+ "(element, localtab, out1, printCountingMap1);");
							sc.add("if (printCountingMap.equals(printCountingMap1)) {");
							if (isMany) {
								sc.add("iterate = false;");
							}
							sc.add("out1.close();");
							sc.add("} else {");
							// check if printed features are needed by
							// subsequent features
							// at least one element has to be preserved in that
							// case!
							// this could be enhanced by a counter on needed
							// features
							Collection<String> reachableFeatures = getReachableFeatures(compound
									.getDefinitions());
							if (!neededFeatures.isEmpty()
									&& !Collections.disjoint(neededFeatures,
											reachableFeatures)) {
								sc.add("if(");
								Iterator<String> it = neededFeatures.iterator();
								sc.add("printCountingMap1.get(\""
										+ it.next() + "\")<1");
								while (it.hasNext()) {
									String feature = it.next();
									sc.add("||printCountingMap1.get(\""
											+ feature + "\")<1");
								}
								sc.add(") {");
								if (isMany) {
									sc.add("iterate = false;");
								}
								sc.add("out1.close();");
								sc.add("} else {");
								sc.add("out1.flush();");
								sc.add("out1.close();");
								sc.add("out.print(sWriter.toString());");
								sc.add("printCountingMap.putAll(printCountingMap1);");
								sc.add("}");
							} else {
								sc.add("out1.flush();");
								sc.add("out1.close();");
								sc.add("out.print(sWriter.toString());");
								sc.add("printCountingMap.putAll(printCountingMap1);");
							}
							sc.add("}");
							if (isMany) {
								sc.add("}");
							}
						}
					}
					// next steps: references --> proxy uri --> tokenresolver!
					else if (definition instanceof Terminal) {
						Terminal terminal = (Terminal) definition;
						final GenFeature genFeature = terminal.getFeature();
						final String featureName = genFeature.getName();
						sc.add("count = printCountingMap.get(\""
								+ featureName + "\");");
						EStructuralFeature feature = genFeature
								.getEcoreFeature();
						StringComposite printStatements = new JavaComposite();

						if (terminal instanceof Placeholder) {
							String tokenName = ((Placeholder) terminal).getToken().getName();

							String featureConstant = generatorUtil.getFeatureConstant(genClass, genFeature);
							if (feature instanceof EReference) {
								printStatements.add(ITokenResolver.class.getName() + " resolver = tokenResolverFactory.createTokenResolver(\""
										+ tokenName
										+ "\");");
								printStatements.add("resolver.setOptions(getOptions());");
								printStatements.add(printPrefix + "resolver.deResolve(" 
										+ getContext().getReferenceResolverAccessor(genFeature)
										+ ".deResolve((" + genClassFinder.getQualifiedInterfaceName(genFeature.getTypeGenClass()) + ") o, element, (" + E_REFERENCE + ") element.eClass().getEStructuralFeature("
										+ featureConstant
										+ ")), element.eClass().getEStructuralFeature("
										+ featureConstant
										+ "), element));");
							} else {
								printStatements.add(ITokenResolver.class.getName() + " resolver = tokenResolverFactory.createTokenResolver(\""
										+ tokenName
										+ "\");");
								printStatements.add("resolver.setOptions(getOptions());");
								printStatements.add(printPrefix
										+ "resolver.deResolve((" + OBJECT + ") o, element.eClass().getEStructuralFeature("
										+ featureConstant
										+ "), element));");
							}


							// the given tokenSpace (>0) causes an additional
							// print statement to be printed
							if (tokenSpace > 0) {
								Definition lookahead = null;
								if (definitionIterator.hasNext()){
									lookahead = definitionIterator.next();
									definitionIterator.previous();
								}
									
								if (lookahead == null
										|| !(lookahead instanceof WhiteSpaces)) {
									String printSuffix = getWhiteSpaceString(tokenSpace);
									printStatements.add("out.print(\"" + printSuffix + "\");");
								}
							}
						} else {
							assert terminal instanceof Containment;
							printStatements.add("doPrint((" + E_OBJECT + ") o, out, localtab);");
						}

						sc.add("if (count > 0) {");
						if (cardinality == null
								|| (cardinality instanceof QUESTIONMARK && !neededFeatures
										.contains(featureName))) {
							sc.add("Object o = element."
									+ getAccessMethod(genClass, genFeature) + ";");
							if (feature.getUpperBound() != 1) {
								sc.add(LIST +"<?> list = (" + LIST + "<?>) o;");
								sc.add("int index = list.size() - count;");
								// we must check the index, because the list in the model
								// may contain less elements than expected.
								//
								// this is for example the case, for a CS definitions like:
								//
								// feature ("," feature)*
								//
								// where the first element is mandatory in the CS rule, but
								// the meta model defines a cardinality of 0..* for 'feature'.
								sc.add("if (index >= 0) {");
								sc.add("o = list.get(index);");
								sc.add("} else {");
								sc.add("o = null;");
								sc.add("}");
							}
							sc.add("if (o != null) {");
							sc.add(printStatements);
							sc.add("}");
							sc.add("printCountingMap.put(\"" + featureName + "\", count - 1);");
							neededFeatures.remove(featureName);
						} else if (cardinality instanceof PLUS
								|| cardinality instanceof STAR) {
							if (feature.getUpperBound() != 1) {
								sc.add(LIST + "<?> list = (" + LIST +"<?>)element."
										+ getAccessMethod(genClass, genFeature)
										+ ";");
								sc.add("int index  = list.size() - count;");
								sc.add("if (index < 0) {");
								sc.add("index = 0;");
								sc.add("}");
								sc.add(LIST_ITERATOR + "<?> it  = list.listIterator(index);");
								sc.add("while (it.hasNext()) {");
								sc.add(OBJECT + " o = it.next();");
								if (cardinality instanceof STAR
										&& neededFeatures.contains(featureName)) {
									sc.add("if(!it.hasNext())");
									sc.add("break;");
								}
								sc.add(printStatements);
								sc.add("}");
								sc.add("printCountingMap.put(\""
										+ featureName + "\",0);");
							} else if (cardinality instanceof PLUS) {
								sc.add(OBJECT + " o =element."
										+ getAccessMethod(genClass, genFeature) + ";");
								sc.add(printStatements);
								sc.add("printCountingMap.put(\""
										+ featureName + "\",count-1);");
							}
							if (cardinality instanceof PLUS)
								neededFeatures.remove(featureName);
						}

						sc.add("}"); // tab for if(count>0)
					}
				}
			}
		}
	}

	private void printMatchCall(Sequence seq, StringComposite sc) {
		if (sequence2NecessaryFeatures.get(seq).isEmpty()) {
			sc.add("0;");
			return;
		}
		sc.add("matchCount(printCountingMap, " + ARRAYS + ".asList(");
		boolean notFirst = false;
		for (String featureName : sequence2NecessaryFeatures.get(seq)) {
			if (notFirst) {
				sc.add(",");
			} else {
				notFirst = true;
			}
			sc.add("\"" + featureName + "\"");
		}
		sc.add("));");
	}


	private void printMatchRule(StringComposite sc) {
		sc.add("protected static int matchCount(" + MAP + "<" + STRING + ", " + INTEGER + "> featureCounter, " + COLLECTION + "<" + STRING+ "> needed){");
		sc.add("int pos = 0;");
		sc.add("int neg = 0;");
		sc.addLineBreak();
		sc.add("for(" + STRING + " featureName:featureCounter.keySet()){");
		sc.add("if(needed.contains(featureName)){");
		sc.add("int value = featureCounter.get(featureName);");
		sc.add("if (value == 0) {");
		sc.add("neg += 1;");
		sc.add("} else {");
		sc.add("pos += 1;");
		sc.add("}");
		sc.add("}");
		sc.add("}");
		sc.add("return neg > 0 ? -neg : pos;");
		sc.add("}");
	}

	protected String getAccessMethod(GenClass genClass, GenFeature genFeature) {
		if (hasMapType(genClass)) {
			return "get" + StringUtil.capitalize(genFeature.getName()) + "()";
		}
		else {
			String method = "eGet(element.eClass().getEStructuralFeature(" + generatorUtil.getFeatureConstant(genClass, genFeature) + "))";
			return method;
		}
	}

	private static boolean hasMapType(GenClass genClass) {
		return java.util.Map.Entry.class.getName().equals(genClass.getEcoreClass().getInstanceClassName());
	}

	private String getTabString(int count) {
		return getRepeatingString(count, '\t');
	}

	private String getWhiteSpaceString(int count) {
		return getRepeatingString(count, ' ');
	}
	
	private String getRepeatingString(int count, char character) {
		StringBuffer spaces = new StringBuffer();
		for (int i = 0; i < count; i++) {
			spaces.append(character);
		}
		return spaces.toString();
	}

	public IGenerator newInstance(GenerationContext context) {
		return new TextPrinterBaseGenerator(context);
	}
}