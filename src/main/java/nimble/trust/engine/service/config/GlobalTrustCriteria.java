package nimble.trust.engine.service.config;


import java.net.URI;

import com.hp.hpl.jena.datatypes.BaseDatatype;
import com.hp.hpl.jena.datatypes.xsd.impl.XSDDouble;

import nimble.trust.engine.model.expression.ExpressionBuilder;
import nimble.trust.engine.model.factory.TrustModelFactory;
import nimble.trust.engine.model.pojo.CertificateAuthorityAttribute;
import nimble.trust.engine.model.pojo.SecurityAttribute;
import nimble.trust.engine.model.pojo.SecurityGoal;
import nimble.trust.engine.model.pojo.SecurityTechnology;
import nimble.trust.engine.model.pojo.TrustAttribute;
import nimble.trust.engine.model.pojo.TrustCriteria;
import nimble.trust.engine.model.types.USDLSecExpression;
import nimble.trust.engine.model.vocabulary.Trust;
import nimble.trust.engine.model.vocabulary.UsdlSec;
import nimble.trust.util.uri.UIDGenerator;

/**
 * Global (or, Absolute) Trust Criteria. This is a temp class for prototype
 * purpose. It will be refined afterwords..perhaps by conducting a survey among
 * users to identify how they see/perceive trust in their respective domains.
 * 
 * @author markov
 * 
 */
public class GlobalTrustCriteria {

	public static TrustCriteria instance() {

		final TrustModelFactory factory = new TrustModelFactory(UIDGenerator.instanceRequest);

		// reputation
		TrustAttribute att1 = factory.createTrustAttibute();
		att1.addType(URI.create(Trust.Reputation.getURI()));
		att1.setValue(Trust.medium.getURI());
		att1.setValueDatatype(new BaseDatatype(Trust.ReputationScale.getURI()));
		att1.setImportance(1);

		// qos
		TrustAttribute att2 = factory.createTrustAttibute();
		att2.addType(URI.create(Trust.QoSAttribute.getURI()));
		att2.setValue("0.3");
		att2.setValueDatatype(XSDDouble.XSDdouble);
		att2.setImportance(1);

		{
			// //security Authorization
			// SecurityAttribute att3 = factory.createSecurityAttribute();
			// att3.setValueDatatype(USDLSecExpression.TYPE);
			// att3.addType(URI.create(Trust.SecurityGuarantee.getURI()));
			// SecurityGoal goal2 = new
			// SecurityGoal(URI.create(UsdlSec.Authorization.getURI()));
			// att3.addSecurityGoal(goal2);
			// att3.setImportance(1);
			// trustRequest.addAttribute(att3);
		}

		// security Authentication
		SecurityAttribute att4 = factory.createSecurityAttribute();
		att4.setValueDatatype(USDLSecExpression.TYPE);
		att4.addType(URI.create(Trust.SecurityGuarantee.getURI()));
		SecurityGoal goal = new SecurityGoal(URI.create(UsdlSec.Authentication.getURI()));
		att4.addSecurityGoal(goal);
		att4.addRealizedByTechnology(new SecurityTechnology(URI
				.create("http://www.compose-project.eu/ns/web-of-things/security#OAuth_2_0")));
		att4.setImportance(1);

		// security Confidentiality
		SecurityAttribute att5 = factory.createSecurityAttribute();
		att5.setValueDatatype(USDLSecExpression.TYPE);
		att5.addType(URI.create(Trust.SecurityGuarantee.getURI()));
		goal = new SecurityGoal(URI.create(UsdlSec.Confidentiality.getURI()));
		att5.addSecurityGoal(goal);
		att5.setImportance(1);

		// security certificate
		CertificateAuthorityAttribute certficate = factory.createCertificteAuthorityAttribute();
		certficate.addType(URI.create(Trust.CertificateAuthorityAttribute.getURI()));
		certficate.setCertificateAuthority("http://www.compose-project.eu/ns/web-of-things/security#US-Based");
		// certficate.setCountry("http://www.compose-project.eu/ns/web-of-things/security#US");

		return new ExpressionBuilder().startNewTrustCriteria().attribute(att1).and().attribute(att2).and().attribute(att4)
				.and().attribute(att5).and().attribute(certficate).build();

	}

}
