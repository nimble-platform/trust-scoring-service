package nimble.trust.engine.model.pojo;



/**
 * Context or domain where trust of participants is to be established. 
 * E.g. public transport, tourism,  retail, shopping, finance,  news feeds.
 *
 * @author marko
 *
 */

import java.net.URI;
import java.util.Collection;

public class Context extends TResource {
	
	
	public Context(URI uri) {
		super(uri);
	}

	private Matcher hasMatcher;

  
    private Collection<TrustProfile> hasRecommendedProfiles;

	public Collection<TrustProfile> getHasRecommendedProfiles() {
		return hasRecommendedProfiles;
	}

	public void setHasRecommendedProfiles(Collection<TrustProfile> hasRecommendedProfiles) {
		this.hasRecommendedProfiles = hasRecommendedProfiles;
	}

	public Matcher getHasMatcher() {
		return hasMatcher;
	}

	public void setHasMatcher(Matcher hasMatcher) {
		this.hasMatcher = hasMatcher;
	}



}
