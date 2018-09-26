package nimble.trust.engine.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import nimble.trust.engine.domain.Status;
import nimble.trust.engine.repository.StatusRepository;

@Service
public class StatusService {
	
	
	@Autowired
	private StatusRepository statusRepository;
	
	@Transactional
	public Status save(Status entity){
		return statusRepository.save(entity);
	}
	
	@Transactional
	public void delete(Status entity){
		statusRepository.delete(entity);
	}

	public Status findOrCreate(String statusName) {
		Status status = statusRepository.findByName(statusName);
		if (status == null){
			status = new Status();
			status.setName(statusName);
			status = save(status);
		}
		return status;
	}
	
}
