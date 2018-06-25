package nimble.trust.engine.op.composition;


import java.util.List;

import nimble.trust.engine.model.graph.Vertex;
import nimble.trust.engine.model.pojo.TrustAttribute;

public class Min extends AggregationFunction {
	
	
	@Override
	public Double compute(List<Vertex> vertices, TrustAttribute attribute) {
		Double result = null ;
		for (Vertex vertex : vertices) {
			Double val = findAttributeNormalizedValue(vertex, attribute);
			if (result==null || val.compareTo(result)==-1){
				result = val;
			}
		}
		return result;
	}

}
