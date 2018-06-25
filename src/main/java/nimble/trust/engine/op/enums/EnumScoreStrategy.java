package nimble.trust.engine.op.enums;


public enum EnumScoreStrategy {

	/**
	 * @see http://en.wikipedia.org/wiki/TOPSIS
	 */
	TOPSIS, // TOPSIS (Technique for Order Preference by Similarity to the Ideal Solution)

	/**
	 * @see <a href="http://en.wikipedia.org/wiki/Weighted_sum_model">http://en.wikipedia.org/wiki/Weighted_sum_model</a>
	 */
	Weighted_sum_model, // Sums weighted values of all features. All features must be scaled to 0..1 scale.

	/**
	 * A modified version of {@link EnumScoreStrategy.Weighted_sum_model} This strategy sums weighted values of all features. All features
	 * values must be scaled to 0..1 scale. Scaling is done by diving a value of a feature with max value of that feature considering values
	 * in a given dataset
	 */
	Weighted_sum_model_to01scale_divMaxInSet,

}
