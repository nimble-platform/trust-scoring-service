package nimble.trust.engine.model.expression;



import nimble.trust.engine.model.pojo.TrustAttribute;

public class SingleElement implements Element{
	
	private  TrustAttribute attribute;
	
	public SingleElement(TrustAttribute attribute){
		this.attribute = attribute; 
	}	
	
	public TrustAttribute getAttribute() {
		return attribute;
	}

}
