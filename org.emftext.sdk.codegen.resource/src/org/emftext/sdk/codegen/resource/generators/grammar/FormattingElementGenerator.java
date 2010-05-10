package org.emftext.sdk.codegen.resource.generators.grammar;

import org.emftext.sdk.codegen.IGenerator;
import org.emftext.sdk.codegen.composites.JavaComposite;
import org.emftext.sdk.codegen.composites.StringComposite;
import org.emftext.sdk.codegen.resource.GenerationContext;
import org.emftext.sdk.codegen.resource.TextResourceArtifacts;
import org.emftext.sdk.codegen.resource.generators.JavaBaseGenerator;

public class FormattingElementGenerator extends JavaBaseGenerator<Object> {

	public FormattingElementGenerator() {
		super();
	}

	private FormattingElementGenerator(GenerationContext context) {
		super(context, TextResourceArtifacts.FORMATTING_ELEMENT);
	}

	public IGenerator<GenerationContext, Object> newInstance(GenerationContext context, Object parameters) {
		return new FormattingElementGenerator(context);
	}

	public boolean generateJavaContents(JavaComposite sc) {
		
		sc.add("package " + getResourcePackageName() + ";");
		sc.addLineBreak();
		
		sc.add("public abstract class " + getResourceClassName() + " extends " + syntaxElementClassName + " {");
		sc.addLineBreak();
		addConstructor(sc);
		sc.add("}");
		return true;
	}

	private void addConstructor(StringComposite sc) {
		sc.add("public " + getResourceClassName() + "(" + cardinalityClassName + " cardinality) {"); 
		sc.add("super(cardinality, null);"); 
		sc.add("}"); 
		sc.addLineBreak();
	}
}
