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
package org.emftext.sdk.codegen.creators;

import java.io.File;
import java.util.ArrayList;
import java.util.Collection;

import org.emftext.sdk.codegen.EArtifact;
import org.emftext.sdk.codegen.GenerationContext;
import org.emftext.sdk.codegen.IGenerator;
import org.emftext.sdk.codegen.OptionManager;
import org.emftext.sdk.codegen.generators.TextPrinterBaseGenerator;
import org.emftext.sdk.concretesyntax.OptionTypes;

/**
 * Creates the Java file for the printer base class using the content
 * provided by TextPrinterBaseGenerator.
 */
public class PrinterBaseCreator extends AbstractArtifactCreator {

	public PrinterBaseCreator() {
		super("printer base class");
	}

	@Override
	public Collection<IArtifact> getArtifactsToCreate(GenerationContext context) {
		boolean generatePrinterStubOnly = OptionManager.INSTANCE.getBooleanOptionValue(context.getConcreteSyntax(), OptionTypes.GENERATE_PRINTER_STUB_ONLY);

		if (generatePrinterStubOnly) {
			return new ArrayList<IArtifact>();
		} else {
		    File printerBaseFile = context.getFile(EArtifact.PRINTER_BASE);
	        IGenerator printerBaseGenerator = new TextPrinterBaseGenerator().newInstance(context);
	        
		    return createArtifact(
		    		context,
		    		printerBaseGenerator,
		    		printerBaseFile,
		    		"Exception while generating printer base class."
		    );
    	}
	}

	public OptionTypes getOverrideOption() {
		return OptionTypes.OVERRIDE_PRINTER_BASE;
	}
}