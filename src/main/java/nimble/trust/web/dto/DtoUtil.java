package nimble.trust.web.dto;

import java.util.List;

import org.springframework.util.CollectionUtils;

import com.google.common.collect.Lists;

import nimble.trust.engine.domain.TrustAttributeType;

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
	
	

}
