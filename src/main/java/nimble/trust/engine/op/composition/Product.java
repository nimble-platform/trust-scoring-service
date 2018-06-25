package nimble.trust.engine.op.composition;


import java.util.List;

import nimble.trust.engine.model.graph.Vertex;
import nimble.trust.engine.model.pojo.TrustAttribute;

public class Product extends AggregationFunction {

	@Override
	public Double compute(List<Vertex> vertices, TrustAttribute attribute) {
		Double result = 1D;
		for (Vertex vertex : vertices) {
			Double val = findAttributeNormalizedValue(vertex, attribute);
			result = result * val;
		}
		return result;
	}
}
