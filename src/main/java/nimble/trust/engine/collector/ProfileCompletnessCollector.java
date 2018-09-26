package nimble.trust.engine.collector;

import java.math.BigDecimal;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import nimble.trust.engine.domain.TrustAttribute;
import nimble.trust.engine.domain.TrustProfile;
import nimble.trust.engine.model.vocabulary.Trust;
import nimble.trust.engine.service.TrustProfileService;


@Service
public class ProfileCompletnessCollector {

	private static Logger log = LoggerFactory.getLogger(ProfileCompletnessCollector.class);
	
	@Autowired
	private TrustProfileService profileService;
	
	
	@Transactional
	public void obtainNewValueCompanyDetails(String companyId) {
		String newValue = "0.25";
		profileService.updateTrustAttributeValue(companyId, Trust.ProfileCompletnessDetails.getLocalName(), newValue);
		recalculateScoreAndSaveIt(companyId);
		recalculateTrustScoreAndSaveIt(companyId);
	}

	@Transactional
	public void obtainNewValueCompanyTrade(String companyId) {
		String newValue = "0.25";
		profileService.updateTrustAttributeValue(companyId, Trust.ProfileCompletnessTrade.getLocalName(), newValue);
		recalculateScoreAndSaveIt(companyId);
		recalculateTrustScoreAndSaveIt(companyId);
	}

	@Transactional
	public void obtainNewValueCompanyCertificates(String companyId) {
		String newValue = "0.25";
		profileService.updateTrustAttributeValue(companyId, Trust.ProfileCompletnessCertificates.getLocalName(), newValue);
		recalculateScoreAndSaveIt(companyId);
		recalculateTrustScoreAndSaveIt(companyId);
	}

	@Transactional
	public void obtainNewValueCompanyDescription(String companyId) {
		String newValue = "0.25";
		profileService.updateTrustAttributeValue(companyId, Trust.ProfileCompletnessDescription.getLocalName(), newValue);
		recalculateScoreAndSaveIt(companyId);
		recalculateTrustScoreAndSaveIt(companyId);
	}
	
	
	public void recalculateScoreAndSaveIt(String companyId){
		TrustProfile profile = profileService.findByAgentAltId(companyId);
		TrustAttribute attr1= profile.findAttribute(Trust.ProfileCompletnessTrade.getLocalName());
		TrustAttribute attr2= profile.findAttribute(Trust.ProfileCompletnessCertificates.getLocalName());
		TrustAttribute attr3= profile.findAttribute(Trust.ProfileCompletnessDescription.getLocalName());
		TrustAttribute attr4= profile.findAttribute(Trust.ProfileCompletnessDetails.getLocalName());
	
		BigDecimal score = (attr1!=null)? new BigDecimal(attr1.getValue()):BigDecimal.ZERO;
		score = score.add((attr2!=null)? new BigDecimal(attr2.getValue()):BigDecimal.ZERO);
		score = score.add((attr3!=null)? new BigDecimal(attr3.getValue()):BigDecimal.ZERO);
		score = score.add((attr4!=null)? new BigDecimal(attr4.getValue()):BigDecimal.ZERO);
		score = (score.compareTo(BigDecimal.ZERO)==0)? BigDecimal.ZERO:score.divide(new BigDecimal(4L));
		
		profileService.updateTrustAttributeValue(companyId, Trust.OverallProfileCompletness.getLocalName(), score.toString());
	}
	
	public void recalculateTrustScoreAndSaveIt(String companyId){
		log.info("trust score updated");
		
	}

}
