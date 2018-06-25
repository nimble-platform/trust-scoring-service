package nimble.trust.engine.service.command;



import java.net.URI;
import java.util.List;

import com.google.common.collect.Lists;
import com.google.common.collect.Ordering;

import nimble.trust.common.OrderType;
import nimble.trust.engine.model.pojo.Agent;
import nimble.trust.util.tuple.Tuple2;

/**
 * 
 *@author markov
 *
 */


public class Sort {

	
	public List<Tuple2<URI, Double>> sort(List<Tuple2<Agent, Double>> list, OrderType order) {
		if (order==OrderType.ASC){
			return asc(list);
		}
		else{
			return desc(list);
		}
		
	}
	
	
	private List<Tuple2<URI, Double>> desc(List<Tuple2<Agent, Double>> list) {
		List<Tuple2<URI, Double>> sort = Lists.newArrayList();
		final Ordering<Tuple2<Agent, Double>> o = new Ordering<Tuple2<Agent, Double>>() {
			@Override
			public int compare(Tuple2<Agent, Double> left, Tuple2<Agent, Double> right) {
				return right.getT2().compareTo(left.getT2());
			}
		};
		list = o.sortedCopy(list);
		for (Tuple2<Agent, Double> t : list) {
			URI id = (t.getT1().getInputUID())!=null? t.getT1().getInputUID():t.getT1().getUri();
			sort.add(new Tuple2<URI, Double>(id, t.getT2()));
		}
		return sort;
	}
	
	
	private List<Tuple2<URI, Double>> asc(List<Tuple2<Agent, Double>> list) {
		List<Tuple2<URI, Double>> sort = Lists.newArrayList();
		final Ordering<Tuple2<Agent, Double>> o = new Ordering<Tuple2<Agent, Double>>() {
			@Override
			public int compare(Tuple2<Agent, Double> left, Tuple2<Agent, Double> right) {
				return left.getT2().compareTo(right.getT2());
			}
		};
		list = o.sortedCopy(list);
		for (Tuple2<Agent, Double> t : list) {
			URI id = (t.getT1().getInputUID())!=null? t.getT1().getInputUID():t.getT1().getUri();
			sort.add(new Tuple2<URI, Double>( id , t.getT2()));
		}
		return sort;
	}

}
