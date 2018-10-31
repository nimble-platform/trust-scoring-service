package nimble.trust.engine.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import nimble.trust.engine.domain.NotificationLog;
import nimble.trust.engine.domain.Status;

@Repository
public interface NotificationLogRepository extends JpaRepository<NotificationLog, Long> {
    
	List<NotificationLog> findByType(String type);
	
	List<NotificationLog> findByStatus(Status status);
	
}