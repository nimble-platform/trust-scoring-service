package nimble.trust.engine.restclient;

import feign.Response;


import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import eu.nimble.service.model.ubl.commonaggregatecomponents.PartyType;

/**
 * Feign client for catalog service microservice.
 */
@FeignClient(name = "catalog-service", url = "${nimble.catalog.url:}")
public interface CatalogServiceClient {
	
    @RequestMapping(value = "/party/{partyId}/trust",consumes = MediaType.APPLICATION_JSON_VALUE,method = RequestMethod.POST)
    public Response postTrustScoreUpdate(@PathVariable("partyId") String partyId,
    		@RequestBody PartyType partyType,@RequestHeader(value = "Authorization") String bearerToken);


}