package nimble.trust.engine.model.vocabulary;




import java.util.HashMap;
import java.util.Map;

import com.hp.hpl.jena.vocabulary.DC;
import com.hp.hpl.jena.vocabulary.OWL;
import com.hp.hpl.jena.vocabulary.RDF;
import com.hp.hpl.jena.vocabulary.RDFS;
import com.hp.hpl.jena.vocabulary.XSD;

public class NSPrefixes {
	
	public static final Map<String, String> map;

    static {
    	map = new HashMap<String, String>();
    	map.put("rdf", RDF.getURI());
    	map.put("rdfs", RDFS.getURI());
        map.put("trust", Trust.getURI());
        map.put("compose-sec", ModelEnum.SecurityOntology.getURI().toString()+"#");
    	map.put("dc", DC.getURI());
    	map.put("xsd", XSD.getURI());
    	map.put("owl", OWL.getURI());
    	map.put("usdl-sec", UsdlSec.getURI());
    	map.put("db", "http://d2rq.147.83.30.133.xip.io/resource/");
    	map.put("map", "http://d2rq.147.83.30.133.xip.io/resource/#");
    	map.put("vocab", "http://localhost:8080/d2rq/compose#");
    }
}
