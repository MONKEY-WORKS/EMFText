package org.emftext.sdk.codegen.resource.generators.util;

import static org.emftext.sdk.codegen.composites.IClassNameConstants.ARRAY_LIST;
import static org.emftext.sdk.codegen.composites.IClassNameConstants.LIST;
import static org.emftext.sdk.codegen.resource.generators.IClassNameConstants.COLLECTION;
import static org.emftext.sdk.codegen.resource.generators.IClassNameConstants.CONSTRAINT_STATUS;
import static org.emftext.sdk.codegen.resource.generators.IClassNameConstants.CORE_EXCEPTION;
import static org.emftext.sdk.codegen.resource.generators.IClassNameConstants.EMF_MODEL_VALIDATION_PLUGIN;
import static org.emftext.sdk.codegen.resource.generators.IClassNameConstants.EVALUATION_MODE;
import static org.emftext.sdk.codegen.resource.generators.IClassNameConstants.E_OBJECT;
import static org.emftext.sdk.codegen.resource.generators.IClassNameConstants.I_BATCH_VALIDATOR;
import static org.emftext.sdk.codegen.resource.generators.IClassNameConstants.I_CONFIGURATION_ELEMENT;
import static org.emftext.sdk.codegen.resource.generators.IClassNameConstants.I_EXTENSION_REGISTRY;
import static org.emftext.sdk.codegen.resource.generators.IClassNameConstants.I_FILE;
import static org.emftext.sdk.codegen.resource.generators.IClassNameConstants.I_STATUS;
import static org.emftext.sdk.codegen.resource.generators.IClassNameConstants.MAP;
import static org.emftext.sdk.codegen.resource.generators.IClassNameConstants.MODEL_VALIDATION_SERVICE;
import static org.emftext.sdk.codegen.resource.generators.IClassNameConstants.PLATFORM;
import static org.emftext.sdk.codegen.resource.generators.IClassNameConstants.RESOURCE;
import static org.emftext.sdk.codegen.resource.generators.IClassNameConstants.RESOURCE_FACTORY;
import static org.emftext.sdk.codegen.resource.generators.IClassNameConstants.RESOURCE_SET;
import static org.emftext.sdk.codegen.resource.generators.IClassNameConstants.RESOURCE_SET_IMPL;
import static org.emftext.sdk.codegen.resource.generators.IClassNameConstants.SET;
import static org.emftext.sdk.codegen.resource.generators.IClassNameConstants.URI;

import org.emftext.sdk.OptionManager;
import org.emftext.sdk.codegen.annotations.SyntaxDependent;
import org.emftext.sdk.codegen.composites.JavaComposite;
import org.emftext.sdk.codegen.parameters.ArtifactParameter;
import org.emftext.sdk.codegen.resource.GenerationContext;
import org.emftext.sdk.codegen.resource.generators.JavaBaseGenerator;
import org.emftext.sdk.concretesyntax.OptionTypes;

@SyntaxDependent
public class EclipseProxyGenerator extends JavaBaseGenerator<ArtifactParameter<GenerationContext>> {

	@Override
	public void generateJavaContents(JavaComposite sc) {
		
		sc.add("package " + getResourcePackageName() + ";");
		sc.addLineBreak();
		
		sc.addJavadoc(
			"A utility class that bundles all dependencies to the Eclipse platform. " +
			"Clients of this class must check whether the Eclipse bundles are available " +
			"in the classpath. If they are not available, this class is not used, which " +
			"allows to run resource plug-in that are generated by EMFText in stand-alone mode. " +
			"In this case the EMF JARs are sufficient to parse and print resources."
		);
		sc.add("public class " + getResourceClassName() + " {");
		sc.addLineBreak();
		OptionTypes option = OptionTypes.REMOVE_ECLIPSE_DEPENDENT_CODE;
		boolean removeEclipseDependentCode = OptionManager.INSTANCE.getBooleanOptionValue(getContext().getConcreteSyntax(), option);
		if (!removeEclipseDependentCode) {
			addMethods(sc);
		} else {
			sc.addComment("This class is intentionally left empty because option '" + option.getLiteral() + "' is set to true.");
		}
		sc.add("}");
	}

	private void addMethods(JavaComposite sc) {
		addGetDefaultLoadOptionProviderExtensions(sc);
		addGetResourceFactoryExtensions(sc);
		addGetResourceMethod(sc);
		addCheckEMFValidationConstraints(sc);
		addAddStatusMethod(sc);
	}

	private void addGetResourceFactoryExtensions(JavaComposite sc) {
		sc.addJavadoc(
			"Adds all registered resource factory extensions to the given map. " +
			"Such extensions can be used to register multiple resource factories " +
			"for the same file extension."
		);
    	sc.add("public void getResourceFactoryExtensions(" + MAP + "<String, " + RESOURCE_FACTORY + "> factories) {");
     	sc.add("if (" + PLATFORM + ".isRunning()) {");
    	sc.add(I_EXTENSION_REGISTRY + " extensionRegistry = " + PLATFORM + ".getExtensionRegistry();");
    	sc.add(I_CONFIGURATION_ELEMENT + " configurationElements[] = extensionRegistry.getConfigurationElementsFor(" + pluginActivatorClassName + ".EP_ADDITIONAL_EXTENSION_PARSER_ID);");
    	sc.add("for (" + I_CONFIGURATION_ELEMENT + " element : configurationElements) {");
    	sc.add("try {");
    	sc.add("String type = element.getAttribute(\"type\");");
    	sc.add(RESOURCE +".Factory factory = (" + RESOURCE + ".Factory) element.createExecutableExtension(\"class\");");
    	sc.add("if (type == null) {");
    	sc.add("type = \"\";");
    	sc.add("}");
    	sc.add(RESOURCE + ".Factory otherFactory = factories.get(type);");
		sc.add("if (otherFactory != null) {");
		sc.add("Class<?> superClass = factory.getClass().getSuperclass();");
		sc.add("while(superClass != Object.class) {");
		sc.add("if (superClass.equals(otherFactory.getClass())) {");
		sc.add("factories.put(type, factory);");
		sc.add("break;");
		sc.add("}");
		sc.add("superClass = superClass.getClass();");
		sc.add("}");
		sc.add("}");
		sc.add("else {");
		sc.add("factories.put(type, factory);");
		sc.add("}");
    	sc.add("} catch (" + CORE_EXCEPTION + " ce) {");
    	sc.add("new " + runtimeUtilClassName + "().logError(\"Exception while getting default options.\", ce);");
    	sc.add("}");
    	sc.add("}");
    	sc.add("}");
    	sc.add("}");
    	sc.addLineBreak();
	}

	private void addGetDefaultLoadOptionProviderExtensions(JavaComposite sc) {
		sc.addJavadoc(
			"Adds all registered load option provider extension to the given map. " +
			"Load option providers can be used to set default options for loading resources " +
			"(e.g. input stream pre-processors)."
		);
		sc.add("public void getDefaultLoadOptionProviderExtensions(" + MAP + "<Object, Object> optionsMap) {");
		sc.add("if (" + PLATFORM + ".isRunning()) {");
		sc.addComment("find default load option providers");
		sc.add(I_EXTENSION_REGISTRY + " extensionRegistry = " + PLATFORM
				+ ".getExtensionRegistry();");
		sc.add(I_CONFIGURATION_ELEMENT
				+ " configurationElements[] = extensionRegistry.getConfigurationElementsFor("
				+ pluginActivatorClassName + ".EP_DEFAULT_LOAD_OPTIONS_ID);");
		sc.add("for (" + I_CONFIGURATION_ELEMENT
				+ " element : configurationElements) {");
		sc.add("try {");
		sc.add(iOptionProviderClassName + " provider = ("
				+ iOptionProviderClassName
				+ ") element.createExecutableExtension(\"class\");");
		sc.add("final " + MAP + "<?, ?> options = provider.getOptions();");
		sc.add("final " + COLLECTION + "<?> keys = options.keySet();");
		sc.add("for (Object key : keys) {");
		sc.add(mapUtilClassName +  ".putAndMergeKeys(optionsMap, key, options.get(key));");
		sc.add("}");
		sc.add("} catch (" + CORE_EXCEPTION + " ce) {");
		sc.add("new " + runtimeUtilClassName + "().logError(\"Exception while getting default options.\", ce);");
		sc.add("}");
		sc.add("}");
		sc.add("}");
		sc.add("}");
		sc.addLineBreak();
	}

	private void addGetResourceMethod(JavaComposite sc) {
		sc.addJavadoc("Gets the resource that is contained in the give file.");
		sc.add("public " + textResourceClassName + " getResource(" + I_FILE + " file) {");
		sc.add(RESOURCE_SET + " rs = new " + RESOURCE_SET_IMPL + "();");
		sc.add(RESOURCE + " resource = rs.getResource(" + URI + ".createPlatformResourceURI(file.getFullPath().toString(),true), true);");
		sc.add("return (" + textResourceClassName + ") resource;");
		sc.add("}");
		sc.addLineBreak();
	}

	private void addCheckEMFValidationConstraints(JavaComposite sc) {
		sc.addJavadoc(
			"Checks all registered EMF validation constraints. " +
			"Note: EMF validation does not work if OSGi is not running.");
		sc.add("@SuppressWarnings(\"restriction\")");
		sc.addLineBreak();
		sc.add("public void checkEMFValidationConstraints() {");
		sc.addComment("The EMF validation framework code throws a NPE if the validation plug-in is not loaded. "
				+ "This is a bug, which is fixed in the Helios release. Nonetheless, we need to catch the "
				+ "exception here.");
		sc.add("if (isEclipsePlatformRunning()) {");
		sc.addComment("The EMF validation framework code throws a NPE if the validation plug-in is not loaded. "
				+ "This is a workaround for bug 322079.");
		sc.add("if (" + EMF_MODEL_VALIDATION_PLUGIN + ".getPlugin() != null) {");
		sc.add("try {");
		sc.add(MODEL_VALIDATION_SERVICE + " service = " + MODEL_VALIDATION_SERVICE + ".getInstance();");
		sc.add(I_BATCH_VALIDATOR + " validator = service.<" + E_OBJECT + ", " + I_BATCH_VALIDATOR + ">newValidator(" + EVALUATION_MODE + ".BATCH);");
		sc.add("validator.setIncludeLiveConstraints(true);");
		sc.add(I_STATUS + " status = validator.validate(root);");
		sc.add("addStatus(status, root);");
		sc.add("} catch (Throwable t) {");
		sc.add("logError(\"Exception while checking contraints provided by EMF validator classes.\", t);");
		sc.add("}");
		sc.add("}");
		sc.add("}");
		sc.addLineBreak();
	}
	
	private void addAddStatusMethod(JavaComposite sc) {
		sc.add("public void addStatus(" + I_STATUS + " status, " + E_OBJECT
				+ " root) {");
		sc.add(LIST + "<" + E_OBJECT + "> causes = new " + ARRAY_LIST + "<"
				+ E_OBJECT + ">();");
		sc.add("causes.add(root);");
		sc.add("if (status instanceof " + CONSTRAINT_STATUS + ") {");
		sc.add(CONSTRAINT_STATUS + " constraintStatus = (" + CONSTRAINT_STATUS
				+ ") status;");
		sc.add(SET + "<" + E_OBJECT
				+ "> resultLocus = constraintStatus.getResultLocus();");
		sc.add("causes.clear();");
		sc.add("causes.addAll(resultLocus);");
		sc.add("}");
		sc.add("boolean hasChildren = status.getChildren() != null && status.getChildren().length > 0;");
		sc.addComment("Ignore composite status objects that have children. "
				+ "The actual status information is then contained in the child objects.");
		sc.add("if (!status.isMultiStatus() || !hasChildren) {");
		sc.add("if (status.getSeverity() == " + I_STATUS + ".ERROR) {");
		sc.add("for (" + E_OBJECT + " cause : causes) {");
		sc.add("addError(status.getMessage(), " + eProblemTypeClassName
				+ ".ANALYSIS_PROBLEM, cause);");
		sc.add("}");
		sc.add("}");
		sc.add("if (status.getSeverity() == " + I_STATUS + ".WARNING) {");
		sc.add("for (" + E_OBJECT + " cause : causes) {");
		sc.add("addWarning(status.getMessage(), " + eProblemTypeClassName
				+ ".ANALYSIS_PROBLEM, cause);");
		sc.add("}");
		sc.add("}");
		sc.add("}");
		sc.add("for (" + I_STATUS + " child : status.getChildren()) {");
		sc.add("addStatus(child, root);");
		sc.add("}");
		sc.add("}");
		sc.addLineBreak();
	}
}
