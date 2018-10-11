package nimble.trust.engine.op.match;


import java.math.BigDecimal;
import java.net.URI;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.hp.hpl.jena.datatypes.RDFDatatype;
import com.hp.hpl.jena.datatypes.xsd.impl.XSDDouble;

import nimble.trust.common.Const;
import nimble.trust.common.ValuesHolder;
import nimble.trust.common.WarnException;
import nimble.trust.engine.model.pojo.TrustAttribute;
import nimble.trust.engine.model.utils.TrustOntologyUtil;
import nimble.trust.engine.model.vocabulary.Trust;

/**
 * 
 * Created by Marko on 2018-10-09
 *
 */
public class ComparisonMatchOp {

	private static final Logger log = LoggerFactory.getLogger(ComparisonMatchOp.class);

	private ValuesHolder valuesHolder;

	public ComparisonMatchOp(ValuesHolder valuesHolder) {
		this.valuesHolder = valuesHolder;
	}

	public double apply(final TrustAttribute requested, final TrustAttribute attribute) throws Exception {

			RDFDatatype datatype = requested.getValueDatatype();
			String type = attribute.getTypesAll().get(0).getUri().toASCIIString();
			if (isNumericDataType(datatype)) {
					return compareNumeric(attribute, requested, type);
				//not numeric
			} else if (isMetricScale(datatype)) {
				String metricValueReqURI = (String) requested.getValue();
				String metricValuePresentURI = (String) attribute.getValue();
				String metricURI = datatype.getURI();
				return new MetricMatchOp().apply( URI.create(metricURI), URI.create(metricValueReqURI), URI.create(metricValuePresentURI));
			} else {
				throw new WarnException("Comparison unsupported for datatype " + datatype);
			}
	}

	/**
	 * 
	 * @param attribute
	 * @param type
	 * @param reqAttribute
	 * @return
	 */
	private double compareNumeric(TrustAttribute attribute, TrustAttribute requested, String type) {
		
		double returnValue = 0;

		if (requested.getValue() == null){
				requested.setValue(0) ;
		}
		
		// should be greater than expected, and if so, return attribute value normalized.
		if (isNotByMinMax(requested)) {
			double value = Double.valueOf(attribute.getValue().toString()).doubleValue();
			double reqestedValue = Double.valueOf(requested.getValue().toString()).doubleValue();
			log.debug("comparing numeric values: requested <= value {" + requested.getValue() + "," + attribute.getValue()+"}");
			if (reqestedValue <= value) {
				return cast(normalize(attribute, type).getValue());
			}
			else{
				return 0;
			}
		}

		// should be greater than expected, and if so, return 1;
		if (isComparisonByMin(requested)) {
			double value = Double.valueOf(attribute.getValue().toString()).doubleValue();
			double requestedMinValue = Double.valueOf(requested.getMinValue().toString()).doubleValue();
			log.debug("comparing numeric values: value >= requestedMinValue {" + attribute.getValue() + "," + requested.getValue()+"}");
			return (value >= requestedMinValue) ? 1 : 0;
		}
		// should be less than expected, and if so, return 1;
		else if (isComparisonByMax(requested)) {
			double value = Double.valueOf(attribute.getValue().toString()).doubleValue();
			double requestedMaxValue = Double.valueOf(requested.getMaxValue().toString()).doubleValue();
			log.debug("comparing numeric values: value <= requestedMaxValue {" + attribute.getValue() + "," + requested.getValue()+"}");
			return (value <= requestedMaxValue) ? 1 : 0;
		}
		// should be greater in a range, and if so, return 1;
		else if (isComparisonWithinRange(requested)) {
			double value = Double.valueOf(attribute.getValue().toString()).doubleValue();
			double requestedMaxValue = Double.valueOf(requested.getMaxValue().toString()).doubleValue();
			double requestedMinValue = Double.valueOf(requested.getMinValue().toString()).doubleValue();
			log.debug("comparing numeric values: value >= requestedMinValue && value <= requestedMaxValue {" + attribute.getValue() + "," + requestedMinValue+","+requestedMaxValue+"}");
			return (value >= requestedMinValue && value <= requestedMaxValue) ? 1 : 0;
		}

		log.debug("comparing numeric values returns " + returnValue);
		return returnValue;
	}

	private boolean isNotByMinMax(TrustAttribute requested) {
		return (isComparisonWithinRange(requested) == false && isComparisonByMax(requested) == false
			&& isComparisonByMin(requested) == false);
	}

	private boolean isComparisonWithinRange(TrustAttribute requested) {
		return (requested.getMaxValue() != null && requested.getMinValue() != null);
	}

	private boolean isComparisonByMax(TrustAttribute requested) {
		return (requested.getMaxValue() != null && requested.getMaxValue().equals(0) == false)
				&& (requested.getMinValue() == null || requested.getMinValue().equals(0));
	}

	private boolean isComparisonByMin(TrustAttribute requested) {
		return (requested.getMinValue() != null && requested.getMinValue().equals(0) == false)
				&& (requested.getMaxValue() == null || requested.getMaxValue().equals(0));
	}

	private TrustAttribute normalize(TrustAttribute attribute, String type) {
		if (attribute.getValue() != null) {
			attribute.setValue(cast(attribute.getValue()) / getScaleMaxValue(type).doubleValue());
		}
		return attribute;

	}

	private Double cast(Object value) {
		return Double.valueOf(value.toString());
	}

	private BigDecimal getScaleMaxValue(String type) {

		if (type.equals(Trust.ProviderWebReputationBy3rdParty.getURI())) {
			return new BigDecimal("100");
		} else if (type.equals(Trust.Reputation.getURI()) || type.equals(Trust.ContractCompliance.getURI())
				|| type.equals(Trust.UserRating.getURI())
				|| type.equals(Trust.OverallCommunicationRating.getURI())
				|| type.equals(Trust.OverallDeliveryAndPackagingRating.getURI())
				|| type.equals(Trust.OverallFullfilmentOfTermsRating.getURI())
				|| type.equals(Trust.OverallCompanyRating.getURI())) {
			return new BigDecimal(5);
		} else if (type.equals(Trust.NumberOfCompletedTransactions.getURI())) {
			return (BigDecimal) valuesHolder.getValue(Const.MAX + Trust.NumberOfCompletedTransactions.getLocalName(), 1);
		} else if (type.equals(Trust.TradingVolume.getURI())) {
			return (BigDecimal) valuesHolder.getValue(Const.MAX + Trust.TradingVolume.getLocalName(), 1);
		} else
			return new BigDecimal(1);
	}

	/**
	 * Answers is typedliteral datatype is of Trust Metric type.
	 * 
	 * @param datatype
	 *            typedliteral datatype
	 * @return true if typedliteral datatype is of Trust Metric type.
	 */
	private boolean isMetricScale(RDFDatatype datatype) {
		String datatypeURI = datatype.getURI();
		return TrustOntologyUtil.instance().isIndividualOfTypeIgnoreSuper(URI.create(datatypeURI),
				Trust.Metric.getURI());
	}

	/**
	 * 
	 * @param datatype
	 * @return
	 */
	private boolean isNumericDataType(RDFDatatype datatype) {
		if (datatype instanceof XSDDouble) {
			return true;
		}
		return false;
	}

}
