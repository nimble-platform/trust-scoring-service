package nimble.trust.engine.model.pojo;



import java.util.List;

import com.google.common.collect.Lists;

import nimble.trust.engine.model.expression.Expression;
import nimble.trust.engine.model.expression.OrElement;
import nimble.trust.engine.model.expression.SingleElement;

public class TrustCriteria extends Expression {
	
	private List<SingleElement> listOperandByAnd = Lists.newArrayList();
	private List<OrElement> listOperandByOrGroup = Lists.newArrayList();

	public void setSingleAttributeList(List<SingleElement> list) {
		listOperandByAnd = list;
	}

	public void setOrGroupAttributeList(List<OrElement> list) {
		listOperandByOrGroup = list;
	}
	
	public List<SingleElement> getListOperandByAnd() {
		return listOperandByAnd;
	}
	
	public List<OrElement> getListOperandByOrGroup() {
		return listOperandByOrGroup;
	}

}
