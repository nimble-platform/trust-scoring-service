package nimble.trust.engine.model.pojo;



import java.net.URI;
import java.util.List;

import com.google.common.collect.Lists;
import com.hp.hpl.jena.rdf.model.ResourceFactory;

import nimble.trust.util.uri.URIUtil;

public class TResource {

	private URI uri;

	private String label;

	private String comment;
	
	private List<TResource> types = Lists.newArrayList();

	public TResource(URI uri) {
		this.uri = uri;
	}

	public URI getUri() {
		return uri;
	}

	public void setUri(URI uri) {
		this.uri = uri;
	}
	
	public String uriToLocalName(){
		return URIUtil.getLocalName(getUri());
	}

	public void setLabel(String label) {
		this.label = label;
	}
	
	public String getLabel() {
		return label;
	}

	public String getComment() {
		return comment;
	}

	public void setComment(String comment) {
		this.comment = comment;
	}

	public com.hp.hpl.jena.rdf.model.Resource asJenaResource() {
		return ResourceFactory.createResource(getUri().toASCIIString());
	}

	public List<TResource> getTypesAll() {
		return types;
	}
	
	public TResource obtainType() {
		return (types.isEmpty())? null:types.get(0);
	}
	
	public void addType(TResource tResource) {
		types.add(tResource);
	}
	
	public void addType(URI uri) {
		types.add(new TResource(uri));
	}
	

}
