package nimble.trust.web.dto;

import org.hibernate.validator.constraints.NotBlank;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TrustAttributeTypeDto {

	private String  id;
	
	@NotBlank(message = "trust attribute type name must not be blank!")
	private String  name;
	
	private String  nameLocalized;
	private Boolean isRoot;
	private Boolean hasSubTypes;
}
