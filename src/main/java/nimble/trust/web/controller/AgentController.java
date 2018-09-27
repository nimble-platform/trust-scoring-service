package nimble.trust.web.controller;

import java.io.IOException;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.logging.LogLevel;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;

import eu.nimble.service.model.ubl.commonaggregatecomponents.PartyType;
import nimble.trust.engine.domain.Agent;
import nimble.trust.engine.repository.AgentRepository;

@RestController
public class AgentController {

    @Autowired
    private AgentRepository agentRepository;

    @GetMapping("/agents")
    public Page<Agent> getQuestions(Pageable pageable) {
        return agentRepository.findAll(pageable);
    }


    @PostMapping("/agents")
    public Agent createQuestion(@Valid @RequestBody Agent agent) {
        return agentRepository.save(agent);
    }
    
    
    @RequestMapping(value = "/trust/party/{partyId}/trust",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            method = RequestMethod.POST)
    public ResponseEntity onPartyTrustUpdate( @RequestBody String serializedParty,  @PathVariable String partyId,
                                              @RequestHeader(value = "Authorization") String bearerToken) {
    	try {
            
            ObjectMapper objectMapper = new ObjectMapper().configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
            PartyType trustParty;
            try {
                trustParty = objectMapper.readValue(serializedParty, PartyType.class);
            } catch (IOException e) {
                String msg = String.format("Failed to parse the provided party data: %s", serializedParty);
                return ResponseEntity.ok().build();
            }
            
            return ResponseEntity.ok().build();

        } catch (Exception e) {
            String msg = "Unexpected error while handling the party synchronization update";
            return ResponseEntity.ok().build();
        }


    }
    
}