package nimble.trust.engine.service;

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
	
}
