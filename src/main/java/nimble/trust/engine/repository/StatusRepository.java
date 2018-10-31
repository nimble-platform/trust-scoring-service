package nimble.trust.engine.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import nimble.trust.engine.domain.Status;

@Repository
public interface StatusRepository extends JpaRepository<Status, Long> {
    
	Status findByName(String name);
	
}