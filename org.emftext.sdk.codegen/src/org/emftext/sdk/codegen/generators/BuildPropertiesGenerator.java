package org.emftext.sdk.codegen.generators;

import java.io.PrintWriter;
import java.util.Collection;
import java.util.LinkedHashSet;

import org.emftext.sdk.EPlugins;
import org.emftext.sdk.codegen.GenerationContext;
import org.emftext.sdk.codegen.IGenerator;
import org.emftext.sdk.codegen.OptionManager;
import org.emftext.sdk.concretesyntax.OptionTypes;
import org.emftext.sdk.util.StringUtil;

public class BuildPropertiesGenerator extends BaseGenerator {

	private EPlugins plugin;

	public BuildPropertiesGenerator(GenerationContext context, EPlugins plugin) {
		super(context, "", "build.properties");
		this.plugin = plugin;
	}

	@Override
	public boolean generate(PrintWriter out) {
		// TODO replace the (duplicate) code below with calls to GenerationContext
		String sourceOptionValue = OptionManager.INSTANCE.getStringOptionValue(getContext().getConcreteSyntax(), OptionTypes.SOURCE_FOLDER);
		String sourceFolder;
		if (sourceOptionValue == null) {
			sourceFolder = "src";
		} else {
			sourceFolder = sourceOptionValue;
		}
		
		String genSourceOptionValue = OptionManager.INSTANCE.getStringOptionValue(getContext().getConcreteSyntax(), OptionTypes.SOURCE_FOLDER);
		String genSourceFolder;
		if (sourceOptionValue == null) {
			genSourceFolder = "src-gen";
		} else {
			genSourceFolder = genSourceOptionValue;
		}
		
		StringBuilder sc = new StringBuilder();
		
		Collection<String> binIncludes = getBinIncludes();
		sc.append("bin.includes = " + StringUtil.explode(binIncludes, ",\\\n"));
		sc.append("\n");
		sc.append("source.. = " + sourceFolder + "/");
		// only the resource plug-in has a 'src-gen' folder
		if (plugin == EPlugins.RESOURCE_PLUGIN) {
			sc.append("," + genSourceFolder + "/");
		}
		sc.append("\n");

		out.write(sc.toString());
		return true;
	}

	private Collection<String> getBinIncludes() {
		Collection<String> binIncludes = new LinkedHashSet<String>();
		binIncludes.add("META-INF/");
		binIncludes.add(".");
		if (plugin == EPlugins.RESOURCE_PLUGIN) {
			binIncludes.add("icons/");
			binIncludes.add("css/");
			binIncludes.add("plugin.xml");
		}
		return binIncludes;
	}

	public IGenerator newInstance(GenerationContext context) {
		throw new UnsupportedOperationException();
	}
}
