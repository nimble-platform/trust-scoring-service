package nimble.trust.engine.collector.monitoring;



import java.net.URI;
import java.util.List;
import java.util.Map;

import com.hp.hpl.jena.rdf.model.Model;

import nimble.trust.engine.collector.AbstractCollector;

public class MonitoringCollector extends AbstractCollector{

	public MonitoringCollector() {
	}

	@Override
	public Model collectInformation(String resourceIdentifier) {
		//OK empty
		return null;
	}
	
	@Override
	public void collectInformation(List<URI> resources, Map<URI, Model> map) {
	
		
	}

	@Override
	public String getName() {
		return "monitoring";
	}
	
	@Override
	public void shutDown() {

	}

}
