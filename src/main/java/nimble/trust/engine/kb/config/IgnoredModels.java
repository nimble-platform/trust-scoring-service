package nimble.trust.engine.kb.config;



import java.util.HashSet;
import java.util.Set;

import nimble.trust.engine.model.vocabulary.ModelEnum;


public class IgnoredModels {

	public static Set<String> getModels() {
		Set<String> set = new  HashSet<String>();
		set.add(ModelEnum.Dul.getURI());
		return set;
	}
	
	

}
