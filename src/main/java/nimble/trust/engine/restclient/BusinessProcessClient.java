package nimble.trust.engine.restclient;

import feign.Response;
import io.swagger.annotations.ApiParam;

import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.*;

/**
 * Feign client for business-process microservice.
 */
@FeignClient(name = "business-process-service", url = "${nimble.business-process.url:}")
public interface BusinessProcessClient {


	@RequestMapping(value = "/statistics/overall", produces = { "application/json" }, method = RequestMethod.GET)
	Response getOverallStatistics(@RequestParam(value = "partyID") String partyID,
			@RequestParam(value = "role", required = false, defaultValue = "SELLER") String role,
			@RequestHeader(value = "Authorization", required = true) String bearerToken);

	
	@RequestMapping(value = "/statistics/trading-volume", produces = { "application/json" }, method = RequestMethod.GET)
	Response getTradingVolume(@RequestParam(value = "startDate", required = false) String startDateStr,
			@RequestParam(value = "endDate", required = false) String endDateStr,
			@RequestParam(value = "companyId", required = false) String companyId,
			@ApiParam(value = "Role in business process. Can be SELLER or BUYER", required = false) @RequestParam(value = "role", required = false, defaultValue = "SELLER") String role,
			@ApiParam(value = "State of transaction. Can be WaitingResponse, Approved or Denied", required = false) @RequestParam(value = "status", required = false) String status);

	
	@RequestMapping(value = "/statistics/total-number/business-process", produces = {"application/json" }, method = RequestMethod.GET)
	Response getProcessCount(
			@ApiParam(value = "Business process type. ", required = false) @RequestParam(value = "businessProcessType", required = false) String businessProcessType,
			@ApiParam(value = "Start date (DD-MM-YYYY)", required = false) @RequestParam(value = "startDate", required = false) String startDateStr,
			@ApiParam(value = "End date (DD-MM-YYYY)", required = false) @RequestParam(value = "endDate", required = false) String endDateStr,
			@ApiParam(value = "Company ID", required = false) @RequestParam(value = "companyId", required = false) String companyId,
			@ApiParam(value = "Role in business process. Can be seller or buyer", required = false) @RequestParam(value = "role", required = false, defaultValue = "SELLER") String role,
			@ApiParam(value = "State of transaction. Can be WaitingResponse, Approved or Denied", required = false) @RequestParam(value = "status", required = false) String status);

}