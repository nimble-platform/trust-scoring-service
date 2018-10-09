package nimble.trust.engine.collector.reputation;



import java.net.URI;
import java.util.List;
import java.util.Map;

import com.hp.hpl.jena.rdf.model.Model;

import nimble.trust.engine.collector.AbstractCollector;

/**
 * 
 * Created by Marko on 2018-10-09
 *
 */
public class ReputationCollector extends AbstractCollector{
	

	public ReputationCollector() {
		
	}

	@Override
	public Model collectInformation(String resourceIdentifier) {
		return null;
	}
	
	@Override
	public void collectInformation(List<URI> resources, Map<URI, Model> map) {
		return ;
	}


	@Override
	public String getName() {
		return "reputation";
	}
	
	@Override
	public void shutDown() {
	}

	
}
