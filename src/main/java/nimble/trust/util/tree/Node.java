package nimble.trust.util.tree;



import java.util.List;

import com.google.common.collect.Lists;


public class Node {

	private String name;
	
	private List<Node> subNodes =Lists.newArrayList();

	public Node(String name) {
		this.name = name;
	}

	public String getName() {
		return name;
	}
//
	public void setName(String name) {
		this.name = name;
	}

	public List<Node> getSubNodes() {
		return subNodes;
	}

	public void setSubNodes(List<Node> subNodes) {
		this.subNodes = subNodes;
	}
	
	@Override
	public String toString() {
		return name;
	}
	
	public void addSubNode(Node node){
		getSubNodes().add(node);
	}

	public void getLevel() {
		
	}

}
