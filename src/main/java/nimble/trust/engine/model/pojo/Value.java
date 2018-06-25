package nimble.trust.engine.model.pojo;


/**
 * 
 * LevelOfTrust is a value between zero (0) and one (1). Zero denotates a distrust, whereas one denotates a full trust.
 * 
 * @author marko
 * 
 */
public class Value {

	private double numericalValue ;

	public static final double Trustworthy = 1;

	public static final double NoTrustworthy = 0;

	public static final double Uknown = -1;
	
	public static final double treshold = 0.5;

	public Value(double val) {
		this.numericalValue = val;
	}

	public double getNumericalValue() {
		return numericalValue;
	}

	public void setNumericalValue(double val) {
		this.numericalValue = val;
	}

	public boolean isTrustworthy(double treshhold) {
		return (numericalValue >= treshhold);
	}

	public boolean isTrustworthy() {
		return (numericalValue >= treshold);
	}

}
