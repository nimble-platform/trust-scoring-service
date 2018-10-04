package nimble.trust.engine.service;

import javax.transaction.Transactional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class TrustCalculationService {
	
	private static Logger log = LoggerFactory.getLogger(TrustCalculationService.class);
	
	@Autowired
	private TrustScoreSync trustScoreSync;

	
	
	@Transactional
	public void score(String partyId){
		
		log.info("trust score updated");
		
		//get profile and policy and calculate;
		
		trustScoreSync.syncWithCatalogService(partyId);
		
	}

}
