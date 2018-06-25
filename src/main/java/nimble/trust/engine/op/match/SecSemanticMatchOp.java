package nimble.trust.engine.op.match;



import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import nimble.trust.engine.kb.KnowledgeBaseManager;
import nimble.trust.engine.kb.config.LocationMapping;
import nimble.trust.engine.model.pojo.SecurityAttribute;
import nimble.trust.engine.model.pojo.SecurityGoal;
import nimble.trust.engine.model.pojo.SecurityMechanism;
import nimble.trust.engine.model.pojo.SecurityTechnology;
import nimble.trust.engine.model.pojo.TrustAttribute;
import nimble.trust.engine.model.vocabulary.ModelEnum;
import nimble.trust.engine.op.semsim.SemSim;

/**
 * A specialized semantic match operator that semantically matches two given
 * security semantic descriptions.
 * 
 * @author markov
 * 
 */
public class SecSemanticMatchOp {

	private static final Logger log = LoggerFactory
			.getLogger(SecSemanticMatchOp.class);

	private SemSim semSim = new SemSim(
			LocationMapping.resolveLocation(ModelEnum.SecurityOntology.getURI()));

	@SuppressWarnings("unused")
	private nimble.trust.engine.kb.KnowledgeBaseManager kbManager;

	public SecSemanticMatchOp(KnowledgeBaseManager kbManager) {
		this.kbManager = kbManager;
	}

	// enhance result by implementing support for graph similarity matching
	// by SML (Semantic Measures Library) and/or graph querying
	/**
	 * 
	 * @param reqAttribute
	 * @param attributes
	 * @return
	 */
	public double apply(TrustAttribute reqAttribute,
			List<TrustAttribute> attributes) throws Exception {
		double result = 0;
		SecurityAttribute reqSecurityAttribute = (SecurityAttribute) reqAttribute;
		double val1 = chechGoalMatch(reqSecurityAttribute, attributes);
		double val2 = chechMechanismMatch(reqSecurityAttribute, attributes);
		double val3 = chechTechnologyMatch(reqSecurityAttribute, attributes);
		if (val1 == 0 && val2 == 0 && val3 == 0) {
			return 0;
		}
		result = sumNotNegative(val1, val2, val3)
				/ countNotNegative(val1, val2, val3);
		log.info("SecSematicMatch result " + result);
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
	private double chechTechnologyMatch(SecurityAttribute reqAttribute,
			List<TrustAttribute> attributes) throws Exception {
		// String concept1 =
		// String concept2 =
		double semsim = 0;
		final List<SecurityMechanism> mechanismsR = reqAttribute
				.getImplementedBy();
		if (mechanismsR.isEmpty()) {
			// log.info("Doing security technology match, but there was no security mechanism matching: no requested so returns -1");
			// return -1;
		}

		List<SecurityTechnology> securityTechnologies = reqAttribute
				.getRealizedByTechnology();
		if (securityTechnologies.isEmpty()) {
			log.info("Security technology matching: no requested so return -1");
			return -1;
		}
		for (SecurityTechnology securityTechnologyR : securityTechnologies) {
			// find the the best possible match (i.e. with most high semsim
			// value)
			for (TrustAttribute a : attributes) {
				final SecurityAttribute securityAttribute = (SecurityAttribute) a;
				List<SecurityTechnology> technologies = securityAttribute
						.getRealizedByTechnology();
				for (SecurityTechnology securityTechnology : technologies) {
					double result = semSim.compute(securityTechnologyR.getUri(),
							securityTechnology.getUri());
					if (result > semsim) {
						{
							semsim = result;
						}
					}
				}

			}

		}

		log.info("Security technology matching: requested and returns "
				+ semsim);
		return semsim;
	}

	/**
	 * 
	 * @param reqAttribute
	 * @param attributes
	 * @return
	 */
	private double chechMechanismMatch(SecurityAttribute reqAttribute,
			List<TrustAttribute> attributes) {
		boolean found = false;
		for (TrustAttribute a : attributes) {
			final List<SecurityMechanism> mechanismsNeeded = reqAttribute
					.getImplementedBy();
			if (mechanismsNeeded.isEmpty()) {
				log.info("Security mechanism matching: no requested so return -1");
				return -1;
			}
			for (SecurityMechanism mechanism : mechanismsNeeded) {
				final SecurityAttribute securityAttribute = (SecurityAttribute) a;
				List<SecurityMechanism> mechanisms2 = securityAttribute
						.getImplementedBy();
				for (SecurityMechanism mechanism2 : mechanisms2) {
					if (mechanism.obtainType() == null) {
						log.info("Security mechanism matching: no requested so return -1");
						return -1;
					}
					if (mechanism.obtainType().getUri()
							.equals(mechanism2.getUri())) {
						found = true;
					}
				}
			}
		}
		log.info("Security mechanism matching: requested and returns "
				+ ((found) ? 1 : 0));
		return (found) ? 1 : 0;
	}

	/**
	 * 
	 * @param reqAttribute
	 * @param attributes
	 * @return
	 */
	private double chechGoalMatch(SecurityAttribute reqAttribute,
			List<TrustAttribute> attributes) {
		boolean found = false;
		for (TrustAttribute a : attributes) {
			final List<SecurityGoal> goalsNeeded = reqAttribute
					.getSecurityGoals();
			if (goalsNeeded.isEmpty()) {
				log.info("Security goal matching: no requested so returns -1");
				return -1;
			}
			for (SecurityGoal securityGoal : goalsNeeded) {
				final SecurityAttribute securityAttribute = (SecurityAttribute) a;
				List<SecurityGoal> goals = securityAttribute.getSecurityGoals();
				for (SecurityGoal securityGoal2 : goals) {
					if (securityGoal.getUri().equals(securityGoal2.getUri())) {
						found = true;
					}
				}
			}
		}
		log.info("Security goal matching: requested and returns "
				+ ((found) ? 1 : 0));
		return (found) ? 1 : 0;
	}


}
