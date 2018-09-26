package nimble.trust.engine.restclient;

import feign.Response;


import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.*;

import eu.nimble.service.model.ubl.commonaggregatecomponents.PartyType;

/**
 * Feign client for catalog service microservice.
 */
@FeignClient(name = "catalog-service", url = "${nimble.catalog.url:}")
public interface CatalogServiceClient {
	
	@RequestMapping(method = RequestMethod.POST, value = "/trustscore/{partyId}/update", consumes = "application/json")
    Response postTrustScoreUpdate(@PathVariable("partyId") Long partyId, PartyType partyType);

}