

package nimble.trust.engine;

public class Configuration {

    // Configuration properties
    public static final String PROXY_HOST_NAME_PROP = "http.proxyHost";
    public static final String PROXY_PORT_PROP = "http.proxyPort";

    // TRust Data Triple Store ENdpoints
    public static final String SPARQL_ENDPOINT_QUERY_PROP = "services.sparql.query";
    public static final String SPARQL_ENDPOINT_UPDATE_PROP = "services.sparql.update";
    public static final String SPARQL_ENDPOINT_SERVICE_PROP = "services.sparql.service";
    
    // Services data Triple Store ENdpoints
    public static final String EXT_SPARQL_ENDPOINT_QUERY = "http://localhost:3033/data/query";
    public static final String EXT_SPARQL_ENDPOINT_UPDATE = "http://localhost:3033/data/upload";
    public static final String EXT_SPARQL_ENDPOINT_SERVICE = "http://localhost:3033/data/data";

}
