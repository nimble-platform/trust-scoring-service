package nimble.trust.engine.service.mgrs.impl;

import java.util.List;

import org.jgrapht.experimental.dag.DirectedAcyclicGraph;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.base.Predicate;
import com.google.common.collect.Iterables;
import com.google.common.collect.Lists;
import com.google.inject.Inject;
import com.hp.hpl.jena.ontology.OntModel;
import com.hp.hpl.jena.ontology.OntModelSpec;

import nimble.trust.common.CompositeServiceWrapper;
import nimble.trust.common.CompositionIdentifier;
import nimble.trust.common.Syntax;
import nimble.trust.engine.json.TrustPOJOFactory;
import nimble.trust.engine.kb.RDFModelsHandler;
import nimble.trust.engine.model.graph.Edge;
import nimble.trust.engine.model.graph.GraphUtility;
import nimble.trust.engine.model.graph.Vertex;
import nimble.trust.engine.model.pojo.TrustCriteria;
import nimble.trust.engine.model.utils.TrustOntologyUtil;
import nimble.trust.engine.model.vocabulary.ModelEnum;
import nimble.trust.engine.op.enums.EnumLevel;
import nimble.trust.engine.service.config.GlobalTrustCriteria;
import nimble.trust.engine.service.interfaces.RankingCompositionsManager;
import nimble.trust.engine.service.interfaces.TrustCompositionManager;
import nimble.trust.util.tuple.ListTupleConvert;
import nimble.trust.util.tuple.Tuple2;





public class TrustCompositionManagerImpl implements TrustCompositionManager{
	
	private TrustCriteria globalTrustCriteria = GlobalTrustCriteria.instance();
	private final RankingCompositionsManager rankingManager;
	
	private static final Logger log = LoggerFactory.getLogger(TrustCompositionManagerImpl.class);
	
	@Inject
	public TrustCompositionManagerImpl(RankingCompositionsManager rankingCompositionsManager) {
		OntModel model = RDFModelsHandler.getGlobalInstance().fetchOntologyFromLocalLocation(ModelEnum.Trust.getURI(), 
				Syntax.TTL.getName(), OntModelSpec.OWL_MEM_TRANS_INF);
		TrustOntologyUtil.init(model);
		this.rankingManager = rankingCompositionsManager;
	}

	@Override
	public void setGlobalTrustCriteria(TrustCriteria criteria) {
		this.globalTrustCriteria = criteria;
	}

	@Override
	public void setGlobalTrustCriteria(String criteriaAsJson) {
		TrustCriteria criteria = new TrustPOJOFactory().ofTrustCriteria(criteriaAsJson);
		this.globalTrustCriteria = criteria;
	}

	@Override
	public TrustCriteria getGlobalTrustCriteria() {
		return this.globalTrustCriteria;
	}

	@Override
	public List<CompositionIdentifier> filterTrustedByThreshold(List<CompositeServiceWrapper> compositeServiceList,TrustCriteria criteria, EnumLevel level, String strategy, final Double thresholdValue) throws Exception {
		List<Tuple2<CompositionIdentifier, Double>> scored = obtainTrustIndexes(compositeServiceList, criteria, level, strategy);
		Iterable<Tuple2<CompositionIdentifier, Double>> filtered = Iterables.filter(scored, new Predicate<Tuple2<CompositionIdentifier, Double>>() {
			@Override
			public boolean apply(Tuple2<CompositionIdentifier, Double> t) {
				return (Double.valueOf(t.getT2()).compareTo(thresholdValue) >= 0);
			}
		});
		printList(Lists.newArrayList(filtered), " filtered with thresholdValue value of " + thresholdValue);
		final List<CompositionIdentifier> filteredList = ListTupleConvert.toListOfTupleElement(Lists.newArrayList(filtered), 1);
		return filteredList;
	}
	
	private void printList(List<Tuple2<CompositionIdentifier, Double>> set, String note) {
		log.info("******** <" + note + "> ************");
		
		for (Tuple2<CompositionIdentifier, Double> t : set) {
			log.info(t.getT1().getId() + " score " + t.getT2());
		}
		log.info("******** </" + note + "> ************");
	}

	@Override
	public List<Tuple2<CompositionIdentifier, Double>> obtainTrustIndexes(List<CompositeServiceWrapper> compositeServiceList, TrustCriteria criteria, EnumLevel level, String strategy) throws Exception {
		List<Tuple2<CompositionIdentifier, Double>> list = Lists.newArrayList();
		for (CompositeServiceWrapper compositeServiceWrapper : compositeServiceList) {
//			DirectedAcyclicGraph<Vertex, Edge> g = GraphUtility.createDAG(compositeServiceWrapper.getFlow());
			Tuple2<CompositionIdentifier, Double> tuple = obtainTrustIndex(compositeServiceWrapper, criteria, level, strategy);
			list.add(tuple);
		}
		return list;
	}
	
	@Override
	public Tuple2<CompositionIdentifier, Double> obtainTrustIndex(CompositeServiceWrapper compositeServiceWrapper, TrustCriteria criteria, EnumLevel level, String strategy) throws Exception {
			DirectedAcyclicGraph<Vertex, Edge> g = GraphUtility.createDAG(compositeServiceWrapper.getFlow());
			Double score = obtainTrustIndex(g, criteria, level, strategy);
			Tuple2<CompositionIdentifier, Double> tuple = new Tuple2<CompositionIdentifier, Double>(compositeServiceWrapper.getCompositionIdentifier(),score);
			return tuple;
	}
	
	@Override
	public Double obtainTrustIndex(DirectedAcyclicGraph<Vertex, Edge> g, TrustCriteria criteria, EnumLevel level, String strategy) throws Exception {
			return rankingManager.computeScore(g, criteria);
	}
		
	
	

	
}
