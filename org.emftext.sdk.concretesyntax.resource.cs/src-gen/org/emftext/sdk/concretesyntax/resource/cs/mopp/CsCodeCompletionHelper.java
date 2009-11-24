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

package org.emftext.sdk.concretesyntax.resource.cs.mopp;

// A CodeCompletionHelper can be used to derive completion proposals for partial
// documents. It runs the parser generated by EMFText in a special mode (i.e., the
// rememberExpectedElements mode). Based on the elements that are expected by the
// parser for different regions in the document, valid proposals are computed.
public class CsCodeCompletionHelper {
	
	// Computes a set of proposals for the given document assuming the cursor is
	// at 'cursorOffset'. The proposals are derived using the meta information, i.e.,
	// the generated language plug-in.
	//
	// @param metaInformation
	// @param content the documents content
	// @param cursorOffset
	// @return
	public java.util.Collection<String> computeCompletionProposals(org.emftext.sdk.concretesyntax.resource.cs.ICsTextResource originalResource, String content, int cursorOffset) {
		org.eclipse.emf.ecore.resource.ResourceSet resourceSet = new org.eclipse.emf.ecore.resource.impl.ResourceSetImpl();
		// the shadow resource needs the same URI because reference resolvers may use the URI to resolve external references
		org.emftext.sdk.concretesyntax.resource.cs.ICsTextResource resource = (org.emftext.sdk.concretesyntax.resource.cs.ICsTextResource) resourceSet.createResource(originalResource.getURI());
		java.io.ByteArrayInputStream inputStream = new java.io.ByteArrayInputStream(content.getBytes());
		org.emftext.sdk.concretesyntax.resource.cs.mopp.CsMetaInformation metaInformation = resource.getMetaInformation();
		org.emftext.sdk.concretesyntax.resource.cs.ICsTextParser parser = metaInformation.createParser(inputStream, null);
		final java.util.List<org.emftext.sdk.concretesyntax.resource.cs.ICsExpectedElement> expectedElements = java.util.Arrays.asList(parseToExpectedElements(parser, resource));
		if (expectedElements == null) {
			return java.util.Collections.emptyList();
		}
		if (expectedElements.size() == 0) {
			return java.util.Collections.emptyList();
		}
		java.util.List<org.emftext.sdk.concretesyntax.resource.cs.ICsExpectedElement> expectedElementsAt = java.util.Arrays.asList(getExpectedElements(expectedElements.toArray(new org.emftext.sdk.concretesyntax.resource.cs.ICsExpectedElement[expectedElements.size()]), cursorOffset));
		setPrefix(expectedElementsAt, content, cursorOffset);
		java.util.Collection<String> proposals = deriveProposals(expectedElementsAt, content, resource, cursorOffset);
		
		final java.util.List<String> sortedProposals = new java.util.ArrayList<String>(proposals);
		java.util.Collections.sort(sortedProposals);
		return sortedProposals;
	}
	
	public org.emftext.sdk.concretesyntax.resource.cs.ICsExpectedElement[] parseToExpectedElements(org.emftext.sdk.concretesyntax.resource.cs.ICsTextParser parser, org.emftext.sdk.concretesyntax.resource.cs.ICsTextResource resource) {
		final java.util.List<org.emftext.sdk.concretesyntax.resource.cs.ICsExpectedElement> expectedElements = parser.parseToExpectedElements(null, resource);
		if (expectedElements == null) {
			return new org.emftext.sdk.concretesyntax.resource.cs.ICsExpectedElement[0];
		}
		removeDuplicateEntries(expectedElements);
		removeInvalidEntriesAtEnd(expectedElements);
		for (org.emftext.sdk.concretesyntax.resource.cs.ICsExpectedElement expectedElement : expectedElements) {
			System.out.println("PARSER EXPECTS:   " + expectedElement);
		}
		return expectedElements.toArray(new org.emftext.sdk.concretesyntax.resource.cs.ICsExpectedElement[expectedElements.size()]);
	}
	
	private void removeDuplicateEntries(java.util.List<org.emftext.sdk.concretesyntax.resource.cs.ICsExpectedElement> expectedElements) {
		for (int i = 0; i < expectedElements.size() - 1; i++) {
			org.emftext.sdk.concretesyntax.resource.cs.ICsExpectedElement elementAtIndex = expectedElements.get(i);
			for (int j = i + 1; j < expectedElements.size();) {
				org.emftext.sdk.concretesyntax.resource.cs.ICsExpectedElement elementAtNext = expectedElements.get(j);
				if (elementAtIndex.equals(elementAtNext) && elementAtIndex.getStartExcludingHiddenTokens() == elementAtNext.getStartExcludingHiddenTokens()) {
					expectedElements.remove(j);
				} else {
					j++;
				}
			}
		}
	}
	
	private void removeInvalidEntriesAtEnd(java.util.List<org.emftext.sdk.concretesyntax.resource.cs.ICsExpectedElement> expectedElements) {
		for (int i = 0; i < expectedElements.size() - 1;) {
			org.emftext.sdk.concretesyntax.resource.cs.ICsExpectedElement elementAtIndex = expectedElements.get(i);
			org.emftext.sdk.concretesyntax.resource.cs.ICsExpectedElement elementAtNext = expectedElements.get(i + 1);
			if (elementAtIndex.getStartExcludingHiddenTokens() == elementAtNext.getStartExcludingHiddenTokens() && shouldRemove(elementAtIndex.getFollowSetID(), elementAtNext.getFollowSetID())) {
				expectedElements.remove(i + 1);
			} else {
				i++;
			}
		}
	}
	
	public boolean shouldRemove(int followSetID1, int followSetID2) {
		return followSetID1 != followSetID2;
	}
	
	private String findPrefix(java.util.List<org.emftext.sdk.concretesyntax.resource.cs.ICsExpectedElement> expectedElements, org.emftext.sdk.concretesyntax.resource.cs.ICsExpectedElement expectedAtCursor, String content, int cursorOffset) {
		if (cursorOffset < 0) {
			return "";
		}
		int end = 0;
		for (org.emftext.sdk.concretesyntax.resource.cs.ICsExpectedElement expectedElement : expectedElements) {
			if (expectedElement == expectedAtCursor) {
				final int start = expectedElement.getStartExcludingHiddenTokens();
				if (start >= 0  && start < Integer.MAX_VALUE) {
					end = start;
				}
				break;
			}
		}
		end = Math.min(end, cursorOffset);
		final String prefix = content.substring(end, Math.min(content.length(), cursorOffset));
		System.out.println("Found prefix '" + prefix + "'");
		return prefix;
	}
	
	private java.util.Collection<String> deriveProposals(java.util.List<org.emftext.sdk.concretesyntax.resource.cs.ICsExpectedElement> expectedElements, String content, org.emftext.sdk.concretesyntax.resource.cs.ICsTextResource resource, int cursorOffset) {
		java.util.Collection<String> resultSet = new java.util.HashSet<String>();
		for (org.emftext.sdk.concretesyntax.resource.cs.ICsExpectedElement expectedElement : expectedElements) {
			resultSet.addAll(deriveProposals(expectedElement, content, resource, cursorOffset));
		}
		return resultSet;
	}
	
	private java.util.Collection<String> deriveProposals(org.emftext.sdk.concretesyntax.resource.cs.ICsExpectedElement expectedElement, String content, org.emftext.sdk.concretesyntax.resource.cs.ICsTextResource resource, int cursorOffset) {
		org.emftext.sdk.concretesyntax.resource.cs.mopp.CsMetaInformation metaInformation = resource.getMetaInformation();
		org.emftext.sdk.concretesyntax.resource.cs.ICsLocationMap locationMap = resource.getLocationMap();
		if (expectedElement instanceof org.emftext.sdk.concretesyntax.resource.cs.mopp.CsExpectedCsString) {
			org.emftext.sdk.concretesyntax.resource.cs.mopp.CsExpectedCsString csString = (org.emftext.sdk.concretesyntax.resource.cs.mopp.CsExpectedCsString) expectedElement;
			return deriveProposal(csString, content, cursorOffset);
		} else if (expectedElement instanceof org.emftext.sdk.concretesyntax.resource.cs.mopp.CsExpectedStructuralFeature) {
			org.emftext.sdk.concretesyntax.resource.cs.mopp.CsExpectedStructuralFeature expectedFeature = (org.emftext.sdk.concretesyntax.resource.cs.mopp.CsExpectedStructuralFeature) expectedElement;
			org.eclipse.emf.ecore.EStructuralFeature feature = expectedFeature.getFeature();
			org.eclipse.emf.ecore.EClassifier featureType = feature.getEType();
			java.util.List<org.eclipse.emf.ecore.EObject> elementsAtCursor = locationMap.getElementsAt(cursorOffset);
			org.eclipse.emf.ecore.EObject container = null;
			// we need to skip the proxy elements at the cursor, because they are not the container for the reference we try to complete
			for (int i = 0; i < elementsAtCursor.size(); i++) {
				container = elementsAtCursor.get(i);
				if (!container.eIsProxy()) {
					break;
				}
			}
			if (container == null) {
				return java.util.Collections.emptySet();
			}
			if (feature instanceof org.eclipse.emf.ecore.EReference) {
				org.eclipse.emf.ecore.EReference reference = (org.eclipse.emf.ecore.EReference) feature;
				if (featureType instanceof org.eclipse.emf.ecore.EClass) {
					if (reference.isContainment()) {
						assert false;
					} else {
						return handleNCReference(metaInformation, container, reference, expectedElement.getPrefix());
					}
				}
			} else if (feature instanceof org.eclipse.emf.ecore.EAttribute) {
				org.eclipse.emf.ecore.EAttribute attribute = (org.eclipse.emf.ecore.EAttribute) feature;
				if (featureType instanceof org.eclipse.emf.ecore.EEnum) {
					org.eclipse.emf.ecore.EEnum enumType = (org.eclipse.emf.ecore.EEnum) featureType;
					return deriveProposals(expectedElement, enumType, content, cursorOffset);
				} else {
					// handle EAttributes (derive default value depending on
					// the type of the attribute, figure out token resolver, and
					// call deResolve())
					return handleAttribute(metaInformation, expectedFeature, container, attribute);
				}
			} else {
				// there should be no other subclass of EStructuralFeature
				assert false;
			}
		} else {
			// there should be no other class implementing IExpectedElement
			assert false;
		}
		return java.util.Collections.emptyList();
	}
	
	private java.util.Collection<String> deriveProposals(org.emftext.sdk.concretesyntax.resource.cs.ICsExpectedElement expectedElement, org.eclipse.emf.ecore.EEnum enumType, String content, int cursorOffset) {
		java.util.Collection<org.eclipse.emf.ecore.EEnumLiteral> enumLiterals = enumType.getELiterals();
		java.util.Collection<String> result = new java.util.HashSet<String>();
		for (org.eclipse.emf.ecore.EEnumLiteral literal : enumLiterals) {
			String proposal = literal.getLiteral();
			if (proposal.startsWith(expectedElement.getPrefix())) {
				result.add(proposal);
			}
		}
		return result;
	}
	
	private java.util.Collection<String> handleNCReference(org.emftext.sdk.concretesyntax.resource.cs.mopp.CsMetaInformation metaInformation, org.eclipse.emf.ecore.EObject container, org.eclipse.emf.ecore.EReference reference, java.lang.String prefix) {
		// handle non-containment references
		org.emftext.sdk.concretesyntax.resource.cs.ICsReferenceResolverSwitch resolverSwitch = metaInformation.getReferenceResolverSwitch();
		org.emftext.sdk.concretesyntax.resource.cs.ICsReferenceResolveResult<org.eclipse.emf.ecore.EObject> result = new org.emftext.sdk.concretesyntax.resource.cs.mopp.CsReferenceResolveResult<org.eclipse.emf.ecore.EObject>(true);
		resolverSwitch.resolveFuzzy(prefix, container, reference, 0, result);
		java.util.Collection<org.emftext.sdk.concretesyntax.resource.cs.ICsReferenceMapping<org.eclipse.emf.ecore.EObject>> mappings = result.getMappings();
		if (mappings != null) {
			java.util.Collection<String> resultSet = new java.util.HashSet<String>();
			for (org.emftext.sdk.concretesyntax.resource.cs.ICsReferenceMapping<org.eclipse.emf.ecore.EObject> mapping : mappings) {
				final String identifier = mapping.getIdentifier();
				resultSet.add(identifier);
			}
			return resultSet;
		}
		return java.util.Collections.emptyList();
	}
	
	private java.util.Collection<String> handleAttribute(org.emftext.sdk.concretesyntax.resource.cs.mopp.CsMetaInformation metaInformation, org.emftext.sdk.concretesyntax.resource.cs.mopp.CsExpectedStructuralFeature expectedFeature, org.eclipse.emf.ecore.EObject container, org.eclipse.emf.ecore.EAttribute attribute) {
		java.lang.Object defaultValue = getDefaultValue(attribute);
		if (defaultValue != null) {
			org.emftext.sdk.concretesyntax.resource.cs.ICsTokenResolverFactory tokenResolverFactory = metaInformation.getTokenResolverFactory();
			String tokenName = expectedFeature.getTokenName();
			if (tokenName != null) {
				org.emftext.sdk.concretesyntax.resource.cs.ICsTokenResolver tokenResolver = tokenResolverFactory.createTokenResolver(tokenName);
				if (tokenResolver != null) {
					String defaultValueAsString = tokenResolver.deResolve(defaultValue, attribute, container);
					java.util.Collection<String> resultSet = new java.util.HashSet<String>();
					resultSet.add(defaultValueAsString);
					return resultSet;
				}
			}
		}
		return java.util.Collections.emptyList();
	}
	
	private java.lang.Object getDefaultValue(org.eclipse.emf.ecore.EAttribute attribute) {
		String typeName = attribute.getEType().getName();
		if ("EString".equals(typeName)) {
			return "some" + org.emftext.sdk.concretesyntax.resource.cs.util.CsStringUtil.capitalize(attribute.getName());
		}
		System.out.println("CodeCompletionHelper.getDefaultValue() unknown type " + typeName);
		return attribute.getDefaultValue();
	}
	
	private java.util.Collection<String> deriveProposal(org.emftext.sdk.concretesyntax.resource.cs.mopp.CsExpectedCsString csString, String content, int cursorOffset) {
		String proposal = csString.getValue();
		java.util.Collection<String> result = new java.util.HashSet<String>(1);
		result.add(proposal);
		return result;
	}
	
	// Returns the element(s) that is most suitable at the given cursor
	// index based on the list of expected elements.
	//
	// @param cursorOffset
	// @param allExpectedElements
	// @return
	public org.emftext.sdk.concretesyntax.resource.cs.ICsExpectedElement[] getExpectedElements(final org.emftext.sdk.concretesyntax.resource.cs.ICsExpectedElement[] allExpectedElements, int cursorOffset) {
		
		java.util.List<org.emftext.sdk.concretesyntax.resource.cs.ICsExpectedElement> expectedAfterCursor = java.util.Arrays.asList(getElementsExpectedAt(allExpectedElements, cursorOffset));
		java.util.List<org.emftext.sdk.concretesyntax.resource.cs.ICsExpectedElement> expectedBeforeCursor = java.util.Arrays.asList(getElementsExpectedAt(allExpectedElements, cursorOffset - 1));
		System.out.println("parseToCursor(" + cursorOffset + ") BEFORE CURSOR " + expectedBeforeCursor);
		System.out.println("parseToCursor(" + cursorOffset + ") AFTER CURSOR  " + expectedAfterCursor);
		java.util.List<org.emftext.sdk.concretesyntax.resource.cs.ICsExpectedElement> allExpectedAtCursor = new java.util.ArrayList<org.emftext.sdk.concretesyntax.resource.cs.ICsExpectedElement>();
		allExpectedAtCursor.addAll(expectedAfterCursor);
		if (expectedBeforeCursor != null) {
			for (org.emftext.sdk.concretesyntax.resource.cs.ICsExpectedElement expectedBefore : expectedBeforeCursor) {
				// if the thing right before the cursor is something that could
				// be long we add it to the list of proposals
				if (expectedBefore instanceof org.emftext.sdk.concretesyntax.resource.cs.mopp.CsExpectedStructuralFeature) {
					//allExpectedAtCursor.clear();
					allExpectedAtCursor.add(expectedBefore);
				}
			}
		}
		return allExpectedAtCursor.toArray(new org.emftext.sdk.concretesyntax.resource.cs.ICsExpectedElement[allExpectedAtCursor.size()]);
	}
	
	private void setPrefix(java.util.List<org.emftext.sdk.concretesyntax.resource.cs.ICsExpectedElement> allExpectedElements, String content, int cursorOffset) {
		if (cursorOffset < 0) {
			return;
		}
		for (org.emftext.sdk.concretesyntax.resource.cs.ICsExpectedElement expectedElementAtCursor : allExpectedElements) {
			expectedElementAtCursor.setPrefix(findPrefix(allExpectedElements, expectedElementAtCursor, content, cursorOffset));
		}
	}
	
	public org.emftext.sdk.concretesyntax.resource.cs.ICsExpectedElement[] getElementsExpectedAt(org.emftext.sdk.concretesyntax.resource.cs.ICsExpectedElement[] allExpectedElements, int cursorOffset) {
		java.util.List<org.emftext.sdk.concretesyntax.resource.cs.ICsExpectedElement> expectedAtCursor = new java.util.ArrayList<org.emftext.sdk.concretesyntax.resource.cs.ICsExpectedElement>();
		for (int i = 0; i < allExpectedElements.length; i++) {
			org.emftext.sdk.concretesyntax.resource.cs.ICsExpectedElement expectedElement = allExpectedElements[i];
			
			int startIncludingHidden = expectedElement.getStartIncludingHiddenTokens();
			//int startExcludingHidden = expectedElement.getStartExcludingHiddenTokens();
			int end = getEnd(allExpectedElements, i);
			//System.out.println("END = " + end + " for " + expectedElement);
			if (cursorOffset >= startIncludingHidden &&			cursorOffset <= end) {
				expectedAtCursor.add(expectedElement);
			}
		}
		return expectedAtCursor.toArray(new org.emftext.sdk.concretesyntax.resource.cs.ICsExpectedElement[expectedAtCursor.size()]);
	}
	
	private int getEnd(org.emftext.sdk.concretesyntax.resource.cs.ICsExpectedElement[] allExpectedElements, int indexInList) {
		org.emftext.sdk.concretesyntax.resource.cs.ICsExpectedElement elementAtIndex = allExpectedElements[indexInList];
		int startIncludingHidden = elementAtIndex.getStartIncludingHiddenTokens();
		int startExcludingHidden = elementAtIndex.getStartExcludingHiddenTokens();
		for (int i = indexInList + 1; i < allExpectedElements.length; i++) {
			org.emftext.sdk.concretesyntax.resource.cs.ICsExpectedElement elementAtI = allExpectedElements[i];
			int startIncludingHiddenForI = elementAtI.getStartIncludingHiddenTokens();
			int startExcludingHiddenForI = elementAtI.getStartExcludingHiddenTokens();
			if (startIncludingHidden != startIncludingHiddenForI || startExcludingHidden != startExcludingHiddenForI) {
				return startIncludingHiddenForI - 1;
			}
		}
		return Integer.MAX_VALUE;
	}
}
