package nimble.trust.engine.restclient;

import feign.Response;


import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.*;

/**
 * Feign client for trust microservice.
 */
@FeignClient(name = "trust-service", url = "${nimble.trust.url:}")
public interface TrustServiceClient {
	
    @RequestMapping(method = RequestMethod.GET, value = "/version", produces = "application/json")
    Response getVersion();

}