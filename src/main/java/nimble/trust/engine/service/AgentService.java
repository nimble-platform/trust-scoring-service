package nimble.trust.engine.service;

import java.math.BigDecimal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import nimble.trust.engine.domain.Agent;
import nimble.trust.engine.repository.AgentRepository;

@Service
public class AgentService {
	
	
	@Autowired
	private AgentRepository agentRepository;
	
	@Transactional
	public Agent save(Agent agent){
		return agentRepository.save(agent);
	}
	
	@Transactional
	public void delete(Agent agent){
		agentRepository.delete(agent);
	}

	public Agent findOrCreateOwner(String altid) {
		Agent agent = agentRepository.findByAltId(altid);
		if (agent==null){
			agent = new Agent();
			agent.setAltId(altid);
			agent  = save(agent);
		}
		return agent;
	}

	@Transactional
	public void updateTrustScore(String partyId, Double trustScore) {
		Agent agent = agentRepository.findByAltId(partyId);
		agent.setTrustScore(new BigDecimal(trustScore));
		agent = save(agent);
	}
	
}
