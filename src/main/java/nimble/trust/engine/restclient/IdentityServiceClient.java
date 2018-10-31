package nimble.trust.engine.restclient;

import feign.Response;

import java.util.List;

import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.*;

/**
 * Feign client for identity microservice.
 */
@FeignClient(name = "identity-service", url = "${nimble.identity.url:}")
public interface IdentityServiceClient {
    
	@RequestMapping(method = RequestMethod.GET, value = "/party/{partyId}", produces = "application/json")
    Response getParty(@RequestHeader("Authorization") String bearerToken, @PathVariable("partyId") String partyId);
	
//	@RequestMapping(method = RequestMethod.GET, value = "/party/completeness/{partyId}", produces = "application/json")
//    Response getPartyTrust(@RequestHeader("Authorization") String bearerToken, @PathVariable("partyId") String partyId);
	
    @RequestMapping(value = "/company-settings/{partyId}/completeness", produces = {"application/json"}, method = RequestMethod.GET)
	Response getProfileCompleteness(@RequestHeader("Authorization") String bearerToken, @PathVariable("partyId") String partyId);

    @RequestMapping(method = RequestMethod.GET, value = "/parties/{partyIds}", produces = "application/json")
    Response getParties(@RequestHeader("Authorization") String bearerToken, @PathVariable("partyIds") String partyIds);

    @RequestMapping(method = RequestMethod.GET, value = "/party/all", produces = "application/json")
    Response getAllPartyIds(@RequestHeader("Authorization") String bearerToken, @RequestParam(value = "exclude", required = false) List<String> exclude);
}