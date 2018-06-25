package nimble.trust.engine.model.factory;



import nimble.trust.engine.model.pojo.CertificateAuthorityAttribute;
import nimble.trust.engine.model.pojo.SecurityAttribute;
import nimble.trust.engine.model.pojo.SecurityMechanism;
import nimble.trust.engine.model.pojo.SecurityTechnology;
import nimble.trust.engine.model.pojo.TrustAttribute;
import nimble.trust.engine.model.pojo.TrustProfile;
import nimble.trust.util.uri.UIDGenerator;

public class TrustModelFactory {

	private UIDGenerator uidGenerator ;
	
	public TrustModelFactory(UIDGenerator uidGenerator){
		this.uidGenerator = uidGenerator;
	}
	
	public TrustProfile createTrustProfile() {
		return new TrustProfile(uidGenerator.create(TrustProfile.class));
	}
	
//	public TrustCriteria createTrustRequest() {
//		return new TrustCriteria(uidGenerator.create(TrustCriteria.class));
//	}

	public TrustAttribute createTrustAttibute() {
		return new TrustAttribute(uidGenerator.create(TrustProfile.class));
	}

	public SecurityAttribute createSecurityAttribute() {
		return  new SecurityAttribute(uidGenerator.create(SecurityAttribute.class));
	}
	
	public CertificateAuthorityAttribute createCertificateAuthorityAttribute() {
		return  new CertificateAuthorityAttribute(uidGenerator.create(CertificateAuthorityAttribute.class));
	}
	
	public CertificateAuthorityAttribute createCertificteAuthorityAttribute() {
		return  new CertificateAuthorityAttribute(uidGenerator.create(CertificateAuthorityAttribute.class));
	}
	
	public SecurityMechanism createSecurityMechanism() {
		return  new SecurityMechanism(uidGenerator.create(SecurityMechanism.class));
	}
	
	public SecurityTechnology createSecurityTechnology() {
		return  new SecurityTechnology(uidGenerator.create(SecurityTechnology.class));
	}

}
