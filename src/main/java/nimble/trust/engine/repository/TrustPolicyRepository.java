package nimble.trust.engine.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import nimble.trust.engine.domain.Agent;
import nimble.trust.engine.domain.TrustPolicy;

@Repository
public interface TrustPolicyRepository extends JpaRepository<TrustPolicy, Long> {
    
	TrustPolicy findByOwner(Agent agent);
	
	TrustPolicy findBy(Long id);
	
}