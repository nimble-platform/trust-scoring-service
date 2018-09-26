package nimble.trust.web.dto;

import org.hibernate.validator.constraints.NotBlank;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ChangeEvent {
	
	@NotBlank(message = "CompanyIdentifier must not be blank!")
	private String companyIdentifier;
	
	@NotBlank(message = "ChangeType must not be blank!")
	private String changeType;
	
	private String message;
	
	@Override
	public String toString() {
		return "ChangeEvent [companyIdentifier=" + companyIdentifier + ", type=" + changeType + "]";
	}
}
