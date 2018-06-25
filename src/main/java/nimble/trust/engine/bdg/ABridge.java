package nimble.trust.engine.bdg;



import com.hp.hpl.jena.rdf.model.Model;

public abstract class ABridge {

	protected String inputServiceID;
	
	public synchronized Model obtainTrustProfile(String serviceId) {
		inputServiceID = serviceId;
		return getTrustProfile(fixServiceID(serviceId));
	}

	protected abstract Model getTrustProfile(String fixServiceID) ;
	
	private String fixServiceID(String serviceId) {
		String lastPart = serviceId.substring(serviceId.lastIndexOf('/') + 1);
		return "http://www.programmableweb.com/api/"+lastPart;
	}

	public abstract  void stop();
	
}
