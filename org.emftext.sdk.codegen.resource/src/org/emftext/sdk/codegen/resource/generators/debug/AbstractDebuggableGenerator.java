package org.emftext.sdk.codegen.resource.generators.debug;

import static org.emftext.sdk.codegen.composites.IClassNameConstants.ARRAY_LIST;
import static org.emftext.sdk.codegen.composites.IClassNameConstants.LIST;
import static org.emftext.sdk.codegen.resource.generators.IClassNameConstants.IO_EXCEPTION;
import static org.emftext.sdk.codegen.resource.generators.IClassNameConstants.MAP;
import static org.emftext.sdk.codegen.resource.generators.IClassNameConstants.PRINT_STREAM;
import static org.emftext.sdk.codegen.resource.generators.IClassNameConstants.SERVER_SOCKET;
import static org.emftext.sdk.codegen.resource.generators.IClassNameConstants.SOCKET;

import org.emftext.sdk.codegen.composites.JavaComposite;
import org.emftext.sdk.codegen.parameters.ArtifactParameter;
import org.emftext.sdk.codegen.resource.GenerationContext;
import org.emftext.sdk.codegen.resource.generators.JavaBaseGenerator;
import org.emftext.sdk.concretesyntax.OptionTypes;

public class AbstractDebuggableGenerator extends JavaBaseGenerator<ArtifactParameter<GenerationContext>> {

	public void generateJavaContents(JavaComposite sc) {
		if (!getContext().isDebugSupportEnabled()) {
			generateEmptyClass(sc, null, OptionTypes.DISABLE_DEBUG_SUPPORT);
			return;
		}
		sc.add("package " + getResourcePackageName() + ";");
		sc.addLineBreak();
		sc.add("public abstract class " + getResourceClassName() + " {");
		sc.addLineBreak();
		addFields(sc);
		addMethods(sc);
		addAbstractMethods(sc);
		sc.add("}");
	}

	private void addFields(JavaComposite sc) {
		sc.add("private " + LIST + "<Integer> lineBreakpoints = new " + ARRAY_LIST + "<Integer>();");
		sc.add("private " + PRINT_STREAM + " outputStream;");
		sc.add("private " + SERVER_SOCKET + " server;");
		sc.add("private boolean debugMode;");
		sc.add("private boolean suspend;");
		sc.addLineBreak();
		sc.add("private " + debugCommunicationHelperClassName + " communicationHelper = new " + debugCommunicationHelperClassName + "();");
		sc.addLineBreak();
	}

	private void addMethods(JavaComposite sc) {
		addStartEventSocketMethod(sc);
		addStopEventSocketMethod(sc);
		addSendEventMethod(sc);
		addEvaluateLineBreakpointMethod(sc);
		addWaitIfSuspendendMethod(sc);
		addAddLineBreakpointMethod(sc);
		addRemoveLineBreakpointMethod(sc);
		addResumeMethod(sc);
		addTerminatemethod(sc);
		addIsDebugModeMethod(sc);
		addSetDebugModeMethod(sc);
		addIsSuspendMethod(sc);
		addSetSuspendMethod(sc);
		addStartUpAndWaitMethod(sc);
	}

	private void addStartEventSocketMethod(JavaComposite sc) {
		sc.add("public void startEventSocket() {");
		sc.add("if (isDebugMode()) {");
		sc.add("try {");
		sc.addComment("starting event server socket (waiting for connection)...");
		sc.add("server = new " + SERVER_SOCKET + "(" + debugTargetClassName + ".DEBUG_PORT_2);");
		sc.add(SOCKET + " accept = server.accept();");
		sc.addComment("starting event server socket done (connection established).");
		sc.add("outputStream = new " + PRINT_STREAM + "(accept.getOutputStream());");
		sc.add("} catch (Exception e) {");
		// TODO
		sc.add("e.printStackTrace();");
		sc.add("}");
		sc.add("}");
		sc.add("}");
		sc.addLineBreak();
	}

	private void addStopEventSocketMethod(JavaComposite sc) {
		sc.add("public void stopEventSocket() {");
		sc.add("try {");
		sc.add("server.close();");
		sc.add("} catch (" + IO_EXCEPTION + " e) {");
		sc.add(pluginActivatorClassName + ".logError(\"Exception while closing socket.\", e);");
		sc.add("}");
		sc.add("}");
		sc.addLineBreak();
	}

	private void addSendEventMethod(JavaComposite sc) {
		sc.add("public void sendEvent(" + eDebugMessageTypesClassName + " command, String... arguments) {");
		sc.add("if (isDebugMode()) {");
		sc.add(debugMessageClassName + " message = new " + debugMessageClassName + "(command, arguments);");
		sc.add("communicationHelper.sendEvent(message, outputStream);");
		sc.add("}");
		sc.add("}");
		sc.addLineBreak();
	}

	private void addEvaluateLineBreakpointMethod(JavaComposite sc) {
		sc.add("public void evaluateLineBreakpoint(int currentLine) {");
		sc.add("if (isDebugMode()) {");
		sc.add("for (int i = 0; i < lineBreakpoints.size(); i++) {");
		sc.add("Integer breakpointLine = lineBreakpoints.get(i);");
		sc.add("if (breakpointLine.intValue() == currentLine) {");
		sc.addComment("suspending...");
		sc.add("setSuspend(true);");
		sc.add("sendEvent(" + eDebugMessageTypesClassName + ".SUSPENDED, new String[] {\"breakpoint\", \"\" + currentLine});");
		sc.add("break;");
		sc.add("}");
		sc.add("}");
		sc.add("waitIfSuspended();");
		sc.add("}");
		sc.add("}");
		sc.addLineBreak();
	}

	private void addWaitIfSuspendendMethod(JavaComposite sc) {
		sc.add("public void waitIfSuspended() {");
		sc.add("try {");
		sc.add("while (isSuspend()) {");
		sc.add("Thread.sleep(100);");
		sc.add("}");
		sc.add("} catch (InterruptedException e) {");
		sc.add("}");
		sc.add("sendEvent(" + eDebugMessageTypesClassName + ".RESUMED);");
		sc.add("}");
		sc.addLineBreak();
	}

	private void addAddLineBreakpointMethod(JavaComposite sc) {
		sc.add("public void addLineBreakpoint(int line) {");
		sc.add("lineBreakpoints.add(new Integer(line));");
		sc.add("}");
		sc.addLineBreak();
	}

	private void addRemoveLineBreakpointMethod(JavaComposite sc) {
		sc.add("public void removeLineBreakpoint(int line) {");
		sc.add("lineBreakpoints.remove(new Integer(line));");
		sc.add("}");
		sc.addLineBreak();
	}

	private void addResumeMethod(JavaComposite sc) {
		sc.add("public void resume() {");
		sc.add("setSuspend(false);");
		sc.add("}");
		sc.addLineBreak();
	}

	private void addTerminatemethod(JavaComposite sc) {
		sc.add("public void terminate() {");
		sc.add("if (isDebugMode()) {");
		sc.add("sendEvent(" + eDebugMessageTypesClassName + ".TERMINATED);");
		sc.add("stopEventSocket();");
		sc.add("}");
		sc.add("}");
		sc.addLineBreak();
	}

	private void addIsDebugModeMethod(JavaComposite sc) {
		sc.add("public boolean isDebugMode() {");
		sc.add("return debugMode;");
		sc.add("}");
		sc.addLineBreak();
	}

	private void addSetDebugModeMethod(JavaComposite sc) {
		sc.add("public void setDebugMode(boolean debugMode) {");
		sc.add("this.debugMode = debugMode;");
		sc.add("}");
		sc.addLineBreak();
	}

	private void addIsSuspendMethod(JavaComposite sc) {
		sc.add("public boolean isSuspend() {");
		sc.add("return suspend;");
		sc.add("}");
		sc.addLineBreak();
	}

	private void addSetSuspendMethod(JavaComposite sc) {
		sc.add("public void setSuspend(boolean suspend) {");
		sc.add("this.suspend = suspend;");
		sc.add("}");
		sc.addLineBreak();
	}

	private void addStartUpAndWaitMethod(JavaComposite sc) {
		sc.addJavadoc(
			"This method must be called before the actual execution of the interpreter or " +
			"generated code. Its purpose is to send an event to the debug server to signal " +
			"that the debuggable has started and wait until the server has installed all " +
			"breakpoints and signals to resume (i.e., to start the actual execution)."
		);
		sc.add("public void startUpAndWait() {");
		sc.addComment("suspend here after startup to wait for the installation of deferred breakpoints");
		sc.add("if (isDebugMode()) {");
		sc.add("sendEvent(" + eDebugMessageTypesClassName + ".STARTED);");
		sc.add("setSuspend(true);");
		sc.addComment("wait until server sends the RESUME event");
		sc.add("while (isSuspend()) {");
		sc.add("try {");
		sc.add("Thread.sleep(100);");
		sc.add("} catch (InterruptedException e) {");
		sc.add("throw new RuntimeException(e.getMessage());");
		sc.add("}");
		sc.add("}");
		sc.addComment("confirm that the debuggable was resumed");
		sc.add("sendEvent(" + eDebugMessageTypesClassName + ".RESUMED);");
		sc.add("}");
		sc.add("}");
		sc.addLineBreak();
	}

	private void addAbstractMethods(JavaComposite sc) {
		sc.add("public abstract void stepOver();");
		sc.add("public abstract void stepInto();");
		sc.add("public abstract void stepReturn();");
		sc.add("public abstract String[] getStack();");
		sc.add("public abstract " + MAP + "<String, Object> getFrameVariables();");
	}
}