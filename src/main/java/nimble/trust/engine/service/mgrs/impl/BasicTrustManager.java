package nimble.trust.engine.service.mgrs.impl;


import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.net.URI;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import javax.inject.Named;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.base.Predicate;
import com.google.common.base.Stopwatch;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Iterables;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.eventbus.EventBus;
import com.google.inject.Inject;
import com.hp.hpl.jena.ontology.OntClass;
import com.hp.hpl.jena.ontology.OntModel;
import com.hp.hpl.jena.ontology.OntModelSpec;
import com.hp.hpl.jena.rdf.model.Model;

import nimble.trust.common.OrderType;
import nimble.trust.engine.Configuration;
import nimble.trust.engine.collector.Collector;
import nimble.trust.engine.json.TrustPOJOFactory;
import nimble.trust.engine.kb.KnowledgeBaseManager;
import nimble.trust.engine.kb.RDFModelsHandler;
import nimble.trust.engine.kb.SharedOntModelSpec;
import nimble.trust.engine.kb.SparqlGraphStoreFactory;
import nimble.trust.engine.kb.SparqlGraphStoreManager;
import nimble.trust.engine.kb.config.IgnoredModels;
import nimble.trust.engine.kb.config.LocationMapping;
import nimble.trust.engine.model.pojo.TrustCriteria;
import nimble.trust.engine.model.pojo.Value;
import nimble.trust.engine.op.enums.EnumScoreStrategy;
import nimble.trust.engine.service.command.CreateUpdateTrustProfile;
import nimble.trust.engine.service.command.FillTaxonomy;
import nimble.trust.engine.service.command.SemanticMetaDataFetcher;
import nimble.trust.engine.service.config.CollectorConfig;
import nimble.trust.engine.service.config.GlobalTrustCriteria;
import nimble.trust.engine.service.interfaces.RankingManager;
import nimble.trust.engine.service.interfaces.TrustSimpleManager;
import nimble.trust.util.tree.Node;
import nimble.trust.util.tree.Tree;
import nimble.trust.util.tuple.ListTuple;
import nimble.trust.util.tuple.ListTupleConvert;
import nimble.trust.util.tuple.TFunctor;
import nimble.trust.util.tuple.Tuple2;

/**
 * Implementation of TrustManager interface
 * 
 * @author markov
 * 
 */
public class BasicTrustManager implements TrustSimpleManager {

	private static final Logger log = LoggerFactory.getLogger(BasicTrustManager.class);
	private final List<Collector> collectors = Lists.newArrayList();
	private boolean doSaveIntoStore = false;
	private final List<SparqlGraphStoreManager> externalGraphStoreMgrs = Lists.newArrayList();
	private final SparqlGraphStoreManager graphStoreManager;
	private final KnowledgeBaseManager kbManager;
	private final RankingManager rankingManager;

	/**
	 * a default strategy for calculating trust index
	 */
	private EnumScoreStrategy globalStrategy = EnumScoreStrategy.Weighted_sum_model;
	
	private TrustCriteria globalTrustCriteria = GlobalTrustCriteria.instance();

	@Inject
	public BasicTrustManager(EventBus eventBus, SparqlGraphStoreFactory graphStoreFactory, RankingManager rankingManager, KnowledgeBaseManager kbManager,
			@Named(Configuration.SPARQL_ENDPOINT_QUERY_PROP) String queryEndpoint, @Named(Configuration.SPARQL_ENDPOINT_UPDATE_PROP) String updateEndpoint,
			@Named(Configuration.SPARQL_ENDPOINT_SERVICE_PROP) String serviceEndpoint) throws Exception {

		Set<String> ignoredImports = IgnoredModels.getModels();
		Set<URI> baseModels = ImmutableSet.of();
		ImmutableMap.Builder<String, String> locationMappings = LocationMapping.getMapping();
		this.graphStoreManager = graphStoreFactory.create(queryEndpoint, updateEndpoint, serviceEndpoint, baseModels, locationMappings.build(), ignoredImports);
		this.rankingManager = rankingManager;
		this.kbManager = kbManager;
		registerExternalGraphStoreManagers(externalGraphStoreMgrs, graphStoreFactory);
		registerCollectors();
	}

	@Override
	public Tree obtainTaxonomy(String graphName, String rootConcept) {
		Tree tree = new Tree();
		OntModel model = kbManager.getModel(graphName,  OntModelSpec.OWL_MEM_TRANS_INF);//graphStoreManager.getGraph(URI.create(graphName), OntModelSpec.OWL_MEM_TRANS_INF);
		OntClass ontClass = model.getOntClass(rootConcept);
		Node root = new Node(ontClass.getLocalName());
		tree.setRoot(root);
		FillTaxonomy.execute(ontClass, root);
		return tree;
	}

	/**
	 * For a given resource (identified by uri), retrieves and updates a trust profile. If the trust profile was not
	 * existing for the given resource, this method will create the profile and will collect all profile data (i.e.
	 * trust parameters).
	 * 
	 * @param model
	 * @param uri
	 */
	protected OntModel fillTrustProfilForResource(OntModel model, URI uri, List<Model> collectedDataList) {
		return new CreateUpdateTrustProfile().apply(model, uri, collectedDataList);
	}

	/**
	 * 
	 * Loads semantic metadata (semantic annotations) for a resource identified with URI from the registered registers
	 * 
	 * @param uri resource URI
	 * @param fetchFromExternalRegistries true if data has to be retrieved from externally registers; otherwise false
	 * @param useMappedLocations true if mapped location (e.g. local cache) should be used to retrieve data; otherwise
	 *            false
	 * @param fetchFromInternalRegirsty true if data has to be retrieved from internal registry; otherwise false
	 * @return ontModel as a Jena model that contains statements about the resource
	 */
	private OntModel loadSemanticMetadata(URI uri, boolean fetchFromExternalRegistries, boolean useMappedLocations, boolean fetchFromInternalRegirsty) {
		return new SemanticMetaDataFetcher().apply(uri, fetchFromExternalRegistries, useMappedLocations, fetchFromInternalRegirsty);
	}

	@Override
	public KnowledgeBaseManager getKnowledgeBaseManager() {
		return kbManager;
	}

	/**
	 * 
	 */
	@Override
	public List<Tuple2<URI, Double>> rankResources(List<URI> resources, TrustCriteria criteria, OrderType order) throws Exception {
		return rankResources(resources, criteria, globalStrategy, false, order);
	}

	/**
	 * 
	 */
	@Override
	public List<Tuple2<URI, Double>> rankResources(List<URI> resources, TrustCriteria criteria, EnumScoreStrategy scoreStrategy, boolean filterByAttributeMissing, OrderType order) throws Exception {
		boolean filterByCriteriaNotMet = false;
		final List<Tuple2<URI, Double>> scores = processCall(resources, criteria, scoreStrategy, filterByAttributeMissing, filterByCriteriaNotMet, order, false);
//		final List<URI> rankedList = ListTupleConvert.toListOfTupleElement(scores, 1);
		return scores;
	}

	@Override
	public Double obtainTrustIndex(URI resourceURI) throws Exception {
		final TrustCriteria criteria = getGlobalTrustCriteria();
		return obtainTrustIndex(resourceURI, criteria);
	}
	
	@Override
	public List<Tuple2<URI, Double>> obtainTrustIndexes(List<URI> resourceURIs) throws Exception {
		final TrustCriteria criteria = getGlobalTrustCriteria();
		boolean filterByCriteriaNotMet = false;
		boolean filterByAttributeMissing = false;
		return processCall(resourceURIs, criteria, globalStrategy, filterByAttributeMissing, filterByCriteriaNotMet, OrderType.DESC, false);
	}

	public Double obtainTrustIndex(URI resourceURI, TrustCriteria criteria) throws Exception {
		List<URI> list = Lists.newArrayList();
		list.add(resourceURI);
		boolean filterByCriteriaNotMet = false;
		boolean filterByAttributeMissing = false;
		final List<Tuple2<URI, Double>> scores = processCall(list, criteria, globalStrategy, filterByAttributeMissing, filterByCriteriaNotMet, OrderType.DESC, false);
		return scores.get(0).getT2();
	}

	@Override
	public List<URI> filterResources(List<URI> resources, TrustCriteria criteria, EnumScoreStrategy scoreStrategy, boolean filterByAttributeMissing, boolean filterByCriteriaNotMet,
			OrderType order, final Double thresholdValue) throws Exception {
		final List<Tuple2<URI, Double>> scores = processCall(resources, criteria, scoreStrategy, filterByAttributeMissing, filterByCriteriaNotMet, order, false);
		Iterable<Tuple2<URI, Double>> filtered = Iterables.filter(scores, new Predicate<Tuple2<URI, Double>>() {
			@Override
			public boolean apply(Tuple2<URI, Double> t) {
				return (Double.valueOf(t.getT2()).compareTo(thresholdValue) >= 0);
			}
		});
		printList(Lists.newArrayList(filtered), " filtered with thresholdValue value of " + thresholdValue);
		final List<URI> filteredList = ListTupleConvert.toListOfTupleElement(Lists.newArrayList(filtered), 1);
		return filteredList;
	}
	
	@Override
	public List<URI> filterByCriteriaNotMeet(List<URI> resources, TrustCriteria criteria) throws Exception {
		boolean filterByCriteriaNotMet = true;
		boolean filterByAttributeMissing = true;
		final List<Tuple2<URI, Double>> scores = processCall(resources, criteria, globalStrategy, filterByAttributeMissing, filterByCriteriaNotMet, OrderType.DESC, false);
		printList(Lists.newArrayList(scores), " filtered out those having at least one of criteria not meet");
		final List<URI> filteredList = ListTupleConvert.toListOfTupleElement(Lists.newArrayList(scores), 1);
		return filteredList;
	}

	private void printList(List<Tuple2<URI, Double>> set, String note) {
		log.info("******** <" + note + "> ************");
		for (Tuple2<URI, Double> t : set) {
			log.info(t.getT1() + " score " + t.getT2());
		}
		log.info("******** </" + note + "> ************");
	}

	@Override
	public List<URI> filterResources(List<URI> resources, TrustCriteria criteria, OrderType order, Double thresholdValue) throws Exception {
		return filterResources(resources, criteria, globalStrategy, true, false, order, thresholdValue);
	}

	/**
	 * Registration of trust information collectors. Typically, the collector grabs data from some source and transforms
	 * data into rdf model.
	 */
	private void registerCollectors() {
		List<Collector> list = CollectorConfig.read();
		for (Collector c : list) {
			collectors.add(c);
		}
	}

	/**
	 * Registration of external repositories where resource descriptions might be found
	 */
	private void registerExternalGraphStoreManagers(List<SparqlGraphStoreManager> list, SparqlGraphStoreFactory factory) {
		Set<String> ignoredImports = IgnoredModels.getModels();
		Set<URI> baseModels = ImmutableSet.of();
		ImmutableMap.Builder<String, String> locationMappings = LocationMapping.getMapping();
		SparqlGraphStoreManager manager = factory.create(Configuration.EXT_SPARQL_ENDPOINT_QUERY, Configuration.EXT_SPARQL_ENDPOINT_UPDATE,
				Configuration.EXT_SPARQL_ENDPOINT_SERVICE, baseModels, locationMappings.build(), ignoredImports);
		list.add(manager);
	}

	protected void saveIntoTripleStore(URI uri, Model model) {
		if (doSaveIntoStore)
			graphStoreManager.putGraph(uri, model);
	}

	/**
	 * 
	 * @param tupleModels
	 */
	private void storeModelsIntoStore(List<Tuple2<URI, Model>> tupleModels) {
		for (Tuple2<URI, Model> t : tupleModels) {
			saveIntoTripleStore(t.getT1(), t.getT2());
		}
	}
	
	public List<Tuple2<URI, Model>> obtainModelsListTuple(List<URI> resources, boolean logRequest) {
		List<Tuple2<URI, Model>> listModels = Lists.newArrayList();
		Map<URI, Model> map = Maps.newHashMap();
		for (URI uri : resources) {
			boolean fetchFromExternalRegistries = false;
			boolean useMappedLocations = false;
			boolean  fetchFromInternalRegirsty = true; 
			OntModel model = loadSemanticMetadata(uri, fetchFromExternalRegistries, useMappedLocations, fetchFromInternalRegirsty);
			map.put(uri, model);
			listModels.add(new Tuple2<URI, Model>(uri, model));
		}
		
		for (Collector collector : collectors) {
			collector.collectInformation(resources, map);	 
		}
		
		if (logRequest) {
			storeModelsIntoStore(listModels);
		}
				
		return listModels;
	}

	/**
	 * 
	 * @param resources
	 * @param logRequest 
	 * @return
	 */
	public List<Model> obtainModels(List<URI> resources, boolean logRequest) {
		Stopwatch timer = Stopwatch.createStarted();
		List<Tuple2<URI, Model>> listModels = obtainModelsListTuple(resources, logRequest);
		List<Model> models = castListModels(listModels);
		timer.stop();
		log.info("loading models  total time: "+timer.elapsed(TimeUnit.MILLISECONDS));
		return models;
	}
	
	/**
	 * 
	 * @param resources
	 * @param logRequest 
	 * @return
	 */
	public List<Model> castListModels(List<Tuple2<URI, Model>> list) {
		List<Model> models = ListTuple.toList(list, new TFunctor<Model>() {
			@Override
			public Model apply(Tuple2<?, ?> t) {
				return (Model) t.getT2();
			}
		});
		return models;
	}

	/**
	 * 
	 * @param resources
	 * @param recriteriaquest
	 * @param scoreStrategy
	 * @param filterByAttributeMissing
	 * @param order
	 * @param logRequest
	 * @return
	 * @throws Exception
	 */
	private List<Tuple2<URI, Double>> processCall(List<URI> resources, TrustCriteria criteria, EnumScoreStrategy scoreStrategy, boolean filterByAttributeMissing, boolean filterByCriteriaNotMet, OrderType order,boolean logRequest) throws Exception {
		List<Model> models =  obtainModels(resources, logRequest);
		return rankingManager.rankServiceModels(models, criteria, scoreStrategy, filterByAttributeMissing, filterByCriteriaNotMet, order);
	}

	@Override
	public boolean isTrusted(URI resourceURI, TrustCriteria criteria) throws Exception {
		final Double index = obtainTrustIndex(resourceURI, criteria);
		return new Value(index).isTrustworthy();
	}

	@Override
	public boolean isTrusted(URI resourceURI) throws Exception {
		final Double index = obtainTrustIndex(resourceURI);
		return new Value(index).isTrustworthy();
	}
	
	
	@Override
	public List<URI> filterTrustedByThreshold(List<URI> resources) throws Exception {
		return filterResources(resources, getGlobalTrustCriteria(), OrderType.DESC, Value.treshold);
	}
	
	@Override
	public List<URI> filterTrustedByThreshold(List<URI> resources, TrustCriteria criteria) throws Exception {
		return filterResources(resources, criteria, OrderType.DESC, Value.treshold);
	}

	@Override
	public boolean match(URI resource1uri, URI resource2uri) throws Exception {
		// TODO implement S-S match
		// get profiles and trust criteria and do vice-versa matching
		return false;
	}
	
	@Override
	public void setGlobalTrustCriteria(String criteriaAsJson) {
		TrustCriteria criteria = new TrustPOJOFactory().ofTrustCriteria(criteriaAsJson);
		this.globalTrustCriteria = criteria;
	}

	/**
	 * Set absolute trust criteria (users' perception of trust is not taken into account)
	 * 
	 * @return
	 */
	public void setGlobalTrustCriteria(TrustCriteria criteria) {
		this.globalTrustCriteria = criteria; 
	}
	
	public TrustCriteria getGlobalTrustCriteria() {
		return globalTrustCriteria;
	}
	
	
	@Override
	public void addResourceDescription(URI resourceURI, InputStream inputStream) {
		//TODO - consider to replace with some real backend db
		RDFModelsHandler.getGlobalInstance().fetch(resourceURI.toASCIIString(), inputStream,  SharedOntModelSpec.getModelSpecShared());
	}
	
	@Override
	public void addResourceDescription(URI resourceURI, File file) {
		//TODO - consider to replace with some real backend db
		FileInputStream inputStream;
		try {
			inputStream = new FileInputStream(file);
			RDFModelsHandler.getGlobalInstance().fetch(resourceURI.toASCIIString(), inputStream,  SharedOntModelSpec.getModelSpecShared());
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	}

}
