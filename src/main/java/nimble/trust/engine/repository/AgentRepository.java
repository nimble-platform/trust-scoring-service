package nimble.trust.engine.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import nimble.trust.engine.domain.Agent;

@Repository
public interface AgentRepository extends JpaRepository<Agent, Long> {
    
	List<Agent> findByName(String name);
	
	Agent findByAltId(String altId);
}