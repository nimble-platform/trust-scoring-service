package nimble.trust.engine.kb.config;



import java.util.Map;

import org.apache.jena.riot.stream.LocationMapper;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableMap.Builder;

import nimble.trust.common.Const;
import nimble.trust.engine.model.vocabulary.ModelEnum;

public class LocationMapping {

	public static Builder<String, String> getMapping() {
		
		ImmutableMap.Builder<String, String> map = ImmutableMap.builder();

		map.put(ModelEnum.Trust.getURI(),  					Const.repoOntologies+"trustontology.ttl");
		map.put(ModelEnum.SecuritypolicyVocab.getURI(),		Const.repoOntologies+"securitypolicyvocab.ttl");
		map.put(ModelEnum.UsdlSec.getURI(),					Const.repoOntologies+"usdl-sec.ttl");
		map.put(ModelEnum.Ssn.getURI(),						Const.repoOntologies+"ssn.owl");
		map.put(ModelEnum.SecurityOntology.getURI(), 		Const.repoOntologies+"securityontology.ttl");
		map.put(ModelEnum.Dul.getURI(), 					"http://www.loa-cnr.it/ontologies/DUL.owl");
		
        return map;
	}
	
	
	public static synchronized String resolveLocation(String modelUri){
		return LocationMapping.getMapping().build().get(modelUri);
	}
	
	public static synchronized LocationMapper obtainLocationMapper(){
		LocationMapper lm = new LocationMapper();
		Map<String, String> locationMappings = LocationMapping.getMapping().build();
		  for (Map.Entry<String, String> mapping : locationMappings.entrySet()) {
	            lm.addAltEntry(mapping.getKey(), mapping.getValue());
	        }

		return lm;
	}

}
