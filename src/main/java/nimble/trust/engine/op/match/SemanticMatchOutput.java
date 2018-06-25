package nimble.trust.engine.op.match;



public enum SemanticMatchOutput {

	// Increasing order of match
	Sem_Disjoint("Semantically, there is no match between concept required and concept present", 0D), // 0
	Sem_MoreGeneral("The concept required is a superclass (a more general concept) of the one present", 0.3D), //
	Sem_MoreSpecific("The concept required is a subclass (a more specific concept) of the one present", 0.6D), // 1
	Sem_Exact("The concept present is semantically exact match to the concept required", 1D); // 1

	private String desc;

	private double asNumeric;

	private SemanticMatchOutput(String desc, double asNumeric) {
		this.desc = desc;
		this.asNumeric = asNumeric;
	}

	public String getDesc() {
		return desc;
	}

	public double asNumeric() {
		return asNumeric;
	}
}
