package nimble.trust.engine.restclient;

import java.util.List;

import feign.Response;
import org.springframework.stereotype.Component;

/**
 *
 * @author Ayeshmantha Perera
 */
@Component
public class IdentityServiceClientFallBack implements IdentityServiceClient {

	@Override
	public Response getParty(String bearerToken, String partyId) {
		return null;
	}

	@Override
	public Response getProfileCompleteness(String partyId) {
		return null;
	}

	@Override
	public Response getParties(String bearerToken, String partyIds) {
		return null;
	}

	@Override
	public Response getAllPartyIds() {
		return null;
	}
}
