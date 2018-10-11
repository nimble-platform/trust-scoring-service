package nimble.trust.engine.service;

import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import nimble.trust.engine.domain.TrustAttribute;
import nimble.trust.engine.domain.TrustAttributeType;
import nimble.trust.engine.domain.TrustPolicy;
import nimble.trust.engine.repository.TrustPolicyRepository;
import nimble.trust.web.dto.TrustAttributeDto;
import nimble.trust.web.dto.TrustPolicyDto;

@Service
public class TrustPolicyService {

	@Autowired
	private TrustPolicyRepository trustPolicyRepository;
	
	@Autowired
	private TrustCalculationService trustCalculationService;

	@Autowired
    private TrustAttributeTypeService trustAttributeTypeService;

	@Transactional
	public TrustPolicy save(TrustPolicy entity) {
		return trustPolicyRepository.save(entity);
	}

	@Transactional
	public void delete(TrustPolicy entity) {
		trustPolicyRepository.delete(entity);
	}

	public TrustPolicy findGlobalTRustPolicy() {
		List<TrustPolicy> result = trustPolicyRepository.findAll();
		if (CollectionUtils.isEmpty(result) == false) {
			return result.get(0);
		}
		return null;
	}

	@Transactional
	public TrustPolicy createOrUpdateTrustPolicy(TrustPolicyDto trustPolicyDto) throws Exception {
		TrustPolicy policy = findById(trustPolicyDto.getId());
		if (policy == null) {
			policy = createTrustPolicy(trustPolicyDto);
		}
		else{
			policy = updateTrustPolicy(policy, trustPolicyDto);
		}
		if (trustPolicyDto.getRecalculateScoresWhenUpdated()){
			trustCalculationService.scoreBatch();
		}
		return policy;
	}

	
	private TrustPolicy updateTrustPolicy(TrustPolicy policy, TrustPolicyDto trustPolicyDto) throws Exception{
		List<TrustAttributeDto> list = trustPolicyDto.getTrustAttributes();
		for (TrustAttributeDto trustAttributeDto : list) {
			for (TrustAttribute trustAttribute : policy.getTrustAttributes()) {
				 if (trustAttribute.getTrustAttributeType().getName().equals(trustAttributeDto.getAttributeType().getName())){
					 createOrUpdateAttribute(policy, trustAttributeDto, trustAttribute);
				 }
			}
		}
		policy = save(policy);
		return policy;
		
	}

	
	private TrustPolicy createTrustPolicy(TrustPolicyDto trustPolicyDto) throws Exception {
		TrustPolicy policy = new TrustPolicy();
		policy.setDescr("global trust policy");
		List<TrustAttributeDto> list = trustPolicyDto.getTrustAttributes();
		for (TrustAttributeDto trustAttributeDto : list) {
			TrustAttribute attr = createOrUpdateAttribute(policy, trustAttributeDto, null);
			policy.getTrustAttributes().add(attr);
		}
		policy = save(policy);
		return policy;
	}

	private TrustAttribute createOrUpdateAttribute(TrustPolicy policy, TrustAttributeDto trustAttributeDto,
			TrustAttribute attribute) throws Exception {
		TrustAttribute attr = (attribute == null) ? new TrustAttribute() : attribute;

		attr.setImportance(trustAttributeDto.getWeight());

		if (attr.getTrustAttributeType() == null) {
			TrustAttributeType type = trustAttributeTypeService.findByName(trustAttributeDto.getAttributeType().getName());
			if (type == null)
				throw new Exception(
						"Unknown TrustAttributeType with name: " + trustAttributeDto.getAttributeType().getName());
			attr.setTrustAttributeType(type);
		}

		String expression = trustAttributeDto.getExpression();
		if (StringUtils.isBlank(expression)) {
			attr.setMinValue(null);
			attr.setValue(null);
			attr.setMaxValue(null);
		} else {
			if (expression.contains("between ")) {
				expression = expression.replaceAll("between ", "");
				String[] parts = StringUtils.split(expression, " ");
				attr.setMinValue(parts[0]);
				attr.setValue(null);
				attr.setMaxValue(parts[1]);
			}
			if (expression.contains("greater or equal ")) {
				expression = expression.replaceAll("greater or equal ", "");
				attr.setMinValue(expression);
				attr.setValue(null);
				attr.setMaxValue(null);
			}
			if (expression.contains("less or equal ")) {
				expression = expression.replaceAll("less or equal ", "");
				attr.setMinValue(null);
				attr.setValue(null);
				attr.setMaxValue(expression);
			}
		}

		return attr;

	}

	@Transactional
	public TrustPolicy createTrustPolicy() {
		TrustPolicy policy = new TrustPolicy();
		List<TrustAttributeType> list = trustAttributeTypeService.findAllRootLevel();
		for (TrustAttributeType trustAttributeType : list) {
			TrustAttribute attr = new TrustAttribute();
			attr.setTrustAttributeType(trustAttributeType);
			attr.setImportance(0);
			attr.setTrustPolicy(policy);
			policy.getTrustAttributes().add(attr);
		}
		policy = save(policy);
		return policy;
	}

	public TrustPolicy findById(Long id) {
		return trustPolicyRepository.findById(id);
	}

}
