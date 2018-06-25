package nimble.trust.engine.model.expression;



import nimble.trust.engine.model.pojo.TrustAttribute;

public class SingleAttribute implements RequiredAttribute{
	
	private  TrustAttribute attribute;
	
	public SingleAttribute(TrustAttribute attribute){
		this.attribute = attribute; 
	}	
	
	public TrustAttribute getAttribute() {
		return attribute;
	}

}
