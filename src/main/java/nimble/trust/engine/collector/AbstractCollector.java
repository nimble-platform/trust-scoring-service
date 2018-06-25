package nimble.trust.engine.collector;

public abstract class AbstractCollector implements Collector{
	
	protected String sourceUri = null;
	
	public void setSourceUri(String sourceUri) {
		this.sourceUri = sourceUri;
	}
	
	public String getSourceUri() {
		return sourceUri;
	}
	
	
	public AbstractCollector() {
	}
}
