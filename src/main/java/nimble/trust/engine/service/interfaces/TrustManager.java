package nimble.trust.engine.service.interfaces;



import nimble.trust.engine.model.pojo.TrustCriteria;
import nimble.trust.engine.service.config.GlobalTrustCriteria;

public interface TrustManager {
	
	
	/**
	 *  Set global trust request. If not set, the trust manager will be using the default the default one {@link GlobalTrustCriteria}
	 * @param criteria Trust criteria as POJO
	 */
	public void setGlobalTrustCriteria(TrustCriteria criteria);
	
	/**
	 * Set global trust request. If not set, the trust manager will be using the default the default one {@link GlobalTrustCriteria}
	 * @param critaeriaAsJson  Trust criteria as Json string
	 */
	public void setGlobalTrustCriteria(String critaeriaAsJson) ; 
	
	
	public TrustCriteria getGlobalTrustCriteria();

}
