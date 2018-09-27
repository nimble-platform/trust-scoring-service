package nimble.trust.engine.service;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import com.google.common.collect.Lists;

import eu.nimble.service.model.ubl.commonaggregatecomponents.PartyType;
import eu.nimble.service.model.ubl.commonaggregatecomponents.QualityIndicatorType;
import eu.nimble.service.model.ubl.commonbasiccomponents.QuantityType;
import feign.Response;
import feign.codec.StringDecoder;
import nimble.trust.engine.domain.TrustAttribute;
import nimble.trust.engine.domain.TrustProfile;
import nimble.trust.engine.model.vocabulary.QualityIndicatorConvert;
import nimble.trust.engine.restclient.CatalogServiceClient;

@Service
public class TrustScoreSync {
	
	
	private static Logger log = LoggerFactory.getLogger(TrustScoreSync.class);

	@Autowired
	private TrustProfileService profileService;
	
	@Autowired
	private CatalogServiceClient catalogServiceClient;
	
	
	public void syncWithCatalogService(String partyId) {

		TrustProfile profile = profileService.findByAgentAltId(partyId);

		// create UBL Party populated with a trust-related data and push it to the catalog service
		PartyType party = new PartyType();
		party.setID(partyId);
		List<TrustAttribute> trustAttributes = profile.getTrustAttributes();
		List<QualityIndicatorType> qualityIndicatorTypes = Lists.newArrayList();
		for (TrustAttribute trustAttribute : trustAttributes) {
			String qualityName = convertToUblQualityParameter(trustAttribute.getTrustAttributeType().getName());
			if (qualityName!=null){
				QualityIndicatorType qualityIndicator = new QualityIndicatorType();
				qualityIndicator.setQualityParameter(qualityName);
				QuantityType quantity = new QuantityType();
				quantity.setValue(new BigDecimal(trustAttribute.getValue()));
				qualityIndicator.setQuantity(quantity);
				qualityIndicatorTypes.add(qualityIndicator);
			}
		}
		party.setQualityIndicator(qualityIndicatorTypes);
		
		
		Response response =  catalogServiceClient.postTrustScoreUpdate(party.getID(), party, 
				SecurityContextHolder.getContext().getAuthentication().getPrincipal().toString());
		try {
			if (response.status() == HttpStatus.OK.value()){
				log.info("PartyId="+partyId+" : New trust score sucessfuly sent to a catalog service");
			}else{
				String resposeBody = new StringDecoder().decode(response, String.class).toString();
				log.info("PartyId="+partyId+" : New trust score un-sucessfuly sent to a catalog serrvice. Message:"+resposeBody);
			}
		} catch (IOException e) {
			log.error("Failed to synchronize trust score with a catalog service", e);
			e.printStackTrace();
		}

	}

	private String convertToUblQualityParameter(String name) {
		QualityIndicatorConvert qic = QualityIndicatorConvert.findByName(name);
		if ( qic!=null){
			return qic.getQualityIndicatorParameter().toString();
		}
		return null;
	}

}
