package nimble.trust.engine.restclient;

import feign.Response;
import io.swagger.annotations.ApiParam;

import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import eu.nimble.service.model.ubl.commonaggregatecomponents.PartyType;

/**
 * Feign client for trust microservice.
 */
@FeignClient(name = "trust-service", url = "${nimble.trust.url:}")
public interface TrustServiceClient {
	
    @RequestMapping(method = RequestMethod.GET, value = "/version", produces = "application/json")
    Response getVersion();
    
    
    @RequestMapping(value = "/party/{partyId}/trust",produces = MediaType.APPLICATION_JSON_VALUE,method = RequestMethod.GET)
    Response getPartyTrustData( @RequestHeader(value = "Authorization") String bearerToken, @PathVariable("partyId") String partyId);
    
}