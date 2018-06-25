package nimble.trust.engine.model.pojo;



import java.net.URI;

public class MetricValue extends TResource{

	public MetricValue(URI uri) {
		super(uri);
	}
	
	private MetricValue next;
	
	private Double rank;
	
	
	public MetricValue getNext() {
		return next;
	}
	
	public void setNext(MetricValue next) {
		this.next = next;
	}
	
	
	public void setRank(Double rank) {
		this.rank = rank;
	}
	
	public Double getRank() {
		return rank;
	}

}
