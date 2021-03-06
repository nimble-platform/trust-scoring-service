

package nimble.trust.engine.kb;


import java.io.StringWriter;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Collections;
import java.util.Map;
import java.util.Properties;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import javax.inject.Named;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.base.Stopwatch;
import com.google.common.collect.ImmutableSet;
import com.google.inject.Inject;
import com.google.inject.assistedinject.Assisted;
import com.hp.hpl.jena.ontology.OntModel;
import com.hp.hpl.jena.ontology.OntModelSpec;
import com.hp.hpl.jena.query.DatasetAccessor;
import com.hp.hpl.jena.query.DatasetAccessorFactory;
import com.hp.hpl.jena.query.Query;
import com.hp.hpl.jena.query.QueryExecution;
import com.hp.hpl.jena.query.QueryExecutionFactory;
import com.hp.hpl.jena.query.QueryFactory;
import com.hp.hpl.jena.query.QuerySolution;
import com.hp.hpl.jena.query.ResultSet;
import com.hp.hpl.jena.rdf.model.Model;
import com.hp.hpl.jena.rdf.model.ModelFactory;
import com.hp.hpl.jena.rdf.model.Resource;
import com.hp.hpl.jena.shared.PrefixMapping;
import com.hp.hpl.jena.sparql.modify.request.Target;
import com.hp.hpl.jena.sparql.modify.request.UpdateCreate;
import com.hp.hpl.jena.sparql.modify.request.UpdateDrop;
import com.hp.hpl.jena.update.UpdateExecutionFactory;
import com.hp.hpl.jena.update.UpdateFactory;
import com.hp.hpl.jena.update.UpdateProcessor;
import com.hp.hpl.jena.update.UpdateRequest;
import com.hp.hpl.jena.vocabulary.OWL;
import com.hp.hpl.jena.vocabulary.RDF;
import com.hp.hpl.jena.vocabulary.RDFS;

import nimble.trust.common.Syntax;
import nimble.trust.engine.Configuration;
import nimble.trust.engine.model.vocabulary.NSPrefixes;


public class ConcurrentSparqlGraphStoreManager implements SparqlGraphStoreManager {

    private static final Logger log = LoggerFactory.getLogger(ConcurrentSparqlGraphStoreManager.class);

    private static final URI RDF_URI = URI.create(RDF.getURI());
    private static final URI RDFS_URI = URI.create(RDFS.getURI());
    private static final URI OWL_URI = URI.create(OWL.NS);
    private static final Set<URI> CORE_MODELS = ImmutableSet.of(RDF_URI, RDFS_URI, OWL_URI);

    private static final String JAVA_PROXY_HOST_PROP = "http.proxyHost";
    private static final String JAVA_PROXY_PORT_PROP = "http.proxyPort";

    private Set<URI> loadedModels;

    private DatasetAccessor datasetAccessor;

    private URI sparqlQueryEndpoint;
    // To use SPARQL Update for modification
    private URI sparqlUpdateEndpoint;
    // To use SPARQL HTTP protocol for graph modification
    private URI sparqlServiceEndpoint;

	private OntModelSpec modelSpec;

    static class ProxyConfiguration {
        @Inject(optional = true)
        @Named(Configuration.PROXY_HOST_NAME_PROP)
        private String proxyHost = null;
        @Inject(optional = true)
        @Named(Configuration.PROXY_PORT_PROP)
        private String proxyPort = null;
    }
    
    @Inject
    ConcurrentSparqlGraphStoreManager(
    		@Assisted("sparqlQueryEndpoint") String sparqlQueryEndpoint,
    		@Assisted("sparqlUpdateEndpoint") String sparqlUpdateEndpoint,
    		@Assisted("sparqlServiceEndpoint") String sparqlServiceEndpoint,
            @Assisted("baseModels") Set<URI> baseModels,
            @Assisted("locationMappings") Map<String, String> locationMappings,
            @Assisted("ignoredImports") Set<String> ignoredImports,
            ProxyConfiguration proxyCfg) throws Exception {

        this.loadedModels = Collections.newSetFromMap(new ConcurrentHashMap<URI, Boolean>());
        this.loadedModels.addAll(CORE_MODELS);
        // Configure proxy if necessary
        configureProxy(proxyCfg);

        modelSpec =  SharedOntModelSpec.createModelSpecification(locationMappings, ignoredImports);

        if (sparqlQueryEndpoint == null) {
            log.error(ConcurrentSparqlGraphStoreManager.class.getSimpleName() + " requires a SPARQL Query endpoint.");
            throw new Exception(ConcurrentSparqlGraphStoreManager.class.getSimpleName() + " requires a SPARQL Query endpoint.");
        }

        if (sparqlUpdateEndpoint == null && sparqlServiceEndpoint == null) {
            log.warn(ConcurrentSparqlGraphStoreManager.class.getSimpleName() + " requires a SPARQL Update endpoint to modify data.");
        }

        try {
            this.sparqlQueryEndpoint = new URI(sparqlQueryEndpoint);
            this.sparqlUpdateEndpoint = new URI(sparqlUpdateEndpoint);
            this.sparqlServiceEndpoint = new URI(sparqlServiceEndpoint);

            if (this.sparqlServiceEndpoint != null) {
                this.datasetAccessor = DatasetAccessorFactory.createHTTP(this.sparqlServiceEndpoint.toASCIIString());
            }
        } catch (URISyntaxException e) {
            log.error("URI error configuring SPARQL Graph Store Manager", e);
        }
    }

    private void configureProxy(ProxyConfiguration proxyCfg) {
        Properties prop = System.getProperties();
        if (proxyCfg != null && proxyCfg.proxyHost != null && proxyCfg.proxyPort != null) {
            log.info("Configuring proxy: Host - {} - Port {} .", proxyCfg.proxyHost, proxyCfg.proxyPort);
            prop.put(JAVA_PROXY_HOST_PROP, proxyCfg.proxyHost);
            prop.put(JAVA_PROXY_PORT_PROP, proxyCfg.proxyPort);
        } else {
            prop.remove(JAVA_PROXY_HOST_PROP);
            prop.remove(JAVA_PROXY_PORT_PROP);
        }
    }   


    /**
     * @return the sparqlQueryEndpoint
     */
    @Override
    public URI getSparqlQueryEndpoint() {
        return this.sparqlQueryEndpoint;
    }

    /**
     * @return the SPARQL update endpoint
     */
    @Override
    public URI getSparqlUpdateEndpoint() {
        return sparqlUpdateEndpoint;
    }

    /**
     * @return the URI of the SPARQL Service
     */
    @Override
    public URI getSparqlServiceEndpoint() {
        return sparqlServiceEndpoint;
    }



    /**
     * Does the Dataset contain a named graph?
     *
     * @param graphUri
     * @return
     */
    @Override
    public boolean containsGraph(URI graphUri) {

        if (graphUri == null)
            return false;

        return this.loadedModels.contains(graphUri);
    }

    
    public boolean containsGraphSparqlQuery(URI graphUri) {

        if (graphUri == null)
            return true; // Default graph always exists

        StringBuilder queryStr = new StringBuilder("ASK { GRAPH <").append(graphUri).append("> {?s a ?o} } \n");

        Query query = QueryFactory.create(queryStr.toString());
        QueryExecution qexec = QueryExecutionFactory.sparqlService(this.getSparqlQueryEndpoint().toASCIIString(), query);
        try {
            return qexec.execAsk();
        } finally {
            qexec.close();
        }
    }

    /**
     * Delete Graph using SPARQL Update
     *
     * @param graphUri
     */
    protected void deleteGraphSparqlUpdate(URI graphUri) {
        UpdateRequest request = UpdateFactory.create();
        request.setPrefixMapping(PrefixMapping.Factory.create().setNsPrefixes(NSPrefixes.map));
        request.add(new UpdateDrop(graphUri.toASCIIString()));

        // Use create form for Sesame-based engines. TODO: Generalise and push to config.
        UpdateProcessor processor = UpdateExecutionFactory.createRemoteForm(request, this.getSparqlUpdateEndpoint().toASCIIString());
        processor.execute(); // TODO: anyway to know if things went ok?
    }

    /**
     * Gets the default model of a Dataset
     *
     * @return
     */
    protected OntModel getGraph() {

        // Use HTTP protocol if possible
        if (this.sparqlServiceEndpoint != null) {
            return ModelFactory.createOntologyModel(this.modelSpec, datasetAccessor.getModel());
        } else {
            return this.getGraphSparqlQuery();
        }
    }

    private OntModel getGraphSparqlQuery() {
        return getGraphSparqlQuery(null);
    }

    /**
     * Get a named model of a Dataset
     *
     * @param graphUri
     * @return the Ontology Model
     */
    @Override
    public OntModel getGraph(URI graphUri) {
        log.info("Obtaining graph: {}", graphUri==null ?"graphUri is null":graphUri.toString());
        if (graphUri == null)
            return null;
        if (this.sparqlServiceEndpoint != null) {
            Model model = datasetAccessor.getModel(graphUri.toASCIIString());
            OntModel ontModel =  ModelFactory.createOntologyModel(modelSpec, model);
            return ontModel;
        } else {
        	return this.getGraphSparqlQuery(graphUri);
        }
    }
    


    /**
     * Obtains the OntModel within a named graph or the default graph if no graphUri is provided
     *
     * @param graphUri
     * @return
     */
    @Override
    public OntModel getGraphSparqlQuery(URI graphUri) {
    	log.debug("Obtaining graph via sparql query: {}", graphUri==null ?"graphUri is null":graphUri.toString());

        StringBuilder queryStr = new StringBuilder("CONSTRUCT { ?s ?p ?o } \n")
                .append("WHERE {\n");

        // Add named graph if necessary
        if (graphUri != null) {
            queryStr.append("GRAPH <").append(graphUri.toASCIIString()).append("> ");
        }
        queryStr.append("{ ?s ?p ?o } } \n");

        log.debug("Querying graph store: {}", queryStr.toString());

        Query query = QueryFactory.create(queryStr.toString());
        QueryExecution qexec = QueryExecutionFactory.sparqlService(this.getSparqlQueryEndpoint().toASCIIString(), query);
        // Note that we are not using the store specification here to avoid retrieving remote models
        OntModel resultModel = ModelFactory.createOntologyModel();
        try {
            qexec.execConstruct(resultModel);
            return resultModel;
        } finally {
            qexec.close();
        }
    }



    protected void putGraphSparqlQuery(Model data) {
        this.putGraphSparqlQuery(null, data);
    }

    /**
     * Put (create/replace) a named graph of a Dataset
     *
     * @param graphUri
     * @param data
     */
    @Override
    public void putGraph(URI graphUri, Model data) {
        if (graphUri == null || data == null)
            return;

        // Use HTTP protocol if possible
        if (this.sparqlServiceEndpoint != null) {
            datasetAccessor.putModel(graphUri.toASCIIString(), data);
        } else {
            this.putGraphSparqlQuery(graphUri, data);
        }

        this.loadedModels.add(graphUri);
        log.info("Graph added to store: {}", graphUri.toASCIIString());
    }

    private void putGraphSparqlQuery(URI graphUri, Model data) {

        UpdateRequest request = UpdateFactory.create();
        request.setPrefixMapping(PrefixMapping.Factory.create().setNsPrefixes(NSPrefixes.map));
        request.add(new UpdateCreate(graphUri.toASCIIString()));
        request.add(generateInsertRequest(graphUri, data));
        log.debug("Sparql Update Query issued: {}", request.toString());

        // Use create form for Sesame-based engines. TODO: Generalise and push to config.
        UpdateProcessor processor = UpdateExecutionFactory.createRemoteForm(request, this.getSparqlUpdateEndpoint().toASCIIString());
        processor.execute(); // TODO: anyway to know if things went ok?
    }

    private String generateInsertRequest(URI graphUri, Model data) {

        StringWriter out = new StringWriter();
        data.write(out, Syntax.TTL.getName());
        StringBuilder updateSB = new StringBuilder();
        updateSB.append("INSERT DATA { \n");
        // If a graph is given use it, otherwise insert in the default graph.
        if (graphUri != null) {
            updateSB.append("GRAPH <").append(graphUri.toASCIIString()).append(">");
        }
        updateSB.append(" { \n");
        updateSB.append(out.getBuffer());
        updateSB.append("}\n");
        updateSB.append("}\n");

        String result = updateSB.toString();

        return result;
    }


    protected void clearDatasetSparqlUpdate() {
        UpdateRequest request = UpdateFactory.create();
        request.add(new UpdateDrop(Target.ALL));
        UpdateProcessor processor = UpdateExecutionFactory.createRemoteForm(request,
                this.getSparqlUpdateEndpoint().toASCIIString());
        processor.execute(); // TODO: any way to know if things went ok?
        log.debug("Dataset cleared.");
    }
    

    /**
     * Figure out the models that are already in the Knowledge Base
     *
     * @return the Set of URIs of the models loaded
     */
    @Override
    public Set<URI> listStoredGraphs() {

        StringBuilder strBuilder = new StringBuilder()
                .append("SELECT DISTINCT ?graph WHERE {").append("\n")
                .append("GRAPH ?graph {").append("?s ?p ?o").append(" }").append("\n")
                .append("}").append("\n");
        return listResourcesByQuery(strBuilder.toString(), "graph");
    }
    
    /**
     * executes queryStr and lists resources placeholded with variableName 
     */
    public Set<URI> listResourcesByQuery(String queryStr, String variableName) {

        ImmutableSet.Builder<URI> result = ImmutableSet.builder();
        // If the SPARQL endpoint does not exist return immediately.
        if (this.getSparqlQueryEndpoint() == null) {
            return result.build();
        }
        // Query the engine
        log.debug("Evaluating SPARQL query in Knowledge Base: \n {}", queryStr);
        Query query = QueryFactory.create(queryStr);
        QueryExecution qexec = QueryExecutionFactory.sparqlService(this.getSparqlQueryEndpoint().toASCIIString(), query);

        try {
        	Stopwatch stopwatch = Stopwatch.createStarted();

            ResultSet qResults = qexec.execSelect();

            stopwatch.stop();
            log.info("Time taken for querying the registry: {}", stopwatch);

            Resource resource;
            URI matchUri;
            // Iterate over the results obtained
            while (qResults.hasNext()) {
                QuerySolution soln = qResults.nextSolution();

                // Get the match URL
                resource = soln.getResource(variableName);

                if (resource != null && resource.isURIResource()) {
                    matchUri = new URI(resource.getURI());
                    result.add(matchUri);
                } else {
                    log.warn("Skipping result as the URL is null");
                    break;
                }
            }
        } catch (URISyntaxException e) {
            log.error("Error obtaining match result. Expected a correct URI", e);
        } finally {
            qexec.close();
        }
        return result.build();
    }


    
}
