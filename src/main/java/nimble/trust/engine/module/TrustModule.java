package nimble.trust.engine.module;


import java.io.IOException;
import java.util.Properties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.inject.AbstractModule;
import com.google.inject.assistedinject.FactoryModuleBuilder;
import com.google.inject.name.Names;

import nimble.trust.engine.kb.ConcurrentSparqlGraphStoreManager;
import nimble.trust.engine.kb.KnowledgeBaseManager;
import nimble.trust.engine.kb.KnowledgeBaseManagerSparql;
import nimble.trust.engine.kb.SparqlGraphStoreFactory;
import nimble.trust.engine.kb.SparqlGraphStoreManager;
import nimble.trust.engine.service.interfaces.RankingCompositionsManager;
import nimble.trust.engine.service.interfaces.RankingManager;
import nimble.trust.engine.service.interfaces.TrustCompositionManager;
import nimble.trust.engine.service.interfaces.TrustSimpleManager;
import nimble.trust.engine.service.mgrs.impl.BasicRankingManager;
import nimble.trust.engine.service.mgrs.impl.BasicTrustManager;
import nimble.trust.engine.service.mgrs.impl.RankingCompositionsManagerImpl;
import nimble.trust.engine.service.mgrs.impl.TrustCompositionManagerImpl;


public class TrustModule extends AbstractModule {
	
    private static final Logger log = LoggerFactory.getLogger(TrustModule.class);

    protected void configure() {

    	Names.bindProperties(binder(), getProperties());
        bind(KnowledgeBaseManager.class).to(KnowledgeBaseManagerSparql.class);
        bind(TrustSimpleManager.class).to(BasicTrustManager.class);
        bind(TrustCompositionManager.class).to(TrustCompositionManagerImpl.class);
        bind(RankingManager.class).to(BasicRankingManager.class);
        bind(RankingCompositionsManager.class).to(RankingCompositionsManagerImpl.class);
        install(new FactoryModuleBuilder()
                .implement(SparqlGraphStoreManager.class, ConcurrentSparqlGraphStoreManager.class)
                .build(SparqlGraphStoreFactory.class));
    }
    

    private Properties getProperties() {
        try {
            Properties properties = new Properties();
            properties.load(getClass().getClassLoader().getResourceAsStream("config.properties"));
            return properties;
        } catch (IOException ex) {
            log.error("Error loading properties from config.properties", ex);
        }
        return new Properties();
    }
}
