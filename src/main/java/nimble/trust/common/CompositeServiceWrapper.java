package nimble.trust.common;

public class CompositeServiceWrapper {
	

	private CompositionIdentifier compositionIdentifier;
	
	private String flow;
	
	public CompositeServiceWrapper(CompositionIdentifier id, String flow) {
		this.compositionIdentifier = id;
		this.flow = flow;
	}

	
	public CompositionIdentifier getCompositionIdentifier() {
		return compositionIdentifier;
	}
	
	public void setCompositionIdentifier(CompositionIdentifier compositionIdentifier) {
		this.compositionIdentifier = compositionIdentifier;
	}
	
	public String getFlow() {
		return flow;
	}
	
	public void setFlow(String flow) {
		this.flow = flow;
	}

}
