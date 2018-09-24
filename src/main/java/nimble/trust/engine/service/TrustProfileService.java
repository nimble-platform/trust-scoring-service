package nimble.trust.engine.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import nimble.trust.engine.domain.TrustProfile;
import nimble.trust.engine.repository.TrustProfileRepository;

@Service
public class TrustProfileService {
	
	
	@Autowired
	private TrustProfileRepository trustProfileRepository;
	
	@Transactional
	public TrustProfile save(TrustProfile entity){
		return trustProfileRepository.save(entity);
	}
	
	@Transactional
	public void delete(TrustProfile entity){
		trustProfileRepository.delete(entity);
	}
	
}
