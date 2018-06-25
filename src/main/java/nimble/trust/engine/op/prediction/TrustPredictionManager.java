package nimble.trust.engine.op.prediction;



import nimble.trust.engine.model.pojo.TResource;



/**
 * Ideja je predikcija trusta. 
 * Moze biti interesantno u slucajevima kad nemamo dovoljno informacija o kvaliteti servisa 
 * ili o reputaciji servisa. (reputacija moze biti bazirana na povratnoj informaciji o
 * od korisnika, popularnosti, i sl.)
 * pogledam hidden markov model / da li predikcija u tom pravcu ima smisla
 * 
 * @author marko
 *
 */
public interface TrustPredictionManager {
	
	
	void predict(TResource tResource);

}
