package nimble.trust.engine.service.interfaces;

import java.util.List;

import org.jgrapht.experimental.dag.DirectedAcyclicGraph;

import nimble.trust.common.CompositeServiceWrapper;
import nimble.trust.common.CompositionIdentifier;
import nimble.trust.engine.model.graph.Edge;
import nimble.trust.engine.model.graph.Vertex;
import nimble.trust.engine.model.pojo.TrustCriteria;
import nimble.trust.engine.op.enums.EnumLevel;
import nimble.trust.util.tuple.Tuple2;




public interface TrustCompositionManager extends TrustManager {
	
	List<CompositionIdentifier> filterTrustedByThreshold(List<CompositeServiceWrapper> compositeServiceList, TrustCriteria criteria, EnumLevel level, String strategy, Double thresholdValue) throws Exception;
	
	List<Tuple2<CompositionIdentifier, Double>> obtainTrustIndexes(List<CompositeServiceWrapper> compositeServiceList,TrustCriteria criteria, EnumLevel level, String strategy) throws Exception;

	Tuple2<CompositionIdentifier, Double> obtainTrustIndex(CompositeServiceWrapper compositeServiceWrapper, TrustCriteria criteria, EnumLevel level, String strategy) throws Exception;

	Double obtainTrustIndex(DirectedAcyclicGraph<Vertex, Edge> g, TrustCriteria criteria, EnumLevel level, String strategy) throws Exception;
	
}
