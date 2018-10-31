package nimble.trust.engine.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import nimble.trust.engine.domain.Agent;
import nimble.trust.engine.domain.TrustProfile;

@Repository
public interface TrustProfileRepository extends JpaRepository<TrustProfile, Long> {
    
	TrustProfile findByOwner(Agent agent);
	
	TrustProfile findByOwnerAltId(String altid);
	
	TrustProfile findBy(Long id);
	
}