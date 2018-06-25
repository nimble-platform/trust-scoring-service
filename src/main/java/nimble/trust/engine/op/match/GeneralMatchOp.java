package nimble.trust.engine.op.match;

import java.util.List;

import com.hp.hpl.jena.rdf.model.Resource;

import nimble.trust.common.ValuesHolder;
import nimble.trust.engine.kb.KnowledgeBaseManager;
import nimble.trust.engine.model.pojo.CertificateAuthorityAttribute;
import nimble.trust.engine.model.pojo.TrustAttribute;
import nimble.trust.engine.model.utils.TrustOntologyUtil;
import nimble.trust.engine.model.vocabulary.Trust;

/**
 * General match operator as a entry point for specific matchers
 *@author markov
 *
 */
/**
 * @author markov
 *
 */
public class GeneralMatchOp {
	
	
	private KnowledgeBaseManager kbManager; 
	
	private ValuesHolder valuesHolder;
	
	public GeneralMatchOp(KnowledgeBaseManager kbManager, ValuesHolder valuesHolder){
		this.kbManager = kbManager;
		this.valuesHolder = valuesHolder;
	}
	
	/**
	 * 
	 * @param requested Requested attribute
	 * @param attributes List of attributes contained in a trust profile
	 * @return Double value as a matching/evaluation score on the requested attribute
	 * @throws Exception
	 */
	public double apply(TrustAttribute requested, List<TrustAttribute> attributes) throws Exception {
		if (attributes.isEmpty()){
			// If there is no attribute matching requested attribute,then value is
			// 0. this is pessimistic approach.
			// A optimistic approach would be to remove attribute out of
			// consideration/
			return 0;
		}
		if (isSubtype(requested, Trust.UnmeasurableTrustAttribute)) {
			//it is descriptive so then apply specific semantic match depending on the attribute's type
			if (requested instanceof CertificateAuthorityAttribute) {
				return new CertSemanticMatchOp().apply(requested, attributes);
			} else if (isSubtype(requested, Trust.SecurityAttribute)) {
				return new SecSemanticMatchOp(kbManager).apply(requested, attributes);
			} else {
				final SemanticMatchOutput result = new SemanticMatchOp(kbManager).apply(requested, attributes);
				return result.asNumeric();
			}

		} else { // if numeric and measurable then do numeric comparisons
			final ComparisonMatchOp op = new ComparisonMatchOp(valuesHolder);
			// at this point, it is assumed that only and only one exists
			TrustAttribute attribute = attributes.get(0);
			return op.apply(requested, attribute);
		}
	}

	private boolean isSubtype(TrustAttribute reqAttribute, Resource resource) {
		return TrustOntologyUtil.instance().isSubtype(reqAttribute.obtainType().getUri().toString(), resource.getURI());
	}
}
