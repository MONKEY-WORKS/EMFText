package org.emftext.sdk.concretesyntax.resource.cs.mopp;

// A factory for ContextDependentURIFragments. Given a feasible reference resolver,
// the factory returns a matching fragment that used the resolver to resolver proxy
// objects.
//
// @param <ContainerType> the type of the class containing the reference to be resolved
// @param <ReferenceType> the type of the reference to be resolved
//
public class CsContextDependentURIFragmentFactory<ContainerType extends org.eclipse.emf.ecore.EObject, ReferenceType extends org.eclipse.emf.ecore.EObject>  implements org.emftext.sdk.concretesyntax.resource.cs.ICsContextDependentURIFragmentFactory<ContainerType, ReferenceType> {
	
	private final org.emftext.sdk.concretesyntax.resource.cs.ICsReferenceResolver<ContainerType, ReferenceType> resolver;
	
	public CsContextDependentURIFragmentFactory(org.emftext.sdk.concretesyntax.resource.cs.ICsReferenceResolver<ContainerType, ReferenceType> resolver) {
		this.resolver = resolver;
	}
	
	public org.emftext.sdk.concretesyntax.resource.cs.ICsContextDependentURIFragment<?> create(String identifier, ContainerType container, org.eclipse.emf.ecore.EReference reference, int positionInReference, org.eclipse.emf.ecore.EObject proxy) {
		
		return new org.emftext.sdk.concretesyntax.resource.cs.mopp.CsContextDependentURIFragment<ContainerType, ReferenceType>(identifier, container, reference, positionInReference, proxy) {
			public org.emftext.sdk.concretesyntax.resource.cs.ICsReferenceResolver<ContainerType, ReferenceType> getResolver() {
				return resolver;
			}
		};
	}
}