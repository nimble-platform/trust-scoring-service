package nimble.trust.engine.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import nimble.trust.engine.domain.TrustPolicy;
import nimble.trust.engine.repository.TrustPolicyRepository;

@Service
public class TrustPolicyService {
	
	
	@Autowired
	private TrustPolicyRepository trustPolicyRepository;
	
	@Transactional
	public TrustPolicy save(TrustPolicy entity){
		return trustPolicyRepository.save(entity);
	}
	
	@Transactional
	public void delete(TrustPolicy entity){
		trustPolicyRepository.delete(entity);
	}
	
}
