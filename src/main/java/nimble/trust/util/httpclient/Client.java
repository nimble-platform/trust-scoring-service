package nimble.trust.util.httpclient;




import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Invocation;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.Response;

import org.glassfish.jersey.client.authentication.HttpAuthenticationFeature;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;


public class Client {

	/**
	 * 
	 * @param uri
	 * @return
	 */
	public JsonNode getJsonAsJsonNode(String uri) {

		final String output = getJSONReponse(uri);
		ObjectMapper m = new ObjectMapper();
		JsonNode node = null;
		try {
			node = m.readTree(output);
			// to parse -> node.get("indexes").get(0).get("rank"));
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException("Failed JSON parsing: error code : " + e.getMessage());
		}
		return node;
	}

	/**
	 * 
	 * @param uri
	 * @return
	 */
	public String getJSONReponse(String uri) {

		//FIXME pass username/password as parameters

		javax.ws.rs.client.Client client = ClientBuilder.newClient();
		HttpAuthenticationFeature authenticationFeature = HttpAuthenticationFeature.universal("user", "superSecretPassword");
		client.register(authenticationFeature);

		WebTarget webTarget = client.target(uri);
		
		Invocation.Builder invocationBuilder =
				webTarget.property("Content-Type", "application/json;charset=UTF-8").request();
		
		Response response = invocationBuilder.get();
		if (response.getStatus() != 200) {
			throw new RuntimeException("Failed : HTTP error code : " + response.getStatus());
		}
		String output = response.readEntity(String.class);
		return output;
	}
	
	/**
	 * 
	 * @param uri
	 * @return
	 */
	public String getRDFReponse(String uri) {

		javax.ws.rs.client.Client client = ClientBuilder.newClient();		
//		String entity = client.target(uri).request().get(String.class);
		WebTarget webTarget = client.target(uri);
		Invocation.Builder invocationBuilder =
				webTarget.property("Content-Type", "application/x-turtle;charset=UTF-8").request();
		Response response = invocationBuilder.get();
		if (response.getStatus() != 200) {
			throw new RuntimeException("Failed : HTTP error code : " + response.getStatus());
		}
		String output = response.readEntity(String.class);
		return output;
	}

}
