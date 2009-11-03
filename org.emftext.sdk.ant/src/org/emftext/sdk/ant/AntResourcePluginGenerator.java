/*******************************************************************************
 * Copyright (c) 2006-2009 
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
package org.emftext.sdk.ant;

import org.eclipse.core.runtime.SubMonitor;
import org.emftext.sdk.EPlugins;
import org.emftext.sdk.codegen.GenerationContext;
import org.emftext.sdk.codegen.creators.PluginsCreator;

/**
 * This class implements an ResourcePluginGenerator that does NOT create
 * the Eclipse project in the current workspace. ANT task can't do so,
 * because no running Eclipse platform is available. However, this generator
 * creates all artifacts of the text resource plug-in. So the plug-in is
 * created, but not registered with the current Eclipse workspace.
 */
public class AntResourcePluginGenerator extends PluginsCreator {

	@Override
	public void createProject(GenerationContext context, SubMonitor progress, EPlugins plugin) {
	}
}
