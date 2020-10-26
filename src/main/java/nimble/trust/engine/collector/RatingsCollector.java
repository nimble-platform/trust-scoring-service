package nimble.trust.engine.collector;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;

import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import nimble.trust.engine.domain.TrustProfile;
import nimble.trust.engine.model.vocabulary.QualityIndicatorConvert;
import nimble.trust.engine.restclient.BusinessProcessClient;
import nimble.trust.engine.service.TrustCalculationService;
import nimble.trust.engine.service.TrustProfileService;

@Service
public class RatingsCollector {

	private static Logger log = LoggerFactory.getLogger(RatingsCollector.class);

	@Autowired
	private BusinessProcessClient businessProcessClient;

	@Autowired
	private TrustProfileService profileService;
	

	@Autowired
	private TrustCalculationService trustCalculationService;

	
	@Autowired
	private ProfileCompletnessCollector completnessCollector;
	
	// collect rating scores
	public void fetchRatingsSummary(String partyId, Boolean recalculateTrustScore) {
		final String bearerToken = SecurityContextHolder.getContext().getAuthentication().getPrincipal().toString();
		try {

			feign.Response response = businessProcessClient.getRatingsSummary(partyId, bearerToken);

			if (response.status() == HttpStatus.OK.value()) {
				
				String body = new feign.codec.StringDecoder().decode(response, String.class).toString();
				processBody(partyId,body);
				
				if (recalculateTrustScore){
					collectOtherDataIfNeeded(partyId);
					trustCalculationService.score(partyId);
				}

			} else {
				log.info("Synchronization with business process ratingsSummary failed due: "
						+ new feign.codec.StringDecoder().decode(response, String.class)+" Return status: "+response.status());
			}
		} catch (Exception e) {
			log.error(" Synchronization with business process ratingsSummary internal error:", e);
		}
	}

	private void collectOtherDataIfNeeded(String partyId) {
		TrustProfile profile = profileService.findByAgentAltId(partyId);
		if (profile.findAttribute(QualityIndicatorConvert.OverallProfileCompletness.getTrustVocabulary()) == null){
			completnessCollector.fetchProfileCompletnessValues(partyId, false);
		}
	}

	/**
	 * Processes the json body containing rating data and stores values into trust profile
	 * @param partyId
	 * @param body
	 */
	private void processBody(String partyId, String body) {
		JSONObject json = new JSONObject(body);

		// retrieve individual ratings
		// each value represents the average value for that sub-rating
		Double qualityOfNegotiationProcess = json.getDouble("qualityOfNegotiationProcess");
		Double qualityOfOrderingProcess = json.getDouble("qualityOfOrderingProcess");
		Double responseTimeRating = json.getDouble("responseTimeRating");
		Double listingAccuracy = json.getDouble("listingAccuracy");
		Double conformanceToContractualTerms = json.getDouble("conformanceToContractualTerms");
		Double deliveryAndPackaging = json.getDouble("deliveryAndPackaging");

		// calculate overall communication rating
		List<Double> overallCommunicationRatings = Arrays.asList(qualityOfNegotiationProcess,qualityOfOrderingProcess,responseTimeRating);
		Double overallCommunicationRating = overallCommunicationRatings.stream().filter(this::isNotNullOrZero).mapToDouble(Double::doubleValue).average().orElse(0);

		// calculate overall fulfilment of terms rating
		List<Double> listingAccuracyRatings = Arrays.asList(listingAccuracy,conformanceToContractualTerms);
		Double overallFullfilmentOfTermsRating = listingAccuracyRatings.stream().filter(this::isNotNullOrZero).mapToDouble(Double::doubleValue).average().orElse(0);

		// calculate overall company rating
		List<Double> overallCompanyRatings = Arrays.asList(overallCommunicationRating,overallFullfilmentOfTermsRating,deliveryAndPackaging);
		Double overallCompanyRating = overallCompanyRatings.stream().filter(this::isNotNullOrZero).mapToDouble(Double::doubleValue).average().orElse(0);

		profileService.updateTrustAttributeValue(partyId,
				QualityIndicatorConvert.OverallCommunicationRating.getTrustVocabulary(),new BigDecimal(overallCommunicationRating).toString());
		
		profileService.updateTrustAttributeValue(partyId,
				QualityIndicatorConvert.OverallFullfilmentOfTermsRating.getTrustVocabulary(), new BigDecimal(overallFullfilmentOfTermsRating).toString());
		
		profileService.updateTrustAttributeValue(partyId,
				QualityIndicatorConvert.OverallDeliveryAndPackagingRating.getTrustVocabulary(), new BigDecimal(deliveryAndPackaging).toString());
		profileService.updateTrustAttributeValue(partyId,
				QualityIndicatorConvert.OverallCompanyRating.getTrustVocabulary(), new BigDecimal(overallCompanyRating).toString());
		
	}

	private boolean isNotNullOrZero(Double d) {
		return d != null && d.compareTo(new Double("0")) != 0;
	}
}
