package org.emftext.sdk.codegen.generators.ui;

import static org.emftext.sdk.codegen.generators.IClassNameConstants.*;
import org.emftext.sdk.codegen.generators.BaseGenerator;
import org.emftext.sdk.codegen.GenerationContext;
import org.emftext.sdk.codegen.IGenerator;

import java.io.PrintWriter;
import org.emftext.sdk.codegen.EArtifact;

public class DocBrowserInformationControlInputGenerator extends BaseGenerator {

	public DocBrowserInformationControlInputGenerator() {
		super();
	}

	private DocBrowserInformationControlInputGenerator(GenerationContext context) {
		super(context, EArtifact.DOC_BROWSER_INFORMATION_CONTROL_INPUT);
	}

	public boolean generate(PrintWriter out) {
		org.emftext.sdk.codegen.composites.StringComposite sc = new org.emftext.sdk.codegen.composites.JavaComposite();
		sc.add("package " + getResourcePackageName() + ";");
		sc.addLineBreak();
		sc.add("// Provides input for the <code>TextHover</code>. The most is copied from");
		sc.add("// <code>org.eclipse.jdt.internal.ui.text.java.hover.JavadocBrowserInformationControlInput</code>");
		sc.add("public class " + getResourceClassName() + " {");
		sc.addLineBreak();

		addFields(sc);
		addConstructor(sc);
		addMethods(sc);
		
		sc.add("}");
		out.print(sc.toString());
		return true;
	}

	private void addMethods(
			org.emftext.sdk.codegen.composites.StringComposite sc) {
		addGetPreviousMethod(sc);
		addGetNextMethod(sc);
		addResourceMethod(sc);
		addGetHtmlMethod(sc);
		addToStringMethod(sc);
		addGetTokenTextMethod(sc);
		addGetInputElementMethod(sc);
		addGetInputNameMethod(sc);
		addGetLeadingImageWidthMethod(sc);
	}

	private void addGetLeadingImageWidthMethod(
			org.emftext.sdk.codegen.composites.StringComposite sc) {
		sc.add("public int getLeadingImageWidth() {");
		sc.add("return 0;");
		sc.add("}");
	}

	private void addGetInputNameMethod(
			org.emftext.sdk.codegen.composites.StringComposite sc) {
		sc.add("public String getInputName() {");
		sc.add("return element == null ? \"\" : element.toString(); ");
		sc.add("}");
		sc.addLineBreak();
	}

	private void addGetInputElementMethod(
			org.emftext.sdk.codegen.composites.StringComposite sc) {
		sc.add("public " + OBJECT + " getInputElement() {");
		sc.add("return element == null ? (" + OBJECT + ") htmlContent : element;");
		sc.add("}");
		sc.addLineBreak();
	}

	private void addGetTokenTextMethod(
			org.emftext.sdk.codegen.composites.StringComposite sc) {
		sc.add("//");
		sc.add("// @return the token text, it is needed for a hyperlink where the caret has");
		sc.add("//         to jump to");
		sc.add("public String getTokenText() {");
		sc.add("return tokenText;");
		sc.add("}");
		sc.addLineBreak();
	}

	private void addToStringMethod(
			org.emftext.sdk.codegen.composites.StringComposite sc) {
		sc.add("public String toString() {");
		sc.add("return getHtml();");
		sc.add("}");
		sc.addLineBreak();
	}

	private void addGetHtmlMethod(
			org.emftext.sdk.codegen.composites.StringComposite sc) {
		sc.add("public String getHtml() {");
		sc.add("return htmlContent;");
		sc.add("}");
		sc.addLineBreak();
	}

	private void addResourceMethod(
			org.emftext.sdk.codegen.composites.StringComposite sc) {
		sc.add("// @return the resource");
		sc.add("public " + RESOURCE + " getResource() {");
		sc.add("return resource;");
		sc.add("}");
		sc.addLineBreak();
	}

	private void addGetNextMethod(
			org.emftext.sdk.codegen.composites.StringComposite sc) {
		sc.add("// The next input or <code>null</code> if this");
		sc.add("// is the last.");
		sc.add("//");
		sc.add("// @return the next input or <code>null</code>");
		sc.add("public " + getResourceClassName() + " getNext() {");
		sc.add("return fNext;");
		sc.add("}");
		sc.addLineBreak();
	}

	private void addGetPreviousMethod(
			org.emftext.sdk.codegen.composites.StringComposite sc) {
		sc.add("// The previous input or <code>null</code> if this");
		sc.add("// is the first.");
		sc.add("//");
		sc.add("// @return the previous input or <code>null</code>");
		sc.add("public " + getResourceClassName() + " getPrevious() {");
		sc.add("return fPrevious;");
		sc.add("}");
		sc.addLineBreak();
	}

	private void addConstructor(
			org.emftext.sdk.codegen.composites.StringComposite sc) {
		sc.add("// Creates a new browser information control input.");
		sc.add("//");
		sc.add("// @param previous");
		sc.add("//            previous input, or <code>null</code> if none available");
		sc.add("// @param element");
		sc.add("//            the element, or <code>null</code> if none available");
		sc.add("// @param htmlContent");
		sc.add("//            HTML contents, must not be null");
		sc.add("// @param leadingImageWidth");
		sc.add("//            the indent required for the element image");
		sc.add("///");
		sc.add("public " + getResourceClassName() + "(" + getResourceClassName() + " previous, " + E_OBJECT + " element, " + RESOURCE + " resource, String htmlContent, String tokenText) {");
		sc.add("fPrevious= previous;");
		sc.add("if (previous != null) {");
		sc.add("previous.fNext= this;");
		sc.add("}");
		sc.add("//super(previous);");
		sc.add("assert htmlContent != null;");
		sc.add("this.element = element;");
		sc.add("this.htmlContent = htmlContent;");
		sc.add("this.tokenText = tokenText;");
		sc.add("this.resource = resource;");
		sc.add("}");
		sc.addLineBreak();
	}

	private void addFields(org.emftext.sdk.codegen.composites.StringComposite sc) {
		sc.add("private final " + getResourceClassName() + " fPrevious;");
		sc.add("private " + getResourceClassName() + " fNext;");
		sc.add("private final " + E_OBJECT + " element;");
		sc.add("private final String htmlContent;");
		sc.add("private final String tokenText;");
		sc.add("private final " + RESOURCE + " resource;");
		sc.addLineBreak();
	}

	public IGenerator newInstance(GenerationContext context) {
		return new DocBrowserInformationControlInputGenerator(context);
	}
}