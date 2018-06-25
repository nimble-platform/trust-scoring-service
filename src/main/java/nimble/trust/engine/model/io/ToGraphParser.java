package nimble.trust.engine.model.io;



import java.util.Collection;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.hp.hpl.jena.ontology.OntModel;
import com.hp.hpl.jena.rdf.model.Model;
import com.hp.hpl.jena.rdf.model.ModelFactory;
import com.hp.hpl.jena.rdf.model.ResourceFactory;
import com.hp.hpl.jena.vocabulary.RDF;

import nimble.trust.engine.model.pojo.Agent;
import nimble.trust.engine.model.pojo.TResource;
import nimble.trust.engine.model.pojo.TrustAttribute;
import nimble.trust.engine.model.pojo.TrustProfile;
import nimble.trust.engine.model.vocabulary.NSPrefixes;
import nimble.trust.engine.model.vocabulary.Trust;

//TODO check if this class implementation is done 
public class ToGraphParser {

	private static final Logger log = LoggerFactory.getLogger(ToGraphParser.class);

	public ToGraphParser() {

	}
	
	public OntModel parse(Agent  agent){
		log.debug("transforming "+agent.getUri().toASCIIString()+" into Triples");
		OntModel model = ModelFactory.createOntologyModel();
		model.setNsPrefixes(NSPrefixes.map);
		model.add(agent.asJenaResource(), RDF.type, Trust.Agent);
		addProfile(agent, model);
		return model;
	}

	private void addProfile(Agent agent, Model model) {
		TrustProfile profile = agent.getHasTrustProfile();
		if (profile != null){
			model.add(profile.asJenaResource(), RDF.type, Trust.TrustProfile);
			model.add(agent.asJenaResource(), Trust.hasProfile, profile.asJenaResource());
			addParameters(profile, model);
		}	
	}

	private void addParameters(TrustProfile profile, Model model) {
		Collection<TrustAttribute> list = profile.getAttributes();
		for (TrustAttribute attribute : list) {
			//u type stavljamo type parametra. ovo se desava pri kolektovanju podataka
			List<TResource> types = attribute.getTypesAll();
			for (TResource type : types) {
				model.add(attribute.asJenaResource(), RDF.type, ResourceFactory.createResource(type.getUri().toASCIIString()));
			}
			
			model.add(profile.asJenaResource(),Trust.hasAttribute, attribute.asJenaResource());
		}
	}

}
