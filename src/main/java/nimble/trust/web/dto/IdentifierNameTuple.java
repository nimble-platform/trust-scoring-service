package nimble.trust.web.dto;

import java.util.Map;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import nimble.trust.engine.service.config.NimbleConfigurationProperties;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class IdentifierNameTuple {

	private String companyID;
	private Map<NimbleConfigurationProperties.LanguageID, String> names;

}
