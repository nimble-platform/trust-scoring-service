package nimble.trust.engine.model.graph;



import java.util.List;
import java.util.Map;

import com.hp.hpl.jena.rdf.model.Model;

import jersey.repackaged.com.google.common.collect.Lists;
import jersey.repackaged.com.google.common.collect.Maps;
import nimble.trust.engine.model.pojo.TrustAttribute;

public class Vertex {
	
	private String id ;
	
	private String name;
	
	private String composeType;
	
	private String composeID;
	
	private Model model;
	
	private Map<TrustAttribute, Double> scores = Maps.newHashMap();
	
	private List<Vertex> merged=Lists.newArrayList();
	
	public Vertex(String id) {
		this.setID(id);
	}
	
	public List<Vertex> getMerged() {
		return merged;
	}

	public boolean isMergeOfServices() {
		return (!merged.isEmpty());
	}


	public String getID() {
		return id;
	}

	public void setID(String id) {
		this.id = id;
	}

	public String getComposeType() {
		return composeType;
	}

	public void setComposeType(String composeType) {
		this.composeType = composeType;
	}

	public String getComposeID() {
		return composeID;
	}

	public void setComposeID(String composeID) {
		this.composeID = composeID;
	}
	
	public Map<TrustAttribute, Double> getScores() {
		return scores;
	}
	
	public void addScore(TrustAttribute attr, Double value){
		this.getScores().put(attr, value);
	}
	
	public void setScores(Map<TrustAttribute, Double> scores) {
		this.scores = scores;
	}

	public Model getModel() {
		return model;
	}

	public void setModel(Model model) {
		this.model = model;
	}
	
	@Override
	public String toString() {
		return id;
	}
	
	public String getName() {
		return name;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
	
}
