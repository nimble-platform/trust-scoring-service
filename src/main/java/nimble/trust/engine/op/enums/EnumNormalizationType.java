package nimble.trust.engine.op.enums;



public enum EnumNormalizationType {
	
	//0-1 Normalization	X' = \frac{X - X_{min}}{X_{max}-X_{min}}	Feature scaling used to bring all values into the range [0,1]
	Zero_One_by_divMax, 
	
	Zero_One

}
