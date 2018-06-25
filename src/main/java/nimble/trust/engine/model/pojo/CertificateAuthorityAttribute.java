package nimble.trust.engine.model.pojo;



import java.net.URI;

public class CertificateAuthorityAttribute extends TrustAttribute{
	
	
	private String certificateAuthority ;
	private String country ;

	public CertificateAuthorityAttribute(URI uri) {
		super(uri);
	}

	public String getCertificateAuthority() {
		return certificateAuthority;
	}

	public void setCertificateAuthority(String certificateAuthorityUri) {
		this.certificateAuthority = certificateAuthorityUri;
	}

	public String getCountry() {
		return country;
	}

	public void setCountry(String countryUri) {
		this.country = countryUri;
	}
	
	
	
	

	
}
