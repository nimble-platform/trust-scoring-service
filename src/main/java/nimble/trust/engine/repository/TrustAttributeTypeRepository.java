package nimble.trust.engine.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import nimble.trust.engine.domain.TrustAttributeType;

@Repository
public interface TrustAttributeTypeRepository extends JpaRepository<TrustAttributeType, Long> {
    
	TrustAttributeType findByName(String name);
	
	TrustAttributeType findById(Long id);
	
	List<TrustAttributeType> findByParentType(TrustAttributeType trustAttributeType);
	
	/**
	 * 
	 * @return a list of root TrustAttributeType
	 */
	@Query("select t from TrustAttributeType t where t.parentType is null")
	List<TrustAttributeType> findAllRootLevel();
	
}