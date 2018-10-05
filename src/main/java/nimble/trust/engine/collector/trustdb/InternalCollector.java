package nimble.trust.engine.collector.trustdb;



import java.net.URI;
import java.util.List;
import java.util.Map;

import com.hp.hpl.jena.rdf.model.Model;

import nimble.trust.engine.bdg.ABridge;
import nimble.trust.engine.bdg.BridgeDB;
import nimble.trust.engine.collector.AbstractCollector;


public class InternalCollector extends AbstractCollector {
	
	private ABridge b ;

	public InternalCollector(String sourceUri) {
		setSourceUri(sourceUri);
		initBridge();
	}

	private void initBridge() {
		b = new BridgeDB();
	}

	@Override
	public Model collectInformation(String resourceIdentifier) {
		return b.obtainTrustProfile((resourceIdentifier));
	}

	@Override
	public String getName() {
		return "InternalCollector";
	}
	
	@Override
	public void shutDown() {
		b.stop();
	}
	
	@Override
	public void collectInformation(List<URI> resources, Map<URI, Model> map) {
		//OK - does nothing.
	}

}
