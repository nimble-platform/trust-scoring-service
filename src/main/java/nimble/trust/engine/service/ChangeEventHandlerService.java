package nimble.trust.engine.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.google.common.eventbus.AsyncEventBus;

import nimble.trust.engine.collector.ProfileCompletnessCollector;
import nimble.trust.web.dto.ChangeEvent;


@Service
public class ChangeEventHandlerService {
	
	
	@Autowired
	private AsyncEventBus asyncEventBus;
	
	@Autowired
	private ProfileCompletnessCollector profileCompletnessCollector;

	public void postChangeEvent(ChangeEvent changeEvent) throws Exception {
		asyncEventBus.post(changeEvent);
	}
	
	
	public void handleChangeEvent(ChangeEvent changeEvent) throws Exception{
		
		String type = changeEvent.getChangeType();
		
		if (type.equalsIgnoreCase("company_details")){
			profileCompletnessCollector.obtainNewValueCompanyDetails(changeEvent.getCompanyIdentifier());
		}
		if (type.equalsIgnoreCase("company_description")){
			profileCompletnessCollector.obtainNewValueCompanyDescription(changeEvent.getCompanyIdentifier());
		}
		if (type.equalsIgnoreCase("company_certificates")){
			profileCompletnessCollector.obtainNewValueCompanyCertificates(changeEvent.getCompanyIdentifier());
		}
		if (type.equalsIgnoreCase("company_trade")){
			profileCompletnessCollector.obtainNewValueCompanyTrade(changeEvent.getCompanyIdentifier());
		}
	}
	
}
