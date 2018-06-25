package nimble.trust.engine.model.pojo;



import java.net.URI;

public class Agent extends TResource {
	
    public Agent(URI uri) {
		super(uri);
	}

	public TrustProfile getHasTrustProfile() {
		return hasTrustProfile;
	}

	public void setHasTrustProfile(TrustProfile hasTrustProfile) {
		this.hasTrustProfile = hasTrustProfile;
	}
	
	@Override
	public URI getUri() {
		return super.getUri();
	}

	private TrustProfile hasTrustProfile;
	private URI compose_ID;
	
	private URI inputUID;
	
	public void setCompose_ID(URI compose_ID) {
		this.compose_ID = compose_ID;
	}

	public URI getCompose_ID() {
		return compose_ID;
	}

	public void setInputUID(URI uid) {
		this.inputUID = uid;
		
	}
	
	public URI getInputUID() {
		return inputUID;
	}
	
	
	
}
