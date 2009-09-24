package org.emftext.sdk.codegen.generators.util;

import static org.emftext.sdk.codegen.generators.IClassNameConstants.E_MAP;
import static org.emftext.sdk.codegen.generators.IClassNameConstants.HASH_MAP;
import static org.emftext.sdk.codegen.generators.IClassNameConstants.ITERATOR;
import static org.emftext.sdk.codegen.generators.IClassNameConstants.MAP;

import java.io.PrintWriter;

import org.emftext.sdk.codegen.EArtifact;
import org.emftext.sdk.codegen.GenerationContext;
import org.emftext.sdk.codegen.IGenerator;
import org.emftext.sdk.codegen.generators.BaseGenerator;

public class MapUtilGenerator extends BaseGenerator {

	public MapUtilGenerator() {
		super();
	}

	private MapUtilGenerator(GenerationContext context) {
		super(context, EArtifact.MAP_UTIL);
	}

	public IGenerator newInstance(GenerationContext context) {
		return new MapUtilGenerator(context);
	}

	public boolean generate(PrintWriter out) {
		org.emftext.sdk.codegen.composites.StringComposite sc = new org.emftext.sdk.codegen.composites.JavaComposite();
		sc.add("package " + getResourcePackageName() + ";");
		sc.addLineBreak();
		sc.add("public class " + getResourceClassName() + " {");
		sc.addLineBreak();
		
		sc.add("// This method encapsulate an unchecked cast from Object to");
		sc.add("// " + MAP + "<Object, Object>. This case can not be performed type");
		sc.add("// safe, because type parameters are not available for");
		sc.add("// reflective access to Ecore models.");
		sc.add("//");
		sc.add("// @param value");
		sc.add("// @return");
		sc.add("@SuppressWarnings(\"unchecked\")").addLineBreak();
		sc.add("public static " + MAP + "<Object, Object> castToMap(Object value) {");
		sc.add("return (" + MAP + "<Object,Object>) value;");
		sc.add("}");
		sc.addLineBreak();
		
		sc.add("// This method encapsulate an unchecked cast from Object to");
		sc.add("// " + E_MAP + "<Object, Object>. This case can not be performed type");
		sc.add("// safe, because type parameters are not available for");
		sc.add("// reflective access to Ecore models.");
		sc.add("//");
		sc.add("// @param value");
		sc.add("// @return");
		
		sc.add("@SuppressWarnings(\"unchecked\")");
		sc.add("public static " + E_MAP + "<Object, Object> castToEMap(Object value) {");
		sc.add("return (" + E_MAP + "<Object,Object>) value;");
		sc.add("}");
		sc.addLineBreak();
		sc.add("public static " + MAP + "<Object, Object> copySafelyToObjectToObjectMap(" + MAP + "<?, ?> map) {");
		sc.add(MAP + "<Object, Object> castedCopy = new " + HASH_MAP + "<Object, Object>();");
		sc.addLineBreak();
		sc.add("if(map == null) {");
		sc.add("return castedCopy;");
		sc.add("}");
		sc.addLineBreak();
		sc.add(ITERATOR + "<?> it = map.keySet().iterator();");
		sc.add("while (it.hasNext()) {");
		sc.add("Object nextKey = it.next();");
		sc.add("castedCopy.put(nextKey, map.get(nextKey));");
		sc.add("}");
		sc.add("return castedCopy;");
		sc.add("}");
		sc.add("}");
		out.print(sc.toString());
		return true;
	}
}