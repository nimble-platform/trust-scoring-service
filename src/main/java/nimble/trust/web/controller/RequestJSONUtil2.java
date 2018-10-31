package nimble.trust.web.controller;

import java.net.URI;
import java.util.List;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.collect.Lists;

import nimble.trust.engine.json.TrustPOJOFactory;
import nimble.trust.engine.model.pojo.TrustCriteria;
import nimble.trust.engine.model.vocabulary.Trust;
import nimble.trust.engine.op.enums.EnumLevel;
import nimble.trust.engine.op.enums.EnumScoreStrategy;

public class RequestJSONUtil2 {

	public static TrustCriteria getCriteria(String request) throws Exception {
		ObjectMapper mapper = new ObjectMapper();
		JsonNode rootNode = mapper.readTree(request);
		JsonNode parameters = rootNode.get("parameters");
		if (parameters == null || parameters.size() == 0){
			return null;
		}
		return new TrustPOJOFactory().ofTrustCriteria(parameters.toString());
	}

	public static List<URI> getResourceList(String request) throws Exception {
		ObjectMapper mapper = new ObjectMapper();
		JsonNode rootNode = mapper.readTree(request);
		JsonNode resources = rootNode.path("parties");
		if (resources == null || resources.size() == 0){
			throw new Exception ("No list of parties in a request payload");
		}
		List<URI> list = Lists.newArrayList();
		for (JsonNode node : resources) {
			String partyId = Trust.NS+node.get("partyId").textValue();
			list.add((URI.create(partyId)));
		}
		return list;
	}
	
	public static EnumScoreStrategy getScoreStrategy(String request) throws Exception{
		ObjectMapper mapper = new ObjectMapper();
		JsonNode rootNode = mapper.readTree(request);
		JsonNode strategy = rootNode.path("strategy");
		if (strategy !=null && strategy.textValue().equalsIgnoreCase("topis")){
		  return EnumScoreStrategy.TOPSIS;
		}
		if (strategy !=null && strategy.textValue().equalsIgnoreCase("standard")){
			  return EnumScoreStrategy.Weighted_sum_model;

		}
		return EnumScoreStrategy.Weighted_sum_model;
	}	


	public static EnumLevel getLevelFromJsonComposite(String request) throws Exception{
		ObjectMapper mapper = new ObjectMapper();
		JsonNode rootNode = mapper.readTree(request);
		JsonNode level = rootNode.path("level");
		if (level == null ||level.textValue()==null)
			throw new Exception ("No attribute level specified in a request payload");
		if (level !=null && level.textValue().equalsIgnoreCase("simple")){
		  return EnumLevel.SIMPLE;
		}
		else if (level !=null && level.textValue().equalsIgnoreCase("composite")){
			  return EnumLevel.COMPOSITE;
		}
		else {
			throw new Exception ("Attribute level specified in a request payload should be either 'simple' or 'global'");
		}
		
	}


	public static String getStrategyFromJsonComposite(String request) {
		//TODO FIXME  - future work
		return null;
	}

	

}
