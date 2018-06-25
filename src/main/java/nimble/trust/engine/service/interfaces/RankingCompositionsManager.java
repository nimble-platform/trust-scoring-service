package nimble.trust.engine.service.interfaces;



import org.jgrapht.experimental.dag.DirectedAcyclicGraph;

import nimble.trust.engine.model.graph.Edge;
import nimble.trust.engine.model.graph.Vertex;
import nimble.trust.engine.model.pojo.TrustCriteria;

public interface RankingCompositionsManager {

	Double computeScore(DirectedAcyclicGraph<Vertex, Edge> g, TrustCriteria criteria);

}
