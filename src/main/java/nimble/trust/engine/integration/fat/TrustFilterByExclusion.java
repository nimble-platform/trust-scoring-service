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
 * TrustFilterByExclusion implements Recommender Filter interface 
 * 
 * @author markov
 *
 */
public class TrustFilterByExclusion{
	
	private nimble.trust.engine.service.interfaces.TrustSimpleManager trustManager; 
	public TrustFilterByExclusion() {
		trustManager = Factory.createInstance(TrustSimpleManager.class);
	}
	
	public TrustFilterByExclusion(TrustSimpleManager trustManager) {
		this.trustManager = trustManager;
	}

	public Set<URI> apply(Set<URI> arg0) {
		List<URI> resources = Lists.newArrayList(arg0);
		TrustCriteria criteria = trustManager.getGlobalTrustCriteria();
		List<URI> filtered ;
		try {
			filtered = trustManager.filterByCriteriaNotMeet(resources, criteria);
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException(e.getMessage());
		}
		return Sets.newHashSet(filtered);
	}
	

	public Set<URI> apply(Set<URI> arg0, String arg1) {
		List<URI> resources = Lists.newArrayList(arg0);
		TrustCriteria criteria = new TrustPOJOFactory().ofTrustCriteria(arg1);
		List<URI> filtered ;
		try {
			filtered = trustManager.filterByCriteriaNotMeet(resources, criteria);
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException(e.getMessage());
		}
		return Sets.newHashSet(filtered);
	}
	
	
	
//	public boolean apply(URI serviceId) {
//		boolean inList = false;
//		try {
//			List<URI> resources = Lists.newArrayList();
//			resources.add(serviceId);
//			TrustCriteria criteria = trustManager.getGlobalTrustCriteria();
//			List<URI> filtered = trustManager.filterByCriteriaNotMeet(resources, criteria);
//			for (URI uri : filtered) {
//				if (uri.compareTo(serviceId)==0){
//					inList = true;
//				}
//			}
//		} catch (Exception e) {
//			e.printStackTrace();
//			return false;
//		}
//		return inList;
//	}

}
