package nimble.trust.engine.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import nimble.trust.config.KafkaConfig;

@Service
public class TrustScoreSync {

	private static Logger log = LoggerFactory.getLogger(TrustScoreSync.class);


	@Value("${nimble.kafka.topics.trustScoreUpdates}")
	private String trustScoreUpdatesTopic;
	
	

	@Autowired
	private KafkaTemplate<String, KafkaConfig.AuthorizedMessage> kafkaTemplate;

	public void syncWithCatalogService(String partyId) {
		
		final String bearerToken = SecurityContextHolder.getContext().getAuthentication().getPrincipal().toString();
        KafkaConfig.AuthorizedMessage message = new KafkaConfig.AuthorizedMessage(partyId, bearerToken);
        try {
			kafkaTemplate.send(trustScoreUpdatesTopic, message);
			log.info("PartyId=" + partyId + " : New trust score message sucessfuly broadcasted");
		} catch (Exception e) {
			log.error("Failed to broadcast trust score change", e);
			e.printStackTrace();
		}
        
	}


}
