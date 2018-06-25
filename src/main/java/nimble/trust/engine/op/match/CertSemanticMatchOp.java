package nimble.trust.engine.op.match;


import java.net.URI;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import nimble.trust.engine.kb.config.LocationMapping;
import nimble.trust.engine.model.pojo.CertificateAuthorityAttribute;
import nimble.trust.engine.model.pojo.TrustAttribute;
import nimble.trust.engine.model.vocabulary.ModelEnum;
import nimble.trust.engine.op.semsim.SemSim;

/**
 * A  semantic match operator for two given Certificate Authority semantic descriptions.
 * 
 * @author markov 
 * 
 */
public class CertSemanticMatchOp {

	private static final Logger log = LoggerFactory.getLogger(CertSemanticMatchOp.class);

	private SemSim semSim = new SemSim(LocationMapping.resolveLocation(ModelEnum.SecurityOntology.getURI()));

	public CertSemanticMatchOp() {
		//---
	}

	/**
	 * 
	 * @param reqAttribute
	 * @param attributes
	 * @return
	 */
	public double apply(TrustAttribute reqAttribute, List<TrustAttribute> attributes) throws Exception {
		double result = 0;
		CertificateAuthorityAttribute reqCertificateAuthorityAttribute = (CertificateAuthorityAttribute) reqAttribute;
		double val1 = chechCAMatch(reqCertificateAuthorityAttribute, attributes);
		double val2 = chechCACountryMatch(reqCertificateAuthorityAttribute, attributes);
		if (val1 == 0 && val2 == 0) {
			return 0;
		}
		result = sumNotNegative(val1, val2) / countNotNegative(val1, val2);
		log.info("CA-SematicMatch result " + result);
		return result;
	}

	private double sumNotNegative(double... val) {
		double sum = 0;
		for (double d : val) {
			if (d >= 0)
				sum = sum + d;
		}
		return sum;
	}

	private int countNotNegative(double... val) {
		int n = 0;
		for (double d : val) {
			if (d >= 0)
				n++;
		}
		return n;
	}

	/**
	 * 
	 * @param reqAttribute
	 * @param attributes
	 * @return
	 */
	private double chechCAMatch(CertificateAuthorityAttribute reqAttribute, List<TrustAttribute> attributes)
			throws Exception {
		// String concept1 =
		// String concept2 =
		double semsim = 0;
		if (reqAttribute.getCertificateAuthority() == null) {
			log.info("CertificateAuthority matching: no requested so return -1");
			return -1;
		}
		// find the the best possible match (i.e. with most high semsim
		// value)
		for (TrustAttribute a : attributes) {
			final CertificateAuthorityAttribute ca = (CertificateAuthorityAttribute) a;
			double result = semSim.compute(URI.create(reqAttribute.getCertificateAuthority()), URI.create(ca.getCertificateAuthority()));
			if (result > semsim) {
				{
					semsim = result;
				}
			}
		}

		log.info("CertificateAuthority matching: requested and returns " + semsim);
		return semsim;
	}

	/**
	 * 
	 * @param reqAttribute
	 * @param attributes
	 * @return
	 */
	private double chechCACountryMatch(CertificateAuthorityAttribute reqAttribute, List<TrustAttribute> attributes)
			throws Exception {
		// String concept1 =
		// String concept2 =
		double semsim = 0;
		if (reqAttribute.getCountry() == null) {
			log.info("CertificateAuthority-Country matching: no requested so return -1");
			return -1;
		}
		// find the the best possible match (i.e. with most high semsim
		// value)
		for (TrustAttribute a : attributes) {
			final CertificateAuthorityAttribute ca = (CertificateAuthorityAttribute) a;
			double result = semSim.compute(URI.create(reqAttribute.getCountry()), URI.create(ca.getCountry()));
			if (result > semsim) {
				{
					semsim = result;
				}
			}
		}

		log.info("CertificateAuthority-Country matching: requested and returns " + semsim);
		return semsim;
	}

}
