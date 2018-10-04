package nimble.trust.engine.collector;

import java.math.BigDecimal;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.type.TypeReference;

import eu.nimble.utility.JsonSerializationUtility;
import nimble.trust.engine.model.vocabulary.QualityIndicatorConvert;
import nimble.trust.engine.restclient.BusinessProcessClient;
import nimble.trust.engine.service.TrustCalculationService;
import nimble.trust.engine.service.TrustProfileService;
import nimble.trust.web.dto.OverallStatistics;

@Service
public class StatisticsCollector {

	private static Logger log = LoggerFactory.getLogger(StatisticsCollector.class);

	@Autowired
	private BusinessProcessClient businessProcessClient;
	
	@Autowired
	private TrustProfileService profileService;
	

	@Autowired
	private TrustCalculationService trustCalculationService;


	// collect statistics

	public void fetchStatistics(String partyId) {
		final String bearerToken = SecurityContextHolder.getContext().getAuthentication().getPrincipal().toString();
		try {

			feign.Response response = businessProcessClient.getOverallStatistics(partyId, "SELLER", bearerToken);

			if (response.status() == HttpStatus.OK.value()) {
				OverallStatistics statistics = JsonSerializationUtility
						.deserializeContent(response.body().asInputStream(), new TypeReference<OverallStatistics>() {
						});
				
				profileService.updateTrustAttributeValue(partyId,
						QualityIndicatorConvert.AverageNegotiationTime.getTrustVocabulary(),
						new BigDecimal(statistics.getAverageNegotiationTime()).toString());
				profileService.updateTrustAttributeValue(partyId,
						QualityIndicatorConvert.AverageTimeToRespond.getTrustVocabulary(),
						new BigDecimal(statistics.getAverageResponseTime()).toString());
				profileService.updateTrustAttributeValue(partyId,
						QualityIndicatorConvert.TradingVolume.getTrustVocabulary(),
						new BigDecimal(statistics.getTradingVolume()).toString());
				profileService.updateTrustAttributeValue(partyId,
						QualityIndicatorConvert.NumberOfCompletedTransactions.getTrustVocabulary(),
						new BigDecimal(statistics.getNumberOfTransactions()).toString());
				
				
				trustCalculationService.score(partyId);
				
			} else {
				log.info("Synchronization with business process statistics failed due: "
						+ new feign.codec.StringDecoder().decode(response, String.class));
			}
		} catch (Exception e) {
			log.error(" Synchronization with business process statistics internal error:", e);
		}
	}
	
	
	public BigDecimal fetchTotalTransactions(String partyId) {
//		final String bearerToken = SecurityContextHolder.getContext().getAuthentication().getPrincipal().toString();
		try {

			feign.Response response = businessProcessClient.getProcessCount(null, null, null, partyId, "SELLER", null);

			if (response.status() == HttpStatus.OK.value()) {
				Object decoded =  new feign.codec.StringDecoder().decode(response, String.class);
				BigDecimal count = new BigDecimal(decoded.toString());
				return count; 
			} else {
				log.info("Rest call to business process getProcessCount failed due: "
						+ new feign.codec.StringDecoder().decode(response, String.class));
			}
		} catch (Exception e) {
			log.error("Rest call to business process getProcessCount internal error:", e);
		}
		return null;
	}
	
	public BigDecimal fetchTotalTrading(String partyId) {
//		final String bearerToken = SecurityContextHolder.getContext().getAuthentication().getPrincipal().toString();
		try {

			feign.Response response = businessProcessClient.getTradingVolume(null, null, partyId, "SELLER", null);

			if (response.status() == HttpStatus.OK.value()) {
				Object decoded =  new feign.codec.StringDecoder().decode(response, String.class);
				BigDecimal count = new BigDecimal(decoded.toString());
				return count; 
			} else {
				log.info("Rest call to business process getTradingVolume failed due: "
						+ new feign.codec.StringDecoder().decode(response, String.class));
			}
		} catch (Exception e) {
			log.error("Rest call to business process getTradingVolume internal error:", e);
		}
		return null;
	}


}
