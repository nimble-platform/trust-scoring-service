package nimble.trust.engine.bdg;



import com.hp.hpl.jena.rdf.model.Model;

public abstract class ABridge {

	protected String inputServiceID;
	
	public synchronized Model obtainTrustProfile(String serviceId) {
		inputServiceID = serviceId;
		return getTrustProfile(serviceId);
	}

	protected abstract Model getTrustProfile(String fixServiceID) ;
	

	public abstract  void stop();
	
}
