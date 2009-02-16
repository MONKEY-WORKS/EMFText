package org.emftext.sdk.codegen;

import java.io.PrintWriter;

import org.eclipse.emf.codegen.ecore.genmodel.GenFeature;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EReference;
import org.emftext.runtime.resource.IReferenceResolveResult;
import org.emftext.runtime.resource.impl.AbstractReferenceResolver;

/**
 * A generator that can create basic stub for a single reference resolver.
 */
public class ReferenceResolverGenerator extends BaseGenerator {
	
	private GenFeature proxyReference;

	public ReferenceResolverGenerator(GenerationContext context, GenFeature proxyReference) {
		super(context.getResolverPackageName(), context.getReferenceResolverClassName(proxyReference));
		this.proxyReference = proxyReference;
	}
	
	@Override
	public boolean generate(PrintWriter out) {     
	    out.println("package " + getResourcePackageName() + ";");	
	    out.println();
	    out.println("public class " + getResourceClassName() + " extends " + AbstractReferenceResolver.class.getName() + "<" + proxyReference.getGenClass().getQualifiedInterfaceName() + "> {\n");
		generateDoDeResolveMethod(out);
		out.println("}");
		return true;
	}

	private void generateDoDeResolveMethod(PrintWriter out) {
		out.println("\t@Override");
		out.println("\tprotected " + String.class.getName() + " doDeResolve(" + EObject.class.getName() + " element, " + proxyReference.getGenClass().getQualifiedInterfaceName() + " container, " + EReference.class.getName() + " reference) {");
		out.println("\t\treturn super.doDeResolve(element, container, reference);");
		out.println("\t}\n");
		out.println("\t@Override");
		out.println("\tprotected void doResolve(" + String.class.getName() + " identifier, " + proxyReference.getGenClass().getQualifiedInterfaceName() + " container, " + EReference.class.getName() + " reference, int position, boolean resolveFuzzy, " + IReferenceResolveResult.class.getName() + " result) {");
		out.println("\t\tsuper.doResolve(identifier, container, reference, position, resolveFuzzy, result);");
		out.println("\t}");
	}
}
