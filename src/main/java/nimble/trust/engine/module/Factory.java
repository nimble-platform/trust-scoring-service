package nimble.trust.engine.module;



import com.google.inject.Guice;

public class Factory {
	
	 public static <T> T createInstance(Class<T> type){
		return Guice.createInjector(new TrustModule()).getInstance(type);
	}

}
