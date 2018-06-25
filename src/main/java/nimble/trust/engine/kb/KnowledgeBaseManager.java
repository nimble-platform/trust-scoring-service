
package nimble.trust.engine.kb;

import java.net.URI;
import java.util.Set;

import com.hp.hpl.jena.ontology.OntModel;
import com.hp.hpl.jena.ontology.OntModelSpec;


public interface KnowledgeBaseManager {
	
    
    Set<URI> getLoadedModels();

    OntModel getModel(String modelUri);

    OntModel getModel(String modelUri, OntModelSpec spec);

}
