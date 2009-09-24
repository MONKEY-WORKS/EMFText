package org.emftext.sdk.codegen.generators.interfaces;

import static org.emftext.sdk.codegen.generators.IClassNameConstants.E_OBJECT;
import static org.emftext.sdk.codegen.generators.IClassNameConstants.RESOURCE;

import java.io.PrintWriter;

import org.emftext.sdk.codegen.EArtifact;
import org.emftext.sdk.codegen.GenerationContext;
import org.emftext.sdk.codegen.IGenerator;
import org.emftext.sdk.codegen.generators.BaseGenerator;

public class ITextDiagnosticGenerator extends BaseGenerator {

	private String iProblemClassName;

	public ITextDiagnosticGenerator() {
		super();
	}

	private ITextDiagnosticGenerator(GenerationContext context) {
		super(context, EArtifact.I_TEXT_DIAGNOSTIC);
		iProblemClassName = getContext().getQualifiedClassName(EArtifact.I_PROBLEM);
	}

	public IGenerator newInstance(GenerationContext context) {
		return new ITextDiagnosticGenerator(context);
	}

	public boolean generate(PrintWriter out) {
		org.emftext.sdk.codegen.composites.StringComposite sc = new org.emftext.sdk.codegen.composites.JavaComposite();
		sc.add("package " + getResourcePackageName() + ";");
		sc.addLineBreak();
		
		sc.add("// An extended diagnostic that gives access to the exact position of the problem");
		sc.add("// in a character stream.");
		sc.add("public interface " + getResourceClassName() + " extends " + RESOURCE + ".Diagnostic {");
		sc.addLineBreak();
		
		sc.add("// @return Position of the first character of the problem area.");
		sc.add("public int getCharStart();");
		sc.addLineBreak();
		
		sc.add("// @return Position of the last character of the problem area.");
		sc.add("public int getCharEnd();");
		sc.addLineBreak();
		
		sc.add("// @return The column of the problem area.");
		sc.add("public int getColumn();");
		sc.addLineBreak();
		
		sc.add("// @return The line that contains the problem area.");
		sc.add("public int getLine();");
		sc.addLineBreak();
		
		sc.add("// Returns the problem that was found.");
		sc.add("//");
		sc.add("// @return");
		sc.add("public " + iProblemClassName + " getProblem();");
		sc.addLineBreak();
		
		sc.add("// Checks whether the problem was caused by the given element.");
		sc.add("public boolean wasCausedBy(" + E_OBJECT + " element);");
		
		sc.add("}");
		out.print(sc.toString());
		return true;
	}
}