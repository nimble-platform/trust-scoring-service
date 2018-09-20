package nimble.trust.engine.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import nimble.trust.engine.domain.TrustAttribute;
import nimble.trust.engine.domain.TrustAttributeType;
import nimble.trust.engine.domain.TrustPolicy;
import nimble.trust.engine.domain.TrustProfile;

@Repository
public interface TrustAttributeRepository extends JpaRepository<TrustAttribute, Long> {
    
	List<TrustAttribute> findByTrustAttributeType(TrustAttributeType trustAttributeType);
	
	List<TrustAttribute> findByTrustProfile(TrustProfile trustProfile);
	
	List<TrustAttribute> findByTrustPolicy(TrustPolicy trustPolicy);
	
}