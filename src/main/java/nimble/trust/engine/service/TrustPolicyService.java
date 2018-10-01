package nimble.trust.engine.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

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

	public TrustPolicy findGlobalTRustPolicy() {
		List<TrustPolicy> result = trustPolicyRepository.findAll();
		if (CollectionUtils.isEmpty(result)==false){
			return result.get(0);
		}
		return null;
	}
	
}
