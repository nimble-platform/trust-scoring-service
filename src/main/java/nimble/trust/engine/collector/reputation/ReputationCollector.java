package nimble.trust.engine.collector.reputation;



import java.net.URI;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.databind.JsonNode;
import com.google.common.collect.Maps;
import com.hp.hpl.jena.datatypes.xsd.impl.XSDDouble;
import com.hp.hpl.jena.ontology.OntModel;
import com.hp.hpl.jena.rdf.model.Model;
import com.hp.hpl.jena.rdf.model.ModelFactory;
import com.hp.hpl.jena.rdf.model.Resource;

import nimble.trust.engine.collector.AbstractCollector;
import nimble.trust.engine.model.factory.TrustModelFactory;
import nimble.trust.engine.model.pojo.Agent;
import nimble.trust.engine.model.pojo.TrustAttribute;
import nimble.trust.engine.model.vocabulary.Trust;
import nimble.trust.util.httpclient.Client;
import nimble.trust.util.tuple.Tuple2;
import nimble.trust.util.uri.UIDGenerator;

public class ReputationCollector extends AbstractCollector{
	
	private static final Logger log = LoggerFactory.getLogger(ReputationCollector.class);

	public ReputationCollector() {
		
	}

	@Override
	public Model collectInformation(String resourceIdentifier) {
		Resource  r = new Agent(URI.create(resourceIdentifier)).asJenaResource();
		Double reputationIndex = obtainReputationIndex(URI.create(resourceIdentifier));
		if (reputationIndex != null){
			OntModel model = ModelFactory.createOntologyModel();
			TrustModelFactory trm = new TrustModelFactory(UIDGenerator.instanceTrust);
			TrustAttribute attribute = trm.createTrustAttibute();
			attribute.addType(URI.create(Trust.Reputation.getURI()));
			attribute.setValue(reputationIndex);
			attribute.setValueDatatype(XSDDouble.XSDdouble);
			model.add(r,Trust.hasAttribute, attribute.asJenaResource());
			return model;
		}
		else{
			return null;	
		}
	}
	
	@Override
	public void collectInformation(List<URI> resources, Map<URI, Model> map) {
		
		return ;
		
//		final Map<URI, Tuple2<String, String>> mapIds = getIDMap(resources);
//		String requestBody = new ReputationAPIRequestBodyBuilder().build(mapIds.values());
//		log.info(" requestBody "+requestBody);
//		javax.ws.rs.client.Client client = ClientBuilder.newClient();
//		client.property(ClientProperties.CONNECT_TIMEOUT, 0);
//		String url = sourceUri+"/class_reputation/batch/";
//		Response response = client.target(url).request().accept(MediaType.APPLICATION_JSON)
//				.post(Entity.entity(requestBody, MediaType.APPLICATION_JSON), Response.class);
//		log.info("Responded");
//		if (response.getStatus() != 200) {
//			log.warn("Failed Reputation Collector: HTTP error code : " + response.getStatus() +" on "+url);
//			return;
//		}
//		String responseBody = response.readEntity(String.class);
//		log.info(" responseBody "+responseBody);
//		try {
//			new ReputationResponseBodyResolver().reslove(mapIds,responseBody, map);
//		} catch (Exception e) {
//			e.printStackTrace();
//			throw new RuntimeException("Error while processing Popularioty response: " + e.getMessage());
//		}
	}

	private Map<URI, Tuple2<String, String>> getIDMap(List<URI> resources) {
		Map<URI, Tuple2<String, String>> map = Maps.newHashMap();
		for (URI uri : resources) {
				Tuple2<String, String>  t = new Tuple2<String, String>(null, null);
				String path = uri.getPath();
				int i=path.lastIndexOf("/");
				t.setT1(path.substring(i+1, path.length()));
				path = path.substring(0, i);
				i=path.lastIndexOf("/");
				path = path.substring(0, i);
				i=path.lastIndexOf("/");
				//FIXME 
				if (path.substring(i+1, path.length()).equals("services")){
					t.setT2("service_instance");
				}
				else{
					t.setT2(path.substring(i+1, path.length()));
				}
				map.put(uri, t);
		}
		return map;
	}


	private double obtainReputationIndex(URI uri) {
		Client client =  new Client();
		JsonNode node = client.getJsonAsJsonNode(getSourceUri()+"/srvcid"+uri.toASCIIString());
//		System.out.println(node);
		return 0;
	}

	@Override
	public String getName() {
		return "reputation";
	}
	
	@Override
	public void shutDown() {
	}

	
}
