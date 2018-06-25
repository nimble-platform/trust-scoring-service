package nimble.trust.engine.kb;



import java.util.Map;
import java.util.Set;

import com.google.common.collect.ImmutableMap;
import com.hp.hpl.jena.ontology.OntDocumentManager;
import com.hp.hpl.jena.ontology.OntModelSpec;

import nimble.trust.engine.kb.config.IgnoredModels;
import nimble.trust.engine.kb.config.LocationMapping;

public class SharedOntModelSpec {
	
	
	private static OntModelSpec modelSpecShared ;
	
	public static OntModelSpec getModelSpecShared() {
		if (modelSpecShared == null) {
	        ImmutableMap.Builder<String, String> locationMappings =  LocationMapping.getMapping();
	        Set<String> ignoredImports = IgnoredModels.getModels();
			modelSpecShared = createModelSpecification(locationMappings.build(), ignoredImports);
		}
		return modelSpecShared;
	}
	
	
	 
	 /**
     * Creates and sets up a the OntModelSpec to be used when parsing models
     * In particular, ignored imports and location mapping / redirecting.
     *
     * @param locationMappings
     * @param ignoredImports
     * @return
     */
    public static OntModelSpec createModelSpecification(Map<String, String> locationMappings, Set<String> ignoredImports) {

        OntDocumentManager documentManager = new OntDocumentManager();


        for (Map.Entry<String, String> mapping : locationMappings.entrySet()) {
            documentManager.addAltEntry(mapping.getKey(), mapping.getValue());
        }

        // Ignore the imports indicated
        for (String ignoreUri : ignoredImports) {
            documentManager.addIgnoreImport(ignoreUri);
        }

        // follow imports for now..
        documentManager.setProcessImports(false);

        OntModelSpec ontModelSpec =  new OntModelSpec(OntModelSpec.OWL_MEM);
        ontModelSpec.setDocumentManager(documentManager);
        return ontModelSpec;
    }



	@SuppressWarnings("unused")
	private static OntModelSpec getModelSpecShared(Map<String, String> locationMappings, Set<String> ignoredImports) {
		if (modelSpecShared == null) {
			modelSpecShared = createModelSpecification(locationMappings, ignoredImports);
		}
		return modelSpecShared;
		
	}
	
	public static OntDocumentManager getDocumentManagerShared(){
		if (modelSpecShared == null){
			getModelSpecShared();
		}
		return modelSpecShared.getDocumentManager();
	}


}
