package nimble.trust.messaging;

import org.apache.commons.lang3.StringUtils;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import nimble.trust.config.KafkaConfig;
import nimble.trust.engine.service.ChangeEventHandlerService;
import nimble.trust.web.dto.ChangeEvent;

/**
 * Created by Johannes Innerbichler on 27.09.18.
 */
@Component
public class KafkaReceiver {
	
	

	private static Logger log = LoggerFactory.getLogger(KafkaReceiver.class);
	
	@Autowired
	private ChangeEventHandlerService eventHandlerService;

	/**
     * Receives messages from companyUpdates channel
     * @param consumerRecord message
     */
    @KafkaListener(topics = "${nimble.kafka.topics.companyUpdates}")
    public void receiveCompanyUpdates(ConsumerRecord<String, KafkaConfig.AuthorizedMessage> consumerRecord) {
    	String bearerToken = consumerRecord.value().getAccessToken();
        String partyId = consumerRecord.value().getCompanyId(); 
        log.info("Received updated for company with ID: " + partyId);
        
        if (partyId==null){
        	log.warn("Received rating updates with null partyId");
        	return;
        }
        try {
    		final Authentication auth = new UsernamePasswordAuthenticationToken(bearerToken, null);
    		SecurityContextHolder.getContext().setAuthentication(auth);
    		eventHandlerService.postChangeEvent(new ChangeEvent(partyId, "company-updates", StringUtils.EMPTY));
		} catch (Exception e) {
			log.error("profile completness notificationTrustDataChange failed ", e);
		}
    }
    
    
    /**
     * Receives messages from ratingsUpdates channel
     * @param consumerRecord message
     */
    @KafkaListener(topics = "${nimble.kafka.topics.ratingsUpdates}")
    public void receiveRatingsUpdates(ConsumerRecord<String, KafkaConfig.AuthorizedMessage> consumerRecord) {
    	String bearerToken = consumerRecord.value().getAccessToken();
        String partyId = consumerRecord.value().getCompanyId(); 
        log.info("Received rating updates for company with ID: " + partyId);
        
        if (partyId==null){
        	log.warn("Received rating updates with null partyId");
        	return;
        }
        try {
    		final Authentication auth = new UsernamePasswordAuthenticationToken(bearerToken, null);
    		SecurityContextHolder.getContext().setAuthentication(auth);
    		eventHandlerService.postChangeEvent(new ChangeEvent(partyId, "ratings-update", StringUtils.EMPTY));
		} catch (Exception e) {
			log.error("ratings notificationTrustDataChange failed ", e);
		}
    }
}
