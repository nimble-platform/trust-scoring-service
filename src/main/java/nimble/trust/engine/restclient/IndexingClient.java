package nimble.trust.engine.restclient;

import eu.nimble.service.model.ubl.commonaggregatecomponents.PartyType;
import feign.Headers;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * REST Client to index entities via the Indexing Service.
 *
 * @author Ayeshmantha Perera
 */
@FeignClient(name = "indexing-service", url = "${nimble.indexing.url}", fallback = IndexingClientFallback.class)
public interface IndexingClient {

	@RequestMapping(method = RequestMethod.POST, value = "/party/trust")
	Boolean partyTrustUpdate(
			@RequestParam(value = "partyId") String partyId,
			@RequestBody PartyType partyType,
			@RequestHeader(value = "Authorization", required = true) String bearerToken);

}
