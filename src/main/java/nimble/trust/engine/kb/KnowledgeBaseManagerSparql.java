

package nimble.trust.engine.kb;



import java.net.URI;
import java.net.URISyntaxException;
import java.util.HashSet;
import java.util.Set;

import javax.inject.Named;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableSet;
import com.google.common.eventbus.EventBus;
import com.google.inject.Inject;
import com.hp.hpl.jena.ontology.OntModel;
import com.hp.hpl.jena.ontology.OntModelSpec;
import com.hp.hpl.jena.rdf.model.Model;
import com.hp.hpl.jena.rdf.model.ModelFactory;
import com.hp.hpl.jena.rdf.model.NodeIterator;
import com.hp.hpl.jena.rdf.model.RDFNode;

import nimble.trust.engine.Configuration;
import nimble.trust.engine.kb.config.IgnoredModels;
import nimble.trust.engine.kb.config.LocationMapping;
import nimble.trust.util.uri.URIUtil;

public class KnowledgeBaseManagerSparql implements KnowledgeBaseManager {

    private static final Logger log = LoggerFactory.getLogger(KnowledgeBaseManagerSparql.class);

    private SparqlGraphStoreManager graphStoreManager;


    @Inject
    KnowledgeBaseManagerSparql(EventBus eventBus, SparqlGraphStoreFactory graphStoreFactory,
    		@Named(Configuration.SPARQL_ENDPOINT_QUERY_PROP) String queryEndpoint,
			@Named(Configuration.SPARQL_ENDPOINT_UPDATE_PROP) String updateEndpoint,
			@Named(Configuration.SPARQL_ENDPOINT_SERVICE_PROP) String serviceEndpoint) throws Exception {

        Set<URI> defaultModels = ImmutableSet.of();
        ImmutableMap.Builder<String, String> locationMappings =  LocationMapping.getMapping();
        Set<String> ignoredImports = IgnoredModels.getModels();
        this.graphStoreManager = graphStoreFactory.create(queryEndpoint, updateEndpoint, serviceEndpoint,
        		defaultModels, locationMappings.build(), ignoredImports);
    }

  
   

    

    
    public boolean fetchAndStoreImportedModels(Model model, boolean isOntology) {
//        Set<URI> modelUris = null;
//        if (isOntology == false) {
//        	modelUris = obtainReferencedModelUris(model);
//        }
//        else{
//        	 OntModelSpec spec = SharedOntModelSpec.getModelSpecShared();
//             OntModel om = ModelFactory.createOntologyModel(spec, model);
//             Set<String> uriLiteral = om.listImportedOntologyURIs();
//             modelUris = Sets.newHashSet();
//             for (String uriString : uriLiteral) {
//            	 modelUris.add(URI.create(uriString));
//     		}
//        }
//        
//        for (URI modelUri : modelUris) {
//            // Only fetch those that are not there
//            if (!this.graphStoreManager.containsGraph(modelUri)) {
//            	   Model m = RDFModelsHandler.getGlobalInstance().fetch(modelUri.toASCIIString(), Syntax.RDFXML.getName(), SharedOntModelSpec.getModelSpecShared());
//                   this.graphStoreManager.putGraph(m);
//            }
//        }
        return true;
    }
    
    public SparqlGraphStoreManager getGraphStoreManager() {
		return graphStoreManager;
	}
    
    /**
     * Obtains a set with all the models loaded  into this Knowledge Base Manager
     *
     * @return the set of loaded models
     */
    @Override
    public Set<URI> getLoadedModels() {
        return this.graphStoreManager.listStoredGraphs();
    }

    
    @SuppressWarnings("unused")
	private Set<URI> obtainReferencedModelUris(Model model) {
        Set<URI> result = new HashSet<URI>();
        if (model != null) {
            RDFNode node;
            NodeIterator modelRefs = model.listObjects();
            while (modelRefs.hasNext()) {
                node = modelRefs.next();
                if (!node.isAnon()) {
                    try {
                        result.add(URIUtil.getNameSpace(node.asResource().getURI()));
                      	log.info("KnowledgeBaseManagerSparql obtainReferencedModelUris " + URIUtil.getNameSpace(node.asResource().getURI()));
                    } catch (URISyntaxException e) {
                        log.error("The namespace from the resource is not a correct URI. Skipping node.", e);
                    }
                }
            }
        }

        return result;
    }
    
    
    /**
     * 
     */
    @Override
    public OntModel getModel(String modelUri) {
    	return getModel(modelUri, SharedOntModelSpec.getModelSpecShared());
    }
    
    
   
    @Override
    public OntModel getModel(String modelUri, OntModelSpec spec) {
    	spec.setDocumentManager(SharedOntModelSpec.getDocumentManagerShared());
    	Model model =  RDFModelsHandler.getGlobalInstance().fetch(URI.create(modelUri),"TURTLE", spec);
    	OntModel oModel = ModelFactory.createOntologyModel(spec, model);
    	return oModel;
    }
    
}
