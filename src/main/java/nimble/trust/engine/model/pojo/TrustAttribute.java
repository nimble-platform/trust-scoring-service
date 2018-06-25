package nimble.trust.engine.model.pojo;



import java.net.URI;

import com.hp.hpl.jena.datatypes.RDFDatatype;

/**
 * TrustAttribute For COMPOSE, it is any trust-relevant functional / non-functional property
 * @author marko
 *
 */
public class TrustAttribute extends TResource{
	
	private double importance = 1;
	
	private Object value;
	
	private Object minValue;
	
	private Object maxValue;
	
	private RDFDatatype valueDatatype;

	public TrustAttribute(URI uri) {
		super(uri);
	}

	public double getImportance() {
		return importance;
	}
	
	public void setImportance(double imp) {
		this.importance = imp;
	}
	

	
	public Object getValue() {
		return value;
	}

	public void setValue(Object value) {
		this.value = value;
	}

	public Object getMinValue() {
		return minValue;
	}

	public void setMinValue(Object minValue) {
		this.minValue = minValue;
	}

	public Object getMaxValue() {
		return maxValue;
	}

	public void setMaxValue(Object maxValue) {
		this.maxValue = maxValue;
	}

	public RDFDatatype getValueDatatype() {
		return valueDatatype;
	}

	public void setValueDatatype(RDFDatatype valueDatatype) {
		this.valueDatatype = valueDatatype;
	}
	
	@Override
	public String toString() {
		return getUri()+" "+getValue()+"^^"+getValueDatatype();
	}
	
	

}
