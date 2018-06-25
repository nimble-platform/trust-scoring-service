package nimble.trust.engine.model.pojo;



import java.net.URI;
import java.util.List;

import com.google.common.collect.Lists;

public class Metric extends TResource{

	public Metric(URI uri) {
		super(uri);
	} 
	
	private List<MetricValue> metricValues = Lists.newArrayList();
	
	
	public List<MetricValue> getMetricValues() {
		return metricValues;
	}
	
	public void addMetricValue(final MetricValue metricValue){
		metricValues.add(metricValue);
	}
	
	


}
