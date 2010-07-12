package org.emftext.sdk.codegen.antlr;

import java.io.File;

import org.emftext.sdk.IPluginDescriptor;
import org.emftext.sdk.codegen.AbstractGenerationContext;
import org.emftext.sdk.codegen.ArtifactDescriptor;
import org.emftext.sdk.codegen.IFileSystemConnector;
import org.emftext.sdk.codegen.IProblemCollector;
import org.emftext.sdk.codegen.ISyntaxContext;
import org.emftext.sdk.concretesyntax.ConcreteSyntax;

/**
 * This class is passed along the creators and generators of the ANTLR commons
 * plug-in. It carries a concrete syntax object, which is used to determine the
 * value of override options.
 */
public class ANTLRGenerationContext extends AbstractGenerationContext<ANTLRGenerationContext> implements ISyntaxContext {

	private ConcreteSyntax concreteSyntax;
	private IPluginDescriptor antlrPlugin;

	public ANTLRGenerationContext(IFileSystemConnector fileSystemConnector, IProblemCollector problemCollector, ConcreteSyntax concreteSyntax, IPluginDescriptor antlrPlugin) {
		super(fileSystemConnector, problemCollector);
		this.concreteSyntax = concreteSyntax;
		this.antlrPlugin = antlrPlugin;
	}

	public File getFile(IPluginDescriptor plugin, ArtifactDescriptor<ANTLRGenerationContext, ?> artifact) {
		throw new UnsupportedOperationException();
	}

	public File getProjectFolder(IPluginDescriptor plugin) {
		return getFileSystemConnector().getProjectFolder(plugin);
	}

	public ConcreteSyntax getConcreteSyntax() {
		return concreteSyntax;
	}

	public IPluginDescriptor getAntlrPlugin() {
		return antlrPlugin;
	}
}
