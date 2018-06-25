package nimble.trust.engine.op.match;


import java.util.List;

import org.slf4j.LoggerFactory;

import nimble.trust.engine.model.pojo.TrustAttribute;

/**
 * A semantic match operator that semantically matches two given semantic descriptions.
 * 
 * @author markov
 * 
 */
public class SemanticMatchOp {

	private static final org.slf4j.Logger log = LoggerFactory.getLogger(SemanticMatchOp.class);

	public SemanticMatchOp(nimble.trust.engine.kb.KnowledgeBaseManager kbManager) {
//		this.kbManager = kbManager;
	}

	public SemanticMatchOutput apply(TrustAttribute reqAttribute, List<TrustAttribute> attributes) {
		// TODO implement me
		log.warn("SemanticMatchOp is not implemented. Returned value is SemanticMatchOutput.Sem_Exact");
		return SemanticMatchOutput.Sem_Exact;
	}
}
