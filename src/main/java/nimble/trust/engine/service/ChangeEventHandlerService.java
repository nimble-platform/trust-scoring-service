package nimble.trust.engine.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.google.common.eventbus.AsyncEventBus;

import nimble.trust.engine.collector.ProfileCompletnessCollector;
import nimble.trust.engine.collector.RatingsCollector;
import nimble.trust.engine.model.vocabulary.Trust;
import nimble.trust.web.dto.ChangeEvent;

@Service
public class ChangeEventHandlerService {

	@Autowired
	private AsyncEventBus asyncEventBus;

	@Autowired
	private ProfileCompletnessCollector profileCompletnessCollector;
	
	@Autowired
	private RatingsCollector ratingsCollector;

	public void postChangeEvent(ChangeEvent changeEvent) throws Exception {
		asyncEventBus.post(changeEvent);
	}

	public void handleChangeEvent(ChangeEvent changeEvent) throws Exception {

		String type = changeEvent.getChangeType();

		if (type.equalsIgnoreCase("company_details")) {
			profileCompletnessCollector.fetchNewValueAndSyncScore(changeEvent.getCompanyIdentifier(),
					Trust.ProfileCompletnessDetails.getLocalName());
		}
		else if (type.equalsIgnoreCase("company_description")) {
			profileCompletnessCollector.fetchNewValueAndSyncScore(changeEvent.getCompanyIdentifier(),
					Trust.ProfileCompletnessDescription.getLocalName());
		}
		else if (type.equalsIgnoreCase("company_certificates")) {
			profileCompletnessCollector.fetchNewValueAndSyncScore(changeEvent.getCompanyIdentifier(),
					Trust.ProfileCompletnessCertificates.getLocalName());
		}
		else if (type.equalsIgnoreCase("company_trade")) {
			profileCompletnessCollector.fetchNewValueAndSyncScore(changeEvent.getCompanyIdentifier(),
					Trust.ProfileCompletnessTrade.getLocalName());
		}
		else if (type.equals("ratings-update")){
			ratingsCollector.fetchRatingsSummary(changeEvent.getCompanyIdentifier(), true);
		}
		else if (type.equals("company-updates")){
			profileCompletnessCollector.fetchProfileCompletnessValues(changeEvent.getCompanyIdentifier(), true);	
		}
		else{
			throw new Exception("uknown change type");
		}
	}
}
