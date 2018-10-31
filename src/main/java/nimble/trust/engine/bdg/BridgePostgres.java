package nimble.trust.engine.bdg;




import java.net.URL;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.hp.hpl.jena.datatypes.xsd.XSDDatatype;
import com.hp.hpl.jena.rdf.model.Model;
import com.hp.hpl.jena.rdf.model.ModelFactory;
import com.hp.hpl.jena.rdf.model.Resource;
import com.hp.hpl.jena.rdf.model.ResourceFactory;
import com.hp.hpl.jena.vocabulary.RDF;

import nimble.trust.engine.domain.TrustAttribute;
import nimble.trust.engine.domain.TrustProfile;
import nimble.trust.engine.model.vocabulary.NSPrefixes;
import nimble.trust.engine.model.vocabulary.Trust;
import nimble.trust.engine.service.BeanUtil;
import nimble.trust.engine.service.TrustProfileService;

/**
 * Obtainer of trust profile from sql database and turn it into graph
 * @author marko
 *
 */
public class BridgePostgres extends ABridge{
	
	
	Logger log = LoggerFactory.getLogger(BridgePostgres.class);

	
	@Override
	public synchronized Model getTrustProfile(String serviceId)   {
		
		TrustProfileService profileService = BeanUtil.getBean(TrustProfileService.class);
		String partyId = serviceId.substring(serviceId.indexOf("#")+1, serviceId.length());
		TrustProfile profile = profileService.findByAgentAltId(partyId);
		
		Model m = toModel(profile, serviceId);
//		RDFDataMgr.write(System.out, m, RDFFormat.TTL);
		return m;
	}



	private Model toModel(TrustProfile profileTrust, String serviceId) {
		
		List<TrustAttribute> attributes = profileTrust.getTrustAttributes();
		
		Model model = ModelFactory.createDefaultModel();
		model.setNsPrefixes(NSPrefixes.map);
		
		if (profileTrust!= null){
			try {
				Resource profile = null;
				for (TrustAttribute trustAttribute : attributes) {
					
					Long agentId = profileTrust.getOwner().getId();
					URL agentComposeUID = new URL(serviceId);
					String agentName = profileTrust.getOwner().getAltId();
					Long profileId = profileTrust.getId();
					if (profile == null){
						profile = addAgentAndProfile(model, agentId, agentName, agentComposeUID, profileId, serviceId);
					}
					Long attributeId = trustAttribute.getId();
					String attributeType = trustAttribute.getTrustAttributeType().getName();
					Object value = trustAttribute.getValue();
					addAttributeToProfile(profile, model,attributeId, attributeType, value);
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return model;
	}



	private Resource addAttributeToProfile(Resource profile, Model model, Long attributeId, String attributeType, Object value) {
		Resource attribute = createJenaResource(NSPrefixes.map.get("trust"),"attribute",attributeId);
		model.add(attribute, RDF.type, ResourceFactory.createResource(Trust.NS+attributeType));
		if (value!=null){
			model.add(attribute, Trust.hasValue, value.toString(), XSDDatatype.XSDdecimal);
		}
		model.add(profile, Trust.hasAttribute, attribute);
		return attribute;
	}


	private Resource addAgentAndProfile(Model model, Long agentId, String agentName, URL internalId, Long profileId, String serviceId) {
		Resource agent = createJenaResource(NSPrefixes.map.get("trust"),"agent",agentId);
		Resource profile = createJenaResource(NSPrefixes.map.get("trust"),"profile",profileId);
		model.add(agent, RDF.type, Trust.Agent);
		model.add(agent, Trust.hasName, agentName);
		model.add(agent,(ModelFactory.createDefaultModel().createProperty(Trust.NS+"nimbleId")), ResourceFactory.createResource(internalId.toString()));
		model.add(agent,(ModelFactory.createDefaultModel().createProperty(Trust.NS+"inputUID")), ResourceFactory.createResource(inputServiceID));
		model.add(agent, Trust.hasProfile, profile);
		model.add(profile, RDF.type, Trust.TrustProfile);
		return profile;
	}
	
	private Resource createJenaResource(String namespace, String type, Long id){
		String uri = namespace+""+type+"/"+id;
		return ResourceFactory.createResource(uri);
	}



	
	@Override
	public void stop() {
		//nothing here
	}
}
