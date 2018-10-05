package nimble.trust.engine.collector;

import java.math.BigDecimal;

import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

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

	
	
	// collect rating scores
	public void fetchRatingsSummary(String partyId) {
		final String bearerToken = SecurityContextHolder.getContext().getAuthentication().getPrincipal().toString();
		try {

			feign.Response response = businessProcessClient.getRatingsSummary(partyId, bearerToken);

			if (response.status() == HttpStatus.OK.value()) {
				
				String body = new feign.codec.StringDecoder().decode(response, String.class).toString();
				processBody(partyId,body);

			} else {
				log.info("Synchronization with business process ratingsSummary failed due: "
						+ new feign.codec.StringDecoder().decode(response, String.class));
			}
		} catch (Exception e) {
			log.error(" Synchronization with business process ratingsSummary internal error:", e);
		}
	}

	/**
	 * Processes the json body containing rating data and stores values into trust profile
	 * @param partyId
	 * @param body
	 */
	private void processBody(String partyId, String body) {
		JSONObject json = new JSONObject(body);

		Double totalNumberOfRatings = json.getDouble("totalNumberOfRatings");

		boolean allZero = false;
		if (isNullOrZero(totalNumberOfRatings)){
			allZero = true;
		}

		Double qualityOfNegotiationProcess = json.getDouble("qualityOfNegotiationProcess");
		Double qualityOfOrderingProcess = json.getDouble("qualityOfOrderingProcess");
		Double responseTimeRating = json.getDouble("responseTimeRating");

		Double qualityOfNegotiationProcess_Average = (allZero)? 0:qualityOfNegotiationProcess / totalNumberOfRatings;
		Double qualityOfOrderingProcess_Average = (allZero)? 0:qualityOfOrderingProcess / totalNumberOfRatings;
		Double responseTimeRating_Average = (allZero)? 0:responseTimeRating / totalNumberOfRatings;

		Double overallCommunicationRating = (allZero)? 0:(qualityOfNegotiationProcess_Average + qualityOfOrderingProcess_Average
				+ responseTimeRating_Average) / 3;

		Double listingAccuracy = json.getDouble("listingAccuracy");
		Double conformanceToContractualTerms = json.getDouble("conformanceToContractualTerms");

		Double listingAccuracy_Average = (allZero)? 0:listingAccuracy / totalNumberOfRatings;
		Double conformanceToContractualTerms_Average = (allZero)? 0:conformanceToContractualTerms / totalNumberOfRatings;

		Double overallFullfilmentOfTermsRating =(allZero)? 0:(listingAccuracy_Average + conformanceToContractualTerms_Average) / 2;

		Double deliveryAndPackaging = json.getDouble("deliveryAndPackaging");

		Double deliveryAndPackaging_Average = (allZero)? 0: deliveryAndPackaging / totalNumberOfRatings;

		Double overallCompanyRating = (allZero)? 0: (overallCommunicationRating + overallFullfilmentOfTermsRating
				+ deliveryAndPackaging_Average) / 3;

		profileService.updateTrustAttributeValue(partyId,
				QualityIndicatorConvert.OverallCommunicationRating.getTrustVocabulary(),new BigDecimal(overallCommunicationRating).toString());
		
		profileService.updateTrustAttributeValue(partyId,
				QualityIndicatorConvert.OverallFullfilmentOfTermsRating.getTrustVocabulary(), new BigDecimal(overallFullfilmentOfTermsRating).toString());
		
		profileService.updateTrustAttributeValue(partyId,
				QualityIndicatorConvert.OverallDeliveryAndPackagingRating.getTrustVocabulary(), new BigDecimal(deliveryAndPackaging_Average).toString());
		profileService.updateTrustAttributeValue(partyId,
				QualityIndicatorConvert.OverallCompanyRating.getTrustVocabulary(), new BigDecimal(overallCompanyRating).toString());
		
		trustCalculationService.score(partyId);
	}

	private boolean isNullOrZero(Double d) {
		if (d == null || d.compareTo(new Double("0"))==0)
				return true;
		return false;
	}
}
