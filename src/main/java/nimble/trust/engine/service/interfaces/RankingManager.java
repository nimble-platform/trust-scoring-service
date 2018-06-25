package nimble.trust.engine.service.interfaces;



import java.net.URI;
import java.util.List;

import com.hp.hpl.jena.rdf.model.Model;

import nimble.trust.common.OrderType;
import nimble.trust.engine.model.expression.SingleElement;
import nimble.trust.engine.model.pojo.Agent;
import nimble.trust.engine.model.pojo.TrustAttribute;
import nimble.trust.engine.model.pojo.TrustCriteria;
import nimble.trust.engine.op.enums.EnumScoreStrategy;
import nimble.trust.util.tuple.Tuple2;


/**
 * 
 *@author markov
 *
 */
public interface RankingManager {
	
	
	
	/**
	 * Ranking of Services by given TrustProfileRequired
	 * @param models 
	 * @param trustProfileRequired
	 * @param strategy 
	 * @param order OrderType ASC / DESC
	 * @return
	 */	
	
	public List<Tuple2<URI, Double>> rankServiceModels(List<Model> models, TrustCriteria trustProfileRequired, EnumScoreStrategy strategy,
			 boolean excludeIfAttributeMissing, boolean filterByCriteriaNotMet, OrderType order) throws Exception ;
	
	/**
	 * 	Prepares data set. It may exclude agents that have no requested attribute if filterByAttributeMissing true, \
	 * or evaluate as zero if rigorous is true and attributed evaluated lower than expected 
	 * @param models
	 * @param trustProfileRequired
	 * @param filterByAttributeMissing
	 * @param rigorous
	 * @return
	 */
	public List<Tuple2<Agent, List<Tuple2<TrustAttribute, Double>>>> prepareDataset(List<Model> models, List<SingleElement> listCriteria, boolean filterByAttributeMissing, boolean rigorous) ;
	
}

