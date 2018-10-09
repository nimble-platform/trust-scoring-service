package nimble.trust.engine.service;

import java.net.URI;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import nimble.trust.engine.domain.Agent;
import nimble.trust.engine.domain.TrustPolicy;
import nimble.trust.engine.model.pojo.TrustCriteria;
import nimble.trust.engine.model.vocabulary.Trust;
import nimble.trust.engine.module.Factory;
import nimble.trust.engine.service.interfaces.TrustSimpleManager;
import nimble.trust.engine.util.PolicyConverter;

@Service
public class TrustCalculationService {

	private static Logger log = LoggerFactory.getLogger(TrustCalculationService.class);

	@Autowired
	private TrustScoreSync trustScoreSync;

	@Autowired
	private TrustPolicyService trustPolicyService;

	@Autowired
	private AgentService agentService;

	@Value("${app.trust.trustScore.syncWithCatalogService}")
	private Boolean syncWithCatalogService;

	public void score(String partyId) {
		// get profile and policy and calculate;
		final TrustSimpleManager trustManager = Factory.createInstance(TrustSimpleManager.class);
		TrustPolicy trustPolicy = trustPolicyService.findGlobalTRustPolicy();
		TrustCriteria criteria = PolicyConverter.fromPolicyToCriteria(trustPolicy);
		Double trustScore = null;
		try {
			trustScore = trustManager.obtainTrustIndex(URI.create(Trust.getURI() + partyId), criteria);

			agentService.updateTrustScore(partyId, trustScore);

			if (syncWithCatalogService) {
				trustScoreSync.syncWithCatalogService(partyId);
			}
			log.info("trust score of party with id" + partyId + " is updated to " + trustScore);
		} catch (Exception e) {
			log.error("Exception", e);
		}
	}

	@Async
	public void scoreBatch() {
		int pageNumber = 0;
		int pageLimit = 100;
		Page<Agent> page;
		do {
			page = agentService.findAll(new PageRequest(pageNumber, pageLimit));
			pageNumber++;
			List<Agent> list = page.getContent();
			for (Agent agent : list) {
				score(agent.getAltId());
			}
		} while (!page.isLast());
	}

}
