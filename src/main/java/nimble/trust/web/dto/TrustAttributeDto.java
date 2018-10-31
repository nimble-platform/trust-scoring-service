package nimble.trust.web.dto;

import org.hibernate.validator.constraints.NotBlank;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TrustAttributeDto {
	
	
	private Long id;

	@NotBlank(message = "weight must not be blank!")
	private Double weight = 1D;
	
	private String expression;
	
	@NotBlank(message = "attributeType must not be blank!")
	private TrustAttributeTypeDto attributeType;
	
}
