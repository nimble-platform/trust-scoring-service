package nimble.trust.web.controller;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
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
	@RequestMapping(value = "/trust/metrictypes/all", produces = { "application/json" }, method = RequestMethod.GET)
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
	@RequestMapping(value = "/trust/metrictypes/sub/{typeId}", produces = { "application/json" }, method = RequestMethod.GET)
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

	
	@ApiOperation(value = "Get trust policy", 
			notes = "Provides trust policy", 
			response = TrustPolicy.class, responseContainer = "")
	@ApiResponses(value = { 
			@ApiResponse(code = 200, message = "Policy returned sucessfully",response=TrustPolicy.class),
			@ApiResponse(code = 404, message = "TrustPolicy not found",response=String.class)
			})
	@RequestMapping(value = "/trust/policy/global", produces = { "application/json" }, method = RequestMethod.GET)
	public ResponseEntity<Object> getTrustPolicy( @RequestHeader(value = "Authorization") String bearerToken) {
		
		TrustPolicy policy = trustPolicyService.findGlobalTRustPolicy();
				
		if (policy == null){
		    String msg = "Failed to find trust policy";
		    log.warn(msg);
		    return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}
		return new ResponseEntity<>(policy, HttpStatus.OK);
	}
	
	
    
}
