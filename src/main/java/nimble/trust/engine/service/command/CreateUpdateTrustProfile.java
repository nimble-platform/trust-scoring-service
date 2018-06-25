package nimble.trust.engine.service.command;



import java.net.URI;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.hp.hpl.jena.ontology.OntModel;
import com.hp.hpl.jena.rdf.model.Model;
import com.hp.hpl.jena.rdf.model.ModelFactory;

import nimble.trust.engine.kb.SharedOntModelSpec;
import nimble.trust.engine.model.factory.TrustModelFactory;
import nimble.trust.engine.model.io.ToGraphParser;
import nimble.trust.engine.model.pojo.Agent;
import nimble.trust.engine.model.vocabulary.Trust;
import nimble.trust.util.uri.UIDGenerator;


/**
 * A CreateUpdateTrustProfile command responsible for creation/update of trust profile object
 * for a given resource identified with URI
 * 
 *@author markov
 *
 */
public class CreateUpdateTrustProfile {
	
	 private static final Logger log = LoggerFactory.getLogger(CreateUpdateTrustProfile.class);
	
	public CreateUpdateTrustProfile(){
		
	}
	
	/**
	 * For a given resource (identified by uri), retrieve and update a trust
	 * profile. If trust profile was not existing for the given resource,
	 * this method will create one. Trust profile data are collected by invoking
	 * collectors.
	 * 
	 * @param model as a rdf graph that either contains trust profile resource or has to get one
	 * @param uri resource that needs trust profile
	 * @param collectors a list of trust information collectors
	 * @return model having trust profile data
	 */
	public OntModel apply(OntModel model, URI uri, List<Model> collectedDataList){
		
		if (model.contains(null, Trust.hasProfile)) {
			log.info("Profile for " + uri.toASCIIString() + " exists and has been found");
		} else {
			log.info("No profile exists for " + uri.toASCIIString() + ". Creating the profile.");
			Agent service = new Agent(uri);
			TrustModelFactory trm = new TrustModelFactory(UIDGenerator.instanceTrust);
			service.setHasTrustProfile(trm.createTrustProfile());
			OntModel m = new ToGraphParser().parse(service);
			model = ModelFactory.createOntologyModel(SharedOntModelSpec.getModelSpecShared(), model.union(m));
		}
		for (Model collectedData : collectedDataList) {
			if (collectedData != null) {
				model = (OntModel) model.union(collectedData);
			}
		}
		return model;
	}
}
