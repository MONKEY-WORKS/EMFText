/**
 * <copyright>
 * </copyright>
 *
 * $Id$
 */
package org.reuseware.emftextedit.concretesyntax.impl;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EPackage;

import org.eclipse.emf.ecore.impl.EFactoryImpl;

import org.eclipse.emf.ecore.plugin.EcorePlugin;

import org.reuseware.emftextedit.concretesyntax.*;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model <b>Factory</b>.
 * <!-- end-user-doc -->
 * @generated
 */
public class ConcretesyntaxFactoryImpl extends EFactoryImpl implements ConcretesyntaxFactory {
	/**
	 * Creates the default factory implementation.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public static ConcretesyntaxFactory init() {
		try {
			ConcretesyntaxFactory theConcretesyntaxFactory = (ConcretesyntaxFactory)EPackage.Registry.INSTANCE.getEFactory("http://www.reuseware.org/emftextedit/concretesyntax"); 
			if (theConcretesyntaxFactory != null) {
				return theConcretesyntaxFactory;
			}
		}
		catch (Exception exception) {
			EcorePlugin.INSTANCE.log(exception);
		}
		return new ConcretesyntaxFactoryImpl();
	}

	/**
	 * Creates an instance of the factory.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public ConcretesyntaxFactoryImpl() {
		super();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EObject create(EClass eClass) {
		switch (eClass.getClassifierID()) {
			case ConcretesyntaxPackage.CONCRETE_SYNTAX: return createConcreteSyntax();
			case ConcretesyntaxPackage.IMPORT: return createImport();
			case ConcretesyntaxPackage.RULE: return createRule();
			case ConcretesyntaxPackage.CHOICE: return createChoice();
			case ConcretesyntaxPackage.SEQUENCE: return createSequence();
			case ConcretesyntaxPackage.CS_STRING: return createCsString();
			case ConcretesyntaxPackage.WHITE_SPACES: return createWhiteSpaces();
			case ConcretesyntaxPackage.LINE_BREAK: return createLineBreak();
			case ConcretesyntaxPackage.PLUS: return createPLUS();
			case ConcretesyntaxPackage.STAR: return createSTAR();
			case ConcretesyntaxPackage.QUESTIONMARK: return createQUESTIONMARK();
			case ConcretesyntaxPackage.COMPOUND_DEFINITION: return createCompoundDefinition();
			case ConcretesyntaxPackage.NORMAL_TOKEN: return createNormalToken();
			case ConcretesyntaxPackage.DECORATED_TOKEN: return createDecoratedToken();
			case ConcretesyntaxPackage.PRE_DEFINED_TOKEN: return createPreDefinedToken();
			case ConcretesyntaxPackage.CONTAINMENT: return createContainment();
			case ConcretesyntaxPackage.DEFINED_PLACEHOLDER: return createDefinedPlaceholder();
			case ConcretesyntaxPackage.DERIVED_PLACEHOLDER: return createDerivedPlaceholder();
			case ConcretesyntaxPackage.OPTION: return createOption();
			default:
				throw new IllegalArgumentException("The class '" + eClass.getName() + "' is not a valid classifier");
		}
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public ConcreteSyntax createConcreteSyntax() {
		ConcreteSyntaxImpl concreteSyntax = new ConcreteSyntaxImpl();
		return concreteSyntax;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public Import createImport() {
		ImportImpl import_ = new ImportImpl();
		return import_;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public Rule createRule() {
		RuleImpl rule = new RuleImpl();
		return rule;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public Choice createChoice() {
		ChoiceImpl choice = new ChoiceImpl();
		return choice;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public Sequence createSequence() {
		SequenceImpl sequence = new SequenceImpl();
		return sequence;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public CsString createCsString() {
		CsStringImpl csString = new CsStringImpl();
		return csString;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public WhiteSpaces createWhiteSpaces() {
		WhiteSpacesImpl whiteSpaces = new WhiteSpacesImpl();
		return whiteSpaces;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public LineBreak createLineBreak() {
		LineBreakImpl lineBreak = new LineBreakImpl();
		return lineBreak;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public PLUS createPLUS() {
		PLUSImpl plus = new PLUSImpl();
		return plus;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public STAR createSTAR() {
		STARImpl star = new STARImpl();
		return star;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public QUESTIONMARK createQUESTIONMARK() {
		QUESTIONMARKImpl questionmark = new QUESTIONMARKImpl();
		return questionmark;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public CompoundDefinition createCompoundDefinition() {
		CompoundDefinitionImpl compoundDefinition = new CompoundDefinitionImpl();
		return compoundDefinition;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public NormalToken createNormalToken() {
		NormalTokenImpl normalToken = new NormalTokenImpl();
		return normalToken;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public DecoratedToken createDecoratedToken() {
		DecoratedTokenImpl decoratedToken = new DecoratedTokenImpl();
		return decoratedToken;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public PreDefinedToken createPreDefinedToken() {
		PreDefinedTokenImpl preDefinedToken = new PreDefinedTokenImpl();
		return preDefinedToken;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public Containment createContainment() {
		ContainmentImpl containment = new ContainmentImpl();
		return containment;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public DefinedPlaceholder createDefinedPlaceholder() {
		DefinedPlaceholderImpl definedPlaceholder = new DefinedPlaceholderImpl();
		return definedPlaceholder;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public DerivedPlaceholder createDerivedPlaceholder() {
		DerivedPlaceholderImpl derivedPlaceholder = new DerivedPlaceholderImpl();
		return derivedPlaceholder;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public Option createOption() {
		OptionImpl option = new OptionImpl();
		return option;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public ConcretesyntaxPackage getConcretesyntaxPackage() {
		return (ConcretesyntaxPackage)getEPackage();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @deprecated
	 * @generated
	 */
	@Deprecated
	public static ConcretesyntaxPackage getPackage() {
		return ConcretesyntaxPackage.eINSTANCE;
	}

} //ConcretesyntaxFactoryImpl
