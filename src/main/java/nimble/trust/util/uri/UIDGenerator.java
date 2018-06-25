package nimble.trust.util.uri;



import java.net.URI;
import java.net.URISyntaxException;
import java.util.Map;
import java.util.UUID;

import com.google.common.collect.Maps;

import nimble.trust.common.Const;

public class UIDGenerator {
	
	public static UIDGenerator  instanceRequest = new UIDGenerator("http://localhost/requests#");
	public static UIDGenerator instanceTrust = new UIDGenerator("http://localhost/trustdata#");
	
	private String baseURI = "http://localhost/triples#" ;
	
	@SuppressWarnings("unused")
	private  Map<Class<?>, Long> map = Maps.newHashMap();
	 
	public UIDGenerator(String baseURI) {
		this.baseURI = baseURI;
	}
	
	public  URI create(Class<?> clazz){
		UUID uid = getUUID(clazz); 
		
		String str = baseURI 
				+
				clazz.getSimpleName()+Const.underScore+uid.toString();
		try {
			return new URI(str);
		} catch (URISyntaxException e) {
			e.printStackTrace();
		}
		return null;
	}

	private static UUID getUUID(Class<?> clazz) {
		return UUID.randomUUID();
	}

}
