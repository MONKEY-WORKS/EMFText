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
package org.emftext.sdk.finders;

import java.io.File;
import java.io.IOException;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IWorkspace;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Platform;
import org.eclipse.core.runtime.Status;
import org.eclipse.core.runtime.jobs.Job;
import org.eclipse.emf.codegen.ecore.genmodel.GenFeature;
import org.eclipse.emf.codegen.ecore.genmodel.GenModel;
import org.eclipse.emf.codegen.ecore.genmodel.GenPackage;
import org.eclipse.emf.common.util.Diagnostic;
import org.eclipse.emf.common.util.DiagnosticException;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.emftext.sdk.EMFTextSDKPlugin;
import org.emftext.sdk.MetamodelManager;
import org.emftext.sdk.codegen.OptionManager;
import org.emftext.sdk.concretesyntax.ConcreteSyntax;
import org.emftext.sdk.concretesyntax.GenPackageDependentElement;
import org.emftext.sdk.concretesyntax.OptionTypes;

/**
 * An abstract super class for all finders that search for generator 
 * packages in files. Concrete sub classes basically determine the
 * URI of the file to look in and the remaining functionality (loading
 * generator models, updating them) is performed in this class.
 */
public abstract class GenPackageInFileFinder implements IGenPackageFinder {

	protected IGenPackageFinderResult findGenPackage(ConcreteSyntax syntax, String nsURI, final ResourceSet rs, URI genModelURI) {
		Resource genModelResource = null;
		
		try {
			genModelResource = rs.getResource(genModelURI, true);
		} catch (Exception e) {}
		
		EList<EObject> contents = null; 
		if (genModelResource != null) {
			contents = genModelResource.getContents();	
		}
		if (contents != null && contents.size() > 0) {
			GenModel genModel = (GenModel) contents.get(0);
			File ecoreFile = null;
			File genmodelFile = null;
			
			if (Platform.isRunning()) {
				// reload generator model if option is enabled
				boolean reloadEnabled = OptionManager.INSTANCE.getBooleanOptionValue(syntax, OptionTypes.RELOAD_GENERATOR_MODEL);
				if (reloadEnabled) {
					genModel = reloadGeneratorModel(genModel, rs);
				}

				if (genModelResource != null) {
					URI genmodelURI = genModelResource.getURI();
					if (genmodelURI != null && genModelURI.isPlatform()) {
						IResource genmodelMember = ResourcesPlugin.getWorkspace().getRoot().findMember(genmodelURI.toPlatformString(true));
						if (genmodelMember != null) {
							genmodelFile = genmodelMember.getLocation().toFile();
						}
					}
				}
				// find the Ecore files used by the generator model 
				List<GenFeature> allGenFeatures = genModel.getAllGenFeatures();
				for (GenFeature genFeature : allGenFeatures) {
					final Resource ecoreResource = genFeature.getEcoreFeature().eResource();
					if (ecoreResource == null) {
						continue;
					}
					URI ecoreURI = ecoreResource.getURI();
					if (ecoreURI == null) {
						continue;
					}
					if(genModelURI.isPlatform()) {
						IResource ecoreMember = ResourcesPlugin.getWorkspace().getRoot().findMember(ecoreURI.toPlatformString(true));
						if (ecoreMember != null) {
							ecoreFile = ecoreMember.getLocation().toFile();
						}
					}
					break;
				}
			}

			Map<String, GenPackage> packages =  MetamodelManager.getGenPackages(genModel);
			for (String uri : packages.keySet()) {
				if (uri == null) {
					continue;
				}
				if (uri.equals(nsURI)) {
					return new GenPackageInFileResult(packages.get(nsURI), ecoreFile, genmodelFile);
				}
			}
		}
		return null;
	}

	private GenModel reloadGeneratorModel(GenModel genModel, ResourceSet rs) {
		if (Platform.isRunning()) {
			IWorkspace workspace = ResourcesPlugin.getWorkspace();
			final URI genModelURI = genModel.eResource().getURI();
			//only reload when we are working on the platform
			if(genModelURI.isPlatform()) {
				IResource member = workspace.getRoot().findMember(genModelURI.toPlatformString(true));
				if (member instanceof IFile) {
					IFile file = (IFile) member;
					if (!file.isReadOnly()) {
		            	try {
		            		updateGenModel(genModel);
		            		Resource genModelResource = rs.getResource(genModelURI, true);
		        			return (GenModel) genModelResource.getContents().get(0);
		            	} catch (Exception e) {
		            		EMFTextSDKPlugin.logError("Error while updating genmodel " + file, e);
		            	}
		        	}
				}
			}
		}
		return genModel;
	}

	private void updateGenModel(final GenModel genModel) throws Exception {
        final Resource genModelResource = genModel.eResource();
 
		final boolean reconcileSucceeded = genModel.reconcile();
		if (!reconcileSucceeded) {
			throw new RuntimeException("Reconciliation of genmodel failed.");
		}
        
        final Diagnostic diag = genModel.diagnose();
        if (diag.getSeverity() != Diagnostic.OK) {
        	throw new DiagnosticException(diag);
        }
        
        new Job("saving genmodel after reconciling") {
        	
        	@Override
        	protected IStatus run(IProgressMonitor monitor) {
        		try {
        			genModelResource.save(Collections.EMPTY_MAP);
        		} catch (IOException e) {
        			throw new RuntimeException(e);
        		}
        		return Status.OK_STATUS;
        	}
        }.schedule();
	}

	protected ConcreteSyntax getSyntax(GenPackageDependentElement container) {
		return (ConcreteSyntax) EcoreUtil.getRootContainer(container);
	}
}
