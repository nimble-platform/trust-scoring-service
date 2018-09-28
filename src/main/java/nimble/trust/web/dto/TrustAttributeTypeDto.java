package nimble.trust.web.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TrustAttributeTypeDto {

	private String  id;
	private String  name;
	private String  nameLocalized;
	private Boolean isRoot;
	private Boolean hasSubTypes;
}
