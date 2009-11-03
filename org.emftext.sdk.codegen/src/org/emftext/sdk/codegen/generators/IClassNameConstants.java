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
package org.emftext.sdk.codegen.generators;

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.PushbackReader;
import java.io.Reader;
import java.io.StringReader;
import java.io.StringWriter;
import java.lang.reflect.InvocationTargetException;
import java.net.URL;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.Set;
import java.util.Stack;
import java.util.Timer;
import java.util.TimerTask;
import java.util.regex.Matcher;

import javax.swing.event.DocumentListener;

import org.antlr.runtime.ANTLRStringStream;
import org.antlr.runtime.BitSet;
import org.antlr.runtime.CommonToken;
import org.antlr.runtime.EarlyExitException;
import org.antlr.runtime.FailedPredicateException;
import org.antlr.runtime.IntStream;
import org.antlr.runtime.Lexer;
import org.antlr.runtime.MismatchedNotSetException;
import org.antlr.runtime.MismatchedRangeException;
import org.antlr.runtime.MismatchedSetException;
import org.antlr.runtime.MismatchedTokenException;
import org.antlr.runtime.MismatchedTreeNodeException;
import org.antlr.runtime.NoViableAltException;
import org.antlr.runtime.RecognitionException;
import org.antlr.runtime.Token;
import org.eclipse.core.resources.IContainer;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IMarker;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IResourceChangeEvent;
import org.eclipse.core.resources.IResourceChangeListener;
import org.eclipse.core.resources.IResourceDelta;
import org.eclipse.core.resources.IResourceDeltaVisitor;
import org.eclipse.core.resources.IWorkspace;
import org.eclipse.core.resources.IWorkspaceRoot;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.Assert;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.core.runtime.IExtensionRegistry;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.ListenerList;
import org.eclipse.core.runtime.Path;
import org.eclipse.core.runtime.Platform;
import org.eclipse.core.runtime.SafeRunner;
import org.eclipse.core.runtime.Status;
import org.eclipse.core.runtime.preferences.AbstractPreferenceInitializer;
import org.eclipse.emf.codegen.ecore.templates.editor.Editor;
import org.eclipse.emf.common.command.BasicCommandStack;
import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.notify.NotificationChain;
import org.eclipse.emf.common.notify.impl.AdapterImpl;
import org.eclipse.emf.common.util.BasicEList;
import org.eclipse.emf.common.util.BasicEMap;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.common.util.EMap;
import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EClassifier;
import org.eclipse.emf.ecore.EEnum;
import org.eclipse.emf.ecore.EEnumLiteral;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EOperation;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.ecore.InternalEObject;
import org.eclipse.emf.ecore.impl.EObjectImpl;
import org.eclipse.emf.ecore.provider.EcoreItemProviderAdapterFactory;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.emf.ecore.resource.URIConverter;
import org.eclipse.emf.ecore.resource.Resource.Diagnostic;
import org.eclipse.emf.ecore.resource.impl.ResourceImpl;
import org.eclipse.emf.ecore.resource.impl.ResourceSetImpl;
import org.eclipse.emf.ecore.util.BasicInternalEList;
import org.eclipse.emf.ecore.util.EObjectWithInverseResolvingEList;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.eclipse.emf.ecore.util.InternalEList;
import org.eclipse.emf.edit.domain.AdapterFactoryEditingDomain;
import org.eclipse.emf.edit.domain.EditingDomain;
import org.eclipse.emf.edit.domain.IEditingDomainProvider;
import org.eclipse.emf.edit.provider.ComposedAdapterFactory;
import org.eclipse.emf.edit.provider.IItemPropertyDescriptor;
import org.eclipse.emf.edit.provider.IItemPropertySource;
import org.eclipse.emf.edit.provider.ReflectiveItemProviderAdapterFactory;
import org.eclipse.emf.edit.provider.resource.ResourceItemProviderAdapterFactory;
import org.eclipse.emf.edit.ui.provider.AdapterFactoryContentProvider;
import org.eclipse.emf.edit.ui.provider.AdapterFactoryLabelProvider;
import org.eclipse.emf.edit.ui.provider.PropertyDescriptor;
import org.eclipse.emf.edit.ui.provider.PropertySource;
import org.eclipse.jface.action.Action;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.action.ToolBarManager;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.operation.IRunnableWithProgress;
import org.eclipse.jface.preference.ColorSelector;
import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.jface.preference.PreferenceConverter;
import org.eclipse.jface.preference.PreferencePage;
import org.eclipse.jface.resource.JFaceResources;
import org.eclipse.jface.text.AbstractInformationControl;
import org.eclipse.jface.text.AbstractReusableInformationControlCreator;
import org.eclipse.jface.text.BadLocationException;
import org.eclipse.jface.text.BadPositionCategoryException;
import org.eclipse.jface.text.DefaultInformationControl;
import org.eclipse.jface.text.Document;
import org.eclipse.jface.text.DocumentEvent;
import org.eclipse.jface.text.IDelayedInputChangeProvider;
import org.eclipse.jface.text.IDocument;
import org.eclipse.jface.text.IDocumentListener;
import org.eclipse.jface.text.IInformationControl;
import org.eclipse.jface.text.IInformationControlCreator;
import org.eclipse.jface.text.IInformationControlExtension2;
import org.eclipse.jface.text.IInformationControlExtension4;
import org.eclipse.jface.text.IInputChangedListener;
import org.eclipse.jface.text.IRegion;
import org.eclipse.jface.text.ITextHover;
import org.eclipse.jface.text.ITextHoverExtension;
import org.eclipse.jface.text.ITextHoverExtension2;
import org.eclipse.jface.text.ITextPresentationListener;
import org.eclipse.jface.text.ITextViewer;
import org.eclipse.jface.text.Position;
import org.eclipse.jface.text.Region;
import org.eclipse.jface.text.TextAttribute;
import org.eclipse.jface.text.TextPresentation;
import org.eclipse.jface.text.TextSelection;
import org.eclipse.jface.text.TextViewer;
import org.eclipse.jface.text.contentassist.CompletionProposal;
import org.eclipse.jface.text.contentassist.ContentAssistant;
import org.eclipse.jface.text.contentassist.ContextInformation;
import org.eclipse.jface.text.contentassist.ICompletionProposal;
import org.eclipse.jface.text.contentassist.IContentAssistProcessor;
import org.eclipse.jface.text.contentassist.IContentAssistant;
import org.eclipse.jface.text.contentassist.IContextInformation;
import org.eclipse.jface.text.contentassist.IContextInformationValidator;
import org.eclipse.jface.text.hyperlink.IHyperlink;
import org.eclipse.jface.text.hyperlink.IHyperlinkDetector;
import org.eclipse.jface.text.presentation.IPresentationReconciler;
import org.eclipse.jface.text.presentation.PresentationReconciler;
import org.eclipse.jface.text.rules.DefaultDamagerRepairer;
import org.eclipse.jface.text.rules.IToken;
import org.eclipse.jface.text.rules.ITokenScanner;
import org.eclipse.jface.text.source.Annotation;
import org.eclipse.jface.text.source.DefaultAnnotationHover;
import org.eclipse.jface.text.source.IAnnotationHover;
import org.eclipse.jface.text.source.ISourceViewer;
import org.eclipse.jface.text.source.IVerticalRuler;
import org.eclipse.jface.text.source.SourceViewerConfiguration;
import org.eclipse.jface.text.source.projection.ProjectionAnnotation;
import org.eclipse.jface.text.source.projection.ProjectionAnnotationModel;
import org.eclipse.jface.text.source.projection.ProjectionSupport;
import org.eclipse.jface.text.source.projection.ProjectionViewer;
import org.eclipse.jface.util.SafeRunnable;
import org.eclipse.jface.viewers.CellEditor;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.ISelectionProvider;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.ITreeContentProvider;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.jface.viewers.StructuredViewer;
import org.eclipse.jface.viewers.TreeSelection;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.jface.wizard.IWizard;
import org.eclipse.jface.wizard.Wizard;
import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.SWTError;
import org.eclipse.swt.browser.Browser;
import org.eclipse.swt.browser.LocationListener;
import org.eclipse.swt.browser.OpenWindowListener;
import org.eclipse.swt.browser.ProgressAdapter;
import org.eclipse.swt.browser.ProgressEvent;
import org.eclipse.swt.browser.WindowEvent;
import org.eclipse.swt.custom.StyleRange;
import org.eclipse.swt.custom.StyledText;
import org.eclipse.swt.custom.VerifyKeyListener;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.events.KeyListener;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.MouseListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.events.VerifyEvent;
import org.eclipse.swt.events.VerifyListener;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.FontData;
import org.eclipse.swt.graphics.FontMetrics;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.RGB;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.graphics.TextLayout;
import org.eclipse.swt.graphics.TextStyle;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.program.Program;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Dialog;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Link;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.MessageBox;
import org.eclipse.swt.widgets.ScrollBar;
import org.eclipse.swt.widgets.Scrollable;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Slider;
import org.eclipse.ui.IEditorDescriptor;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IMemento;
import org.eclipse.ui.INewWizard;
import org.eclipse.ui.IPartListener2;
import org.eclipse.ui.ISharedImages;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.IWorkbenchPartReference;
import org.eclipse.ui.IWorkbenchPreferencePage;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.XMLMemento;
import org.eclipse.ui.dialogs.ContainerSelectionDialog;
import org.eclipse.ui.editors.text.EditorsUI;
import org.eclipse.ui.editors.text.FileDocumentProvider;
import org.eclipse.ui.editors.text.TextEditor;
import org.eclipse.ui.part.FileEditorInput;
import org.eclipse.ui.part.IPageSite;
import org.eclipse.ui.part.Page;
import org.eclipse.ui.plugin.AbstractUIPlugin;
import org.eclipse.ui.texteditor.ContentAssistAction;
import org.eclipse.ui.texteditor.ITextEditorActionDefinitionIds;
import org.eclipse.ui.views.contentoutline.IContentOutlinePage;
import org.eclipse.ui.views.properties.IPropertyDescriptor;
import org.eclipse.ui.views.properties.IPropertySheetPage;
import org.eclipse.ui.views.properties.IPropertySource;
import org.eclipse.ui.views.properties.PropertySheetPage;
import org.eclipse.ui.wizards.IWizardCategory;
import org.osgi.framework.Bundle;
import org.osgi.framework.BundleContext;

/**
 * Constants for class names used in the generated code.
 * 
 * TODO find all other occurrences of class names in the generator package
 * and use constants instead. 
 */
public class IClassNameConstants {
	public static String ABSTRACT_INFORMATION_CONTROL = AbstractInformationControl.class.getName();
	public static String ABSTRACT_PREFERENCE_INITIALIZER = AbstractPreferenceInitializer.class.getName();
	public static String ABSTRACT_REUSABLE_INFORMATION_CONTROL_CREATOR = AbstractReusableInformationControlCreator.class.getName();
	public static String ABSTRACT_UI_PLUGIN = AbstractUIPlugin.class.getName();
	public static String ACTION = Action.class.getName();
	public static String ADAPTER_FACTORY_CONTENT_PROVIDER = AdapterFactoryContentProvider.class.getName();
	public static String ADAPTER_FACTORY_EDITING_DOMAIN = AdapterFactoryEditingDomain.class.getName();
	public static String ADAPTER_FACTORY_LABEL_PROVIDER = AdapterFactoryLabelProvider.class.getName();
	public static String ADAPTER_IMPL = AdapterImpl.class.getName();
	public static String ANNOTATION = Annotation.class.getName();
	public static String ANTLR_PARSER = org.antlr.runtime.Parser.class.getName();
	public static String ANTLR_STRING_STREAM = ANTLRStringStream.class.getName();
	public static String ARRAYS = java.util.Arrays.class.getName();
	public static String ARRAY_LIST = ArrayList.class.getName();
	public static String ASSERT = Assert.class.getName();
	public static String BAD_LOCATION_EXCEPTION = BadLocationException.class.getName();
	public static String BAD_POSITION_CATEGORY_EXCEPTION = BadPositionCategoryException.class.getName();
	public static String BASIC_COMMAND_STACK = BasicCommandStack.class.getName();
	public static String BASIC_E_LIST = BasicEList.class.getName();
	public static String BASIC_E_MAP = BasicEMap.class.getName();
	public static String BASIC_INTERNAL_E_LIST = BasicInternalEList.class.getName();
	public static String BIT_SET = BitSet.class.getName();
	public static String BROWSER = Browser.class.getName();
	public static String BUFFERED_OUTPUT_STREAM = BufferedOutputStream.class.getName();
	public static String BUFFERED_READER = BufferedReader.class.getName();
	public static String BUNDLE = Bundle.class.getName();
	public static String BUNDLE_CONTEXT = BundleContext.class.getName();
	public static String BUTTON = Button.class.getName();
	public static String BYTE_ARRAY_INPUT_STREAM = ByteArrayInputStream.class.getName();
	public static String BYTE_ARRAY_OUTPUT_STREAM = ByteArrayOutputStream.class.getName();
	public static String CELL_EDITOR = CellEditor.class.getName();
	public static String COLLECTION = Collection.class.getName();
	public static String COLLECTIONS = Collections.class.getName();
	public static String COLOR = Color.class.getName();
	public static String COLOR_SELECTOR = ColorSelector.class.getName();
	public static String COMBO = Combo.class.getName();
	public static String COMMON_TOKEN = CommonToken.class.getName();
	public static String COMPARATOR = Comparator.class.getName();
	public static String COMPLETION_PROPOSAL = CompletionProposal.class.getName();
	public static String COMPOSED_ADAPTER_FACTORY = ComposedAdapterFactory.class.getName();
	public static String COMPOSITE = Composite.class.getName();
	public static String CONTAINER_SELECTION_DIALOG = ContainerSelectionDialog.class.getName();
	public static String CONTENT_ASSISTANT = ContentAssistant.class.getName();
	public static String CONTENT_ASSIST_ACTION = ContentAssistAction.class.getName();
	public static String CONTEXT_INFORMATION = ContextInformation.class.getName();
	public static String CONTROL = Control.class.getName();
	public static String CORE_EXCEPTION = CoreException.class.getName();
	public static String DEFAULT_ANNOTATION_HOVER = DefaultAnnotationHover.class.getName();
	public static String DEFAULT_DAMAGER_REPAIRER = DefaultDamagerRepairer.class.getName();
	public static String DEFAULT_INFORMATION_CONTROL = DefaultInformationControl.class.getName();
	public static String DIAGNOSTIC = Diagnostic.class.getCanonicalName();
	public static String DIALOG = Dialog.class.getName();
	public static String DISPLAY = Display.class.getName();
	public static String DOCUMENT = Document.class.getName();
	public static String DOCUMENT_EVENT = DocumentEvent.class.getName();
	public static String DOCUMENT_LISTENER = DocumentListener.class.getName();
	public static String EARLY_EXIT_EXCEPTION = EarlyExitException.class.getName();
	public static String ECORE_ITEM_PROVIDER_ADAPTER_FACTORY = EcoreItemProviderAdapterFactory.class.getName();
	public static String ECORE_UTIL = EcoreUtil.class.getName();
	public static String EDITING_DOMAIN = EditingDomain.class.getName();
	public static String EDITOR = Editor.class.getName();
	public static String EDITORS_UI = EditorsUI.class.getName();
	public static String ELEMENT_BASED_TEXT_DIAGNOSTIC = "ElementBasedTextDiagnostic";
	public static String EXCEPTION = Exception.class.getName();
	public static String E_ATTRIBUTE = EAttribute.class.getName();
	public static String E_CLASS = EClass.class.getName();
	public static String E_CLASSIFIER = EClassifier.class.getName();
	public static String E_ENUM = EEnum.class.getName();
	public static String E_ENUM_LITERAL = EEnumLiteral.class.getName();
	public static String E_LIST = EList.class.getName();
	public static String E_MAP = EMap.class.getName();
	public static String E_OBJECT = EObject.class.getName();
	public static String E_OBJECT_IMPL = EObjectImpl.class.getName();
	public static String E_OPERATION = EOperation.class.getName();
	public static String E_PACKAGE = EPackage.class.getName();
	public static String E_REFERENCE = EReference.class.getName();
	public static String E_STRUCTURAL_FEATURE = EStructuralFeature.class.getName();
	public static String FAILED_PREDICATE_EXCEPTION = FailedPredicateException.class.getName();
	public static String FILE = File.class.getName();
	public static String FILE_DOCUMENT_PROVIDER = FileDocumentProvider.class.getName();
	public static String FILE_EDITOR_INPUT = FileEditorInput.class.getName();
	public static String FILE_INPUT_STREAM = FileInputStream.class.getName();
	public static String FILE_OUTPUT_STREAM = FileOutputStream.class.getName();
	public static String FONT = Font.class.getName();
	public static String FONT_DATA = FontData.class.getName();
	public static String FONT_METRICS = FontMetrics.class.getName();
	public static String GC = GC.class.getName();
	public static String GRID_DATA = GridData.class.getName();
	public static String GRID_LAYOUT = GridLayout.class.getName();
	public static String HASH_MAP = HashMap.class.getName();
	public static String HASH_SET = HashSet.class.getName();
	public static String IDE = org.eclipse.ui.ide.IDE.class.getName();
	public static String ILLEGAL_ARGUMENT_EXCEPTION = IllegalArgumentException.class.getName();
	public static String INPUT_STREAM = InputStream.class.getName();
	public static String INPUT_STREAM_READER = InputStreamReader.class.getName();
	public static String INTEGER = Integer.class.getName();
	public static String INTERNAL_E_LIST = InternalEList.class.getName();
	public static String INTERNAL_E_OBJECT = InternalEObject.class.getName();
	public static String INT_STREAM = IntStream.class.getName();
	public static String INVOCATION_TARGET_EXCEPTION = InvocationTargetException.class.getName();
	public static String IO_EXCEPTION = IOException.class.getName();
	public static String ITERATOR = Iterator.class.getName();
	public static String I_ACTION = IAction.class.getName();
	public static String I_ANNOTATION_HOVER = IAnnotationHover.class.getName();
	public static String I_COMPLETION_PROPOSAL = ICompletionProposal.class.getName();
	public static String I_CONFIGURATION_ELEMENT = IConfigurationElement.class.getName();
	public static String I_CONTAINER = IContainer.class.getName();
	public static String I_CONTENT_ASSISTANT = IContentAssistant.class.getName();
	public static String I_CONTENT_ASSIST_PROCESSOR = IContentAssistProcessor.class.getName();
	public static String I_CONTENT_OUTLINE_PAGE = IContentOutlinePage.class.getName();
	public static String I_CONTEXT_INFORMATION = IContextInformation.class.getName();
	public static String I_CONTEXT_INFORMATION_VALIDATOR = IContextInformationValidator.class.getName();
	public static String I_DELAYED_INPUT_CHANGE_PROVIDER = IDelayedInputChangeProvider.class.getName();
	public static String I_DIALOG_CONSTANTS = IDialogConstants.class.getName();
	public static String I_DOCUMENT = IDocument.class.getName();
	public static String I_DOCUMENT_LISTENER = IDocumentListener.class.getName();
	public static String I_EDITING_DOMAIN_PROVIDER = IEditingDomainProvider.class.getName();
	public static String I_EDITOR_DESCRIPTOR = IEditorDescriptor.class.getName();
	public static String I_EDITOR_INPUT = IEditorInput.class.getName();
	public static String I_EDITOR_PART = IEditorPart.class.getName();
	public static String I_EXTENSION_REGISTRY = IExtensionRegistry.class.getName();
	public static String I_FILE = IFile.class.getName();
	public static String I_HYPERLINK = IHyperlink.class.getName();
	public static String I_HYPERLINK_DETECTOR = IHyperlinkDetector.class.getName();
	public static String I_INFORMATION_CONTROL = IInformationControl.class.getName();
	public static String I_INFORMATION_CONTROL_CREATOR = IInformationControlCreator.class.getName();
	public static String I_INFORMATION_CONTROL_EXTENSION2 = IInformationControlExtension2.class.getName();
	public static String I_INFORMATION_CONTROL_EXTENSION4 = IInformationControlExtension4.class.getName();
	public static String I_INPUT_CHANGED_LISTENER = IInputChangedListener.class.getName();
	public static String I_ITEM_PROPERTY_DESCRIPTOR = IItemPropertyDescriptor.class.getName();
	public static String I_ITEM_PROPERTY_SOURCE = IItemPropertySource.class.getName();
	public static String I_MARKER = IMarker.class.getName();
	public static String I_MEMENTO = IMemento.class.getName();
	public static String I_NEW_WIZARD = INewWizard.class.getName();
	public static String I_PAGE_SITE = IPageSite.class.getName();
	public static String I_PART_LISTENER2 = IPartListener2.class.getName();
	public static String I_PATH = IPath.class.getName();
	public static String I_PREFERENCE_STORE = IPreferenceStore.class.getName();
	public static String I_PRESENTATION_RECONCILER = IPresentationReconciler.class.getName();
	public static String I_PROGRESS_MONITOR = IProgressMonitor.class.getName();
	public static String I_PROPERTY_DESCRIPTOR = IPropertyDescriptor.class.getName();
	public static String I_PROPERTY_SHEET_PAGE = IPropertySheetPage.class.getName();
	public static String I_PROPERTY_SOURCE = IPropertySource.class.getName();
	public static String I_REGION = IRegion.class.getName();
	public static String I_RESOURCE = IResource.class.getName();
	public static String I_RESOURCE_CHANGE_EVENT = IResourceChangeEvent.class.getName();
	public static String I_RESOURCE_CHANGE_LISTENER = IResourceChangeListener.class.getName();
	public static String I_RESOURCE_DELTA = IResourceDelta.class.getName();
	public static String I_RESOURCE_DELTA_VISITOR = IResourceDeltaVisitor.class.getName();
	public static String I_RUNNABLE_WITH_PROGRESS = IRunnableWithProgress.class.getName();
	public static String I_SELECTION = ISelection.class.getName();
	public static String I_SELECTION_CHANGED_LISTENER = ISelectionChangedListener.class.getName();
	public static String I_SELECTION_PROVIDER = ISelectionProvider.class.getName();
	public static String I_SHARED_IMAGES = ISharedImages.class.getName();
	public static String I_SOURCE_VIEWER = ISourceViewer.class.getName();
	public static String I_STATUS = IStatus.class.getName();
	public static String I_STRUCTURED_SELECTION = IStructuredSelection.class.getName();
	public static String I_TEXT_EDITOR_ACTION_DEFINITION_IDS = ITextEditorActionDefinitionIds.class.getName();
	public static String I_TEXT_HOVER = ITextHover.class.getName();
	public static String I_TEXT_HOVER_EXTENSION = ITextHoverExtension.class.getName();
	public static String I_TEXT_HOVER_EXTENSION2 = ITextHoverExtension2.class.getName();
	public static String I_TEXT_PRESENTATION_LISTENER = ITextPresentationListener.class.getName();
	public static String I_TEXT_VIEWER = ITextViewer.class.getName();
	public static String I_TOKEN = IToken.class.getName();
	public static String I_TOKEN_SCANNER = ITokenScanner.class.getName();
	public static String I_TREE_CONTENT_PROVIDER = ITreeContentProvider.class.getName();
	public static String I_VERTICAL_RULER = IVerticalRuler.class.getName();
	public static String I_WIZARD = IWizard.class.getName();
	public static String I_WIZARD_CATEGORY = IWizardCategory.class.getName();
	public static String I_WORKBENCH = IWorkbench.class.getName();
	public static String I_WORKBENCH_PAGE = IWorkbenchPage.class.getName();
	public static String I_WORKBENCH_PART = IWorkbenchPart.class.getName();
	public static String I_WORKBENCH_PART_REFERENCE = IWorkbenchPartReference.class.getName();
	public static String I_WORKBENCH_PREFERENCE_PAGE = IWorkbenchPreferencePage.class.getName();
	public static String I_WORKSPACE = IWorkspace.class.getName();
	public static String I_WORKSPACE_ROOT = IWorkspaceRoot.class.getName();
	public static String JFACE_DIALOG = org.eclipse.jface.dialogs.Dialog.class.getName();
	public static String JOB = org.eclipse.core.runtime.jobs.Job.class.getName();
	public static String J_FACE_RESOURCES = JFaceResources.class.getName();
	public static String KEY_EVENT = KeyEvent.class.getName();
	public static String KEY_LISTENER = KeyListener.class.getName();
	public static String LABEL = Label.class.getName();
	public static String LABEL_PROVIDER = LabelProvider.class.getName();
	public static String LEXER = Lexer.class.getName();
	public static String LINK = Link.class.getName();
	public static String LINKED_HASH_MAP = LinkedHashMap.class.getName();
	public static String LINKED_HASH_SET = LinkedHashSet.class.getName();
	public static String LINKED_LIST = LinkedList.class.getName();
	public static String LIST = List.class.getName();
	public static String LISTENER = Listener.class.getName();
	public static String LISTENER_LIST = ListenerList.class.getName();
	public static String LIST_ITERATOR = ListIterator.class.getName();
	public static String LOCATION_LISTENER = LocationListener.class.getName();
	public static String MANY_INVERSE = EObjectWithInverseResolvingEList.ManyInverse.class.getCanonicalName();
	public static String MAP = Map.class.getName();
	public static String MATCHER = Matcher.class.getName();
	public static String MATH = Math.class.getName();
	public static String MENU = Menu.class.getName();
	public static String MESSAGE_BOX = MessageBox.class.getName();
	public static String MESSAGE_DIALOG = MessageDialog.class.getName();
	public static String MESSAGE_DIGEST = MessageDigest.class.getName();
	public static String METHOD = java.lang.reflect.Method.class.getName();
	public static String MISMATCHED_NOT_SET_EXCEPTION = MismatchedNotSetException.class.getName();
	public static String MISMATCHED_RANGE_EXCEPTION = MismatchedRangeException.class.getName();
	public static String MISMATCHED_SET_EXCEPTION = MismatchedSetException.class.getName();
	public static String MISMATCHED_TOKEN_EXCEPTION = MismatchedTokenException.class.getName();
	public static String MISMATCHED_TREE_NODE_EXCEPTION = MismatchedTreeNodeException.class.getName();
	public static String MODIFY_EVENT = ModifyEvent.class.getName();
	public static String MODIFY_LISTENER = ModifyListener.class.getName();
	public static String MOUSE_EVENT = MouseEvent.class.getName();
	public static String MOUSE_LISTENER = MouseListener.class.getName();
	public static String NOTIFICATION = Notification.class.getName();
	public static String NOTIFICATION_CHAIN = NotificationChain.class.getName();
	public static String NO_SUCH_ALGORITHM_EXCEPTION = NoSuchAlgorithmException.class.getName();
	public static String NO_VIABLE_ALT_EXCEPTION = NoViableAltException.class.getName();
	public static String NULL_POINTER_EXCEPTION = NullPointerException.class.getName();
	public static String OBJECT = Object.class.getName();
	public static String OPEN_WINDOW_LISTENER = OpenWindowListener.class.getName();
	public static String OUTPUT_STREAM = OutputStream.class.getName();
	public static String OUTPUT_STREAM_WRITER = OutputStreamWriter.class.getName();
	public static String PAGE = Page.class.getName();
	public static String PART_INIT_EXCEPTION = PartInitException.class.getName();
	public static String PATH = Path.class.getName();
	public static String PATTERN = java.util.regex.Pattern.class.getName();
	public static String PLATFORM = Platform.class.getName();
	public static String PLATFORM_UI = PlatformUI.class.getName();
	public static String POINT = Point.class.getName();
	public static String POSITION = Position.class.getName();
	public static String POSITION_BASED_TEXT_DIAGNOSTIC = "PositionBasedTextDiagnostic";
	public static String PREFERENCE_CONVERTER = PreferenceConverter.class.getName();
	public static String PREFERENCE_PAGE = PreferencePage.class.getName();
	public static String PRESENTATION_RECONCILER = PresentationReconciler.class.getName();
	public static String PRINTER_WRITER = PrintWriter.class.getName();
	public static String PROGRAM = Program.class.getName();
	public static String PROGRESS_ADAPTER = ProgressAdapter.class.getName();
	public static String PROGRESS_EVENT = ProgressEvent.class.getName();
	public static String PROJECTION_ANNOTATION = ProjectionAnnotation.class.getName();
	public static String PROJECTION_ANNOTATION_MODEL = ProjectionAnnotationModel.class.getName();
	public static String PROJECTION_SUPPORT = ProjectionSupport.class.getName();
	public static String PROJECTION_VIEWER = ProjectionViewer.class.getName();
	public static String PROPERTY_DESCRIPTOR = PropertyDescriptor.class.getName();
	public static String PROPERTY_SHEET_PAGE = PropertySheetPage.class.getName();
	public static String PROPERTY_SOURCE = PropertySource.class.getName();
	public static String PUSHBACK_READER = PushbackReader.class.getName();
	public static String READER = Reader.class.getName();
	public static String RECOGNITION_EXCEPTION = RecognitionException.class.getName();
	public static String RECOGNIZER_SHARED_STATE = org.antlr.runtime.RecognizerSharedState.class.getName();
	public static String RECTANGLE = Rectangle.class.getName();
	public static String REFLECTIVE_ITEM_PROVIDER_ADAPTER_FACTORY = ReflectiveItemProviderAdapterFactory.class.getName();
	public static String REGION = Region.class.getName();
	public static String RESOLVER_SWITCH_FIELD_NAME = "resolverSwitch";
	public static String RESOURCE = org.eclipse.emf.ecore.resource.Resource.class.getName();
	public static String RESOURCES_PLUGIN = ResourcesPlugin.class.getName();
	public static String RESOURCE_BUNDLE = ResourceBundle.class.getName();
	public static String RESOURCE_IMPL = ResourceImpl.class.getName();
	public static String RESOURCE_ITEM_PROVIDER_ADAPTER_FACTORY = ResourceItemProviderAdapterFactory.class.getName();
	public static String RESOURCE_SET = ResourceSet.class.getName();
	public static String RESOURCE_SET_IMPL = ResourceSetImpl.class.getName();
	public static String RGB = RGB.class.getName();
	public static String RUNNABLE = Runnable.class.getName();
	public static String RUNTIME_EXCEPTION = RuntimeException.class.getName();
	public static String SAFE_RUNNABLE = SafeRunnable.class.getName();
	public static String SAFE_RUNNER = SafeRunner.class.getName();
	public static String SCROLLABLE = Scrollable.class.getName();
	public static String SCROLL_BAR = ScrollBar.class.getName();
	public static String SELECTION_ADAPTER = SelectionAdapter.class.getName();
	public static String SELECTION_CHANGED_EVENT = SelectionChangedEvent.class.getName();
	public static String SELECTION_EVENT = SelectionEvent.class.getName();
	public static String SELECTION_LISTENER = SelectionListener.class.getName();
	public static String SET = Set.class.getName();
	public static String SHELL = Shell.class.getName();
	public static String SLIDER = Slider.class.getName();
	public static String SOURCE_VIEWER_CONFIGURATION = SourceViewerConfiguration.class.getName();
	public static String STACK = Stack.class.getName();
	public static String STATUS = Status.class.getName();
	public static String STRING = String.class.getName();
	public static String STRING_READER = StringReader.class.getName();
	public static String STRING_WRITER = StringWriter.class.getName();
	public static String STRUCTURED_SELECTION = StructuredSelection.class.getName();
	public static String STRUCTURED_VIEWER = StructuredViewer.class.getName();
	public static String STYLED_TEXT = StyledText.class.getName();
	public static String STYLE_RANGE = StyleRange.class.getName();
	public static String SWT = SWT.class.getName();
	public static String SWT_ERROR = SWTError.class.getName();
	public static String SWT_LIST = org.eclipse.swt.widgets.List.class.getName();
	public static String TEXT = org.eclipse.swt.widgets.Text.class.getName();
	public static String TEXT_ATTRIBUTE = TextAttribute.class.getName();
	public static String TEXT_EDITOR = TextEditor.class.getName();
	public static String TEXT_LAYOUT = TextLayout.class.getName();
	public static String TEXT_PRESENTATION = TextPresentation.class.getName();
	public static String TEXT_SELECTION = TextSelection.class.getName();
	public static String TEXT_STYLE = TextStyle.class.getName();
	public static String TEXT_VIEWER = TextViewer.class.getName();
	public static String TIMER = Timer.class.getName();
	public static String TIMER_TASK = TimerTask.class.getName();
	public static String TOKEN = Token.class.getName();
	public static String TOKEN_STREAM = org.antlr.runtime.TokenStream.class.getName();
	public static String TOOL_BAR_MANAGER = ToolBarManager.class.getName();
	public static String TREE_SELECTION = TreeSelection.class.getName();
	public static String TREE_VIEWER = TreeViewer.class.getName();
	public static String URI = org.eclipse.emf.common.util.URI.class.getName();
	public static String URI_CONVERTER = URIConverter.class.getName();
	public static String URL = URL.class.getName();
	public static String VERIFY_EVENT = VerifyEvent.class.getName();
	public static String VERIFY_KEY_LISTENER = VerifyKeyListener.class.getName();
	public static String VERIFY_LISTENER = VerifyListener.class.getName(); 
	public static String VIEWER = Viewer.class.getName();
	public static String WINDOW_EVENT = WindowEvent.class.getName();
	public static String WIZARD = Wizard.class.getName();
	public static String WIZARD_PAGE = WizardPage.class.getName();
	public static String XML_MEMENTO = XMLMemento.class.getName();
}
