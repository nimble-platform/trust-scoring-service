package nimble.trust.engine.collector;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import com.fasterxml.jackson.core.type.TypeReference;
import com.google.common.collect.Lists;

import eu.nimble.service.model.ubl.commonaggregatecomponents.PartyType;
import eu.nimble.service.model.ubl.commonaggregatecomponents.QualityIndicatorType;
import eu.nimble.service.model.ubl.extension.QualityIndicatorParameter;
import eu.nimble.utility.JsonSerializationUtility;
import nimble.trust.engine.domain.TrustAttribute;
import nimble.trust.engine.domain.TrustProfile;
import nimble.trust.engine.model.vocabulary.QualityIndicatorConvert;
import nimble.trust.engine.model.vocabulary.Trust;
import nimble.trust.engine.restclient.IdentityServiceClient;
import nimble.trust.engine.service.TrustProfileService;
import nimble.trust.engine.service.TrustScoreSync;

@Service
public class ProfileCompletnessCollector {

	private static Logger log = LoggerFactory.getLogger(ProfileCompletnessCollector.class);

	@Autowired
	private TrustProfileService profileService;

	@Autowired
	private IdentityServiceClient identityServiceClient;

	@Autowired
	private TrustScoreSync trustScoreSync;

	/**
	 * responsible to call an identity service to obtain new data and to recalc
	 * scores
	 * 
	 * @param partyId
	 */
	public void obtainNewValues(String partyId) {

		final String bearerToken = SecurityContextHolder.getContext().getAuthentication().getPrincipal().toString();

		PartyType partyType;
		try {
			partyType = JsonSerializationUtility.deserializeContent(
					identityServiceClient.getPartyTrust(bearerToken, partyId).body().asInputStream(),
					new TypeReference<PartyType>() {
					});
			List<QualityIndicatorType> qualityIndicators = partyType.getQualityIndicator();
			List<String> ofInterest = Lists.newArrayList();
			ofInterest.add(QualityIndicatorParameter.COMPLETENESS_OF_COMPANY_CERTIFICATE_DETAILS.toString());
			ofInterest.add(QualityIndicatorParameter.COMPLETENESS_OF_COMPANY_TRADE_DETAILS.toString());
			ofInterest.add(QualityIndicatorParameter.COMPLETENESS_OF_COMPANY_DESCRIPTION.toString());
			ofInterest.add(QualityIndicatorParameter.COMPLETENESS_OF_COMPANY_GENERAL_DETAILS.toString());
			ofInterest.add(QualityIndicatorParameter.PROFILE_COMPLETENESS.toString());
			if (!CollectionUtils.isEmpty(qualityIndicators)) {
				for (QualityIndicatorType qualityIndicator : qualityIndicators) {
					String parameterName = qualityIndicator.getQualityParameter();
					if (ofInterest.contains(parameterName) && qualityIndicator.getQuantity() != null) {
						updateCompanyProfileAndSyncScore(partyId, QualityIndicatorConvert
								.findByQualityIndicatorParameterName(parameterName).getTrustVocabulary(),
								qualityIndicator.getQuantity().getValue().toString());
					}
				}
			}
		} catch (IOException e) {
			log.error(" Synchronization with identity service failed or internal error happened", e);
		}

	}

	public void updateCompanyProfileAndSyncScore(String companyId, String attributeTypeName, String newValue) {
		profileService.updateTrustAttributeValue(companyId, attributeTypeName, newValue);
		recalculateScoreAndSaveIt(companyId);
		recalculateTrustScoreAndSaveIt(companyId);
	}

	public void fetchNewValueAndSyncScore(String companyId, String attributeTypeName) {
		String newValue = fetchNewValue(companyId, attributeTypeName);
		if (newValue == null)
			return;
		profileService.updateTrustAttributeValue(companyId, attributeTypeName, newValue);
		recalculateScoreAndSaveIt(companyId);
		recalculateTrustScoreAndSaveIt(companyId);
	}

	private String fetchNewValue(String partyId, String attributeTypeName) {

		final String bearerToken = SecurityContextHolder.getContext().getAuthentication().getPrincipal().toString();

		PartyType partyType;
		try {

			feign.Response response = identityServiceClient.getPartyTrust(bearerToken, partyId);
//			System.out.println(new feign.codec.StringDecoder().decode(response, String.class));

			if (response.status() == HttpStatus.OK.value()) {
				partyType = JsonSerializationUtility.deserializeContent(response.body().asInputStream(),new TypeReference<PartyType>(){});
				List<QualityIndicatorType> qualityIndicators = partyType.getQualityIndicator();
				String parameterName = QualityIndicatorConvert.findByName(attributeTypeName)
						.getQualityIndicatorParameter().toString();
				if (!CollectionUtils.isEmpty(qualityIndicators)) {
					for (QualityIndicatorType qualityIndicator : qualityIndicators) {
						String qparameterName = qualityIndicator.getQualityParameter();
						if (qparameterName.equals(parameterName) && qualityIndicator.getQuantity() != null) {
							return qualityIndicator.getQuantity().getValue().toString();
						}
					}
				}
			}
			else{
				log.info("Synchronization with identity service failed due: "+new feign.codec.StringDecoder().decode(response, String.class));
			}
		} catch (IOException e) {
			log.error("Synchronization with identity service failed or internal error happened", e);
		}

		return null;
	}

	public void recalculateScoreAndSaveIt(String companyId) {
		TrustProfile profile = profileService.findByAgentAltId(companyId);
		TrustAttribute attr1 = profile.findAttribute(Trust.ProfileCompletnessTrade.getLocalName());
		TrustAttribute attr2 = profile.findAttribute(Trust.ProfileCompletnessCertificates.getLocalName());
		TrustAttribute attr3 = profile.findAttribute(Trust.ProfileCompletnessDescription.getLocalName());
		TrustAttribute attr4 = profile.findAttribute(Trust.ProfileCompletnessDetails.getLocalName());

		BigDecimal score = (attr1 != null && attr1.getValue() != null) ? new BigDecimal(attr1.getValue())
				: BigDecimal.ZERO;
		score = score
				.add((attr2 != null && attr2.getValue() != null) ? new BigDecimal(attr2.getValue()) : BigDecimal.ZERO);
		score = score
				.add((attr3 != null && attr3.getValue() != null) ? new BigDecimal(attr3.getValue()) : BigDecimal.ZERO);
		score = score
				.add((attr4 != null && attr4.getValue() != null) ? new BigDecimal(attr4.getValue()) : BigDecimal.ZERO);
		score = (score.compareTo(BigDecimal.ZERO) == 0) ? BigDecimal.ZERO : score.divide(new BigDecimal(4L));

		profileService.updateTrustAttributeValue(companyId, Trust.OverallProfileCompletness.getLocalName(),
				score.toString());
	}

	@Transactional
	public void recalculateTrustScoreAndSaveIt(String companyId) {

		log.info("trust score updated");

		trustScoreSync.syncWithCatalogService(companyId);

	}

}
