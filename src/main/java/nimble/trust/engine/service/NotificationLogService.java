package nimble.trust.engine.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import nimble.trust.engine.domain.NotificationLog;
import nimble.trust.engine.repository.NotificationLogRepository;

@Service
public class NotificationLogService {
	
	
	@Autowired
	private NotificationLogRepository notificationLogRepository;
	
	@Transactional
	public NotificationLog save(NotificationLog entity){
		return notificationLogRepository.save(entity);
	}
	
	@Transactional
	public void delete(NotificationLog entity){
		notificationLogRepository.delete(entity);
	}
	
}
