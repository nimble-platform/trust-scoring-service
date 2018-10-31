package nimble.trust.engine.service.command;


import java.net.URI;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.hp.hpl.jena.ontology.OntModel;
import com.hp.hpl.jena.rdf.model.Model;
import com.hp.hpl.jena.rdf.model.ModelFactory;

import nimble.trust.common.Syntax;
import nimble.trust.engine.kb.RDFModelsHandler;
import nimble.trust.engine.kb.SharedOntModelSpec;
import nimble.trust.engine.model.factory.TrustModelFactory;
import nimble.trust.engine.model.io.ToGraphParser;
import nimble.trust.engine.model.pojo.Agent;
import nimble.trust.engine.model.vocabulary.NSPrefixes;
import nimble.trust.engine.model.vocabulary.Trust;
import nimble.trust.engine.service.config.CollectorConfig;
import nimble.trust.util.uri.UIDGenerator;

/**
 * Metadata fetch command responsible for obtaining resource annotations either from local file system / online / triple
 * stores
 * 
 * @author marko
 * 
 */
public class SemanticMetaDataFetcher {

	
	
	private static final Logger log = LoggerFactory.getLogger(SemanticMetaDataFetcher.class);

	public SemanticMetaDataFetcher() {
	}

	/**
	 * 
	 * @param uri
	 * @param fetchFromExternalRegistries
	 * @param useMappedLocations
	 * @param fetchFromInternalRegirsty
	 * @return
	 */
	public OntModel apply(URI uri, boolean fetchFromExternalRegistries, boolean useMappedLocations, boolean fetchFromInternalRegirsty) {

		Model externalModel = null;
		// Try to get the service descr from the external registries.
		try {
			if (fetchFromExternalRegistries) {
				log.debug("obtaining model from external registries using sparqlEndpoint");
				externalModel = fetchServiceFromExternalRegistry(uri);
			}
		} catch (Exception e) {
			// Not found. It is safe not to handle the exception.
		}
		// try to find it on the web or via location mapping
		if (externalModel == null && useMappedLocations) {
			log.debug("obtaining model using com.inn.RDFModelHandler for loading / retrieving {cached} models. "
					+ "	Caching is "+RDFModelsHandler.getGlobalInstance().isCachingModels());
			try {
			   externalModel = RDFModelsHandler.getGlobalInstance().
							fetchDescriptionFromFileSystem(uri.toASCIIString(), Syntax.TTL.getName(), SharedOntModelSpec.getModelSpecShared());

				if ( externalModel == null){
						log.debug("requested model for "+ uri.toASCIIString()+ " not found. If you have description use "
								+ "addResourceDescription(URI resourceURI, InputStream inputStream) of TrustManager to add it into module");

				}
				
//				return ModelFactory.createOntologyModel(SharedOntModelSpec.getModelSpecShared());
				
			} catch (org.apache.jena.atlas.web.HttpException e) {
				log.debug(" There was model retrival failure. Failed to retrive model because " + e.getMessage());
			}
		}
		// try to find it in internal registry

		Model internalModel = null;
		try {
			if (fetchFromInternalRegirsty) {
				log.debug("obtaining model from trust database");
				internalModel = CollectorConfig.InternalCollector.getCollector().collectInformation(uri.toASCIIString());
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		Model modelUnion;
		if (internalModel != null) {
			modelUnion = internalModel;
			if (externalModel != null)
				modelUnion = internalModel.union(externalModel);
		} else {
			modelUnion = externalModel;
		}
		OntModel model = ModelFactory.createOntologyModel(SharedOntModelSpec.getModelSpecShared(), modelUnion);

		if (model.contains(null, Trust.hasProfile) == false) {
			model.setNsPrefixes(NSPrefixes.map);
			Agent service = new Agent(uri);
			TrustModelFactory trm = new TrustModelFactory(UIDGenerator.instanceTrust);
			service.setHasTrustProfile(trm.createTrustProfile());
			OntModel m = new ToGraphParser().parse(service);
			model = ModelFactory.createOntologyModel(SharedOntModelSpec.getModelSpecShared(), model.union(m));
		}
		return model;
	}

	/**
	 * goes thru a list of spaqrqlEndpoints and tries to find any metadata for the service
	 * 
	 * @param uri - service URI.
	 * @return
	 */
	private Model fetchServiceFromExternalRegistry(URI uri) {
		Model model = null;
//		for (SparqlGraphStoreManager storeManager : externalGraphStoreMgrs) {
//			model = storeManager.getGraph(uri);
//			if (model.isEmpty() == false) // stop when model is found
//				continue;
//		}
		return model;
	}

}
