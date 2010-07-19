package org.emftext.sdk.concretesyntax.resource.cs.analysis.helper;

import org.eclipse.emf.common.util.EList;
import org.emftext.sdk.concretesyntax.CompleteTokenDefinition;
import org.emftext.sdk.concretesyntax.ConcreteSyntax;
import org.emftext.sdk.concretesyntax.Import;
import org.emftext.sdk.concretesyntax.TokenDirective;
import org.emftext.sdk.concretesyntax.resource.cs.ICsReferenceResolveResult;

public class TokenReferenceResolver {

	public void resolveImportedToken(ConcreteSyntax syntax, String syntaxName,
			String tokenName,
			final ICsReferenceResolveResult<CompleteTokenDefinition> result,
			boolean resolveFuzzy) {
		EList<Import> imports = syntax.getImports();
		for (Import nextImport : imports) {
			ConcreteSyntax nextImportedSyntax = nextImport.getConcreteSyntax();
			if ((resolveFuzzy || syntaxName.equals(nextImport.getPrefix())) && nextImportedSyntax != null) {
				EList<TokenDirective> allTokenDirectives = nextImportedSyntax.getAllTokenDirectives();
				for (TokenDirective tokenDirective : allTokenDirectives) {
					if (tokenDirective instanceof CompleteTokenDefinition) {
						CompleteTokenDefinition completeDefinition = (CompleteTokenDefinition) tokenDirective;
						String name = completeDefinition.getName();
						if (resolveFuzzy || tokenName.equals(name)) {
							result.addMapping(nextImport.getPrefix() + "." + name, completeDefinition);
						}
					}
				}
			}
		}
	}
}
