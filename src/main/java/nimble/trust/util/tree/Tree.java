package nimble.trust.util.tree;



import java.util.ArrayList;
import java.util.List;

import nimble.trust.common.Const;

public class Tree {
	
	
	private Node root;
	
	public Node getRoot() {
		return root;
	}
	
	public void setRoot(Node root) {
		this.root = root;
	}
	
	
	@Override
	public String toString() {
		StringBuffer b = new StringBuffer();
		append(root, b, 1);
		return b.toString();
	}

	private void append(Node node, StringBuffer b, int pos) {
		b.append(node.toString()).append(Const.NEW_LINE);
		List<Node> nodes = node.getSubNodes();
		for (Node subNode : nodes) {
			append(subNode, b, pos);
		}
	}
	
	
	public Object[] toArray() {
		ArrayList<String> list  = new ArrayList<String>();
		append(root, list);
		list.add("");
		return list.toArray();
	}

	private void append(Node node, ArrayList<String> list) {
		list.add(node.toString());
		List<Node> nodes = node.getSubNodes();
		for (Node subNode : nodes) {
			append(subNode, list);
		}
	}
	
	

}
