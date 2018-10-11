package nimble.trust.web.controller;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import nimble.trust.engine.domain.TrustAttributeType;
import nimble.trust.engine.domain.TrustPolicy;
import nimble.trust.engine.service.TrustAttributeTypeService;
import nimble.trust.engine.service.TrustPolicyService;
import nimble.trust.web.dto.DtoUtil;
import nimble.trust.web.dto.TrustAttributeTypeDto;
import nimble.trust.web.dto.TrustPolicyDto;

@Api(value = "trust-policy", description = "The trust policy API")
@Controller
public class TrustPolicyController {
	
	private static Logger log = LoggerFactory.getLogger(TrustPolicyController.class);
	
	
	@Autowired
    private TrustAttributeTypeService trustAttributeTypeService;
	
	@Autowired
	private TrustPolicyService trustPolicyService;


	@ApiOperation(value = "List trust metric types", 
			notes = "Returns a list of trust metric types", 
			response = TrustAttributeTypeDto.class, responseContainer = "List")
	@ApiResponses(value = { @ApiResponse(code = 200, message = "List returned sucessfully",response=TrustAttributeTypeDto.class)})
	@RequestMapping(value = "/metrictypes/all", produces = { "application/json" }, method = RequestMethod.GET)
	public ResponseEntity<List<TrustAttributeTypeDto>> getAllMetricTypes(
			@RequestHeader(value = "Authorization") String bearerToken) {
		List<TrustAttributeType> attributeTypes = trustAttributeTypeService.findAllRootLevel();
		List<TrustAttributeTypeDto> converted = DtoUtil.toDto(attributeTypes);
		return new ResponseEntity<List<TrustAttributeTypeDto>>(converted, HttpStatus.OK);
	}
	
	@ApiOperation(value = "List trust metric subtypes", 
			notes = "Returns a list trust metric subtypes for a given parent type id", 
			response = TrustAttributeTypeDto.class, responseContainer = "List")
	@ApiResponses(value = { 
			@ApiResponse(code = 200, message = "List returned sucessfully",response=TrustAttributeTypeDto.class),
			@ApiResponse(code = 404, message = "Type not found",response=String.class)
			})
	@RequestMapping(value = "/metrictypes/sub/{typeId}", produces = { "application/json" }, method = RequestMethod.GET)
	public ResponseEntity<Object> getSubmetricTypes( @PathVariable Long typeId, 
			@RequestHeader(value = "Authorization") String bearerToken) {
		
		TrustAttributeType attributeType = trustAttributeTypeService.findById(typeId);
		
		if (attributeType == null){
		    String msg = String.format("Failed to find trust metric type with: %s", typeId);
		    log.warn(msg);
		    return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}
		List<TrustAttributeTypeDto> converted = DtoUtil.toDto(attributeType.getSubTypes());
		return new ResponseEntity<>(converted, HttpStatus.OK);
	}

	
	@ApiOperation(value = "Get global trust policy", 
			notes = "Provides global trust policy", 
			response = TrustPolicy.class, responseContainer = "")
	@ApiResponses(value = { 
			@ApiResponse(code = 200, message = "Policy returned sucessfully",response=TrustPolicyDto.class),
			@ApiResponse(code = 404, message = "Policy not found",response=String.class)
			})
	@RequestMapping(value = "/policy/global", produces = { "application/json" }, method = RequestMethod.GET)
	public ResponseEntity<TrustPolicyDto> getTrustPolicy( @RequestHeader(value = "Authorization") String bearerToken) {
		
		TrustPolicy policy = trustPolicyService.findGlobalTRustPolicy();
				
		if (policy == null){
		    String msg = "Failed to find trust policy";
		    log.warn(msg);
		    return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}
		TrustPolicyDto dto = DtoUtil.toDto(policy);
		return new ResponseEntity<>(dto, HttpStatus.OK);
	}
	
	
	@ApiOperation(value = "Update global trust policy", 
			notes = "Updates global trust policy", 
			response = TrustPolicy.class, responseContainer = "")
	@ApiResponses(value = { 
			@ApiResponse(code = 200, message = "Policy configured sucessfully",response=TrustPolicyDto.class),
			@ApiResponse(code = 404, message = "Policy not found",response=String.class),
			@ApiResponse(code = 500, message = "Internal error",response=String.class)
			})
	@RequestMapping(value = "/policy/global/update", produces = { "application/json" }, method = RequestMethod.POST)
	public ResponseEntity<Object> updateTrustPolicy(@RequestHeader(value = "Authorization") String bearerToken, @RequestBody(required=true) TrustPolicyDto trustPolicyDto) {
		
		try {
			TrustPolicy policy = trustPolicyService.findById(trustPolicyDto.getId());
			if (policy == null){
			    String msg = "Failed to find trust policy with id"+trustPolicyDto.getId();
			    log.warn(msg);
			    return new ResponseEntity<>(HttpStatus.NOT_FOUND);
			}
			trustPolicyService.createOrUpdateTrustPolicy(trustPolicyDto);
			log.info("trust policy updated");
			return new ResponseEntity<>(HttpStatus.OK);
		} catch (Exception e) {
			 log.error("Internal error", e);
			 return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
	
	}
	
	@ApiOperation(value = "Initialize new global trust policy", 
			notes = "Initializes new global trust policy", 
			response = TrustPolicy.class, responseContainer = "")
	@ApiResponses(value = { 
			@ApiResponse(code = 200, message = "Policy sucessfully initialized",response=TrustPolicyDto.class),
			@ApiResponse(code = 500, message = "Internal error",response=String.class)
			})
	@RequestMapping(value = "/policy/global/initialize", produces = { "application/json" }, method = RequestMethod.POST)
	public ResponseEntity<TrustPolicyDto> createNewTrustPolicy(@RequestHeader(value = "Authorization") String bearerToken) {
		
		try {
			TrustPolicy policy = trustPolicyService.createTrustPolicy();
			return new ResponseEntity<TrustPolicyDto>(DtoUtil.toDto(policy), HttpStatus.OK);
		} catch (Exception e) {
			 log.error("Internal error", e);
			 return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
    
}
