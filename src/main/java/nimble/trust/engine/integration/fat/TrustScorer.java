package nimble.trust.engine.integration.fat;



import java.net.URI;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.inject.Guice;

import nimble.trust.engine.module.TrustModule;
import nimble.trust.engine.service.interfaces.TrustSimpleManager;
import nimble.trust.util.tuple.Tuple2;

/**
 * TrustScorer implements  Scorer interface 
 * to support trust scoring 
 * @author markov
 *
 */
public class TrustScorer{
	
	private nimble.trust.engine.service.interfaces.TrustSimpleManager trustManager;
	
	public TrustScorer() {
		trustManager =  Guice.createInjector(new TrustModule()).getInstance(TrustSimpleManager.class);
	}
	
	public TrustScorer(TrustSimpleManager trustManager) {
		this.trustManager = trustManager;
	}
	
	
	public Map<URI, Double> apply(Set<URI> arg0) {
		List<URI> resources = Lists.newArrayList(arg0);
		Map<URI, Double> map = Maps.newHashMap();
		try {
			List<Tuple2<URI, Double>>  list = trustManager.obtainTrustIndexes(resources);
			for (Tuple2<URI, Double> t : list) {
				map.put(t.getT1(), t.getT2());
			}
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException(e.getMessage());
		}
		
		return map;
	}
	

	public Map<URI, Double> apply(Set<URI> arg0, String arg1) {
		trustManager.setGlobalTrustCriteria(arg1);
		return apply(arg0);
	}

//	/**
//	 * returns trust index of the resource identified with serviceId URI
//	 */
//	@Override
//	public Double apply(URI serviceId) {
//		try {
//			return trustManager.obtainTrustIndex(serviceId);
//		} catch (Exception e) {
//			e.printStackTrace();
//			return 0D;
//		}
//	}

}
