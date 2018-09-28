package nimble.trust.engine.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import nimble.trust.engine.domain.TrustAttributeType;
import nimble.trust.engine.repository.TrustAttributeTypeRepository;

@Service
public class TrustAttributeTypeService {
	
	
	@Autowired
	private TrustAttributeTypeRepository trustAttributeTypeRepository;
	
	@Transactional
	public TrustAttributeType save(TrustAttributeType entity){
		return trustAttributeTypeRepository.save(entity);
	}
	
	@Transactional
	public void delete(TrustAttributeType entity){
		trustAttributeTypeRepository.delete(entity);
	}
	
	public TrustAttributeType findByName(String name){
		return trustAttributeTypeRepository.findByName(name);
	}
	
	public List<TrustAttributeType> findAll(){
		return trustAttributeTypeRepository.findAll();
	}
	
	public List<TrustAttributeType> findAllRootLevel(){
		return trustAttributeTypeRepository.findAllRootLevel();
	}
	
	
	
}
