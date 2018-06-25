package nimble.trust.engine.model.vocabulary;



import java.util.HashMap;
import java.util.Map;


public class QNSPrefixes {
	
	
	public static final Map<String, String> map = new HashMap<String, String>();

    static {
    	map.putAll(NSPrefixes.map);
    	map.put("fn", "http://www.w3.org/2005/xpath-functions#");
    	map.put("apf", "http://jena.hpl.hp.com/ARQ/property#");
    }
	


}
