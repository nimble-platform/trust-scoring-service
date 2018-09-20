package nimble.trust.engine.model.vocabulary;


public enum ModelEnum {

	Trust("http://www.nimble-project.org/ns/trust"), 
	SecuritypolicyVocab("http://www.nimble-project.org/ns/securitypolicyvocab"), 
	UsdlSec("http://www.linked-usdl.org/ns/usdl-sec#"), 
	Dul("http://www.loa-cnr.it/ontologies/DUL.owl"), 
	Ssn("http://purl.oclc.org/NET/ssnx/ssn"), 
	SecurityOntology("http://www.nimble-project.org/ns/security"),
	// MergedTrust("http://www.nimble-project.org/ns/mergedtrust", Const.repoOntologies+"mergedtrust.ttl")
	;

	private String URI;

	ModelEnum(String uri) {
		this.URI = uri;
	}

	public String getURI() {
		return URI;
	}

}
