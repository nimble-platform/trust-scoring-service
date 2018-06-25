package nimble.trust.web.controller;

import java.net.URI;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;

import nimble.trust.common.CompositeServiceWrapper;
import nimble.trust.common.CompositionIdentifier;
import nimble.trust.common.OrderType;
import nimble.trust.engine.json.ProduceJSON;
import nimble.trust.engine.model.pojo.TrustCriteria;
import nimble.trust.engine.module.Factory;
import nimble.trust.engine.op.enums.EnumLevel;
import nimble.trust.engine.op.enums.EnumScoreStrategy;
import nimble.trust.engine.service.interfaces.TrustCompositionManager;
import nimble.trust.engine.service.interfaces.TrustSimpleManager;
import nimble.trust.swagger.api.TrustApi;
import nimble.trust.util.tuple.Tuple2;

@Controller
public class TrustController implements TrustApi {
	
	private static Logger log = LoggerFactory.getLogger(VersionController.class);
	
	public ResponseEntity<String> scoring(@RequestBody String request) {
		
		log.info("Invoked Rest: scoring");
		try {
					
			final TrustSimpleManager trustManager = Factory.createInstance(TrustSimpleManager.class);
			TrustCriteria criteria = RequestJSONUtil.getCriteria(request);
			if (criteria == null) 
				criteria = trustManager.getGlobalTrustCriteria();
			final List<URI> list = RequestJSONUtil.getResourceList(request);
			EnumScoreStrategy strategy = RequestJSONUtil.getScoreStrategy(request);
			List<Tuple2<URI, Double>> result = null;
			if (strategy == EnumScoreStrategy.TOPSIS) {
				result = trustManager.rankResources(list, criteria, EnumScoreStrategy.TOPSIS, false,OrderType.DESC);
			} else {
				trustManager.setGlobalTrustCriteria(criteria);
				result = trustManager.obtainTrustIndexes(list);
			}
			return new ResponseEntity<>(new ProduceJSON().ofRankingResult(result), HttpStatus.OK);
		} catch (Exception e) {
			 e.printStackTrace();
			 return new ResponseEntity<>(new ProduceJSON().ofError(e), HttpStatus.BAD_REQUEST);
		}
	}
	

	
	public ResponseEntity<String> filteringByThreshold(@RequestBody String request) {
		log.info("Invoked Rest: filteringByThreshold");
		try {
			List<URI> filtered ;
			final TrustSimpleManager trustManager = Factory.createInstance(TrustSimpleManager.class);
			TrustCriteria criteria = RequestJSONUtil.getCriteria(request);
			if (criteria == null) 
				criteria = trustManager.getGlobalTrustCriteria();
			final List<URI> resources = RequestJSONUtil.getResourceList(request);
			filtered = trustManager.filterTrustedByThreshold(resources, criteria);
			return new ResponseEntity<>(new ProduceJSON().ofFilteringResult(filtered), HttpStatus.OK);
		} catch (Exception e) {
			 e.printStackTrace();
			 return new ResponseEntity<>(new ProduceJSON().ofError(e), HttpStatus.BAD_REQUEST);
		}
	}
	
	
	public ResponseEntity<String> filterByCriteriaNotMeet(@RequestBody String request) {
		log.info("Invoked Rest: filterByCriteriaNotMeet");
		
		try {
			List<URI> filtered ;
			final TrustSimpleManager trustManager = Factory.createInstance(TrustSimpleManager.class);
			TrustCriteria criteria = RequestJSONUtil.getCriteria(request);
			if (criteria == null) 
				criteria = trustManager.getGlobalTrustCriteria();
			final List<URI> resources = RequestJSONUtil.getResourceList(request);
			filtered = trustManager.filterByCriteriaNotMeet(resources, criteria);
			return new ResponseEntity<>(new ProduceJSON().ofFilteringResult(filtered), HttpStatus.OK);
		} catch (Exception e) {
			 e.printStackTrace();
			 return new ResponseEntity<>(new ProduceJSON().ofError(e), HttpStatus.BAD_REQUEST);
		}
	}
	
	public ResponseEntity<String> scoringCompositions(@RequestBody String request) {
		log.info("Invoked Rest: scoring compositions");
		try {
			TrustCompositionManager trustManager = Factory.createInstance(TrustCompositionManager.class);
			TrustCriteria criteria = RequestJSONUtil.getCriteria(request);
			if (criteria == null) criteria = trustManager.getGlobalTrustCriteria();
			final List<CompositeServiceWrapper> compositeServiceList = RequestJSONUtil.getCompositeServiceWrapperList(request);
			trustManager.setGlobalTrustCriteria(criteria);
			EnumLevel level = RequestJSONUtil.getLevelFromJsonComposite(request);
			String strategy = RequestJSONUtil.getStrategyFromJsonComposite(request);
			List<Tuple2<CompositionIdentifier, Double>> scored = trustManager.obtainTrustIndexes(compositeServiceList, criteria, level, strategy);
			return new ResponseEntity<>(new ProduceJSON().ofRankingCompositionsResult(scored), HttpStatus.OK);
		} catch (Exception e) {
			 e.printStackTrace();
			 return new ResponseEntity<>(new ProduceJSON().ofError(e), HttpStatus.BAD_REQUEST);
		}
	}
	


}
