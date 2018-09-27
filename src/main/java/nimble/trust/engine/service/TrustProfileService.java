package nimble.trust.engine.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import nimble.trust.engine.domain.Agent;
import nimble.trust.engine.domain.TrustAttribute;
import nimble.trust.engine.domain.TrustProfile;
import nimble.trust.engine.repository.TrustProfileRepository;

@Service
public class TrustProfileService {
	
	
	@Autowired
	private TrustProfileRepository trustProfileRepository;
	
	@Autowired
	private AgentService agentService;
	
	
	@Autowired
	private TrustAttributeTypeService attributeTypeService;
	
	
	
	@Transactional
	public TrustProfile save(TrustProfile entity){
		return trustProfileRepository.save(entity);
	}
	
	@Transactional
	public void delete(TrustProfile entity){
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
		TrustProfile profile =  trustProfileRepository.findByOwnerAltId(altid);
		if (profile == null){
			profile = createNewTrustProfile(altid);
		}
		return profile;
	}
	
	
	@Transactional
	public TrustProfile createNewTrustProfile(String altid){
		TrustProfile profile = new TrustProfile();
		Agent owner = agentService.findOrCreateOwner(altid);
		profile.setOwner(owner);
		profile = save(profile);
		return profile;
	}

	private TrustAttribute findInProfileOrCreate(TrustProfile profile, String trustAttributeTypeName) {
		List<TrustAttribute> trustAttributes =  profile.getTrustAttributes();
		TrustAttribute result = null;
		if (CollectionUtils.isEmpty(trustAttributes) == false){
			for (TrustAttribute trustAttribute : trustAttributes) {
				if (trustAttribute.getTrustAttributeType().getName().equals(trustAttributeTypeName)){
					return trustAttribute;
				}
			}
		}
		result = new  TrustAttribute();
		result.setTrustProfile(profile);
		result.setTrustAttributeType(attributeTypeService.findByName(trustAttributeTypeName));
		profile.getTrustAttributes().add(result);
		return result;
	}
	
	
}
