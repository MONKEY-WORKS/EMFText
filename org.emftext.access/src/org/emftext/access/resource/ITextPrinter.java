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
package org.emftext.access.resource;

import java.io.IOException;

import org.eclipse.emf.ecore.EObject;

/**
 * Converts a tree of <code>EObject</code>s into a plain text.
 */
public interface ITextPrinter extends IConfigurable {

	/**
	 * Prints the given <code>EObject</code> and its content to some 
	 * underlying output stream.
	 * 
	 * @param element The element to print.
	 * @throws IOException if printing to an underlying stream or device fails. 
	 */
    public void print(EObject element) throws IOException;
}