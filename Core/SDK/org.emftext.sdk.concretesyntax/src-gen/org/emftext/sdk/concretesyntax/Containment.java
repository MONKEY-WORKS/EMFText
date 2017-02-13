/**
 * Copyright (c) 2006-2012
 * Software Technology Group, Dresden University of Technology
 * DevBoost GmbH, Berlin, Amtsgericht Charlottenburg, HRB 140026
 * 
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *   Software Technology Group - TU Dresden, Germany;
 *   DevBoost GmbH - Berlin, Germany
 *      - initial API and implementation
 *  
 */
package org.emftext.sdk.concretesyntax;

import org.eclipse.emf.codegen.ecore.genmodel.GenClass;
import org.eclipse.emf.common.util.EList;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Containment</b></em>'.
 * <!-- end-user-doc -->
 *
 * <!-- begin-model-doc -->
 * Calls the syntax rules of the type of the containment reference.
 * <!-- end-model-doc -->
 *
 * <p>
 * The following features are supported:
 * </p>
 * <ul>
 *   <li>{@link org.emftext.sdk.concretesyntax.Containment#getTypes <em>Types</em>}</li>
 * </ul>
 *
 * @see org.emftext.sdk.concretesyntax.ConcretesyntaxPackage#getContainment()
 * @model
 * @generated
 */
public interface Containment extends Terminal {
	/**
	 * Returns the value of the '<em><b>Types</b></em>' reference list.
	 * The list contents are of type {@link org.eclipse.emf.codegen.ecore.genmodel.GenClass}.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Types</em>' reference list isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Types</em>' reference list.
	 * @see org.emftext.sdk.concretesyntax.ConcretesyntaxPackage#getContainment_Types()
	 * @model
	 * @generated
	 */
	EList<GenClass> getTypes();

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * <!-- begin-model-doc -->
	 * 
	 *  Returns all types that are allowed for the given containment.
	 *  If type restrictions are specified in the syntax rule, this
	 *  list contains the allowed types. If no restriction are present
	 *  the type of the feature references by the containment is 
	 *  returned.
	 *  
	 *  @param containment
	 *  @return
	 * 
	 * <!-- end-model-doc -->
	 * @model kind="operation"
	 *        annotation="http://www.eclipse.org/emf/2002/GenModel body='org.eclipse.emf.common.util.EList< org.eclipse.emf.codegen.ecore.genmodel.GenClass> types;\n// is there an explicit type defined?\nif (!getTypes().isEmpty()) {\n\ttypes = getTypes();\n} else {\n\ttypes = new org.eclipse.emf.common.util.BasicEList< org.eclipse.emf.codegen.ecore.genmodel.GenClass>();\n\ttypes.add(getFeature().getTypeGenClass());\n}\nreturn types;'"
	 * @generated
	 */
	EList<GenClass> getAllowedSubTypes();

} // Containment
