package org.emftext.runtime;

import java.util.HashMap;
import java.util.Map;

import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.core.runtime.IExtensionPoint;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Platform;
import org.eclipse.core.runtime.Plugin;
import org.eclipse.core.runtime.Status;
import org.eclipse.emf.common.util.URI;
import org.osgi.framework.BundleContext;

/**
 * The activator class controls the EMFText runtime plug-in life cycle
 * 
 * @author Jendrik Johannes <jendrik.johannes@tu-dresden.de>
 */
public class EMFTextPlugin extends Plugin {
	
	public static final String PLUGIN_ID = "org.reuseware.ecoretextedit.runtime";
	public static final String EP_CONCRETESYNTAX_ID = "org.emftext.runtime.concretesyntax";
	public static final String EP_DEFAULT_LOAD_OPTIONS_ID = "org.emftext.runtime.default_load_options";
	
	private static EMFTextPlugin plugin;
	
	/**
	 * The constructor
	 */
	public EMFTextPlugin() {
	}

	public void start(BundleContext context) throws Exception {
		super.start(context);
		plugin = this;
	}

	public void stop(BundleContext context) throws Exception {
		plugin = null;
		super.stop(context);
	}

	/**
	 * Returns the shared instance
	 *
	 * @return the shared instance
	 */
	public static EMFTextPlugin getDefault() {
		return plugin;
	}
	
	private static Map<String, URI> URIToConcreteSyntaxLocationMap = null;
	
	/**
	 * Returns the concrete syntax models that are registered through generated plugins. 
	 * The returned maps contains keys that have the format: 
	 * 
	 * <ul>
	 *   <li> <i>MetmaodelURI</i>%%<i>ConcreteSyntaxName</i>
	 * </ul>
	 * 
	 * @return The map. 
	 */
	public static Map<String, URI> getURIToConcreteSyntaxLocationMap() {
		if (URIToConcreteSyntaxLocationMap == null) {
			URIToConcreteSyntaxLocationMap = new HashMap<String, URI>();
			
	        IExtensionPoint csExtensionPoint = Platform.getExtensionRegistry().getExtensionPoint(EP_CONCRETESYNTAX_ID);
	        IConfigurationElement[] parserPoints = csExtensionPoint.getConfigurationElements();
	        for(int i = 0;i < parserPoints.length;i++) {
	        	String uri       = parserPoints[i].getAttribute("uri");
	            String csName    = parserPoints[i].getAttribute("csName");
	            String file      = parserPoints[i].getAttribute("csDefinition");
	            URI fileURI = URI.createPlatformPluginURI("/" + file, true);
	            
	            URIToConcreteSyntaxLocationMap.put(uri + "%%" + csName, fileURI);
	        }
			
		}
		return URIToConcreteSyntaxLocationMap;
	}
	
	/**
	 * Helper method for error logging.
	 * 
	 * @param message
	 *            the error message
	 * @param exception
	 *            the exception that describes the error in detail
	 * @return the status object describing the error
	 */
	public static IStatus logError(String message, Throwable exception) {
		IStatus status = new Status(IStatus.ERROR, EMFTextPlugin.PLUGIN_ID,
				0, message, exception);
		final EMFTextPlugin pluginInstance = EMFTextPlugin.getDefault();
		if (pluginInstance == null) {
			exception.printStackTrace();
		} else {
			pluginInstance.getLog().log(status);
		}
		return status;
	}

}
