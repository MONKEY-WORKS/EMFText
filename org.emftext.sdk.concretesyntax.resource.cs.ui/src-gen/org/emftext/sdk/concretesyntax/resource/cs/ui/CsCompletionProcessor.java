/*******************************************************************************
 * Copyright (c) 2006-2010 
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

package org.emftext.sdk.concretesyntax.resource.cs.ui;

public class CsCompletionProcessor implements org.eclipse.jface.text.contentassist.IContentAssistProcessor {
	
	private org.emftext.sdk.concretesyntax.resource.cs.ui.CsEditor editor;
	
	public CsCompletionProcessor(org.emftext.sdk.concretesyntax.resource.cs.ui.CsEditor editor) {
		this.editor = editor;
	}
	
	public org.eclipse.jface.text.contentassist.ICompletionProposal[] computeCompletionProposals(org.eclipse.jface.text.ITextViewer viewer, int offset) {
		
		org.eclipse.emf.ecore.resource.Resource resource = editor.getResource();
		org.emftext.sdk.concretesyntax.resource.cs.ICsTextResource textResource = (org.emftext.sdk.concretesyntax.resource.cs.ICsTextResource) resource;
		java.lang.String content = viewer.getDocument().get();
		org.emftext.sdk.concretesyntax.resource.cs.ui.CsCodeCompletionHelper helper = new org.emftext.sdk.concretesyntax.resource.cs.ui.CsCodeCompletionHelper();
		org.emftext.sdk.concretesyntax.resource.cs.ui.CsCompletionProposal[] computedProposals = helper.computeCompletionProposals(textResource, content, offset);
		
		// call completion proposal post processor to allow for customizing the proposals
		org.emftext.sdk.concretesyntax.resource.cs.ui.CsProposalPostProcessor proposalPostProcessor = new org.emftext.sdk.concretesyntax.resource.cs.ui.CsProposalPostProcessor();
		java.util.List<org.emftext.sdk.concretesyntax.resource.cs.ui.CsCompletionProposal> computedProposalList = new java.util.ArrayList<org.emftext.sdk.concretesyntax.resource.cs.ui.CsCompletionProposal>(computedProposals.length);
		java.util.List<org.emftext.sdk.concretesyntax.resource.cs.ui.CsCompletionProposal> extendedProposalList = proposalPostProcessor.process(computedProposalList);
		if (extendedProposalList == null) {
			extendedProposalList = java.util.Collections.emptyList();
		}
		java.util.List<org.emftext.sdk.concretesyntax.resource.cs.ui.CsCompletionProposal> finalProposalList = new java.util.ArrayList<org.emftext.sdk.concretesyntax.resource.cs.ui.CsCompletionProposal>();
		for (org.emftext.sdk.concretesyntax.resource.cs.ui.CsCompletionProposal proposal : extendedProposalList) {
			if (proposal.getMatchesPrefix()) {
				finalProposalList.add(proposal);
			}
		}
		org.eclipse.jface.text.contentassist.ICompletionProposal[] result = new org.eclipse.jface.text.contentassist.ICompletionProposal[finalProposalList.size()];
		int i = 0;
		for (org.emftext.sdk.concretesyntax.resource.cs.ui.CsCompletionProposal proposal : finalProposalList) {
			java.lang.String proposalString = proposal.getInsertString();
			java.lang.String displayString = proposal.getDisplayString();
			java.lang.String prefix = proposal.getPrefix();
			org.eclipse.swt.graphics.Image image = proposal.getImage();
			org.eclipse.jface.text.contentassist.IContextInformation info;
			info = new org.eclipse.jface.text.contentassist.ContextInformation(image, proposalString, proposalString);
			int begin = offset - prefix.length();
			int replacementLength = prefix.length();
			// if a closing bracket was automatically inserted right before, we enlarge the
			// replacement length in order to overwrite the bracket.
			org.emftext.sdk.concretesyntax.resource.cs.ui.ICsBracketHandler bracketHandler = editor.getBracketHandler();
			java.lang.String closingBracket = bracketHandler.getClosingBracket();
			if (bracketHandler.addedClosingBracket() && proposalString.endsWith(closingBracket)) {
				replacementLength += closingBracket.length();
			}
			result[i++] = new org.eclipse.jface.text.contentassist.CompletionProposal(proposalString, begin, replacementLength, proposalString.length(), image, displayString, info, proposalString);
		}
		return result;
	}
	
	public org.eclipse.jface.text.contentassist.IContextInformation[] computeContextInformation(org.eclipse.jface.text.ITextViewer viewer, int offset) {
		return null;
	}
	
	public char[] getCompletionProposalAutoActivationCharacters() {
		return null;
	}
	
	public char[] getContextInformationAutoActivationCharacters() {
		return null;
	}
	
	public org.eclipse.jface.text.contentassist.IContextInformationValidator getContextInformationValidator() {
		return null;
	}
	
	public String getErrorMessage() {
		return null;
	}
}
