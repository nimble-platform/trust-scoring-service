package nimble.trust.engine.kb;


import java.io.InputStream;
import java.net.URI;
import java.util.Map;

import org.apache.jena.riot.adapters.AdapterFileManager;
import org.apache.jena.riot.stream.StreamManager;

import com.google.common.collect.Maps;
import com.hp.hpl.jena.ontology.OntModel;
import com.hp.hpl.jena.ontology.OntModelSpec;
import com.hp.hpl.jena.rdf.model.Model;
import com.hp.hpl.jena.rdf.model.ModelFactory;

import nimble.trust.engine.kb.config.LocationMapping;

public class RDFModelsHandler {

	boolean useCache = true;
	Map<String, OntModel> modelCache = Maps.newConcurrentMap();
	
	private static RDFModelsHandler globalInstance  = null;

	
	private RDFModelsHandler() {
	}

	public synchronized OntModel fetch(URI uri, String syntax, OntModelSpec modelSpec) {
		return fetch(uri.toASCIIString(), syntax, modelSpec);
	}

	/**
	 * 
	 * @param url
	 * @param syntax
	 * @param modelSpec
	 * @return
	 */
	private synchronized OntModel fetch(String filenameOrURI, String syntax, OntModelSpec modelSpec) {
		
		if (hasCachedModel(filenameOrURI))
			return getFromCache(filenameOrURI);
		
		StreamManager streamManager = StreamManager.makeDefaultStreamManager();
		streamManager.setLocationMapper(LocationMapping.obtainLocationMapper());
		
		Model m = new AdapterFileManager(streamManager).loadModel(filenameOrURI);
		OntModel model = ModelFactory.createOntologyModel(modelSpec, m);
		
		if (isCachingModels())
			addCacheModel(filenameOrURI, model);

		
		return model;
	}
	
	
	public synchronized OntModel fetchDescriptionFromFileSystem(String uri, String syntax, OntModelSpec modelSpec) {
		return null;
	}
	
	public synchronized OntModel fetchOntologyFromLocalLocation(String uri, String syntax, OntModelSpec modelSpec) {
		modelSpec.setDocumentManager(SharedOntModelSpec.getDocumentManagerShared());
		if (hasCachedModel(uri))
			return getFromCache(uri);
		else{
			InputStream is = getClass().getResourceAsStream("/"+LocationMapping.obtainLocationMapper().altMapping(uri));
			return fetch(uri, is, modelSpec);
		}
		
	}

	/**
	 * 
	 * @param url
	 * @param syntax
	 * @param modelSpec
	 * @return
	 */
	public synchronized OntModel fetch(String uri, InputStream inputStream, OntModelSpec modelSpec) {

		if (hasCachedModel(uri))
			return getFromCache(uri);

		OntModel m = ModelFactory.createOntologyModel(modelSpec);
		m.read(inputStream, null, "TURTLE");

		if (isCachingModels())
			addCacheModel(uri, m);

		return m;
	}

	private void addCacheModel(String filenameOrURI, OntModel m) {
		modelCache.put(filenameOrURI, m);
	}

	public OntModel getFromCache(String filenameOrURI) {
		return modelCache.get(filenameOrURI);
	}

	public boolean hasCachedModel(String filenameOrURI) {
//		System.out.println("cache size "+modelCache.size());
		return modelCache.containsKey(filenameOrURI);
	}

	public boolean isCachingModels() {
		return useCache;
	}

	public void setUseCache(boolean useCache) {
		this.useCache = useCache;
	}
	

	public static RDFModelsHandler getGlobalInstance() {
		 if(globalInstance == null) {
			 globalInstance = new RDFModelsHandler();
	      }
	      return globalInstance;
	}

}
