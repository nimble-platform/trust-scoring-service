package nimble.trust.engine.collector;



import java.net.URI;
import java.util.List;
import java.util.Map;

import com.hp.hpl.jena.rdf.model.Model;


/**
 * 
 * @author marko
 *
 */
//FIXME make a collector.json
public interface Collector {
	
	/**
	 * 
	 * FIXME maybe collectors should be asynchr services, which run periodically.
	 * However, in this case there should be runtime monitors, because after collector returns its value,
	 * the value may influence the trust assessment.
	 * @param list resource uri
	 * @return Model
	 * 
	 */
	public Model collectInformation(String resourceIdentifier);
	
	abstract String getName();
	
	void shutDown();

	void collectInformation(List<URI> resources, Map<URI, Model> map);

}
