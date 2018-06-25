package nimble.trust.engine.service.command;



import java.util.Iterator;

import com.hp.hpl.jena.ontology.OntClass;

import nimble.trust.util.tree.Node;

public class FillTaxonomy {

	public static void execute(OntClass ontClass, Node node) {
			Iterator<OntClass>	it = ontClass.listSubClasses(true);
			while (it.hasNext()) {
				OntClass ontClass2 = (OntClass) it.next();
				Node sNode = new Node(ontClass2.getLocalName());
				node.addSubNode(sNode);
				execute(ontClass2, sNode);
			}
	}

}
