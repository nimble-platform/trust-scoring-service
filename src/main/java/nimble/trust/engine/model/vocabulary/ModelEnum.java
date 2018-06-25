package nimble.trust.engine.model.vocabulary;


public enum ModelEnum {

	Trust("http://www.compose-project.eu/ns/web-of-things/trust"), 
	SecuritypolicyVocab("http://www.compose-project.eu/ns/web-of-things/securitypolicyvocab"), 
	UsdlSec("http://www.linked-usdl.org/ns/usdl-sec#"), 
	Dul("http://www.loa-cnr.it/ontologies/DUL.owl"), 
	Ssn("http://purl.oclc.org/NET/ssnx/ssn"), 
	SecurityOntology("http://www.compose-project.eu/ns/web-of-things/security"),
	// MergedTrust("http://www.compose-project.eu/ns/web-of-things/mergedtrust", Const.repoOntologies+"mergedtrust.ttl")
	;

	private String URI;

	ModelEnum(String uri) {
		this.URI = uri;
	}

	public String getURI() {
		return URI;
	}

}
