/*******************************************************************************
 * Copyright (c) 2006-2009 
 * Software Technology Group, Dresden University of Technology
 * 
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0 
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *   Software Technology Group - TU Dresden, Germany 
 *      - initial API and implementation
 ******************************************************************************/
package org.emftext.sdk.codegen.generators.ui;

import static org.emftext.sdk.codegen.generators.IClassNameConstants.ARRAY_LIST;
import static org.emftext.sdk.codegen.generators.IClassNameConstants.BUTTON;
import static org.emftext.sdk.codegen.generators.IClassNameConstants.COLLECTION;
import static org.emftext.sdk.codegen.generators.IClassNameConstants.COLLECTIONS;
import static org.emftext.sdk.codegen.generators.IClassNameConstants.COLOR_SELECTOR;
import static org.emftext.sdk.codegen.generators.IClassNameConstants.COMPOSITE;
import static org.emftext.sdk.codegen.generators.IClassNameConstants.CONTROL;
import static org.emftext.sdk.codegen.generators.IClassNameConstants.EDITOR;
import static org.emftext.sdk.codegen.generators.IClassNameConstants.GRID_DATA;
import static org.emftext.sdk.codegen.generators.IClassNameConstants.GRID_LAYOUT;
import static org.emftext.sdk.codegen.generators.IClassNameConstants.HASH_MAP;
import static org.emftext.sdk.codegen.generators.IClassNameConstants.ITERATOR;
import static org.emftext.sdk.codegen.generators.IClassNameConstants.I_EDITOR_PART;
import static org.emftext.sdk.codegen.generators.IClassNameConstants.I_PREFERENCE_STORE;
import static org.emftext.sdk.codegen.generators.IClassNameConstants.I_SELECTION_CHANGED_LISTENER;
import static org.emftext.sdk.codegen.generators.IClassNameConstants.I_STRUCTURED_SELECTION;
import static org.emftext.sdk.codegen.generators.IClassNameConstants.I_TREE_CONTENT_PROVIDER;
import static org.emftext.sdk.codegen.generators.IClassNameConstants.I_WORKBENCH;
import static org.emftext.sdk.codegen.generators.IClassNameConstants.I_WORKBENCH_PREFERENCE_PAGE;
import static org.emftext.sdk.codegen.generators.IClassNameConstants.LABEL;
import static org.emftext.sdk.codegen.generators.IClassNameConstants.LABEL_PROVIDER;
import static org.emftext.sdk.codegen.generators.IClassNameConstants.LIST;
import static org.emftext.sdk.codegen.generators.IClassNameConstants.MAP;
import static org.emftext.sdk.codegen.generators.IClassNameConstants.PREFERENCE_CONVERTER;
import static org.emftext.sdk.codegen.generators.IClassNameConstants.PREFERENCE_PAGE;
import static org.emftext.sdk.codegen.generators.IClassNameConstants.RGB;
import static org.emftext.sdk.codegen.generators.IClassNameConstants.SCROLLABLE;
import static org.emftext.sdk.codegen.generators.IClassNameConstants.SCROLL_BAR;
import static org.emftext.sdk.codegen.generators.IClassNameConstants.SELECTION_CHANGED_EVENT;
import static org.emftext.sdk.codegen.generators.IClassNameConstants.SELECTION_EVENT;
import static org.emftext.sdk.codegen.generators.IClassNameConstants.SELECTION_LISTENER;
import static org.emftext.sdk.codegen.generators.IClassNameConstants.STRUCTURED_SELECTION;
import static org.emftext.sdk.codegen.generators.IClassNameConstants.STRUCTURED_VIEWER;
import static org.emftext.sdk.codegen.generators.IClassNameConstants.SWT;
import static org.emftext.sdk.codegen.generators.IClassNameConstants.TREE_VIEWER;
import static org.emftext.sdk.codegen.generators.IClassNameConstants.VIEWER;

import java.io.PrintWriter;

import org.emftext.sdk.codegen.EArtifact;
import org.emftext.sdk.codegen.GenerationContext;
import org.emftext.sdk.codegen.IGenerator;
import org.emftext.sdk.codegen.generators.BaseGenerator;

public class SyntaxColoringPreferencePageGenerator extends BaseGenerator {

	private String activatorClassName;
	private String editorClassName;
	private String syntaxColoringHelperClassName;
	private String antlrTokenHelperClassName;
	private String pixelConverterClassName;
	private String metaInformationClassName;

	public SyntaxColoringPreferencePageGenerator() {
		super();
	}

	private SyntaxColoringPreferencePageGenerator(GenerationContext context) {
		super(context, EArtifact.SYNTAX_COLORING_PREFERENCE_PAGE);
		activatorClassName = getContext().getQualifiedClassName(EArtifact.PLUGIN_ACTIVATOR);
		editorClassName = getContext().getQualifiedClassName(EArtifact.EDITOR);
		syntaxColoringHelperClassName = getContext().getQualifiedClassName(EArtifact.SYNTAX_COLORING_HELPER);
		antlrTokenHelperClassName = getContext().getQualifiedClassName(EArtifact.ANTLR_TOKEN_HELPER);
		pixelConverterClassName = getContext().getQualifiedClassName(EArtifact.PIXEL_CONVERTER);
		metaInformationClassName = getContext().getQualifiedClassName(EArtifact.META_INFORMATION);
	}

	public IGenerator newInstance(GenerationContext context) {
		return new SyntaxColoringPreferencePageGenerator(context);
	}

	public boolean generate(PrintWriter out) {
		org.emftext.sdk.codegen.composites.StringComposite sc = new org.emftext.sdk.codegen.composites.JavaComposite();
		sc.add("package " + getResourcePackageName() + ";");
		sc.addLineBreak();
		sc.add("// Preference page for configuring syntax coloring.");
		sc.add("// <p>");
		sc.add("// <i>Parts of the code were taken from the JDT Java " + EDITOR + "</i>");
		sc.add("///");
		sc.add("public class " + getResourceClassName() + " extends " + PREFERENCE_PAGE + " implements " + I_WORKBENCH_PREFERENCE_PAGE + " {");
		sc.addLineBreak();
		addFields(sc);
		addIChangePreferenceInterface(sc);
		addAbstractChangedPreferenceClass(sc);
		addChangedBooleanPreferenceClass(sc);
		addChangedRGBPreferenceClass(sc);
		addHighlightingColorListItemClass(sc);
		addColorListLabelProviderClass(sc);
		sc.add("// Color list content provider.");
		sc.add("private class ColorListContentProvider implements " + I_TREE_CONTENT_PROVIDER + " {");
		sc.addLineBreak();
		sc.add("public ColorListContentProvider() {");
		sc.add("super();");
		sc.add("}");
		sc.addLineBreak();
		sc.add("public Object[] getElements(Object inputElement) {");
		sc.add("" + LIST + "<HighlightingColorListItem> contentsList = new " + ARRAY_LIST + "<HighlightingColorListItem>();");
		sc.add("for(" + LIST + "<HighlightingColorListItem> l : content.values()) {");
		sc.add("contentsList.addAll(l);");
		sc.add("}");
		sc.add("return contentsList.toArray();");
		sc.add("}");
		sc.addLineBreak();
		sc.add("public void dispose() {");
		sc.add("}");
		sc.addLineBreak();
		sc.add("public void inputChanged(" + VIEWER + " viewer, Object oldInput, Object newInput) {");
		sc.add("}");
		sc.addLineBreak();
		sc.add("public Object[] getChildren(Object parentElement) {");
		sc.add("return new Object[0];");
		sc.add("}");
		sc.addLineBreak();
		sc.add("public Object getParent(Object element) {");
		sc.add("return null;");
		sc.add("}");
		sc.addLineBreak();
		sc.add("public boolean hasChildren(Object element) {");
		sc.add("return false;");
		sc.add("}");
		sc.add("}");
		sc.addLineBreak();
		sc.add("private " + COLOR_SELECTOR + " fSyntaxForegroundColorEditor;");
		sc.add("private " + LABEL + " fColorEditorLabel;");
		sc.add("private " + BUTTON + " fBoldCheckBox;");
		sc.add("private " + BUTTON + " fEnableCheckbox;");
		sc.add("// Check box for italic preference.");
		sc.add("private " + BUTTON + " fItalicCheckBox;");
		sc.add("// Check box for strikethrough preference.");
		sc.add("private " + BUTTON + " fStrikethroughCheckBox;");
		sc.add("// Check box for underline preference.");
		sc.add("private " + BUTTON + " fUnderlineCheckBox;");
		sc.add("private " + BUTTON + " fForegroundColorButton;");
		sc.addLineBreak();
		sc.add("// Highlighting color list viewer");
		sc.add("private " + STRUCTURED_VIEWER + " fListViewer;");
		sc.addLineBreak();
		sc.add("public void dispose() {");
		sc.add("super.dispose();");
		sc.add("}");
		sc.addLineBreak();
		sc.add("private void handleSyntaxColorListSelection() {");
		sc.add("HighlightingColorListItem item = getHighlightingColorListItem();");
		sc.add("if (item == null) {");
		sc.add("fEnableCheckbox.setEnabled(false);");
		sc.add("fSyntaxForegroundColorEditor.getButton().setEnabled(false);");
		sc.add("fColorEditorLabel.setEnabled(false);");
		sc.add("fBoldCheckBox.setEnabled(false);");
		sc.add("fItalicCheckBox.setEnabled(false);");
		sc.add("fStrikethroughCheckBox.setEnabled(false);");
		sc.add("fUnderlineCheckBox.setEnabled(false);");
		sc.add("return;");
		sc.add("}");
		sc.add(RGB + " rgb = " + PREFERENCE_CONVERTER + ".getColor(getPreferenceStore(), item.getColorKey());");
		sc.add("fSyntaxForegroundColorEditor.setColorValue(rgb);");
		sc.add("fBoldCheckBox.setSelection(getPreferenceStore().getBoolean(");
		sc.add("item.getBoldKey()));");
		sc.add("fItalicCheckBox.setSelection(getPreferenceStore().getBoolean(");
		sc.add("item.getItalicKey()));");
		sc.add("fStrikethroughCheckBox.setSelection(getPreferenceStore().getBoolean(");
		sc.add("item.getStrikethroughKey()));");
		sc.add("fUnderlineCheckBox.setSelection(getPreferenceStore().getBoolean(");
		sc.add("item.getUnderlineKey()));");
		sc.addLineBreak();
		sc.add("fEnableCheckbox.setEnabled(true);");
		sc.add("boolean enable = getPreferenceStore().getBoolean(item.getEnableKey());");
		sc.add("fEnableCheckbox.setSelection(enable);");
		sc.add("fSyntaxForegroundColorEditor.getButton().setEnabled(enable);");
		sc.add("fColorEditorLabel.setEnabled(enable);");
		sc.add("fBoldCheckBox.setEnabled(enable);");
		sc.add("fItalicCheckBox.setEnabled(enable);");
		sc.add("fStrikethroughCheckBox.setEnabled(enable);");
		sc.add("fUnderlineCheckBox.setEnabled(enable);");
		sc.add("}");
		sc.addLineBreak();
		sc.add("private " + CONTROL + " createSyntaxPage(final " + COMPOSITE + " parent) {");
		sc.addLineBreak();
		sc.add(COMPOSITE + " colorComposite = new " + COMPOSITE + "(parent, " + SWT + ".NONE);");
		sc.add(GRID_LAYOUT + " layout = new " + GRID_LAYOUT + "();");
		sc.add("layout.marginHeight = 0;");
		sc.add("layout.marginWidth = 0;");
		sc.add("colorComposite.setLayout(layout);");
		sc.addLineBreak();
		sc.add("addFiller(colorComposite, 1);");
		sc.addLineBreak();
		sc.add(LABEL + " label = new " + LABEL + "(colorComposite, " + SWT + ".LEFT);");
		sc.add("label.setText(\"Element:\");");
		sc.add("label.setLayoutData(new " + GRID_DATA + "(" + GRID_DATA + ".FILL, " + GRID_DATA + ".BEGINNING,");
		sc.add("true, false));");
		sc.addLineBreak();
		sc.add(COMPOSITE + " editorComposite = createEditorComposite(colorComposite);");
		sc.add("createListViewer(editorComposite);");
		sc.add("createStylesComposite(editorComposite);");
		sc.addLineBreak();
		sc.add("addListenersToStyleButtons();");
		sc.add("colorComposite.layout(false);");
		sc.add("handleSyntaxColorListSelection();");
		sc.addLineBreak();
		sc.add("return colorComposite;");
		sc.add("}");
		sc.addLineBreak();
		sc.add("private " + COMPOSITE + " createEditorComposite(" + COMPOSITE + " colorComposite) {");
		sc.add(GRID_LAYOUT + " layout;");
		sc.add(COMPOSITE + " editorComposite = new " + COMPOSITE + "(colorComposite, " + SWT + ".NONE);");
		sc.add("layout = new " + GRID_LAYOUT + "();");
		sc.add("layout.numColumns = 2;");
		sc.add("layout.marginHeight = 0;");
		sc.add("layout.marginWidth = 0;");
		sc.add("editorComposite.setLayout(layout);");
		sc.add(GRID_DATA + " gd = new " + GRID_DATA + "(" + GRID_DATA + ".FILL, " + GRID_DATA + ".FILL, true, true);");
		sc.add("editorComposite.setLayoutData(gd);");
		sc.add("return editorComposite;");
		sc.add("}");
		sc.addLineBreak();
		sc.add("private void createListViewer(" + COMPOSITE + " editorComposite) {");
		sc.add("fListViewer = new " + TREE_VIEWER + "(editorComposite, " + SWT + ".SINGLE | " + SWT + ".BORDER);");
		sc.add("fListViewer.setLabelProvider(new ColorListLabelProvider());");
		sc.add("fListViewer.setContentProvider(new ColorListContentProvider());");
		sc.addLineBreak();
		sc.add(GRID_DATA + " gd = new " + GRID_DATA + "(" + GRID_DATA + ".FILL, " + GRID_DATA + ".FILL, true, true);");
		sc.add("gd.heightHint = convertHeightInCharsToPixels(26);");
		sc.add("int maxWidth = 0;");
		sc.add("for (" + ITERATOR + "<" + LIST + "<HighlightingColorListItem>> it = content.values()");
		sc.add(".iterator(); it.hasNext();) {");
		sc.add("for (" + ITERATOR + "<HighlightingColorListItem> j = it.next().iterator(); j.hasNext();) {");
		sc.add("HighlightingColorListItem item = j.next();");
		sc.add("maxWidth = Math.max(maxWidth, convertWidthInCharsToPixels(item.getDisplayName().length()));");
		sc.add("}");
		sc.add("}");
		sc.add(SCROLL_BAR + " vBar = ((" + SCROLLABLE + ") fListViewer.getControl()).getVerticalBar();");
		sc.add("if (vBar != null)");
		sc.add("maxWidth += vBar.getSize().x * 3; // scrollbars and tree indentation");
		sc.add("// guess");
		sc.add("gd.widthHint = maxWidth;");
		sc.addLineBreak();
		sc.add("fListViewer.getControl().setLayoutData(gd);");
		sc.addLineBreak();
		sc.add("fListViewer.setInput(content);");
		sc.add("fListViewer.setSelection(new " + STRUCTURED_SELECTION + "(content.values().iterator().next()));");
		sc.add("fListViewer");
		sc.add(".addSelectionChangedListener(new " + I_SELECTION_CHANGED_LISTENER + "() {");
		sc.add("public void selectionChanged(" + SELECTION_CHANGED_EVENT + " event) {");
		sc.add("handleSyntaxColorListSelection();");
		sc.add("}");
		sc.add("});");
		sc.add("}");
		sc.addLineBreak();
		sc.add("private void addListenersToStyleButtons() {");
		sc.add("fForegroundColorButton.addSelectionListener(new " + SELECTION_LISTENER + "() {");
		sc.add("public void widgetDefaultSelected(" + SELECTION_EVENT + " e) {");
		sc.add("// do nothing");
		sc.add("}");
		sc.addLineBreak();
		sc.add("public void widgetSelected(" + SELECTION_EVENT + " e) {");
		sc.add("HighlightingColorListItem item = getHighlightingColorListItem();");
		sc.addLineBreak();
		sc.add("changedPreferences.add(new ChangedRGBPreference(item");
		sc.add(".getColorKey(), fSyntaxForegroundColorEditor");
		sc.add(".getColorValue()));");
		sc.add("}");
		sc.add("});");
		sc.addLineBreak();
		sc.add("fBoldCheckBox.addSelectionListener(new " + SELECTION_LISTENER + "() {");
		sc.add("public void widgetDefaultSelected(" + SELECTION_EVENT + " e) {");
		sc.add("// do nothing");
		sc.add("}");
		sc.addLineBreak();
		sc.add("public void widgetSelected(" + SELECTION_EVENT + " e) {");
		sc.add("HighlightingColorListItem item = getHighlightingColorListItem();");
		sc.add("changedPreferences.add(new ChangedBooleanPreference(item.getBoldKey(),");
		sc.add("fBoldCheckBox.getSelection()));");
		sc.add("}");
		sc.add("});");
		sc.addLineBreak();
		sc.add("fItalicCheckBox.addSelectionListener(new " + SELECTION_LISTENER + "() {");
		sc.add("public void widgetDefaultSelected(" + SELECTION_EVENT + " e) {");
		sc.add("// do nothing");
		sc.add("}");
		sc.addLineBreak();
		sc.add("public void widgetSelected(" + SELECTION_EVENT + " e) {");
		sc.add("HighlightingColorListItem item = getHighlightingColorListItem();");
		sc.add("changedPreferences.add(new ChangedBooleanPreference(item.getItalicKey(),");
		sc.add("fItalicCheckBox.getSelection()));");
		sc.add("}");
		sc.add("});");
		sc.add("fStrikethroughCheckBox.addSelectionListener(new " + SELECTION_LISTENER + "() {");
		sc.add("public void widgetDefaultSelected(" + SELECTION_EVENT + " e) {");
		sc.add("// do nothing");
		sc.add("}");
		sc.addLineBreak();
		sc.add("public void widgetSelected(" + SELECTION_EVENT + " e) {");
		sc.add("HighlightingColorListItem item = getHighlightingColorListItem();");
		sc.add("changedPreferences.add(new ChangedBooleanPreference(item.getStrikethroughKey(),");
		sc.add("fStrikethroughCheckBox.getSelection()));");
		sc.add("}");
		sc.add("});");
		sc.addLineBreak();
		sc.add("fUnderlineCheckBox.addSelectionListener(new " + SELECTION_LISTENER + "() {");
		sc.add("public void widgetDefaultSelected(" + SELECTION_EVENT + " e) {");
		sc.add("// do nothing");
		sc.add("}");
		sc.addLineBreak();
		sc.add("public void widgetSelected(" + SELECTION_EVENT + " e) {");
		sc.add("HighlightingColorListItem item = getHighlightingColorListItem();");
		sc.add("changedPreferences.add(new ChangedBooleanPreference(item.getUnderlineKey(),");
		sc.add("fUnderlineCheckBox.getSelection()));");
		sc.add("}");
		sc.add("});");
		sc.addLineBreak();
		sc.add("fEnableCheckbox.addSelectionListener(new " + SELECTION_LISTENER + "() {");
		sc.add("public void widgetDefaultSelected(" + SELECTION_EVENT + " e) {");
		sc.add("// do nothing");
		sc.add("}");
		sc.addLineBreak();
		sc.add("public void widgetSelected(" + SELECTION_EVENT + " e) {");
		sc.add("HighlightingColorListItem item = getHighlightingColorListItem();");
		sc.addLineBreak();
		sc.add("boolean enable = fEnableCheckbox.getSelection();");
		sc.add("changedPreferences.add(new ChangedBooleanPreference(item.getEnableKey(), enable));");
		sc.add("fEnableCheckbox.setSelection(enable);");
		sc.add("fSyntaxForegroundColorEditor.getButton().setEnabled(enable);");
		sc.add("fColorEditorLabel.setEnabled(enable);");
		sc.add("fBoldCheckBox.setEnabled(enable);");
		sc.add("fItalicCheckBox.setEnabled(enable);");
		sc.add("fStrikethroughCheckBox.setEnabled(enable);");
		sc.add("fUnderlineCheckBox.setEnabled(enable);");
		sc.add("// uninstallSemanticHighlighting();");
		sc.add("// installSemanticHighlighting();");
		sc.add("}");
		sc.add("});");
		sc.add("}");
		sc.addLineBreak();
		sc.add("private void createStylesComposite(" + COMPOSITE + " editorComposite) {");
		sc.add(GRID_LAYOUT + " layout;");
		sc.add(GRID_DATA + " gd;");
		sc.add(COMPOSITE + " stylesComposite = new " + COMPOSITE + "(editorComposite, " + SWT + ".NONE);");
		sc.add("layout = new " + GRID_LAYOUT + "();");
		sc.add("layout.marginHeight = 0;");
		sc.add("layout.marginWidth = 0;");
		sc.add("layout.numColumns = 2;");
		sc.add("stylesComposite.setLayout(layout);");
		sc.add("stylesComposite.setLayoutData(new " + GRID_DATA + "(" + GRID_DATA + ".END, " + GRID_DATA + ".FILL,");
		sc.add("false, true));");
		sc.addLineBreak();
		sc.add("fEnableCheckbox = new " + BUTTON + "(stylesComposite, " + SWT + ".CHECK);");
		sc.add("fEnableCheckbox.setText(\"Enable\");");
		sc.add("gd = new " + GRID_DATA + "(" + GRID_DATA + ".FILL_HORIZONTAL);");
		sc.add("gd.horizontalAlignment = " + GRID_DATA + ".BEGINNING;");
		sc.add("gd.horizontalSpan = 2;");
		sc.add("fEnableCheckbox.setLayoutData(gd);");
		sc.addLineBreak();
		sc.add("fColorEditorLabel = new " + LABEL + "(stylesComposite, " + SWT + ".LEFT);");
		sc.add("fColorEditorLabel.setText(\"Color:\");");
		sc.add("gd = new " + GRID_DATA + "(" + GRID_DATA + ".HORIZONTAL_ALIGN_BEGINNING);");
		sc.add("gd.horizontalIndent = 20;");
		sc.add("fColorEditorLabel.setLayoutData(gd);");
		sc.addLineBreak();
		sc.add("fSyntaxForegroundColorEditor = new " + COLOR_SELECTOR + "(stylesComposite);");
		sc.add("fForegroundColorButton = fSyntaxForegroundColorEditor.getButton();");
		sc.add("gd = new " + GRID_DATA + "(" + GRID_DATA + ".HORIZONTAL_ALIGN_BEGINNING);");
		sc.add("fForegroundColorButton.setLayoutData(gd);");
		sc.addLineBreak();
		sc.add("fBoldCheckBox = new " + BUTTON + "(stylesComposite, " + SWT + ".CHECK);");
		sc.add("fBoldCheckBox.setText(\"Bold\");");
		sc.add("gd = new " + GRID_DATA + "(" + GRID_DATA + ".HORIZONTAL_ALIGN_BEGINNING);");
		sc.add("gd.horizontalIndent = 20;");
		sc.add("gd.horizontalSpan = 2;");
		sc.add("fBoldCheckBox.setLayoutData(gd);");
		sc.addLineBreak();
		sc.add("fItalicCheckBox = new " + BUTTON + "(stylesComposite, " + SWT + ".CHECK);");
		sc.add("fItalicCheckBox.setText(\"Italic\");");
		sc.add("gd = new " + GRID_DATA + "(" + GRID_DATA + ".HORIZONTAL_ALIGN_BEGINNING);");
		sc.add("gd.horizontalIndent = 20;");
		sc.add("gd.horizontalSpan = 2;");
		sc.add("fItalicCheckBox.setLayoutData(gd);");
		sc.addLineBreak();
		sc.add("fStrikethroughCheckBox = new " + BUTTON + "(stylesComposite, " + SWT + ".CHECK);");
		sc.add("fStrikethroughCheckBox.setText(\"Strikethrough\");");
		sc.add("gd = new " + GRID_DATA + "(" + GRID_DATA + ".HORIZONTAL_ALIGN_BEGINNING);");
		sc.add("gd.horizontalIndent = 20;");
		sc.add("gd.horizontalSpan = 2;");
		sc.add("fStrikethroughCheckBox.setLayoutData(gd);");
		sc.addLineBreak();
		sc.add("fUnderlineCheckBox = new " + BUTTON + "(stylesComposite, " + SWT + ".CHECK);");
		sc.add("fUnderlineCheckBox.setText(\"Underlined\");");
		sc.add("gd = new " + GRID_DATA + "(" + GRID_DATA + ".HORIZONTAL_ALIGN_BEGINNING);");
		sc.add("gd.horizontalIndent = 20;");
		sc.add("gd.horizontalSpan = 2;");
		sc.add("fUnderlineCheckBox.setLayoutData(gd);");
		sc.add("}");
		sc.addLineBreak();
		sc.add("private void addFiller(" + COMPOSITE + " composite, int horizontalSpan) {");
		sc.add(pixelConverterClassName + " pixelConverter = new " + pixelConverterClassName + "(composite);");
		sc.add(LABEL + " filler = new " + LABEL + "(composite, " + SWT + ".LEFT);");
		sc.add(GRID_DATA + " gd = new " + GRID_DATA + "(" + GRID_DATA + ".HORIZONTAL_ALIGN_FILL);");
		sc.add("gd.horizontalSpan = horizontalSpan;");
		sc.add("gd.heightHint = pixelConverter.convertHeightInCharsToPixels(1) / 2;");
		sc.add("filler.setLayoutData(gd);");
		sc.add("}");
		sc.addLineBreak();
		sc.add("// Returns the current highlighting color list item.");
		sc.add("private HighlightingColorListItem getHighlightingColorListItem() {");
		sc.add(I_STRUCTURED_SELECTION + " selection = (" + I_STRUCTURED_SELECTION + ") fListViewer.getSelection();");
		sc.add("Object element = selection.getFirstElement();");
		sc.add("if (element instanceof String)");
		sc.add("return null;");
		sc.add("return (HighlightingColorListItem) element;");
		sc.add("}");
		sc.addLineBreak();
		sc.add("public " + getResourceClassName() + "() {");
		sc.add("super();");
		sc.addLineBreak();
		sc.add(getClassNameHelper().getI_TEXT_RESOURCE_PLUGIN_META_INFORMATION() + " syntaxPlugin = new " + metaInformationClassName + "();");
		sc.addLineBreak();
		sc.add("String languageId = syntaxPlugin.getSyntaxName();");
		sc.addLineBreak();
		sc.add(LIST + "<HighlightingColorListItem> terminals = new " + ARRAY_LIST + "<HighlightingColorListItem>();");
		sc.add("String[] tokenNames = syntaxPlugin.getTokenNames();");
		sc.addLineBreak();
		sc.add("for (int i = 0; i < tokenNames.length; i++) {");
		sc.add("if (!tokenHelper.canBeUsedForSyntaxColoring(i)) {");
		sc.add("continue;");
		sc.add("}");
		sc.addLineBreak();
		sc.add("String tokenName = tokenHelper.getTokenName(tokenNames, i);");
		sc.add("if (tokenName == null) {");
		sc.add("continue;");
		sc.add("}");
		sc.add("HighlightingColorListItem item = new HighlightingColorListItem(languageId, tokenName);");
		sc.add("terminals.add(item);");
		sc.add("}");
		sc.add(COLLECTIONS + ".sort(terminals);");
		sc.add("content.put(languageId, terminals);");
		sc.addLineBreak();
		sc.add("setPreferenceStore(" + activatorClassName + ".getDefault().getPreferenceStore());");
		sc.add("setDescription(\"Configure syntax coloring for .\" + languageId + \" files.\");");
		sc.add("}");
		sc.addLineBreak();
		sc.add("public void init(" + I_WORKBENCH + " workbench) {");
		sc.add("}");
		sc.addLineBreak();
		sc.add("protected " + CONTROL + " createContents(" + COMPOSITE + " parent) {");
		sc.add(CONTROL + " content = createSyntaxPage(parent);");
		sc.add("return content;");
		sc.add("}");
		sc.addLineBreak();
		sc.add("public boolean performOk() {");
		sc.add("if (!super.performOk()) {");
		sc.add("return false;");
		sc.add("}");
		sc.add("performApply();");
		sc.add("return true;");
		sc.add("}");
		sc.addLineBreak();
		sc.add("public boolean performCancel() {");
		sc.add("if (!super.performCancel()) {");
		sc.add("return false;");
		sc.add("}");
		sc.add("// discard all changes that were made");
		sc.add("changedPreferences.clear();");
		sc.add("return true;");
		sc.add("}");
		sc.addLineBreak();
		sc.add("protected void performApply() {");
		sc.add("for (IChangedPreference changedPreference : changedPreferences) {");
		sc.add("changedPreference.apply(getPreferenceStore());");
		sc.add("}");
		sc.add("changedPreferences.clear();");
		sc.add("updateActiveEditor();");
		sc.add("}");
		sc.addLineBreak();
		sc.add("public void performDefaults() {");
		sc.add("super.performDefaults();");
		sc.addLineBreak();
		sc.add(I_PREFERENCE_STORE + " preferenceStore = getPreferenceStore();");
		sc.add("// reset all preferences to their default values");
		sc.add("for (String languageID : content.keySet()) {");
		sc.add(LIST + "<HighlightingColorListItem> items = content.get(languageID);");
		sc.add("for (HighlightingColorListItem item : items) {");
		sc.add("restoreDefaultBooleanValue(preferenceStore, item.getBoldKey());");
		sc.add("restoreDefaultBooleanValue(preferenceStore, item.getEnableKey());");
		sc.add("restoreDefaultBooleanValue(preferenceStore, item.getItalicKey());");
		sc.add("restoreDefaultBooleanValue(preferenceStore, item.getStrikethroughKey());");
		sc.add("restoreDefaultBooleanValue(preferenceStore, item.getUnderlineKey());");
		sc.add("restoreDefaultStringValue(preferenceStore, item.getColorKey());");
		sc.add("}");
		sc.add("}");
		sc.add("handleSyntaxColorListSelection();");
		sc.add("updateActiveEditor();");
		sc.add("}");
		sc.addLineBreak();
		sc.add("private void restoreDefaultBooleanValue(" + I_PREFERENCE_STORE + " preferenceStore, String key) {");
		sc.add("preferenceStore.setValue(key, preferenceStore.getDefaultBoolean(key));");
		sc.add("}");
		sc.addLineBreak();
		sc.add("private void restoreDefaultStringValue(" + I_PREFERENCE_STORE + " preferenceStore, String key) {");
		sc.add("preferenceStore.setValue(key, preferenceStore.getDefaultString(key));");
		sc.add("}");
		sc.addLineBreak();
		sc.add("private void updateActiveEditor() {");
		sc.add("" + I_WORKBENCH + " workbench = org.eclipse.ui.PlatformUI.getWorkbench();");
		sc.add("" + I_EDITOR_PART + " editor = workbench.getActiveWorkbenchWindow()");
		sc.add(".getActivePage().getActiveEditor();");
		sc.add("if (editor != null && editor instanceof " + editorClassName + ") {");
		sc.add(editorClassName + " emfTextEditor = (" + editorClassName + ") editor;");
		sc.add("emfTextEditor.invalidateTextRepresentation();");
		sc.add("}");
		sc.add("}");
		sc.add("}");
		out.print(sc.toString());
		return true;
	}

	private void addColorListLabelProviderClass(
			org.emftext.sdk.codegen.composites.StringComposite sc) {
		sc.add("// Color list label provider.");
		sc.add("private class ColorListLabelProvider extends " + LABEL_PROVIDER + " {");
		sc.addLineBreak();
		sc.add("public String getText(Object element) {");
		sc.add("if (element instanceof String)");
		sc.add("return (String) element;");
		sc.add("return ((HighlightingColorListItem) element).getDisplayName();");
		sc.add("}");
		sc.add("}");
		sc.addLineBreak();
	}

	private void addHighlightingColorListItemClass(
			org.emftext.sdk.codegen.composites.StringComposite sc) {
		sc.add("// Item in the highlighting color list.");
		sc.add("private static class HighlightingColorListItem implements Comparable<HighlightingColorListItem> {");
		sc.add("// Display name ");
		sc.add("private String fDisplayName;");
		sc.add("// Color preference key");
		sc.add("private String fColorKey;");
		sc.add("// Bold preference key");
		sc.add("private String fBoldKey;");
		sc.add("// Italic preference key");
		sc.add("private String fItalicKey;");
		sc.add("// Strikethrough preference key.");
		sc.add("private String fStrikethroughKey;");
		sc.add("// Underline preference key.");
		sc.add("private String fUnderlineKey;");
		sc.add("private String fEnableKey;");
		sc.addLineBreak();
		sc.add("// Initialize the item with the given values.");
		sc.add("public HighlightingColorListItem(String languageID, String tokenName) {");
		sc.add("fDisplayName = tokenName;");
		sc.add("fColorKey = " + syntaxColoringHelperClassName + ".getPreferenceKey(languageID, tokenName, " + syntaxColoringHelperClassName + ".StyleProperty.COLOR);");
		sc.add("fBoldKey = " + syntaxColoringHelperClassName + ".getPreferenceKey(languageID, tokenName, " + syntaxColoringHelperClassName + ".StyleProperty.BOLD);");
		sc.add("fItalicKey = " + syntaxColoringHelperClassName + ".getPreferenceKey(languageID, tokenName, " + syntaxColoringHelperClassName + ".StyleProperty.ITALIC);");
		sc.add("fStrikethroughKey = " + syntaxColoringHelperClassName + ".getPreferenceKey(languageID, tokenName, " + syntaxColoringHelperClassName + ".StyleProperty.STRIKETHROUGH);");
		sc.add("fUnderlineKey = " + syntaxColoringHelperClassName + ".getPreferenceKey(languageID, tokenName, " + syntaxColoringHelperClassName + ".StyleProperty.UNDERLINE);");
		sc.add("fEnableKey = " + syntaxColoringHelperClassName + ".getPreferenceKey(languageID, tokenName, " + syntaxColoringHelperClassName + ".StyleProperty.ENABLE);");
		sc.add("}");
		sc.addLineBreak();
		sc.add("// @return the bold preference key");
		sc.add("public String getBoldKey() {");
		sc.add("return fBoldKey;");
		sc.add("}");
		sc.addLineBreak();
		sc.add("// @return the bold preference key");
		sc.add("public String getItalicKey() {");
		sc.add("return fItalicKey;");
		sc.add("}");
		sc.addLineBreak();
		sc.add("// @return the strikethrough preference key");
		sc.add("public String getStrikethroughKey() {");
		sc.add("return fStrikethroughKey;");
		sc.add("}");
		sc.addLineBreak();
		sc.add("// @return the underline preference key");
		sc.add("public String getUnderlineKey() {");
		sc.add("return fUnderlineKey;");
		sc.add("}");
		sc.addLineBreak();
		sc.add("// @return the color preference key");
		sc.add("public String getColorKey() {");
		sc.add("return fColorKey;");
		sc.add("}");
		sc.addLineBreak();
		sc.add("// @return the display name");
		sc.add("public String getDisplayName() {");
		sc.add("return fDisplayName;");
		sc.add("}");
		sc.addLineBreak();
		sc.add("public String getEnableKey() {");
		sc.add("return fEnableKey;");
		sc.add("}");
		sc.addLineBreak();
		sc.add("public int compareTo(HighlightingColorListItem o) {");
		sc.add("return fDisplayName.compareTo(o.getDisplayName());");
		sc.add("}");
		sc.add("}");
		sc.addLineBreak();
	}

	private void addChangedRGBPreferenceClass(
			org.emftext.sdk.codegen.composites.StringComposite sc) {
		sc.add("private static class ChangedRGBPreference extends AbstractChangedPreference {");
		sc.addLineBreak();
		sc.add("private " + RGB + " newValue;");
		sc.addLineBreak();
		sc.add("public ChangedRGBPreference(String key, " + RGB + " newValue) {");
		sc.add("super(key);");
		sc.add("this.newValue = newValue;");
		sc.add("}");
		sc.addLineBreak();
		sc.add("public void apply(" + I_PREFERENCE_STORE + " store) {");
		sc.add("" + PREFERENCE_CONVERTER + ".setValue(store, getKey(), newValue);");
		sc.add("}");
		sc.add("}");
		sc.addLineBreak();
	}

	private void addChangedBooleanPreferenceClass(
			org.emftext.sdk.codegen.composites.StringComposite sc) {
		sc.add("private static class ChangedBooleanPreference extends AbstractChangedPreference {");
		sc.addLineBreak();
		sc.add("private boolean newValue;");
		sc.addLineBreak();
		sc.add("public ChangedBooleanPreference(String key, boolean newValue) {");
		sc.add("super(key);");
		sc.add("this.newValue = newValue;");
		sc.add("}");
		sc.addLineBreak();
		sc.add("public void apply(" + I_PREFERENCE_STORE + " store) {");
		sc.add("store.setValue(getKey(), newValue);");
		sc.add("}");
		sc.add("}");
		sc.addLineBreak();
	}

	private void addAbstractChangedPreferenceClass(
			org.emftext.sdk.codegen.composites.StringComposite sc) {
		sc.add("private abstract static class AbstractChangedPreference implements IChangedPreference {");
		sc.addLineBreak();
		sc.add("private String key;");
		sc.addLineBreak();
		sc.add("public AbstractChangedPreference(String key) {");
		sc.add("super();");
		sc.add("this.key = key;");
		sc.add("}");
		sc.addLineBreak();
		sc.add("public String getKey() {");
		sc.add("return key;");
		sc.add("}");
		sc.add("}");
		sc.addLineBreak();
	}

	private void addIChangePreferenceInterface(
			org.emftext.sdk.codegen.composites.StringComposite sc) {
		sc.add("private interface IChangedPreference {");
		sc.add("public void apply(" + I_PREFERENCE_STORE + " store);");
		sc.add("}");
		sc.addLineBreak();
	}

	private void addFields(org.emftext.sdk.codegen.composites.StringComposite sc) {
		sc.add("private final static " + antlrTokenHelperClassName + " tokenHelper = new " + antlrTokenHelperClassName + "();");
		sc.add("private static final " + MAP + "<String, " + LIST + "<HighlightingColorListItem>> content = new " + HASH_MAP + "<String, " + LIST + "<HighlightingColorListItem>>();");
		sc.add("private static final " + COLLECTION + "<IChangedPreference> changedPreferences = new " + ARRAY_LIST + "<IChangedPreference>();");
		sc.addLineBreak();
	}
}
