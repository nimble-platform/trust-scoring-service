package nimble.trust.web.dto;

import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.util.CollectionUtils;

import com.google.common.collect.Lists;

import nimble.trust.engine.domain.TrustAttribute;
import nimble.trust.engine.domain.TrustAttributeType;
import nimble.trust.engine.domain.TrustPolicy;

public class DtoUtil {
	
	
	

	public static List<TrustAttributeTypeDto> toDto(List<TrustAttributeType> attributeTypes) {
		List<TrustAttributeTypeDto> atrributeTypeDtos = Lists.newArrayList();
		if (attributeTypes!=null){
			for (TrustAttributeType t : attributeTypes) {
				atrributeTypeDtos.add(new TrustAttributeTypeDto(t.getId().toString(), t.getName(), t.getName(), 
						(t.getParentType()==null), !(CollectionUtils.isEmpty(t.getSubTypes()))));
			}
		}
		return atrributeTypeDtos;
	}
	
	
	public static TrustPolicyDto toDto(TrustPolicy policy) {
		TrustPolicyDto policyDto = new TrustPolicyDto();
		policyDto.setId(policy.getId());
		List<TrustAttribute> attributes = policy.getTrustAttributes();
		for (TrustAttribute trustAttribute : attributes) {
			TrustAttributeDto attributeDto = new TrustAttributeDto();
			TrustAttributeTypeDto typeDto = new TrustAttributeTypeDto();
			typeDto.setName(trustAttribute.getTrustAttributeType().getName());
			attributeDto.setId(trustAttribute.getId());
			attributeDto.setAttributeType(typeDto);
			attributeDto.setWeight(trustAttribute.getImportance());
			attributeDto.setExpression(formulateExpression(trustAttribute));
			policyDto.getTrustAttributes().add(attributeDto);
		}
		return policyDto;
		
	}


	private static String formulateExpression(TrustAttribute trustAttribute) {
		
		
		if (isNotByMinMax(trustAttribute)){
			return StringUtils.EMPTY;
//			if (nullIfEmpty(trustAttribute.getValue())!=null){
//				return "";
//			}
//			else{
//				return StringUtils.EMPTY;
//			}
		}
		
		if (isComparisonWithinRange(trustAttribute)){
			return "between "+trustAttribute.getMinValue()+" "+trustAttribute.getMaxValue();
		}
		
		if (isComparisonByMax(trustAttribute)){
			return "less or equal "+trustAttribute.getMaxValue();
		}
		
		if (isComparisonByMin(trustAttribute)){
			return "greater or equal "+trustAttribute.getMinValue();
		}
		
		return "?expression?";

	}
	
	
	private static boolean isNotByMinMax(TrustAttribute requested) {
		return (isComparisonWithinRange(requested) == false && isComparisonByMax(requested) == false
			&& isComparisonByMin(requested) == false);
	}

	private static boolean isComparisonWithinRange(TrustAttribute requested) {
		return (nullIfEmpty(requested.getMaxValue()) != null && nullIfEmpty(requested.getMinValue()) != null);
	}

	private static boolean isComparisonByMax(TrustAttribute requested) {
		return (nullIfEmpty(requested.getMaxValue()) != null && requested.getMaxValue().equals(0) == false)
				&& (nullIfEmpty(requested.getMinValue()) == null || requested.getMinValue().equals(0));
	}

	private static boolean isComparisonByMin(TrustAttribute requested) {
		return (nullIfEmpty(requested.getMinValue()) != null && requested.getMinValue().equals(0) == false)
				&& (nullIfEmpty(requested.getMaxValue()) == null || requested.getMaxValue().equals(0));
	}
	
	private static String nullIfEmpty(String s){
		
		if (StringUtils.isBlank(s)) return null;
		return s;
	}


	public static TrustAttribute resolveExpression(TrustAttribute attr, String expression) {
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
	
	

}
