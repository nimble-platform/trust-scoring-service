package nimble.trust.engine.integration.fat;



import java.net.URI;
import java.util.List;
import java.util.Set;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;

import nimble.trust.engine.json.TrustPOJOFactory;
import nimble.trust.engine.model.pojo.TrustCriteria;
import nimble.trust.engine.module.Factory;
import nimble.trust.engine.service.interfaces.TrustSimpleManager;


/**
 * TrustFilterByThreshold implements implements Recommender Filter interface 
 * 
 * @author markov
 *
 */
public class TrustFilterByThreshold{
	
	private nimble.trust.engine.service.interfaces.TrustSimpleManager trustManager; 
	
	
	public TrustFilterByThreshold() {
		trustManager = Factory.createInstance(TrustSimpleManager.class);
	}
	
	public TrustFilterByThreshold(TrustSimpleManager trustManager) {
		this.trustManager = trustManager;
	}

	
	/**
	 * returns true if resource identified with serviceId URI is evaluated as trusted in respect to the trust threshold value
	 */
	public Set<URI> apply(Set<URI> arg0) {
		List<URI> resources = Lists.newArrayList(arg0);
		try {
			return Sets.newHashSet(trustManager.filterTrustedByThreshold(resources));
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException(e.getMessage());
		}
	}
	
	public Set<URI> apply(Set<URI> arg0, String arg1) {
		List<URI> resources = Lists.newArrayList(arg0);
		try {
			TrustCriteria criteria = new TrustPOJOFactory().ofTrustCriteria(arg1);
			return Sets.newHashSet(trustManager.filterTrustedByThreshold(resources, criteria));
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException(e.getMessage());
		}
	}

}
