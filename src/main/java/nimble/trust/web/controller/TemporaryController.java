package nimble.trust.web.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import nimble.trust.engine.collector.RatingsCollector;
import nimble.trust.engine.collector.StatisticsCollector;
import nimble.trust.engine.domain.Agent;
import nimble.trust.engine.repository.AgentRepository;

@RestController
public class TemporaryController {

    @Autowired
    private AgentRepository agentRepository;
    
    @Autowired
    private StatisticsCollector collector;
    
    
    @Autowired
    private RatingsCollector ratingCollector;

    @GetMapping("/agents")
    public Page<Agent> getAllAgents(Pageable pageable, @RequestHeader(value = "Authorization") String bearerToken) {
        return agentRepository.findAll(pageable);
    }
    
 
     
    @PostMapping("/test-fetch-from-business-service")
    public ResponseEntity<?> test( @RequestParam(value = "companyId", required = false) String partyId, @RequestHeader(value = "Authorization") String bearerToken) {
    
    	collector.fetchStatistics(partyId, false);
//    	collector.fetchTotalTrading(null);
//    	collector.fetchTotalTransactions(null);
//    	
    	ratingCollector.fetchRatingsSummary(partyId,false);
    	
    	return new ResponseEntity<>(HttpStatus.OK);
    	
    }

    
}