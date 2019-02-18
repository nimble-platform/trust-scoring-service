package nimble.trust.web.controller;

import java.net.URI;
import java.util.List;

import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.google.common.collect.Lists;

import eu.nimble.service.model.ubl.commonaggregatecomponents.PartyType;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import nimble.trust.common.OrderType;
import nimble.trust.engine.domain.TrustPolicy;
import nimble.trust.engine.json.ProduceJSON;
import nimble.trust.engine.model.pojo.TrustCriteria;
import nimble.trust.engine.module.Factory;
import nimble.trust.engine.op.enums.EnumScoreStrategy;
import nimble.trust.engine.service.ChangeEventHandlerService;
import nimble.trust.engine.service.TrustCalculationService;
import nimble.trust.engine.service.TrustPolicyService;
import nimble.trust.engine.service.TrustProfileService;
import nimble.trust.engine.service.interfaces.TrustSimpleManager;
import nimble.trust.engine.util.PolicyConverter;
import nimble.trust.swagger.api.CalculateApi;
import nimble.trust.swagger.api.FilterApi;
import nimble.trust.util.tuple.Tuple2;
import nimble.trust.web.dto.ChangeEvent;

@Controller
public class TrustScoreController implements FilterApi, CalculateApi {
	
	private static Logger log = LoggerFactory.getLogger(TrustScoreController.class);
	
	@Autowired
	private ChangeEventHandlerService eventHandlerService;

	@Autowired
	private TrustProfileService trustProfileService;
	
	@Autowired
	private TrustPolicyService trustPolicyService;
	
	@Autowired
	private TrustCalculationService trustCalculationService;
	
	
    @ApiOperation(value = "Notification of trust data change", 
    		notes = "Call this operation when trust-related data of a company are changed. "
    				+ "After calling this operation, trust service will collect updates are will recalculate the trust score. "
    				+ "Valid options for changeType are 'ratings-update','company_details', 'company_description','company_certificates','company_trade'", 
    		response = String.class, tags={  })
    @ApiResponses(value = { 
        @ApiResponse(code = 200, message = "ChangeEvent succesfull processed", response = String.class),
        @ApiResponse(code = 400, message = "Bad request - validation failed", response = String.class),
        @ApiResponse(code = 500, message = "Error in processing the notification", response = String.class)})
    @RequestMapping(value = "/notifyChange", produces = { "application/json" },  method = RequestMethod.POST)
    public ResponseEntity<String> notificationTrustDataChange(@RequestHeader(value="Authorization", required=true) String bearerToken,
    		@ApiParam(value = "ChangeNotification" ,required=true) @Valid @RequestBody ChangeEvent changeEvent){
    	try {
    		final Authentication auth = new UsernamePasswordAuthenticationToken(bearerToken, null);
    		SecurityContextHolder.getContext().setAuthentication(auth);
    		eventHandlerService.postChangeEvent(changeEvent);
			return new ResponseEntity<>(HttpStatus.OK);
		} catch (Exception e) {
			log.error("notificationTrustDataChange failed ", e);
			return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
		}
    }
    
    
    @ApiOperation(value = "Obtain Party with trust score", notes = "Obtain UBL Party with trust score",
    		response = PartyType.class, tags={  })
    @ApiResponses(value = { 
            @ApiResponse(code = 200, message = "Request succesfull processed", response = String.class),
            @ApiResponse(code = 404, message = "Party with partyId not found", response = String.class),
            @ApiResponse(code = 500, message = "Internal Server Error", response = String.class)})
    @RequestMapping(value = "/party/{partyId}/trust",produces = MediaType.APPLICATION_JSON_VALUE,method = RequestMethod.GET)
    public ResponseEntity<?> getPartyTrustData(@ApiParam(value = "Identifier of the party") @PathVariable("partyId") String partyId,
                                             @ApiParam(value = "Authorization header to be obtained via login to the NIMBLE platform") @RequestHeader(value = "Authorization") String bearerToken) {
    	
    	PartyType partyType = null;
    	try {
    		partyType = trustProfileService.createPartyType(partyId);
    		if (partyType == null){
    			log.warn("GetPartyTrustData not found data for partyId:"+partyId);
        		return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        	}
        	else{
        		log.info("GetPartyTrustData successfully executed for partyId:"+partyId);
        		return new ResponseEntity<>(partyType, HttpStatus.OK);
        	}
		} catch (Exception e) {
			log.error("GetPartyTrustData failed for partyId"+partyId, e);
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
    }
    
    
    @ApiOperation(value = "Obtain list of parties with their trust score", notes = "Obtain list of parties with their trust score",
    		response = PartyType.class, tags={  }, responseContainer = "List")
    @ApiResponses(value = { 
            @ApiResponse(code = 200, message = "Request succesfull processed", response = String.class),
            @ApiResponse(code = 404, message = "No data found", response = String.class),
            @ApiResponse(code = 500, message = "Internal Server Error", response = String.class)})
    @RequestMapping(value = "/party/list/trust",produces = MediaType.APPLICATION_JSON_VALUE,method = RequestMethod.GET)
    public ResponseEntity<?> getListPartyTrustData(@ApiParam(value = "Authorization header to be obtained via login to the NIMBLE platform") @RequestHeader(value = "Authorization") String bearerToken) {
    	
    	List<PartyType> partyTypes = Lists.newArrayList();
    	try {
    		partyTypes = trustProfileService.listPartiesWithTrustData();
    		log.info("getListPartyTrustData successfully executed. List size+"+partyTypes.size());
    		return new ResponseEntity<>(partyTypes, HttpStatus.OK);
		} catch (Exception e) {
			log.error("getListPartyTrustData failed", e);
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
    }
    
    
    
    @ApiOperation(value = "Calculate trust score using global policy", notes = "Calculate trust score using global policy",
    		response = PartyType.class, tags={  })
    @ApiResponses(value = { 
            @ApiResponse(code = 200, message = "Request succesfull processed", response = String.class),
            @ApiResponse(code = 404, message = "Party with partyId not found", response = String.class),
            @ApiResponse(code = 500, message = "Internal Server Error", response = String.class)})
    @RequestMapping(value = "/calculate/global/{partyId}",produces = MediaType.APPLICATION_JSON_VALUE,method = RequestMethod.POST)
    public ResponseEntity<?> calculateTrustScore(@ApiParam(value = "Identifier of the party") @PathVariable String partyId,
                                             @ApiParam(value = "Authorization header to be obtained via login to the NIMBLE platform") @RequestHeader(value = "Authorization") String bearerToken) {
    	
    	try {
    		
    		PartyType partyType = trustProfileService.createPartyType(partyId);
    		if (partyType == null){
    			log.warn("GetPartyTrustData not found data for partyId:"+partyId);
        		return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        	}
    		
    		trustCalculationService.score(partyId);
            return new ResponseEntity<>(HttpStatus.OK);
		} catch (Exception e) {
			log.error("Calculation failed for partyId"+partyId, e);
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
    }
    
    
    @ApiOperation(value = "Recalculates trust score using global policy for all parties in trust database", 
    		notes = "Asynchronous operation that recalculates a trust score  for all parties in trust database using global policy. Only ADMINs should use this operation",
    		response = PartyType.class, tags={  })
    @ApiResponses(value = { 
            @ApiResponse(code = 200, message = "Request succesfull processed", response = String.class),
            @ApiResponse(code = 500, message = "Internal Server Error", response = String.class)})
    @RequestMapping(value = "/recalculate/batch",produces = MediaType.APPLICATION_JSON_VALUE,method = RequestMethod.POST)
    public ResponseEntity<?> recalculateTrustScoreBatch(
                                             @ApiParam(value = "Authorization header to be obtained via login to the NIMBLE platform") @RequestHeader(value = "Authorization") String bearerToken) {
    	
    	try {
    		trustCalculationService.scoreBatch();
            return new ResponseEntity<>(HttpStatus.OK);
		} catch (Exception e) {
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
 
    }
   
    
    @ApiOperation(value = "Create trust profiles for all parties registered in the platform", 
    		notes = "Asynchronous operation that creates trust profiles for all parties registered in the platform. "
    				+ "Trust score is calculated using global trust policy. "
    				+ "Only ADMINs should use this operation",
    		response = PartyType.class, tags={  })
    @ApiResponses(value = { 
            @ApiResponse(code = 200, message = "Request succesfull processed", response = String.class),
            @ApiResponse(code = 500, message = "Internal Server Error", response = String.class)})
    @RequestMapping(value = "/fetch-all-calculate/batch",produces = MediaType.APPLICATION_JSON_VALUE,method = RequestMethod.POST)
    public ResponseEntity<?> fetchAndCalculateTrustScoreBatch(
                                             @ApiParam(value = "Authorization header to be obtained via login to the NIMBLE platform") @RequestHeader(value = "Authorization") String bearerToken) {
    	
    	try {
    		trustCalculationService.createAllAndScoreBatch();
            return new ResponseEntity<>(HttpStatus.OK);
		} catch (Exception e) {
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
 
    }
    
    
	public ResponseEntity<String> calculateCustom(@RequestBody String request) {
		
		log.info("Invoked Rest: scoring using custom policy");
		try {
					
			final TrustSimpleManager trustManager = Factory.createInstance(TrustSimpleManager.class);
			TrustCriteria criteria = RequestJSONUtil2.getCriteria(request);
			if (criteria == null)  {
				TrustPolicy trustPolicy = trustPolicyService.findGlobalTRustPolicy();
				criteria = PolicyConverter.fromPolicyToCriteria(trustPolicy);
			}
			final List<URI> list = RequestJSONUtil2.getResourceList(request);
			EnumScoreStrategy strategy = RequestJSONUtil2.getScoreStrategy(request);
			List<Tuple2<URI, Double>> result = null;
			if (strategy == EnumScoreStrategy.TOPSIS) {
				result = trustManager.rankResources(list, criteria, EnumScoreStrategy.TOPSIS, false,OrderType.DESC);
			} else {
				trustManager.setGlobalTrustCriteria(criteria);
				result = trustManager.obtainTrustIndexes(list);
			}
			return new ResponseEntity<>(new ProduceJSON().ofRankingResult(result), HttpStatus.OK);
		} catch (Exception e) {
			 e.printStackTrace();
			 return new ResponseEntity<>(new ProduceJSON().ofError(e), HttpStatus.BAD_REQUEST);
		}
	}
	

	
	public ResponseEntity<String> filteringByThreshold(@RequestBody String request) {
		log.info("Invoked Rest: filteringByThreshold");
		try {
			List<URI> filtered ;
			final TrustSimpleManager trustManager = Factory.createInstance(TrustSimpleManager.class);
			TrustCriteria criteria = RequestJSONUtil2.getCriteria(request);
			if (criteria == null) 
				criteria = trustManager.getGlobalTrustCriteria();
			final List<URI> resources = RequestJSONUtil2.getResourceList(request);
			filtered = trustManager.filterTrustedByThreshold(resources, criteria);
			return new ResponseEntity<>(new ProduceJSON().ofFilteringResult(filtered), HttpStatus.OK);
		} catch (Exception e) {
			 e.printStackTrace();
			 return new ResponseEntity<>(new ProduceJSON().ofError(e), HttpStatus.BAD_REQUEST);
		}
	}
	
	
	public ResponseEntity<String> filterByCriteriaNotMeet(@RequestBody String request) {
		log.info("Invoked Rest: filterByCriteriaNotMeet");
		
		try {
			List<URI> filtered ;
			final TrustSimpleManager trustManager = Factory.createInstance(TrustSimpleManager.class);
			TrustCriteria criteria = RequestJSONUtil2.getCriteria(request);
			if (criteria == null) 
				criteria = trustManager.getGlobalTrustCriteria();
			final List<URI> resources = RequestJSONUtil2.getResourceList(request);
			filtered = trustManager.filterByCriteriaNotMeet(resources, criteria);
			return new ResponseEntity<>(new ProduceJSON().ofFilteringResult(filtered), HttpStatus.OK);
		} catch (Exception e) {
			 e.printStackTrace();
			 return new ResponseEntity<>(new ProduceJSON().ofError(e), HttpStatus.BAD_REQUEST);
		}
	}
	
}
