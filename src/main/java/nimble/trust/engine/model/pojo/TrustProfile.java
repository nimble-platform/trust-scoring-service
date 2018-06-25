package nimble.trust.engine.model.pojo;



import java.net.URI;
import java.util.List;

import com.google.common.collect.Lists;

public class TrustProfile extends TResource {
   
	public TrustProfile(URI uri) {
		super(uri);
	}

	private List<TrustAttribute> attributeSet =  Lists.newArrayList();
	
	private Agent agent ; 
	
	private Context context ;

	public void addAttribute(TrustAttribute... p) {
		for (TrustAttribute a : p) {
			attributeSet.add(a);
		}
	}

	public void removeAttribute(TrustAttribute... p) {
		for (TrustAttribute a : p) {
			attributeSet.remove(a);
		}
	}

	public void addAttributes(List<TrustAttribute> set) {
		attributeSet.addAll(set);
	}

	public List<TrustAttribute> getAttributes(){
		return attributeSet;
	}

	public Context getContext() {
		return context;
	}

	public void setContext(Context context) {
		this.context = context;
	}

	public Agent getAgent() {
		return agent;
	}

	public void setAgent(Agent agent) {
		this.agent = agent;
	}
}
