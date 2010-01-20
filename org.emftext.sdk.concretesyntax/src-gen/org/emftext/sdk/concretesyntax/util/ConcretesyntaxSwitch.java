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
package org.emftext.sdk.concretesyntax.util;

import java.util.List;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;
import org.emftext.sdk.concretesyntax.Abstract;
import org.emftext.sdk.concretesyntax.Annotable;
import org.emftext.sdk.concretesyntax.Annotation;
import org.emftext.sdk.concretesyntax.AtomicRegex;
import org.emftext.sdk.concretesyntax.Cardinality;
import org.emftext.sdk.concretesyntax.CardinalityDefinition;
import org.emftext.sdk.concretesyntax.Choice;
import org.emftext.sdk.concretesyntax.CompoundDefinition;
import org.emftext.sdk.concretesyntax.ConcreteSyntax;
import org.emftext.sdk.concretesyntax.ConcretesyntaxPackage;
import org.emftext.sdk.concretesyntax.Containment;
import org.emftext.sdk.concretesyntax.CsString;
import org.emftext.sdk.concretesyntax.Definition;
import org.emftext.sdk.concretesyntax.GenPackageDependentElement;
import org.emftext.sdk.concretesyntax.Import;
import org.emftext.sdk.concretesyntax.KeyValuePair;
import org.emftext.sdk.concretesyntax.LineBreak;
import org.emftext.sdk.concretesyntax.NormalToken;
import org.emftext.sdk.concretesyntax.Option;
import org.emftext.sdk.concretesyntax.PLUS;
import org.emftext.sdk.concretesyntax.PartialToken;
import org.emftext.sdk.concretesyntax.Placeholder;
import org.emftext.sdk.concretesyntax.PlaceholderInQuotes;
import org.emftext.sdk.concretesyntax.PlaceholderUsingDefaultToken;
import org.emftext.sdk.concretesyntax.PlaceholderUsingSpecifiedToken;
import org.emftext.sdk.concretesyntax.QUESTIONMARK;
import org.emftext.sdk.concretesyntax.QuotedToken;
import org.emftext.sdk.concretesyntax.RegexComposite;
import org.emftext.sdk.concretesyntax.RegexOwner;
import org.emftext.sdk.concretesyntax.RegexPart;
import org.emftext.sdk.concretesyntax.RegexReference;
import org.emftext.sdk.concretesyntax.Rule;
import org.emftext.sdk.concretesyntax.STAR;
import org.emftext.sdk.concretesyntax.Sequence;
import org.emftext.sdk.concretesyntax.Terminal;
import org.emftext.sdk.concretesyntax.TokenDefinition;
import org.emftext.sdk.concretesyntax.TokenDirective;
import org.emftext.sdk.concretesyntax.TokenPriorityDirective;
import org.emftext.sdk.concretesyntax.TokenStyle;
import org.emftext.sdk.concretesyntax.WhiteSpaces;


/**
 * <!-- begin-user-doc -->
 * The <b>Switch</b> for the model's inheritance hierarchy.
 * It supports the call {@link #doSwitch(EObject) doSwitch(object)}
 * to invoke the <code>caseXXX</code> method for each class of the model,
 * starting with the actual class of the object
 * and proceeding up the inheritance hierarchy
 * until a non-null result is returned,
 * which is the result of the switch.
 * <!-- end-user-doc -->
 * @see org.emftext.sdk.concretesyntax.ConcretesyntaxPackage
 * @generated
 */
public class ConcretesyntaxSwitch<T> {
	/**
	 * The cached model package
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected static ConcretesyntaxPackage modelPackage;

	/**
	 * Creates an instance of the switch.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public ConcretesyntaxSwitch() {
		if (modelPackage == null) {
			modelPackage = ConcretesyntaxPackage.eINSTANCE;
		}
	}

	/**
	 * Calls <code>caseXXX</code> for each class of the model until one returns a non null result; it yields that result.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the first non-null result returned by a <code>caseXXX</code> call.
	 * @generated
	 */
	public T doSwitch(EObject theEObject) {
		return doSwitch(theEObject.eClass(), theEObject);
	}

	/**
	 * Calls <code>caseXXX</code> for each class of the model until one returns a non null result; it yields that result.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the first non-null result returned by a <code>caseXXX</code> call.
	 * @generated
	 */
	protected T doSwitch(EClass theEClass, EObject theEObject) {
		if (theEClass.eContainer() == modelPackage) {
			return doSwitch(theEClass.getClassifierID(), theEObject);
		}
		else {
			List<EClass> eSuperTypes = theEClass.getESuperTypes();
			return
				eSuperTypes.isEmpty() ?
					defaultCase(theEObject) :
					doSwitch(eSuperTypes.get(0), theEObject);
		}
	}

	/**
	 * Calls <code>caseXXX</code> for each class of the model until one returns a non null result; it yields that result.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the first non-null result returned by a <code>caseXXX</code> call.
	 * @generated
	 */
	protected T doSwitch(int classifierID, EObject theEObject) {
		switch (classifierID) {
			case ConcretesyntaxPackage.GEN_PACKAGE_DEPENDENT_ELEMENT: {
				GenPackageDependentElement genPackageDependentElement = (GenPackageDependentElement)theEObject;
				T result = caseGenPackageDependentElement(genPackageDependentElement);
				if (result == null) result = defaultCase(theEObject);
				return result;
			}
			case ConcretesyntaxPackage.CONCRETE_SYNTAX: {
				ConcreteSyntax concreteSyntax = (ConcreteSyntax)theEObject;
				T result = caseConcreteSyntax(concreteSyntax);
				if (result == null) result = caseGenPackageDependentElement(concreteSyntax);
				if (result == null) result = caseAnnotable(concreteSyntax);
				if (result == null) result = defaultCase(theEObject);
				return result;
			}
			case ConcretesyntaxPackage.IMPORT: {
				Import import_ = (Import)theEObject;
				T result = caseImport(import_);
				if (result == null) result = caseGenPackageDependentElement(import_);
				if (result == null) result = defaultCase(theEObject);
				return result;
			}
			case ConcretesyntaxPackage.RULE: {
				Rule rule = (Rule)theEObject;
				T result = caseRule(rule);
				if (result == null) result = caseAnnotable(rule);
				if (result == null) result = defaultCase(theEObject);
				return result;
			}
			case ConcretesyntaxPackage.CHOICE: {
				Choice choice = (Choice)theEObject;
				T result = caseChoice(choice);
				if (result == null) result = defaultCase(theEObject);
				return result;
			}
			case ConcretesyntaxPackage.SEQUENCE: {
				Sequence sequence = (Sequence)theEObject;
				T result = caseSequence(sequence);
				if (result == null) result = defaultCase(theEObject);
				return result;
			}
			case ConcretesyntaxPackage.DEFINITION: {
				Definition definition = (Definition)theEObject;
				T result = caseDefinition(definition);
				if (result == null) result = defaultCase(theEObject);
				return result;
			}
			case ConcretesyntaxPackage.CARDINALITY_DEFINITION: {
				CardinalityDefinition cardinalityDefinition = (CardinalityDefinition)theEObject;
				T result = caseCardinalityDefinition(cardinalityDefinition);
				if (result == null) result = caseDefinition(cardinalityDefinition);
				if (result == null) result = defaultCase(theEObject);
				return result;
			}
			case ConcretesyntaxPackage.TERMINAL: {
				Terminal terminal = (Terminal)theEObject;
				T result = caseTerminal(terminal);
				if (result == null) result = caseCardinalityDefinition(terminal);
				if (result == null) result = caseDefinition(terminal);
				if (result == null) result = defaultCase(theEObject);
				return result;
			}
			case ConcretesyntaxPackage.CS_STRING: {
				CsString csString = (CsString)theEObject;
				T result = caseCsString(csString);
				if (result == null) result = caseDefinition(csString);
				if (result == null) result = defaultCase(theEObject);
				return result;
			}
			case ConcretesyntaxPackage.WHITE_SPACES: {
				WhiteSpaces whiteSpaces = (WhiteSpaces)theEObject;
				T result = caseWhiteSpaces(whiteSpaces);
				if (result == null) result = caseDefinition(whiteSpaces);
				if (result == null) result = defaultCase(theEObject);
				return result;
			}
			case ConcretesyntaxPackage.LINE_BREAK: {
				LineBreak lineBreak = (LineBreak)theEObject;
				T result = caseLineBreak(lineBreak);
				if (result == null) result = caseDefinition(lineBreak);
				if (result == null) result = defaultCase(theEObject);
				return result;
			}
			case ConcretesyntaxPackage.CARDINALITY: {
				Cardinality cardinality = (Cardinality)theEObject;
				T result = caseCardinality(cardinality);
				if (result == null) result = defaultCase(theEObject);
				return result;
			}
			case ConcretesyntaxPackage.PLUS: {
				PLUS plus = (PLUS)theEObject;
				T result = casePLUS(plus);
				if (result == null) result = caseCardinality(plus);
				if (result == null) result = defaultCase(theEObject);
				return result;
			}
			case ConcretesyntaxPackage.STAR: {
				STAR star = (STAR)theEObject;
				T result = caseSTAR(star);
				if (result == null) result = caseCardinality(star);
				if (result == null) result = defaultCase(theEObject);
				return result;
			}
			case ConcretesyntaxPackage.QUESTIONMARK: {
				QUESTIONMARK questionmark = (QUESTIONMARK)theEObject;
				T result = caseQUESTIONMARK(questionmark);
				if (result == null) result = caseCardinality(questionmark);
				if (result == null) result = defaultCase(theEObject);
				return result;
			}
			case ConcretesyntaxPackage.COMPOUND_DEFINITION: {
				CompoundDefinition compoundDefinition = (CompoundDefinition)theEObject;
				T result = caseCompoundDefinition(compoundDefinition);
				if (result == null) result = caseCardinalityDefinition(compoundDefinition);
				if (result == null) result = caseDefinition(compoundDefinition);
				if (result == null) result = defaultCase(theEObject);
				return result;
			}
			case ConcretesyntaxPackage.TOKEN_DIRECTIVE: {
				TokenDirective tokenDirective = (TokenDirective)theEObject;
				T result = caseTokenDirective(tokenDirective);
				if (result == null) result = defaultCase(theEObject);
				return result;
			}
			case ConcretesyntaxPackage.REGEX_OWNER: {
				RegexOwner regexOwner = (RegexOwner)theEObject;
				T result = caseRegexOwner(regexOwner);
				if (result == null) result = defaultCase(theEObject);
				return result;
			}
			case ConcretesyntaxPackage.REGEX_PART: {
				RegexPart regexPart = (RegexPart)theEObject;
				T result = caseRegexPart(regexPart);
				if (result == null) result = caseRegexOwner(regexPart);
				if (result == null) result = defaultCase(theEObject);
				return result;
			}
			case ConcretesyntaxPackage.REGEX_COMPOSITE: {
				RegexComposite regexComposite = (RegexComposite)theEObject;
				T result = caseRegexComposite(regexComposite);
				if (result == null) result = caseRegexOwner(regexComposite);
				if (result == null) result = defaultCase(theEObject);
				return result;
			}
			case ConcretesyntaxPackage.ATOMIC_REGEX: {
				AtomicRegex atomicRegex = (AtomicRegex)theEObject;
				T result = caseAtomicRegex(atomicRegex);
				if (result == null) result = caseRegexPart(atomicRegex);
				if (result == null) result = caseRegexOwner(atomicRegex);
				if (result == null) result = defaultCase(theEObject);
				return result;
			}
			case ConcretesyntaxPackage.REGEX_REFERENCE: {
				RegexReference regexReference = (RegexReference)theEObject;
				T result = caseRegexReference(regexReference);
				if (result == null) result = caseRegexPart(regexReference);
				if (result == null) result = caseRegexOwner(regexReference);
				if (result == null) result = defaultCase(theEObject);
				return result;
			}
			case ConcretesyntaxPackage.PARTIAL_TOKEN: {
				PartialToken partialToken = (PartialToken)theEObject;
				T result = casePartialToken(partialToken);
				if (result == null) result = caseTokenDirective(partialToken);
				if (result == null) result = caseRegexComposite(partialToken);
				if (result == null) result = caseRegexOwner(partialToken);
				if (result == null) result = defaultCase(theEObject);
				return result;
			}
			case ConcretesyntaxPackage.TOKEN_DEFINITION: {
				TokenDefinition tokenDefinition = (TokenDefinition)theEObject;
				T result = caseTokenDefinition(tokenDefinition);
				if (result == null) result = caseTokenDirective(tokenDefinition);
				if (result == null) result = caseRegexOwner(tokenDefinition);
				if (result == null) result = defaultCase(theEObject);
				return result;
			}
			case ConcretesyntaxPackage.NORMAL_TOKEN: {
				NormalToken normalToken = (NormalToken)theEObject;
				T result = caseNormalToken(normalToken);
				if (result == null) result = caseTokenDefinition(normalToken);
				if (result == null) result = caseAnnotable(normalToken);
				if (result == null) result = caseRegexComposite(normalToken);
				if (result == null) result = caseTokenDirective(normalToken);
				if (result == null) result = caseRegexOwner(normalToken);
				if (result == null) result = defaultCase(theEObject);
				return result;
			}
			case ConcretesyntaxPackage.QUOTED_TOKEN: {
				QuotedToken quotedToken = (QuotedToken)theEObject;
				T result = caseQuotedToken(quotedToken);
				if (result == null) result = caseTokenDefinition(quotedToken);
				if (result == null) result = caseTokenDirective(quotedToken);
				if (result == null) result = caseRegexOwner(quotedToken);
				if (result == null) result = defaultCase(theEObject);
				return result;
			}
			case ConcretesyntaxPackage.TOKEN_PRIORITY_DIRECTIVE: {
				TokenPriorityDirective tokenPriorityDirective = (TokenPriorityDirective)theEObject;
				T result = caseTokenPriorityDirective(tokenPriorityDirective);
				if (result == null) result = caseTokenDirective(tokenPriorityDirective);
				if (result == null) result = defaultCase(theEObject);
				return result;
			}
			case ConcretesyntaxPackage.CONTAINMENT: {
				Containment containment = (Containment)theEObject;
				T result = caseContainment(containment);
				if (result == null) result = caseTerminal(containment);
				if (result == null) result = caseCardinalityDefinition(containment);
				if (result == null) result = caseDefinition(containment);
				if (result == null) result = defaultCase(theEObject);
				return result;
			}
			case ConcretesyntaxPackage.PLACEHOLDER: {
				Placeholder placeholder = (Placeholder)theEObject;
				T result = casePlaceholder(placeholder);
				if (result == null) result = caseTerminal(placeholder);
				if (result == null) result = caseCardinalityDefinition(placeholder);
				if (result == null) result = caseDefinition(placeholder);
				if (result == null) result = defaultCase(theEObject);
				return result;
			}
			case ConcretesyntaxPackage.PLACEHOLDER_USING_SPECIFIED_TOKEN: {
				PlaceholderUsingSpecifiedToken placeholderUsingSpecifiedToken = (PlaceholderUsingSpecifiedToken)theEObject;
				T result = casePlaceholderUsingSpecifiedToken(placeholderUsingSpecifiedToken);
				if (result == null) result = casePlaceholder(placeholderUsingSpecifiedToken);
				if (result == null) result = caseTerminal(placeholderUsingSpecifiedToken);
				if (result == null) result = caseCardinalityDefinition(placeholderUsingSpecifiedToken);
				if (result == null) result = caseDefinition(placeholderUsingSpecifiedToken);
				if (result == null) result = defaultCase(theEObject);
				return result;
			}
			case ConcretesyntaxPackage.PLACEHOLDER_USING_DEFAULT_TOKEN: {
				PlaceholderUsingDefaultToken placeholderUsingDefaultToken = (PlaceholderUsingDefaultToken)theEObject;
				T result = casePlaceholderUsingDefaultToken(placeholderUsingDefaultToken);
				if (result == null) result = casePlaceholder(placeholderUsingDefaultToken);
				if (result == null) result = caseTerminal(placeholderUsingDefaultToken);
				if (result == null) result = caseCardinalityDefinition(placeholderUsingDefaultToken);
				if (result == null) result = caseDefinition(placeholderUsingDefaultToken);
				if (result == null) result = defaultCase(theEObject);
				return result;
			}
			case ConcretesyntaxPackage.PLACEHOLDER_IN_QUOTES: {
				PlaceholderInQuotes placeholderInQuotes = (PlaceholderInQuotes)theEObject;
				T result = casePlaceholderInQuotes(placeholderInQuotes);
				if (result == null) result = casePlaceholder(placeholderInQuotes);
				if (result == null) result = caseTerminal(placeholderInQuotes);
				if (result == null) result = caseCardinalityDefinition(placeholderInQuotes);
				if (result == null) result = caseDefinition(placeholderInQuotes);
				if (result == null) result = defaultCase(theEObject);
				return result;
			}
			case ConcretesyntaxPackage.OPTION: {
				Option option = (Option)theEObject;
				T result = caseOption(option);
				if (result == null) result = defaultCase(theEObject);
				return result;
			}
			case ConcretesyntaxPackage.ABSTRACT: {
				Abstract abstract_ = (Abstract)theEObject;
				T result = caseAbstract(abstract_);
				if (result == null) result = defaultCase(theEObject);
				return result;
			}
			case ConcretesyntaxPackage.TOKEN_STYLE: {
				TokenStyle tokenStyle = (TokenStyle)theEObject;
				T result = caseTokenStyle(tokenStyle);
				if (result == null) result = defaultCase(theEObject);
				return result;
			}
			case ConcretesyntaxPackage.ANNOTATION: {
				Annotation annotation = (Annotation)theEObject;
				T result = caseAnnotation(annotation);
				if (result == null) result = defaultCase(theEObject);
				return result;
			}
			case ConcretesyntaxPackage.ANNOTABLE: {
				Annotable annotable = (Annotable)theEObject;
				T result = caseAnnotable(annotable);
				if (result == null) result = defaultCase(theEObject);
				return result;
			}
			case ConcretesyntaxPackage.KEY_VALUE_PAIR: {
				KeyValuePair keyValuePair = (KeyValuePair)theEObject;
				T result = caseKeyValuePair(keyValuePair);
				if (result == null) result = defaultCase(theEObject);
				return result;
			}
			default: return defaultCase(theEObject);
		}
	}

	/**
	 * Returns the result of interpreting the object as an instance of '<em>Gen Package Dependent Element</em>'.
	 * <!-- begin-user-doc -->
	 * This implementation returns null;
	 * returning a non-null result will terminate the switch.
	 * <!-- end-user-doc -->
	 * @param object the target of the switch.
	 * @return the result of interpreting the object as an instance of '<em>Gen Package Dependent Element</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
	public T caseGenPackageDependentElement(GenPackageDependentElement object) {
		return null;
	}

	/**
	 * Returns the result of interpreting the object as an instance of '<em>Concrete Syntax</em>'.
	 * <!-- begin-user-doc -->
	 * This implementation returns null;
	 * returning a non-null result will terminate the switch.
	 * <!-- end-user-doc -->
	 * @param object the target of the switch.
	 * @return the result of interpreting the object as an instance of '<em>Concrete Syntax</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
	public T caseConcreteSyntax(ConcreteSyntax object) {
		return null;
	}

	/**
	 * Returns the result of interpreting the object as an instance of '<em>Import</em>'.
	 * <!-- begin-user-doc -->
	 * This implementation returns null;
	 * returning a non-null result will terminate the switch.
	 * <!-- end-user-doc -->
	 * @param object the target of the switch.
	 * @return the result of interpreting the object as an instance of '<em>Import</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
	public T caseImport(Import object) {
		return null;
	}

	/**
	 * Returns the result of interpreting the object as an instance of '<em>Rule</em>'.
	 * <!-- begin-user-doc -->
	 * This implementation returns null;
	 * returning a non-null result will terminate the switch.
	 * <!-- end-user-doc -->
	 * @param object the target of the switch.
	 * @return the result of interpreting the object as an instance of '<em>Rule</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
	public T caseRule(Rule object) {
		return null;
	}

	/**
	 * Returns the result of interpreting the object as an instance of '<em>Choice</em>'.
	 * <!-- begin-user-doc -->
	 * This implementation returns null;
	 * returning a non-null result will terminate the switch.
	 * <!-- end-user-doc -->
	 * @param object the target of the switch.
	 * @return the result of interpreting the object as an instance of '<em>Choice</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
	public T caseChoice(Choice object) {
		return null;
	}

	/**
	 * Returns the result of interpreting the object as an instance of '<em>Sequence</em>'.
	 * <!-- begin-user-doc -->
	 * This implementation returns null;
	 * returning a non-null result will terminate the switch.
	 * <!-- end-user-doc -->
	 * @param object the target of the switch.
	 * @return the result of interpreting the object as an instance of '<em>Sequence</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
	public T caseSequence(Sequence object) {
		return null;
	}

	/**
	 * Returns the result of interpreting the object as an instance of '<em>Definition</em>'.
	 * <!-- begin-user-doc -->
	 * This implementation returns null;
	 * returning a non-null result will terminate the switch.
	 * <!-- end-user-doc -->
	 * @param object the target of the switch.
	 * @return the result of interpreting the object as an instance of '<em>Definition</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
	public T caseDefinition(Definition object) {
		return null;
	}

	/**
	 * Returns the result of interpreting the object as an instance of '<em>Cardinality Definition</em>'.
	 * <!-- begin-user-doc -->
   * This implementation returns null;
   * returning a non-null result will terminate the switch.
   * <!-- end-user-doc -->
	 * @param object the target of the switch.
	 * @return the result of interpreting the object as an instance of '<em>Cardinality Definition</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
  public T caseCardinalityDefinition(CardinalityDefinition object)
  {
		return null;
	}

  /**
	 * Returns the result of interpreting the object as an instance of '<em>Terminal</em>'.
	 * <!-- begin-user-doc -->
	 * This implementation returns null;
	 * returning a non-null result will terminate the switch.
	 * <!-- end-user-doc -->
	 * @param object the target of the switch.
	 * @return the result of interpreting the object as an instance of '<em>Terminal</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
	public T caseTerminal(Terminal object) {
		return null;
	}

	/**
	 * Returns the result of interpreting the object as an instance of '<em>Cs String</em>'.
	 * <!-- begin-user-doc -->
	 * This implementation returns null;
	 * returning a non-null result will terminate the switch.
	 * <!-- end-user-doc -->
	 * @param object the target of the switch.
	 * @return the result of interpreting the object as an instance of '<em>Cs String</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
	public T caseCsString(CsString object) {
		return null;
	}

	/**
	 * Returns the result of interpreting the object as an instance of '<em>White Spaces</em>'.
	 * <!-- begin-user-doc -->
	 * This implementation returns null;
	 * returning a non-null result will terminate the switch.
	 * <!-- end-user-doc -->
	 * @param object the target of the switch.
	 * @return the result of interpreting the object as an instance of '<em>White Spaces</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
	public T caseWhiteSpaces(WhiteSpaces object) {
		return null;
	}

	/**
	 * Returns the result of interpreting the object as an instance of '<em>Line Break</em>'.
	 * <!-- begin-user-doc -->
	 * This implementation returns null;
	 * returning a non-null result will terminate the switch.
	 * <!-- end-user-doc -->
	 * @param object the target of the switch.
	 * @return the result of interpreting the object as an instance of '<em>Line Break</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
	public T caseLineBreak(LineBreak object) {
		return null;
	}

	/**
	 * Returns the result of interpreting the object as an instance of '<em>Cardinality</em>'.
	 * <!-- begin-user-doc -->
	 * This implementation returns null;
	 * returning a non-null result will terminate the switch.
	 * <!-- end-user-doc -->
	 * @param object the target of the switch.
	 * @return the result of interpreting the object as an instance of '<em>Cardinality</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
	public T caseCardinality(Cardinality object) {
		return null;
	}

	/**
	 * Returns the result of interpreting the object as an instance of '<em>PLUS</em>'.
	 * <!-- begin-user-doc -->
	 * This implementation returns null;
	 * returning a non-null result will terminate the switch.
	 * <!-- end-user-doc -->
	 * @param object the target of the switch.
	 * @return the result of interpreting the object as an instance of '<em>PLUS</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
	public T casePLUS(PLUS object) {
		return null;
	}

	/**
	 * Returns the result of interpreting the object as an instance of '<em>STAR</em>'.
	 * <!-- begin-user-doc -->
	 * This implementation returns null;
	 * returning a non-null result will terminate the switch.
	 * <!-- end-user-doc -->
	 * @param object the target of the switch.
	 * @return the result of interpreting the object as an instance of '<em>STAR</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
	public T caseSTAR(STAR object) {
		return null;
	}

	/**
	 * Returns the result of interpreting the object as an instance of '<em>QUESTIONMARK</em>'.
	 * <!-- begin-user-doc -->
	 * This implementation returns null;
	 * returning a non-null result will terminate the switch.
	 * <!-- end-user-doc -->
	 * @param object the target of the switch.
	 * @return the result of interpreting the object as an instance of '<em>QUESTIONMARK</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
	public T caseQUESTIONMARK(QUESTIONMARK object) {
		return null;
	}

	/**
	 * Returns the result of interpreting the object as an instance of '<em>Compound Definition</em>'.
	 * <!-- begin-user-doc -->
	 * This implementation returns null;
	 * returning a non-null result will terminate the switch.
	 * <!-- end-user-doc -->
	 * @param object the target of the switch.
	 * @return the result of interpreting the object as an instance of '<em>Compound Definition</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
	public T caseCompoundDefinition(CompoundDefinition object) {
		return null;
	}

	/**
	 * Returns the result of interpreting the object as an instance of '<em>Token Directive</em>'.
	 * <!-- begin-user-doc -->
	 * This implementation returns null;
	 * returning a non-null result will terminate the switch.
	 * <!-- end-user-doc -->
	 * @param object the target of the switch.
	 * @return the result of interpreting the object as an instance of '<em>Token Directive</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
	public T caseTokenDirective(TokenDirective object) {
		return null;
	}

	/**
	 * Returns the result of interpreting the object as an instance of '<em>Regex Owner</em>'.
	 * <!-- begin-user-doc -->
	 * This implementation returns null;
	 * returning a non-null result will terminate the switch.
	 * <!-- end-user-doc -->
	 * @param object the target of the switch.
	 * @return the result of interpreting the object as an instance of '<em>Regex Owner</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
	public T caseRegexOwner(RegexOwner object) {
		return null;
	}

	/**
	 * Returns the result of interpreting the object as an instance of '<em>Regex Part</em>'.
	 * <!-- begin-user-doc -->
	 * This implementation returns null;
	 * returning a non-null result will terminate the switch.
	 * <!-- end-user-doc -->
	 * @param object the target of the switch.
	 * @return the result of interpreting the object as an instance of '<em>Regex Part</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
	public T caseRegexPart(RegexPart object) {
		return null;
	}

	/**
	 * Returns the result of interpreting the object as an instance of '<em>Regex Composite</em>'.
	 * <!-- begin-user-doc -->
	 * This implementation returns null;
	 * returning a non-null result will terminate the switch.
	 * <!-- end-user-doc -->
	 * @param object the target of the switch.
	 * @return the result of interpreting the object as an instance of '<em>Regex Composite</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
	public T caseRegexComposite(RegexComposite object) {
		return null;
	}

	/**
	 * Returns the result of interpreting the object as an instance of '<em>Atomic Regex</em>'.
	 * <!-- begin-user-doc -->
	 * This implementation returns null;
	 * returning a non-null result will terminate the switch.
	 * <!-- end-user-doc -->
	 * @param object the target of the switch.
	 * @return the result of interpreting the object as an instance of '<em>Atomic Regex</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
	public T caseAtomicRegex(AtomicRegex object) {
		return null;
	}

	/**
	 * Returns the result of interpreting the object as an instance of '<em>Regex Reference</em>'.
	 * <!-- begin-user-doc -->
	 * This implementation returns null;
	 * returning a non-null result will terminate the switch.
	 * <!-- end-user-doc -->
	 * @param object the target of the switch.
	 * @return the result of interpreting the object as an instance of '<em>Regex Reference</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
	public T caseRegexReference(RegexReference object) {
		return null;
	}

	/**
	 * Returns the result of interpreting the object as an instance of '<em>Partial Token</em>'.
	 * <!-- begin-user-doc -->
	 * This implementation returns null;
	 * returning a non-null result will terminate the switch.
	 * <!-- end-user-doc -->
	 * @param object the target of the switch.
	 * @return the result of interpreting the object as an instance of '<em>Partial Token</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
	public T casePartialToken(PartialToken object) {
		return null;
	}

	/**
	 * Returns the result of interpreting the object as an instance of '<em>Token Definition</em>'.
	 * <!-- begin-user-doc -->
	 * This implementation returns null;
	 * returning a non-null result will terminate the switch.
	 * <!-- end-user-doc -->
	 * @param object the target of the switch.
	 * @return the result of interpreting the object as an instance of '<em>Token Definition</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
	public T caseTokenDefinition(TokenDefinition object) {
		return null;
	}

	/**
	 * Returns the result of interpreting the object as an instance of '<em>Normal Token</em>'.
	 * <!-- begin-user-doc -->
	 * This implementation returns null;
	 * returning a non-null result will terminate the switch.
	 * <!-- end-user-doc -->
	 * @param object the target of the switch.
	 * @return the result of interpreting the object as an instance of '<em>Normal Token</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
	public T caseNormalToken(NormalToken object) {
		return null;
	}

	/**
	 * Returns the result of interpreting the object as an instance of '<em>Quoted Token</em>'.
	 * <!-- begin-user-doc -->
	 * This implementation returns null;
	 * returning a non-null result will terminate the switch.
	 * <!-- end-user-doc -->
	 * @param object the target of the switch.
	 * @return the result of interpreting the object as an instance of '<em>Quoted Token</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
	public T caseQuotedToken(QuotedToken object) {
		return null;
	}

	/**
	 * Returns the result of interpreting the object as an instance of '<em>Token Priority Directive</em>'.
	 * <!-- begin-user-doc -->
	 * This implementation returns null;
	 * returning a non-null result will terminate the switch.
	 * <!-- end-user-doc -->
	 * @param object the target of the switch.
	 * @return the result of interpreting the object as an instance of '<em>Token Priority Directive</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
	public T caseTokenPriorityDirective(TokenPriorityDirective object) {
		return null;
	}

	/**
	 * Returns the result of interpreting the object as an instance of '<em>Containment</em>'.
	 * <!-- begin-user-doc -->
	 * This implementation returns null;
	 * returning a non-null result will terminate the switch.
	 * <!-- end-user-doc -->
	 * @param object the target of the switch.
	 * @return the result of interpreting the object as an instance of '<em>Containment</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
	public T caseContainment(Containment object) {
		return null;
	}

	/**
	 * Returns the result of interpreting the object as an instance of '<em>Placeholder</em>'.
	 * <!-- begin-user-doc -->
	 * This implementation returns null;
	 * returning a non-null result will terminate the switch.
	 * <!-- end-user-doc -->
	 * @param object the target of the switch.
	 * @return the result of interpreting the object as an instance of '<em>Placeholder</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
	public T casePlaceholder(Placeholder object) {
		return null;
	}

	/**
	 * Returns the result of interpreting the object as an instance of '<em>Placeholder Using Specified Token</em>'.
	 * <!-- begin-user-doc -->
	 * This implementation returns null;
	 * returning a non-null result will terminate the switch.
	 * <!-- end-user-doc -->
	 * @param object the target of the switch.
	 * @return the result of interpreting the object as an instance of '<em>Placeholder Using Specified Token</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
	public T casePlaceholderUsingSpecifiedToken(PlaceholderUsingSpecifiedToken object) {
		return null;
	}

	/**
	 * Returns the result of interpreting the object as an instance of '<em>Placeholder Using Default Token</em>'.
	 * <!-- begin-user-doc -->
	 * This implementation returns null;
	 * returning a non-null result will terminate the switch.
	 * <!-- end-user-doc -->
	 * @param object the target of the switch.
	 * @return the result of interpreting the object as an instance of '<em>Placeholder Using Default Token</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
	public T casePlaceholderUsingDefaultToken(PlaceholderUsingDefaultToken object) {
		return null;
	}

	/**
	 * Returns the result of interpreting the object as an instance of '<em>Placeholder In Quotes</em>'.
	 * <!-- begin-user-doc -->
	 * This implementation returns null;
	 * returning a non-null result will terminate the switch.
	 * <!-- end-user-doc -->
	 * @param object the target of the switch.
	 * @return the result of interpreting the object as an instance of '<em>Placeholder In Quotes</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
	public T casePlaceholderInQuotes(PlaceholderInQuotes object) {
		return null;
	}

	/**
	 * Returns the result of interpreting the object as an instance of '<em>Option</em>'.
	 * <!-- begin-user-doc -->
	 * This implementation returns null;
	 * returning a non-null result will terminate the switch.
	 * <!-- end-user-doc -->
	 * @param object the target of the switch.
	 * @return the result of interpreting the object as an instance of '<em>Option</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
	public T caseOption(Option object) {
		return null;
	}

	/**
	 * Returns the result of interpreting the object as an instance of '<em>Abstract</em>'.
	 * <!-- begin-user-doc -->
	 * This implementation returns null;
	 * returning a non-null result will terminate the switch.
	 * <!-- end-user-doc -->
	 * @param object the target of the switch.
	 * @return the result of interpreting the object as an instance of '<em>Abstract</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
	public T caseAbstract(Abstract object) {
		return null;
	}

	/**
	 * Returns the result of interpreting the object as an instance of '<em>Token Style</em>'.
	 * <!-- begin-user-doc -->
	 * This implementation returns null;
	 * returning a non-null result will terminate the switch.
	 * <!-- end-user-doc -->
	 * @param object the target of the switch.
	 * @return the result of interpreting the object as an instance of '<em>Token Style</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
	public T caseTokenStyle(TokenStyle object) {
		return null;
	}

	/**
	 * Returns the result of interpreting the object as an instance of '<em>Annotation</em>'.
	 * <!-- begin-user-doc -->
	 * This implementation returns null;
	 * returning a non-null result will terminate the switch.
	 * <!-- end-user-doc -->
	 * @param object the target of the switch.
	 * @return the result of interpreting the object as an instance of '<em>Annotation</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
	public T caseAnnotation(Annotation object) {
		return null;
	}

	/**
	 * Returns the result of interpreting the object as an instance of '<em>Annotable</em>'.
	 * <!-- begin-user-doc -->
	 * This implementation returns null;
	 * returning a non-null result will terminate the switch.
	 * <!-- end-user-doc -->
	 * @param object the target of the switch.
	 * @return the result of interpreting the object as an instance of '<em>Annotable</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
	public T caseAnnotable(Annotable object) {
		return null;
	}

	/**
	 * Returns the result of interpreting the object as an instance of '<em>Key Value Pair</em>'.
	 * <!-- begin-user-doc -->
	 * This implementation returns null;
	 * returning a non-null result will terminate the switch.
	 * <!-- end-user-doc -->
	 * @param object the target of the switch.
	 * @return the result of interpreting the object as an instance of '<em>Key Value Pair</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
	public T caseKeyValuePair(KeyValuePair object) {
		return null;
	}

	/**
	 * Returns the result of interpreting the object as an instance of '<em>EObject</em>'.
	 * <!-- begin-user-doc -->
	 * This implementation returns null;
	 * returning a non-null result will terminate the switch, but this is the last case anyway.
	 * <!-- end-user-doc -->
	 * @param object the target of the switch.
	 * @return the result of interpreting the object as an instance of '<em>EObject</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject)
	 * @generated
	 */
	public T defaultCase(EObject object) {
		return null;
	}

} //ConcretesyntaxSwitch
