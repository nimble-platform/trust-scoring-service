package nimble.trust.engine.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import nimble.trust.engine.domain.TrustAttributeType;

@Repository
public interface TrustAttributeTypeRepository extends JpaRepository<TrustAttributeType, Long> {
    
	TrustAttributeType findByName(String name);
	
	TrustAttributeType findById(Long id);
	
}