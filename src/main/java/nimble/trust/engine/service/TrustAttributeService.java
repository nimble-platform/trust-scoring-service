package nimble.trust.engine.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import nimble.trust.engine.domain.TrustAttribute;
import nimble.trust.engine.repository.TrustAttributeRepository;

@Service
public class TrustAttributeService {
	
	
	@Autowired
	private TrustAttributeRepository trustAttributeRepository;
	
	@Transactional
	public TrustAttribute save(TrustAttribute entity){
		return trustAttributeRepository.save(entity);
	}
	
	@Transactional
	public void delete(TrustAttribute entity){
		trustAttributeRepository.delete(entity);
	}
	
}
