package nimble.trust.engine.util;

import java.net.URI;
import java.util.List;

import org.apache.commons.lang3.StringUtils;

import com.hp.hpl.jena.datatypes.xsd.impl.XSDDouble;

import nimble.trust.engine.domain.TrustAttribute;
import nimble.trust.engine.domain.TrustAttributeType;
import nimble.trust.engine.domain.TrustPolicy;
import nimble.trust.engine.model.expression.ExpressionBuilder;
import nimble.trust.engine.model.factory.TrustModelFactory;
import nimble.trust.engine.model.pojo.TrustCriteria;
import nimble.trust.engine.model.vocabulary.QualityIndicatorConvert;
import nimble.trust.engine.model.vocabulary.Trust;
import nimble.trust.util.uri.UIDGenerator;

/**
 * 
 * Created by Marko on 2018-10-04
 *
 */
public class PolicyConverter {
	
	
	public static TrustCriteria fromPolicyToCriteria (TrustPolicy trustPolicy){
		
		ExpressionBuilder builder = new ExpressionBuilder().startNewTrustCriteria();
		
		List<TrustAttribute> list = trustPolicy.getTrustAttributes();
		
		for (TrustAttribute trustAttribute : list) {
			nimble.trust.engine.model.pojo.TrustAttribute attribute = createTrustAttribute(trustAttribute);
			builder = builder.attribute(attribute).and();
		}
		
		return builder.build();
	}

	private static nimble.trust.engine.model.pojo.TrustAttribute createTrustAttribute(TrustAttribute attr) {
		TrustModelFactory factory = new TrustModelFactory(UIDGenerator.instanceRequest);
		nimble.trust.engine.model.pojo.TrustAttribute a = factory.createTrustAttibute();
		a.setValue(toNullIfEmpty(attr.getValue()));
		a.setImportance(attr.getImportance());
		a.setValueDatatype(XSDDouble.XSDdouble);
		a.setMinValue(toNullIfEmpty(attr.getMinValue()));
		a.setMaxValue(toNullIfEmpty(attr.getMaxValue()));
		TrustAttributeType attributeType =  attr.getTrustAttributeType();
		a.addType(URI.create(Trust.NS+QualityIndicatorConvert.findByName(attributeType.getName()).getTrustVocabulary()));
		return a;
	}

	private static Object toNullIfEmpty(String value) {
		if (value!=null && StringUtils.isEmpty(value)) 
			return null;
		return value;
	}

}
