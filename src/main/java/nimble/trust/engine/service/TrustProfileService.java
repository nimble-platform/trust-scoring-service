package nimble.trust.engine.service;

import java.math.BigDecimal;
import java.util.List;

import nimble.trust.engine.restclient.BusinessProcessClient;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import com.google.common.collect.Lists;

import eu.nimble.service.model.ubl.commonaggregatecomponents.PartyIdentificationType;
import eu.nimble.service.model.ubl.commonaggregatecomponents.PartyType;
import eu.nimble.service.model.ubl.commonaggregatecomponents.QualityIndicatorType;
import eu.nimble.service.model.ubl.commonbasiccomponents.QuantityType;
import eu.nimble.service.model.ubl.extension.QualityIndicatorParameter;
import nimble.trust.engine.domain.Agent;
import nimble.trust.engine.domain.TrustAttribute;
import nimble.trust.engine.domain.TrustProfile;
import nimble.trust.engine.model.vocabulary.QualityIndicatorConvert;
import nimble.trust.engine.repository.TrustProfileRepository;

@Service
public class TrustProfileService {

	@Autowired
	private TrustProfileRepository trustProfileRepository;

	@Autowired
	private AgentService agentService;

	@Autowired
	private TrustAttributeTypeService attributeTypeService;

	@Autowired
	private BusinessProcessClient businessProcessClient;

	private static Logger log = LoggerFactory.getLogger(TrustProfileService.class);


	@Transactional
	public TrustProfile save(TrustProfile entity) {
		return trustProfileRepository.save(entity);
	}

	@Transactional
	public void delete(TrustProfile entity) {
		trustProfileRepository.delete(entity);
	}

	public TrustProfile findByAgentAltId(String altid) {
		return trustProfileRepository.findByOwnerAltId(altid);
	}

	@Transactional
	public TrustProfile updateTrustAttributeValue(String companyId, String localName, String newValue) {
		TrustProfile profile = this.findOrCreateByAgentAltId(companyId);
		TrustAttribute trustAttribute = this.findInProfileOrCreate(profile, localName);
		trustAttribute.setValue(newValue);
		profile = save(profile);
		return profile;
	}

	public TrustProfile findOrCreateByAgentAltId(String altid) {
		TrustProfile profile = trustProfileRepository.findByOwnerAltId(altid);
		if (profile == null) {
			profile = createNewTrustProfile(altid);
		}
		return profile;
	}

	@Transactional
	public TrustProfile createNewTrustProfile(String altid) {
		TrustProfile profile = new TrustProfile();
		Agent owner = agentService.findOrCreateOwner(altid);
		profile.setOwner(owner);
		profile = save(profile);
		return profile;
	}

	private TrustAttribute findInProfileOrCreate(TrustProfile profile, String trustAttributeTypeName) {
		List<TrustAttribute> trustAttributes = profile.getTrustAttributes();
		TrustAttribute result = null;
		if (CollectionUtils.isEmpty(trustAttributes) == false) {
			for (TrustAttribute trustAttribute : trustAttributes) {
				if (trustAttribute.getTrustAttributeType().getName().equals(trustAttributeTypeName)) {
					return trustAttribute;
				}
			}
		}
		result = new TrustAttribute();
		result.setTrustProfile(profile);
		result.setTrustAttributeType(attributeTypeService.findByName(trustAttributeTypeName));
		profile.getTrustAttributes().add(result);
		return result;
	}
	
	
	public List<PartyType> listPartiesWithTrustData(){

		List<PartyType> partyTypes = Lists.newArrayList();
		List<Agent> list = agentService.findAll();
		if (CollectionUtils.isEmpty(list)) return partyTypes;
		for (Agent agent : list) {
			partyTypes.add(createPartyType(agent.getAltId()));
		}
		return partyTypes;
	}

	public PartyType createPartyType(String partyId) {
		TrustProfile profile = findByAgentAltId(partyId);
		
		if (profile == null)
			return null;

		// create UBL Party populated with a trust-related data
		PartyType party = new PartyType();
		PartyIdentificationType partyIdentification = new PartyIdentificationType();
		partyIdentification.setID(partyId);
		List<PartyIdentificationType> identificationTypes = Lists.newArrayList();
		identificationTypes.add(partyIdentification);
		party.setPartyIdentification(identificationTypes);
		List<TrustAttribute> trustAttributes = profile.getTrustAttributes();
		List<QualityIndicatorType> qualityIndicatorTypes = Lists.newArrayList();
		for (TrustAttribute trustAttribute : trustAttributes) {
			String qualityName = convertToUblQualityParameter(trustAttribute.getTrustAttributeType().getName());
			if (qualityName != null) {
				QualityIndicatorType qualityIndicator = new QualityIndicatorType();
				qualityIndicator.setQualityParameter(qualityName);
				QuantityType quantity = new QuantityType();
				if (trustAttribute.getValue()==null) continue;
				quantity.setValue(new BigDecimal(trustAttribute.getValue()));
				qualityIndicator.setQuantity(quantity);
				qualityIndicatorTypes.add(qualityIndicator);
			}
		}

		Agent profileOwner = profile.getOwner();
		QualityIndicatorType trustScoreIndicator = new QualityIndicatorType();
		trustScoreIndicator.setQualityParameter(QualityIndicatorParameter.TRUST_SCORE.toString());
		QuantityType score = new QuantityType();
		score.setValue((profileOwner.getTrustScore() != null) ? profileOwner.getTrustScore() : BigDecimal.ZERO);
		trustScoreIndicator.setQuantity(score);
		qualityIndicatorTypes.add(trustScoreIndicator);

		QualityIndicatorType noOfEvaluationsIndicator = new QualityIndicatorType();
		noOfEvaluationsIndicator.setQualityParameter(QualityIndicatorParameter.Number_OF_EVALUATIONS.toString());
		QuantityType evaluations = new QuantityType();

		final String bearerToken = SecurityContextHolder.getContext().getAuthentication().getPrincipal().toString();
		try {

			feign.Response response = businessProcessClient.getRatingsSummary(partyId, bearerToken);

			if (response.status() == HttpStatus.OK.value()) {

				String body = new feign.codec.StringDecoder().decode(response, String.class).toString();
				JSONObject json = new JSONObject(body);
				Double totalNumberOfRatings = json.getDouble("totalNumberOfRatings");
				evaluations.setValue((totalNumberOfRatings != null) ? new BigDecimal(totalNumberOfRatings) : BigDecimal.ZERO);
				noOfEvaluationsIndicator.setQuantity(evaluations);
				qualityIndicatorTypes.add(noOfEvaluationsIndicator);
			} else {
				log.info("Synchronization with business process ratingsSummary failed due: "
						+ new feign.codec.StringDecoder().decode(response, String.class)+" Return status: "+response.status());
			}
		} catch (Exception e) {
			log.error(" Synchronization with business process ratingsSummary internal error:", e);
		}

		party.setQualityIndicator(qualityIndicatorTypes);

		return party;

	}

	private String convertToUblQualityParameter(String name) {
		QualityIndicatorConvert qic = QualityIndicatorConvert.findByName(name);
		if (qic != null) {
			return qic.getQualityIndicatorParameter().toString();
		}
		return null;
	}

}
