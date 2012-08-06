/*******************************************************************************
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
 ******************************************************************************/
package org.jamopp.junit3;

import static org.junit.Assert.*;

public class Test2 {

	protected boolean calledSetUp = false;
	@ org.junit. Before

	public void setUp() {
		calledSetUp = true;
	}
	@ org.junit. Test

	public void test01() {
	assertTrue(calledSetUp);
	}
}