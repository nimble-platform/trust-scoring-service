package nimble.trust.engine.model.vocabulary;

import eu.nimble.service.model.ubl.extension.QualityIndicatorParameter;

public enum QualityIndicatorConvert {
	
	
	OverallCompanyRating(Trust.OverallCompanyRating.getLocalName(), QualityIndicatorParameter.COMPANY_RATING),
	
	OverallCommunicationRating (Trust.OverallCommunicationRating.getLocalName(),QualityIndicatorParameter.SELLER_COMMUNICATION),
    
	OverallFullfilmentOfTermsRating (Trust.OverallFullfilmentOfTermsRating.getLocalName(), QualityIndicatorParameter.FULFILLMENT_OF_TERMS ),

	OverallDeliveryAndPackagingRating (Trust.OverallDeliveryAndPackagingRating.getLocalName(), QualityIndicatorParameter.DELIVERY_PACKAGING),
	NumberOfCompletedTransactions (Trust.NumberOfCompletedTransactions.getLocalName(), QualityIndicatorParameter.NUMBER_OF_TRANSACTIONS),
	
	TradingVolume (Trust.TradingVolume.getLocalName(), QualityIndicatorParameter.TRADING_VOLUME),
	
	OverallProfileCompletness (Trust.OverallProfileCompletness.getLocalName(), QualityIndicatorParameter.PROFILE_COMPLETENESS),
	
	ProfileCompletnessDetails (Trust.ProfileCompletnessDetails.getLocalName(), QualityIndicatorParameter.COMPLETENESS_OF_COMPANY_GENERAL_DETAILS),
	
	ProfileCompletnessDescription (Trust.ProfileCompletnessDescription.getLocalName(), QualityIndicatorParameter.COMPLETENESS_OF_COMPANY_DESCRIPTION),
	
	ProfileCompletnessTrade (Trust.ProfileCompletnessTrade.getLocalName(), QualityIndicatorParameter.COMPLETENESS_OF_COMPANY_TRADE_DETAILS),
	
	ProfileCompletnessCertificates (Trust.ProfileCompletnessTrade.getLocalName(), QualityIndicatorParameter.COMPLETENESS_OF_COMPANY_CERTIFICATE_DETAILS),
	
	AverageTimeToRespond (Trust.AverageTimeToRespond.getLocalName(), QualityIndicatorParameter.RESPONSE_TIME),
	
	AverageNegotiationTime (Trust.AverageNegotiationTime.getLocalName(), QualityIndicatorParameter.NEGOTIATION_TIME);
	
	
	public static QualityIndicatorConvert findByName(String name){
		
		QualityIndicatorConvert[] array = QualityIndicatorConvert.values();
		for (QualityIndicatorConvert qualityIndicatorConvert : array) {
			if (qualityIndicatorConvert.getTrustVocabulary().equalsIgnoreCase(name))
				return qualityIndicatorConvert;
		}
		return null;
	}
	


//	    
//	AverageTimeToRespond
//	AverageNegotiationTime
	
	
	private String trustVocabulary;
	private QualityIndicatorParameter qualityIndicatorParameter;
	

	
	QualityIndicatorConvert(String name, QualityIndicatorParameter parameter ){
		this.trustVocabulary = name;
		this.qualityIndicatorParameter = parameter;
	}
	
	public String getTrustVocabulary() {
		return trustVocabulary;
	}
	
	public QualityIndicatorParameter getQualityIndicatorParameter() {
		return qualityIndicatorParameter;
	}
	
	

}
