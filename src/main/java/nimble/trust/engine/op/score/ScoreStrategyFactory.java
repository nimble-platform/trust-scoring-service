package nimble.trust.engine.op.score;



import java.util.List;

import nimble.trust.engine.model.expression.SingleElement;
import nimble.trust.engine.model.pojo.Agent;
import nimble.trust.engine.model.pojo.TrustAttribute;
import nimble.trust.engine.op.enums.EnumNormalizationType;
import nimble.trust.engine.op.enums.EnumScoreStrategy;
import nimble.trust.util.tuple.Tuple2;

/**
 * Factory for Trust Scorers
 * @author markov
 *
 */
public class ScoreStrategyFactory {
	
	
	public static synchronized AbstractScoreStrategy createScoreStrategy(List<SingleElement> listCriteria,  List<Tuple2<Agent, List<Tuple2<TrustAttribute, Double>>>> dataSet,  EnumScoreStrategy scoreStrategy){
		
		if (scoreStrategy == EnumScoreStrategy.TOPSIS){
			return new TopsisScoreStrategy(listCriteria, dataSet);
		}
		if (scoreStrategy == EnumScoreStrategy.Weighted_sum_model){
			return new WeightedSumStrategy(listCriteria, dataSet, EnumNormalizationType.Zero_One);
		}
		if (scoreStrategy == EnumScoreStrategy.Weighted_sum_model_to01scale_divMaxInSet){
			return new WeightedSumStrategy(listCriteria, dataSet, EnumNormalizationType.Zero_One_by_divMax);
		}
		//default strategy is a Weighted Sum Strategy with 0..1 scaling and all feature having 1 as max value and 0 as min value.
		return new WeightedSumStrategy(listCriteria, dataSet, EnumNormalizationType.Zero_One);
	}

}
