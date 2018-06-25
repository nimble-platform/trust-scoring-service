package nimble.trust.engine.kb;


import java.net.URI;
import java.util.Map;
import java.util.Set;

import com.google.inject.assistedinject.Assisted;


public interface SparqlGraphStoreFactory {

    SparqlGraphStoreManager create(@Assisted("sparqlQueryEndpoint") String sparqlQueryEndpoint,
                                   @Assisted("sparqlUpdateEndpoint") String sparqlUpdateEndpoint,
                                   @Assisted("sparqlServiceEndpoint") String sparqlServiceEndpoint,
                                   @Assisted("baseModels") Set<URI> baseModels,
                                   @Assisted("locationMappings") Map<String, String> locationMappings,
                                   @Assisted("ignoredImports") Set<String> ignoredImports);
}
