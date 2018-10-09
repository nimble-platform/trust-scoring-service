package nimble.trust.engine.service;

import java.net.URI;

import javax.transaction.Transactional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
	
	
	
	@Transactional
	public void score(String partyId){
		

		
		//get profile and policy and calculate;
		final TrustSimpleManager trustManager = Factory.createInstance(TrustSimpleManager.class);
		TrustPolicy trustPolicy = trustPolicyService.findGlobalTRustPolicy();
		TrustCriteria criteria = PolicyConverter.fromPolicyToCriteria(trustPolicy);
		Double trustScore = null;
		try {
			trustScore = trustManager.obtainTrustIndex(URI.create(Trust.getURI()+partyId), criteria);
		} catch (Exception e) {
			e.printStackTrace();
		}

		System.out.println(trustScore);
		log.info("trust score updated");
		
//		trustScoreSync.syncWithCatalogService(partyId);
		
	}

}
