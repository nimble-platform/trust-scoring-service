package nimble.trust.engine.collector;



import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import nimble.trust.common.Const;
import nimble.trust.common.ValuesHolder;
import nimble.trust.engine.model.vocabulary.Trust;
import nimble.trust.engine.service.BeanUtil;

public class ValuesHolderLoader {

	final Logger log = LoggerFactory.getLogger(ValuesHolderLoader.class);
	
	private ValuesHolder valueHolder = null;
		
	public ValuesHolderLoader() {}
	
	public  ValuesHolder loadValues() {
		
		StatisticsCollector collector = BeanUtil.getBean(StatisticsCollector.class);
		
		ValuesHolder v = new ValuesHolder();
		v.setValue(Const.MAX+Trust.NumberOfCompletedTransactions.getLocalName(), collector.fetchTotalTransactions(null));
		v.setValue(Const.MAX+Trust.TradingVolume.getLocalName(),collector.fetchTotalTrading(null));
		return v;
	}

	public ValuesHolder getValueHolder() {
		return valueHolder;
	}
	
	public void setValueHolder(ValuesHolder valueHolder) {
		this.valueHolder = valueHolder;
	}


}
