/**
 * Copyright (c) 2006-2010 
 * Software Technology Group, Dresden University of Technology
 * 
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0 
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 * Software Technology Group - TU Dresden, Germany 
 *       - initial API and implementation
 * 
 *
 * $Id$
 */
package org.emftext.sdk.concretesyntax;



/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Compound Definition</b></em>'.
 * <!-- end-user-doc -->
 *
 *
 * @see org.emftext.sdk.concretesyntax.ConcretesyntaxPackage#getCompoundDefinition()
 * @model
 * @generated
 */
public interface CompoundDefinition extends CardinalityDefinition {
	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * <!-- begin-model-doc -->
	 * 
	 * <!-- end-model-doc -->
	 * @model kind="operation"
	 *        annotation="http://www.eclipse.org/emf/2002/GenModel body='org.eclipse.emf.common.util.EList < org.emftext.sdk.concretesyntax.SyntaxElement > children = getChildren ( ) ; \r\n// there should be at most one child\nassert children == null || children .size ( ) == 1 ; \r\nif ( children .size ( ) > 0 ) { \r\n\torg.emftext.sdk.concretesyntax.SyntaxElement firstChild = children .get ( 0 ) ; \r\n\tif ( firstChild instanceof org.emftext.sdk.concretesyntax.Choice ) { \r\n\t\treturn ( org.emftext.sdk.concretesyntax.Choice ) firstChild ; \r\n\t} else { \r\n\t\t// there should be no element other than Choice\nassert false ; \r\n\t\treturn null ; \r\n\t} \r\n} \r\nreturn null ; \r\n'"
	 * @generated
	 */
	Choice getDefinition();

} // CompoundDefinition
