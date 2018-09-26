package nimble.trust.engine.collector;

import java.math.BigDecimal;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.google.common.collect.Lists;

import eu.nimble.service.model.ubl.commonaggregatecomponents.PartyType;
import eu.nimble.service.model.ubl.commonaggregatecomponents.QualityIndicatorType;
import eu.nimble.service.model.ubl.commonbasiccomponents.QuantityType;
import nimble.trust.engine.domain.TrustAttribute;
import nimble.trust.engine.domain.TrustProfile;
import nimble.trust.engine.model.vocabulary.Trust;
import nimble.trust.engine.restclient.CatalogServiceClient;
import nimble.trust.engine.restclient.IdentityServiceClient;
import nimble.trust.engine.service.TrustProfileService;

@Service
public class ProfileCompletnessCollector {

	private static Logger log = LoggerFactory.getLogger(ProfileCompletnessCollector.class);

	@Autowired
	private TrustProfileService profileService;

	@Autowired
	IdentityServiceClient identityServiceClient;
	
	@Autowired
	CatalogServiceClient catalogServiceClient;

	@Transactional
	public void obtainNewValueCompanyProfile(String companyId, String attributeTypeName) {
		String newValue = getNewValue(companyId, attributeTypeName);
		profileService.updateTrustAttributeValue(companyId, attributeTypeName, newValue);
		recalculateScoreAndSaveIt(companyId);
		recalculateTrustScoreAndSaveIt(companyId);
	}

	private String getNewValue(String companyId, String attributeTypeName) {

		String newValue = "0.25";

//		if (attributeTypeName.equals(Trust.ProfileCompletnessCertificates.getLocalName())) {
//			identityServiceClient.getParty(..
//		}
//		if (attributeTypeName.equals(Trust.ProfileCompletnessDetails.getLocalName())) {
//
//		}
//		if (attributeTypeName.equals(Trust.ProfileCompletnessDescription.getLocalName())) {
//
//		}
//		if (attributeTypeName.equals(Trust.ProfileCompletnessTrade.getLocalName())) {
//
//		}

		return newValue;
	}

	public void recalculateScoreAndSaveIt(String companyId) {
		TrustProfile profile = profileService.findByAgentAltId(companyId);
		TrustAttribute attr1 = profile.findAttribute(Trust.ProfileCompletnessTrade.getLocalName());
		TrustAttribute attr2 = profile.findAttribute(Trust.ProfileCompletnessCertificates.getLocalName());
		TrustAttribute attr3 = profile.findAttribute(Trust.ProfileCompletnessDescription.getLocalName());
		TrustAttribute attr4 = profile.findAttribute(Trust.ProfileCompletnessDetails.getLocalName());

		BigDecimal score = (attr1 != null) ? new BigDecimal(attr1.getValue()) : BigDecimal.ZERO;
		score = score.add((attr2 != null) ? new BigDecimal(attr2.getValue()) : BigDecimal.ZERO);
		score = score.add((attr3 != null) ? new BigDecimal(attr3.getValue()) : BigDecimal.ZERO);
		score = score.add((attr4 != null) ? new BigDecimal(attr4.getValue()) : BigDecimal.ZERO);
		score = (score.compareTo(BigDecimal.ZERO) == 0) ? BigDecimal.ZERO : score.divide(new BigDecimal(4L));

		profileService.updateTrustAttributeValue(companyId, Trust.OverallProfileCompletness.getLocalName(),
				score.toString());
	}

	public void recalculateTrustScoreAndSaveIt(String companyId) {
		
		log.info("trust score updated");
		
		
		//notify catalog service
		PartyType party = new PartyType();
		List<QualityIndicatorType> qualityIndicatorTypes = Lists.newArrayList();
		QualityIndicatorType score = new QualityIndicatorType();
		score.setQualityParameter("TrustScore");
		QuantityType quantity = new QuantityType();
		quantity.setValue(new BigDecimal("1"));
		score.setQuantity(quantity);
		party.setQualityIndicator(qualityIndicatorTypes);
		
//		catalogServiceClient.postTrustScoreUpdate(companyId, party);
		

	}

}
