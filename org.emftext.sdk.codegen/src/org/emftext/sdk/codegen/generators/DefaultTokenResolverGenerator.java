package org.emftext.sdk.codegen.generators;

import static org.emftext.sdk.codegen.generators.IClassNameConstants.*;
import org.emftext.sdk.codegen.generators.BaseGenerator;
import org.emftext.sdk.codegen.EArtifact;
import org.emftext.sdk.codegen.GenerationContext;
import org.emftext.sdk.codegen.IGenerator;

import java.io.PrintWriter;

public class DefaultTokenResolverGenerator extends BaseGenerator {

	public DefaultTokenResolverGenerator() {
		super();
	}

	private DefaultTokenResolverGenerator(GenerationContext context) {
		super(context, EArtifact.DEFAULT_TOKEN_RESOLVER);
	}

	public boolean generate(PrintWriter out) {
		org.emftext.sdk.codegen.composites.StringComposite sc = new org.emftext.sdk.codegen.composites.JavaComposite();
		sc.add("package " + getResourcePackageName() + ";");
		sc.addLineBreak();
		sc.add("// A base implementation for token resolvers. It tries to resolve lexems using java methods.");
		sc.add("public class " + getResourceClassName() + " implements " + getClassNameHelper().getI_TOKEN_RESOLVER() + " {");
		sc.addLineBreak();
		sc.add("private " + MAP + "<?, ?> options;");
		sc.addLineBreak();
		sc.add("public String deResolve(" + OBJECT + " value, " + E_STRUCTURAL_FEATURE + " feature, " + E_OBJECT + " container) {");
		sc.add("if (value == null) {");
		sc.add("return \"null\";");
		sc.add("}");
		sc.add("return value.toString();");
		sc.add("}");
		sc.addLineBreak();
		sc.add("public void resolve(String lexem, " + E_STRUCTURAL_FEATURE + " feature, " + getClassNameHelper().getI_TOKEN_RESOLVE_RESULT() + " result) {");
		sc.addLineBreak();
		sc.add("if (feature instanceof " + E_ATTRIBUTE + ") {");
		sc.add("if (feature.getEType() instanceof " + E_ENUM + ") {");
		sc.add(E_ENUM_LITERAL + " literal = ((" + E_ENUM + ")feature.getEType()).getEEnumLiteralByLiteral(lexem);");
		sc.add("if (literal != null) {");
		sc.add("result.setResolvedToken(literal.getInstance());");
		sc.add("return;");
		sc.add("} else {");
		sc.add("result.setErrorMessage(\"Could not map lexem '\"+lexem+\"' to enum '\"+feature.getEType().getName()+\"'.\");");
		sc.add("return;");
		sc.add("}");
		sc.add("} else {");
		sc.add("String typeName = feature.getEType().getInstanceClassName();");
		sc.add("try {");
		sc.add("if (typeName.equals(java.lang.String.class.getName())) {");
		sc.add("result.setResolvedToken(lexem);");
		sc.add("return;");
		sc.add("} else if (typeName.equals(\"char\") || java.lang.Character.class.getName().equals(typeName)) {");
		sc.add("if (lexem.length() != 1) {");
		sc.add("result.setErrorMessage(\"Can convert to single Character only.\");");
		sc.add("} else {");
		sc.add("result.setResolvedToken(lexem.charAt(0));");
		sc.add("return;");
		sc.add("}");
		sc.add("}");
		sc.add("else if (typeName.equals(\"boolean\") || java.lang.Boolean.class.getName().equals(typeName)){");
		sc.add("result.setResolvedToken(Boolean.parseBoolean(lexem));");
		sc.add("return;");
		sc.add("}");
		sc.add("else if (typeName.equals(\"int\") || java.lang.Integer.class.getName().equals(typeName)){");
		sc.add("result.setResolvedToken(Integer.parseInt(lexem));");
		sc.add("return;");
		sc.add("}");
		sc.add("else if (typeName.equals(\"long\") || java.lang.Long.class.getName().equals(typeName)){");
		sc.add("result.setResolvedToken(Long.parseLong(lexem));");
		sc.add("return;");
		sc.add("}");
		sc.add("else if (typeName.equals(\"double\") || java.lang.Double.class.getName().equals(typeName)){");
		sc.add("result.setResolvedToken(Double.parseDouble(lexem));");
		sc.add("return;");
		sc.add("}");
		sc.add("else if (typeName.equals(\"short\") || java.lang.Short.class.getName().equals(typeName)){");
		sc.add("result.setResolvedToken(Short.parseShort(lexem));");
		sc.add("return;");
		sc.add("}");
		sc.add("else if (typeName.equals(\"float\") || java.lang.Float.class.getName().equals(typeName)){");
		sc.add("result.setResolvedToken(Float.parseFloat(lexem));");
		sc.add("return;");
		sc.add("}");
		sc.add("} catch (NumberFormatException e) {");
		sc.add("result.setErrorMessage(\"Could not convert '\"+lexem+\"' to \"+typeName+\".\");");
		sc.add("return;");
		sc.add("}");
		sc.add("result.setErrorMessage(\"The type \"+typeName+\" is unknown.\");");
		sc.add("return;");
		sc.add("}");
		sc.add("} else {");
		sc.add("result.setResolvedToken(lexem);");
		sc.add("return;");
		sc.add("}");
		sc.add("}");
		sc.addLineBreak();
		sc.add("public void setOptions(" + MAP + "<?, ?> options) {");
		sc.add("this.options = options;");
		sc.add("}");
		sc.addLineBreak();
		sc.add("public " + MAP + "<?, ?> getOptions() {");
		sc.add("return options;");
		sc.add("}");
		sc.add("}");
		out.print(sc.toString());
		return true;
	}

	public IGenerator newInstance(GenerationContext context) {
		return new DefaultTokenResolverGenerator(context);
	}
}