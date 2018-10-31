package nimble.trust.web.dto;

import java.util.List;

import com.google.common.collect.Lists;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;




@Data
@NoArgsConstructor
@AllArgsConstructor
public class TrustPolicyDto {
	
	
	private Long id;
	
	private Boolean recalculateScoresWhenUpdated = false;
	
	private List<TrustAttributeDto> trustAttributes = Lists.newArrayList();

}
