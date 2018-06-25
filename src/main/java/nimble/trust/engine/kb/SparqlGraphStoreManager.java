
package nimble.trust.engine.kb;



import java.net.URI;
import java.util.Set;

import com.hp.hpl.jena.ontology.OntModel;
import com.hp.hpl.jena.rdf.model.Model;


public interface SparqlGraphStoreManager {

    URI getSparqlQueryEndpoint();

    URI getSparqlUpdateEndpoint();

    URI getSparqlServiceEndpoint();

    boolean containsGraph(URI graphUri);

    void putGraph(URI graphUri, Model data);

//    Set<URI> listStoredGraphs();
//
//    Set<URI> listResourcesByQuery(String queryStr, String variableName);
    
    OntModel getGraph(URI graphUri);
    
    OntModel getGraphSparqlQuery(URI graphUri);

	 Set<URI> listStoredGraphs();
    
}
