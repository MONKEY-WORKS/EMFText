package org.emftext.runtime.resource.impl;

import java.util.ArrayList;
import java.util.Collection;

import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.EObject;
import org.emftext.runtime.resource.IReferenceMapping;
import org.emftext.runtime.resource.IResolveResult;

public class ResolveResult implements IResolveResult {
	
	private Collection<IReferenceMapping> mappings;
	private String errorMessage;
	private boolean resolveFuzzy;

	public ResolveResult(boolean resolveFuzzy) {
		super();
		this.resolveFuzzy = resolveFuzzy;
	}

	public String getErrorMessage() {
		//assert mappings == null || mappings.size() == 0;
		return errorMessage;
	}

	public Collection<IReferenceMapping> getMappings() {
		assert errorMessage == null;
		return mappings;
	}

	public boolean wasResolved() {
		// TODO an additional check is needed here (the identifier
		// is not resolved if there is only a String-to-String
		// mapping that replaced an internal identifier with
		// another one. this check might also be performed in
		// EMFTextTreeAnalyser when the proxy object is removed
		// from the list of unresolved references.
		return mappings != null;
	}

	public boolean wasResolvedMultiple() {
		return mappings != null && mappings.size() > 1;
	}

	public boolean wasResolvedUniquely() {
		return mappings != null && mappings.size() == 1;
	}

	public void setErrorMessage(String message) {
		if (mappings != null) {
			return;
		}
		errorMessage = message;
	}

	public void addMapping(String identifier, EObject target) {
		if (resolveFuzzy && target == null) {
			throw new IllegalArgumentException("Mapping references to null is only allowed for fuzzy resolution.");
		}
		addMapping(identifier, target, null);
	}

	public void addMapping(String identifier, EObject target, String warning) {
		if (mappings == null) {
			mappings = new ArrayList<IReferenceMapping>();
		}
		mappings.add(new ElementMapping(identifier, target, warning));
		errorMessage = null;
	}

	public void addMapping(String identifier, URI uri) {
		addMapping(identifier, uri, null);
	}
	
	public void addMapping(String identifier, URI uri, String warning) {
		if (mappings == null) {
			mappings = new ArrayList<IReferenceMapping>();
		}
		mappings.add(new URIMapping(identifier, uri, warning));
	}
}
